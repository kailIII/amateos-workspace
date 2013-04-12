#include "codigo.h"
#include <ctype.h>
#include <float.h>
#include <string.h>
#include "DS1624.c"
#include "LCD_B.c"

// ------ VARIABLES y CONSTANTES GLOBALES -------------------------------------
#define PERIODOTEMPERATURA 3        //periodo de medida de temperatura
#define PRECARGATIMER1 3036         //valor inicial TIMER 1
#define UMBRALTEMPERATURA 25.0      //umbral superior de temperatura para encender el led
#define UMBRALLUZ 50                //umbral inferior de luz para encender el led
BOOLEAN flagLuz=true;              //indica si hay que realizar la conversi�n del valor de la luz
BOOLEAN flagTemperatura=true;      //indica si hay que obtener la temperatura del DS1624
int contadorSegundos=0;             //cuenta el n�mero de segundos transcurridos (valores entre 0 y 3)   
int contadorDesborde=0;             //cuenta cu�ntas veces se ha desbordado el timer 1(se desborda cada 0,5 segundos)

// ------- FUNCIONES ----------------------------------------------------------
float medirTemperatura();
int medirLuz();
void mostrarLuz(int);
void mostrarTemperatura (float);

float medirTemperatura(){ // Lee el valor de la temperatura del DS1624
   
   float temp;
   init();
   init_temp();
   temp=read_temp();
   
   return temp;
}//fin medirTemperatura

unsigned int medirLuz(){  // Convierte el valor de tensi�n procedente del LDR
   
   unsigned int luz;
   set_adc_channel(0);
   read_adc(ADC_START_ONLY);
   while(!adc_done());        //esperamos a que finalice la conversion
   luz=read_adc();
   return luz;
}//fin medirLuz

void mostrarTemperatura(float temp){ // Muestra el valor de la temperatura por pantalla
   lcd_init();    
   lcd_send_byte(0,1);           //Limpia la pantalla
   lcd_send_byte(0,2);           //Coloca el cursor al inicio
   printf(lcd_putc,"TEMP: %3.2f ", temp);
   lcd_putc(223);
   lcd_putc("C");            
}//fin mostrarTemperatura

void mostrarLuz(unsigned int luz){   // Muestra el valor de luz por pantalla
   lcd_gotoxy(1,2);   
   printf(lcd_putc,"LUZ: %u ", luz);
} //Fin mostrarLuz


// ------ Interrupci�n TIMER1 ------------------------------------------------
#int_TIMER1
void  TIMER1_isr(void) 
{
   if (contadorDesborde==1){ //Ha pasado 1 segundo
      contadorDesborde=0;
      contadorSegundos++;
      flagLuz=true;
      if (contadorSegundos==PERIODOTEMPERATURA) { //Han pasado 3 segundos
         flagTemperatura=true;
         contadorSegundos=0;
      }
   }else {  //No ha pasado un segundo
      contadorDesborde++;
   }
   set_timer1(PRECARGATIMER1+get_timer1());
}// Fin interrupci�n Timer 1

// -------- MAIN -------------------------------------------------------------
void main()
{
   float temperatura;
   unsigned int luz;
   init();
   lcd_init();   
   delay_ms(200);
   lcd_putc("�: Iniciando :�");
   delay_ms(2000);
   lcd_send_byte(0,1);
   
   //Configuraci�n puertos B y C como salida
   set_tris_b(0x00);  
   set_tris_c(0x00);
  
   //configuracion del ADC
   setup_adc_ports(AN0);
   setup_adc(ADC_CLOCK_INTERNAL);
   
   //configuracion del TIMER1
   setup_timer_1(T1_INTERNAL|T1_DIV_BY_8);
   set_timer1(PRECARGATIMER1);
   enable_interrupts(INT_TIMER1);
   enable_interrupts(GLOBAL);

   //BUCLE INFINITO
   while (1) {
   
    if (flagTemperatura){ //Hay que leer temperatura
      temperatura=medirTemperatura();
      mostrarTemperatura(temperatura);
      //mostrarLuz(luz);           
      flagTemperatura=false;
   } // Fin if medir temperatura
   
    if (flagLuz){ //Hay que leer nivel de luz
      luz=medirLuz();
      mostrarLuz(luz);      
      flagLuz=false;
    } // Fin if medir luz
    
    // Encendido de LEDs
    if (temperatura>UMBRALTEMPERATURA){
         output_bit(PIN_C1,1);
      }else {
         output_bit(PIN_C1,0);
      }
    if (luz<UMBRALLUZ){
         output_bit(PIN_C0,1);
      }else {
         output_bit(PIN_C0,0);
      }
  } // Fin WHILE (1)

} // Fin Main
