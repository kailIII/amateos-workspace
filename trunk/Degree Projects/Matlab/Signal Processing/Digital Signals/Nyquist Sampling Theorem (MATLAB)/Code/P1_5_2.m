%5.1 
%- b)  Representar graficamente el modulo del espectro de las tres señales
%muestreadas obtenidas graficamente.

%Caracteristicas de la Señal

Amp=1.0;%Amplitud
f0=20;%Frecuencia
tita=0;%Fase
tfin=2;%Tiempo final de muestreo
tin=0;%Tiempo inicial de muestreo

%Frecuencias de muestreo(fs=fmuestreo*f0)

fmuestreo1=5.0;
fmuestreo2=2.0;
fmuestreo3=1.5;

%Aplicacion de la funcion muestreo la cual nos devuelve:
%nn-> Vector de tiempos
%xn-> Señal muestreada

%Muestreo primera frecuencia de muestreo(5*f0 Hz)

[nn1,xn1]=muestreo(Amp,f0,tita,tin,tfin,fmuestreo1);

%Muestreo segunda frecuencia de muestreo(2*f0 Hz)

[nn2,xn2]=muestreo(Amp,f0,tita,tin,tfin,fmuestreo2);

%Muestreo tercera frecuencia de muestreo(1.5*f0 Hz)

[nn3,xn3]=muestreo(Amp,f0,tita,tin,tfin,fmuestreo3);

%Calculo del modulo del espectro de la señal con la primera frecuencia de
%muestreo(5*f0 Hz)

fft(xn1,length(xn1));
modx1=abs(fft(xn1,length(xn1)));
fs1=f0*fmuestreo1;
frec1=0:(fs1)/(length(xn1)-1):fs1;


%Calculo del modulo del espectro de la señal con la primera frecuencia de
%muestreo(2*f0 Hz)
fft(xn2,length(xn2));
modx2=abs(fft(xn2,length(xn2)));
fs2=f0*fmuestreo2;
frec2=0:(fs2)/(length(xn2)-1):fs2;

%Calculo del modulo del espectro de la señal con la primera frecuencia de
%muestreo(1.5*f0 Hz)
fft(xn3,length(xn3));
modx3=abs(fft(xn3,length(xn3)));
fs3=f0*fmuestreo3;
frec3=0:(fs3)/(length(xn3)-1):fs3;

%Representacion de los modulos del espectro de la señal con las tres
%frecuencias de muestreo.

%Representacion de la primera frecuencia(5*f0 Hz)
subplot(6,1,1)
plot(frec1,modx1);
title('Funcion con Frecuencia de Muestreo(5*f0)')
xlabel('Frecuencia (Hz)');
ylabel('Modulo del especro');

%Representacion de la segunda frecuencia(2*f0 Hz)
subplot(6,1,3)
plot(frec2,modx2);
title('Funcion con Frecuencia de Muestreo(2*f0)')
xlabel('Frecuencia (Hz)');
ylabel('Modulo del especro');

%Representacion de la tercera frecuencia(1.5*f0 Hz)
subplot(6,1,5)
plot(frec3,modx3);
title('Funcion con Frecuencia de Muestreo(1.5*f0)')
xlabel('Frecuencia (Hz)');
ylabel('Modulo del especro');