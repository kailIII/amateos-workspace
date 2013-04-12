%%%%%%% SONIDOS ARMONICOS %%%%%%%%%%%%%%

fs=44100;
tiempos=0:1/fs:1;
f_fund=1000;
tono=sin(2*pi*f_fund*tiempos);

%%% Generamos el tono de 1000 Hz y le sumamos sus 50 primeros armonicos
tono1=tono;

for i=2:51
    muestras=sin(i*2*pi*f_fund*tiempos);
    tono1=tono1+muestras;
end

%%% Generamos el tono de 1000 Hz y le sumamos sus 50 primeros armonicos con
%%% amplitud disminuida
tono2=tono;

for i=2:51
    muestras=sin(i*2*pi*f_fund*tiempos)/i;
    tono2=tono2+muestras;
end


%%% Representamos los tonos con sus armonicos
figure;
subplot(2,1,1)
plot (tiempos(1:150),tono1(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Tono de 1000 Hz con sus 50 armonicos');
subplot(2,1,2)
plot (tiempos(1:150),tono2(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Tono de 1000 Hz con sus 50 armonicos disminuidos en amplitud');


%%% Calculamos los espectros
fourier1=fft(tono1);
f1=44100*(0:1:length(fourier1)-1)/length(fourier1);

fourier2=fft(tono2);
f2=44100*(0:1:length(fourier2)-1)/length(fourier2);

%%% Representamos los espectros
figure;
subplot(2,1,1);
plot(f1,abs(fourier1));
title('Espectro tono con sus 50 armonicos');
xlabel('Frecuencias');
ylabel('Amplitud');
subplot(2,1,2);
plot(f2,abs(fourier2));
title('Espectro tono con sus 50 armonicos disminuidos en amplitud');
xlabel('Frecuencias');
ylabel('Amplitud');



%%% ARMONICOS PARES E IMPARES %%%

%%% Armonicos pares
tono3=tono;

for i=2:2:51
    muestras=sin(i*2*pi*f_fund*tiempos)/i;
    tono3=tono3+muestras;
end

%%% Armonicos impares
tono4=tono;

for i=3:2:51
    muestras=sin(i*2*pi*f_fund*tiempos)/i;
    tono4=tono4+muestras;
end

%%% Representamos los tonos con sus armonicos pares e impares
figure;
subplot(2,1,1)
plot (tiempos(1:150),tono3(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Tono de 1000 Hz con sus 50 armonicos pares disminuidos en amplitud');
subplot(2,1,2)
plot (tiempos(1:150),tono4(1:150));
xlabel('Tiempo');
ylabel('Amplitud');
title('Tono de 1000 Hz con sus 50 armonicos impares disminuidos en amplitud');

%%% Calculamos los espectros
fourier3=fft(tono3);
f3=44100*(0:1:length(fourier3)-1)/length(fourier3);

fourier4=fft(tono4);
f4=44100*(0:1:length(fourier4)-1)/length(fourier4);

%%% Representamos los espectros
figure;
subplot(2,1,1);
plot(f3,abs(fourier3));
title('Espectro tono con sus 50 armonicos pares disminuidos en amplitud');
xlabel('Frecuencias');
ylabel('Amplitud');
subplot(2,1,2);
plot(f4,abs(fourier4));
title('Espectro tono con sus 50 armonicos impares disminuidos en amplitud');
xlabel('Frecuencias');
ylabel('Amplitud');
