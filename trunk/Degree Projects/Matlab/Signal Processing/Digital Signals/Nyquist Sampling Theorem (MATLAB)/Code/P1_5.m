%5 Teorema de muestreo

%5.1 Realice los siguientes ejercicios

%a) Partiendo de la expresion de la sinusoide de tiempo continuo,escriba un
%programa MATLAB que obtenga muestras de s(t) para crear con ellas una
%funcio de tiempo discreto de longitud finita

%Declaracion Variables

%Parametros de la señal

Amp=1.0;%Amplitud
f0=20;%frecuencia
teta=0;%Fase

%Parametros tiempo

tin=0;%Tiempo de inicio
tfin=2;%Tiempo final

%Frecuencias de muestreo(fs=fmuestreo*f0)

fmuestreo1=5;
fmuestreo2=2;
fmuestreo3=1.5;


%Aplicacion de la funcion muestreo la cual nos devuelve:
%nn-> Vector de tiempos
%xn-> Señal muestreada

%Muestreo primera frecuencia de muestreo(5*f0 Hz)
[nn1,xn1]=muestreo(Amp,f0,teta,tin,tfin,fmuestreo1);

%Muestreo segunda frecuencia de muestreo(2*f0 Hz)
[nn2,xn2]=muestreo(Amp,f0,teta,tin,tfin,fmuestreo2);

%Muestreo tercera frecuencia de muestreo(1.5*f0 Hz)
[nn3,xn3]=muestreo(Amp,f0,teta,tin,tfin,fmuestreo3);


% Representacion grafica


%Representacion grafica primera frecuencia de muestreo(5*f0 Hz)

subplot(3,2,1)
stem(nn1,xn1)
title('Funcion con Frecuencia de Muestreo(5*f0)')
xlabel('Variable discreta n');
ylabel('Amplitud');

%Representacion grafica primera frecuencia de muestreo(2*f0 Hz)

subplot(3,2,2)
stem(nn2,xn2)
title('Funcion con Frecuencia de Muestreo(2*f0)')
xlabel('Variable discreta n');
ylabel('Amplitud');

%Representacion grafica primera frecuencia de muestreo(1.5*f0 Hz)

subplot(3,2,3)
stem(nn3,xn3)
title('Funcion con Frecuencia de Muestreo(1.5*f0)')
xlabel('Variable discreta n');
ylabel('Amplitud');
