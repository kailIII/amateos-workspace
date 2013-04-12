%%%%%%%%%%%% MEZCLA DE SEÑALES %%%%%%%%%%%%%%%%%

tiempos=0:1/fs:1;
fs=44100;
duracion=1;

%%% Generamos un tono puro de 500 Hz
tono_puro=sin(2*pi*500*tiempos);

%%% Le añadimos los 10 primeros armonicos al tono puro
tono=tono_puro;
for i=2:11
    muestras=sin(i*2*pi*500*tiempos);
    tono=tono+muestras;
end

%%% Generamos el ruido blanco con amplitud menor que el tono puro
ruido_blanco=50*normrnd(0,0.1,1,fs*duracion);

%%% Mezclamos el tono y el ruido blanco
mezcla=tono(1:44100)+ruido_blanco;

%%% Calculamos el espectro de la mezcla
fourier=fft(mezcla);
f=44100*(0:1:length(fourier)-1)/length(fourier);

%%% Representamos la mezcla de señales y el espectro
subplot(2,1,1);
plot(tiempos(1:150),mezcla(1:150));
title('Mezcla de tono con sus 10 primeros armonicos y ruido blanco');
xlabel('Tiempo');
ylabel('Amplitud');
subplot(2,1,2);
plot(f,abs(fourier));
title('Espectro de la mezcla');
xlabel('Frecuencia');
ylabel('Amplitud');
