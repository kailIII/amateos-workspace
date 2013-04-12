%%%%%%% GENERACION DE RUIDO BLANCO %%%%%%%%

tiempos=0:1/fs:15;
fs=44100;
duracion=15;

%%% Generamos el ruido blanco
ruido=normrnd(0,0.1,1,fs*duracion);

%%% Calculamos la PSD
fourier=fft(ruido);
psd=abs(fourier.*fourier)/(fs*length(fourier));
f=fs*(0:1:length(fourier)-1)/length(fourier);

%%% Representamos el ruido blanco
figure;
subplot(2,1,1);
plot(tiempos(1:fs*duracion), ruido);
title('Ruido blanco');
xlabel('Tiempo');
ylabel('Amplitud');

subplot(2,1,2);
semilogx(f,10*log10(psd));
title('PSD ruido blanco');
xlabel('Frecuencia (Hz)');
ylabel('Amplitud');
axis([10^(-1.2) 10^5 -140 -40]);

