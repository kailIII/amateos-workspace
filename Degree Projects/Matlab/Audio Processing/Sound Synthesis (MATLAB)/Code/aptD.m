%%%%%%%%% SUMA DE TONOS %%%%%%%%%%%%

%% Declaramos la frecuencia de muestreo y el vector de tiempos para todos
%% los tonos

fs=44100;
tiempos=0:1/fs:1;

%%% Declaramos la frecuencia central y las frecuencias
fc=500;

f1=fc-100;
f2=fc+100;
f3=fc-25;
f4=fc+25;
f5=fc-2.5;
f6=fc+2.5;

%%% Generamos los tonos para cada una de las frecuencias

tono1=sin(2*pi*f1*tiempos);
tono2=sin(2*pi*f2*tiempos);
tono3=sin(2*pi*f3*tiempos);
tono4=sin(2*pi*f4*tiempos);
tono5=sin(2*pi*f5*tiempos);
tono6=sin(2*pi*f6*tiempos);

%%% Sumamos los tonos

%200 Hz
tono12=tono1+tono2;

%50 Hz
tono34=tono3+tono4;

%5 Hz
tono56=tono5+tono6;

%%% Calculamos los espectros de cada una de las sumas de tonos

fourier12=fft(tono12);
f12=44100*(0:1:length(fourier12)-1)/length(fourier12);

fourier34=fft(tono34);
f34=44100*(0:1:length(fourier34)-1)/length(fourier34);

fourier56=fft(tono56);
f56=44100*(0:1:length(fourier56)-1)/length(fourier56);


%%% Hacemos la representacion grafica de cada una de las sumas de tonos con
%%% sus respectivos espectros

%200 Hz
figure;
subplot(2,1,1);
plot(tiempos(1:150),tono12(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Suma tonos para anchura 200 Hz');
subplot(2,1,2);
semilogx(f12,abs(fourier12));
title('Espectro');
xlabel('Frecuencias');
ylabel('Amplitud');

%50 Hz
figure;
subplot(2,1,1);
plot(tiempos(1:150),tono34(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Suma tonos para anchura 50 Hz');
subplot(2,1,2);
semilogx(f34,abs(fourier34));
title('Espectro');
xlabel('Frecuencias');
ylabel('Amplitud');

%5 Hz
figure;
subplot(2,1,1);
plot(tiempos(1:150),tono56(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Suma tonos para anchura 5 Hz');
subplot(2,1,2);
semilogx(f56,abs(fourier56));
title('Espectro');
xlabel('Frecuencias');
ylabel('Amplitud');


%%% Escuchamos los tonos
soundsc(tono12,44100);
soundsc(tono34,44100);
soundsc(tono56,44100);