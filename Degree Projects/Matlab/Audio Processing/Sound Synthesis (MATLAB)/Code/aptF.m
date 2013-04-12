%%%%%%%%% GENERACION DE RUIDO ROSA %%%%%%%%%

tiempos=0:1/fs:10;
fs=44100;

%%% Generamos el ruido blanco
ruido_blanco=normrnd(0,0.1,1,fs*10);

%%% Definimos los ceros y polos del filtro
polos=[0.9986823; 0.9914651; 0.9580812; 0.8090598; 0.2896591];
ceros=[0.9963594 ;0.9808756; 0.9097290; 0.6128445 ;-0.0324723];

%%% Obtenemos los coeficientes de la funcion de transferencia
[num,den]=zp2tf(ceros,polos,1);

%%% Obtenemos el ruido rosa
ruido_rosa=filter(ceros,polos,ruido_blanco);



%%% Calculamos la PSD del ruido rosa
fourier_rosa=fft(ruido_rosa);
psd_rosa=abs(fourier_rosa.*fourier_rosa)/(fs*length(fourier_rosa));
f_rosa=fs*(0:1:length(fourier_rosa)-1)/length(fourier_rosa);

%%% Calculamos la PSD del ruido blanco
fourier_blanco=fft(ruido_blanco);
psd_blanco=abs(fourier_blanco.*fourier_blanco)/(fs*length(fourier_blanco));
f_blanco=fs*(0:1:length(fourier_blanco)-1)/length(fourier_blanco);

%%% Representamos el ruido rosa junto con su PSD
figure;
subplot(2,1,1)
plot(tiempos(1:441000),ruido_rosa);
title('Ruido rosa');
xlabel('Tiempo');
ylabel('Amplitud');
subplot(2,1,2)
semilogx(f_rosa,10*log10(psd_rosa));
title('PSD ruido rosa');
xlabel('Frecuencia');
ylabel('Amplitud (dB)');

%%% Representamos las PSDs
figure
subplot(2,1,1);
semilogx(f_rosa,10*log10(psd_rosa));
title('PSD ruido rosa');
xlabel('Frecuencia');
ylabel('Amplitud (dB)');
subplot(2,1,2);
semilogx(f_blanco,10*log10(psd_blanco));
title('PSD ruido blanco');
xlabel('Frecuencia');
ylabel('Amplitud (dB)');

