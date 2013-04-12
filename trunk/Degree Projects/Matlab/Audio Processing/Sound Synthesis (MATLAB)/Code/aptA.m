%%%%%SINTESIS DE TONOS PUROS%%%%%%%%%%

%% Declaramos la frecuencia de muestreo y el vector de tiempos para todos
%% los tonos

fs=44100;
tiempos=0:1/fs:1;


%%%%%%%%% GENERACION DE TONOS %%%%%%%%
%%%% generamos el tono de 100 HZ
tono100=sin(2*pi*100*tiempos);

%%%% generamos el tono de 3200 HZ
tono3200=sin(2*pi*3200*tiempos);

%%%% generamos el tono de 12800 HZ
tono12800=sin(2*pi*12800*tiempos);


%%%% representamos los tonos (500 muestras)
figure;
subplot(3,1,1);
plot(tiempos(1:500),tono100(1:500));
title('Tono 100 Hz');
xlabel('Tiempo');
ylabel('Amplitud');

subplot(3,1,2);
plot(tiempos(1:500),tono3200(1:500));
title('Tono 3200 Hz');
xlabel('Tiempo');
ylabel('Amplitud');

subplot(3,1,3);
plot(tiempos(1:500),tono12800(1:500));
title('Tono 12800 Hz');
xlabel('Tiempo');
ylabel('Amplitud');



%%%%%%% CALCULO DE ENERGIA Y PSD %%%%%%%%%%%%%

%%Energia y  PSD tono 100 Hz
fourier100=fft(tono100);
energia100=sum(abs(fourier100).^2)/(fs*length(fourier100));
psd100=abs(fourier100.*fourier100)/(fs*length(fourier100));
f100=fs*(0:1:length(fourier100)-1)/length(fourier100);


%%Energia y  PSD tono 3200 Hz
fourier3200=fft(tono3200);
energia3200=sum(abs(fourier3200).^2)/(fs*length(fourier3200));
psd3200=abs(fourier3200.*fourier3200)/(fs*length(fourier3200));
f3200=fs*(0:1:length(fourier3200)-1)/length(fourier3200);

%%Energia y  PSD tono 12800 Hz
fourier12800=fft(tono12800);
energia12800=sum(abs(fourier12800).^2)/(fs*length(fourier12800));
psd12800=abs(fourier12800.*fourier12800)/(fs*length(fourier12800));
f12800=fs*(0:1:length(fourier12800)-1)/length(fourier12800);

%% Representacion de las PSDs
figure;
subplot(3,1,1);
semilogx(f100(1:(length(fourier100)+1)/2),2*psd100(1:(length(fourier100)+1)/2));
title('PSD 100 Hz');
xlabel('Frecuencia');
ylabel('Amplitud');

subplot(3,1,2);
semilogx(f3200(1:(length(fourier3200)+1)/2),2*psd3200(1:(length(fourier3200)+1)/2));
title('PSD 3200 Hz');
xlabel('Frecuencia');
ylabel('Amplitud');

subplot(3,1,3);
semilogx(f12800(1:(length(fourier12800)+1)/2),2*psd12800(1:(length(fourier12800)+1)/2));
title('PSD 12800 Hz');
xlabel('Frecuencia');
ylabel('Amplitud');
