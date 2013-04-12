%ESTE ES EL MUESTREO VALIDO !!!!!

%Le meteremos los tiempos inicial=0, final=2, fmuestreo=2*fo, 5*fo y 1.5*fo,
%a=1, fo=20 y teta=0

%muestreo2(0,2,40,1,20,0)
%muestreo2(0,2,100,1,20,0)
%muestreo2(0,2,30,1,20,0)

function y = muestreo2(tinicial,tfinal,fmuestreo,a,f0,teta)

%Vector de tiempos del muestreo
nn=[tinicial:1/fmuestreo:tfinal];

%funcion en tiempo continuo
senal=a*cos((2*pi*f0*nn)+teta);

%Representamos dichos valores
subplot(2,1,1)
stem(nn,senal)
title('Señal muestreada')

%Definimos el numero de puntos que queremos usar para el calculo de la
%transformada
nnfft=256;

%Hallamos la transformada de Fourier.
modx=abs(fft(senal, nnfft));

%Hallamos el vector de tiempos de la frecuencia
frec=0:fmuestreo/(nnfft-1):fmuestreo;

%Representamos el espectro
subplot(2,1,2)
plot(frec,modx)
title('Espectro de la señal')
