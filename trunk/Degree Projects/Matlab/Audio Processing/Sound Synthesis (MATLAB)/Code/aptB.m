%%%%%%%%%%%% CAMBIOS DE FASE %%%%%%%%%%%%%%%%

fs=44100;
tiempos=0:1/fs:1;

%%%% Generamos el tono de 500 Hz
tono=sin(2*pi*500*tiempos);

%%% Generamos los tonos desfasados
tono1=sin((2*pi*500*tiempos)+pi/4);
tono2=sin((2*pi*500*tiempos)+2*pi/4);
tono3=sin((2*pi*500*tiempos)+3*pi/4);
tono4=sin((2*pi*500*tiempos)+4*pi/4);
tono5=sin((2*pi*500*tiempos)+5*pi/4);
tono6=sin((2*pi*500*tiempos)+6*pi/4);
tono7=sin((2*pi*500*tiempos)+7*pi/4);
tono8=sin((2*pi*500*tiempos)+8*pi/4);

%%% Representamos los tonos

figure;
plot(tiempos(1:150),tono(1:150),'r');
hold on
plot(tiempos(1:150),tono1(1:150),'b');
hold on
plot(tiempos(1:150),tono2(1:150),'y');
hold on
plot(tiempos(1:150),tono3(1:150),'g');
hold on
plot(tiempos(1:150),tono4(1:150),'m');
hold on
plot(tiempos(1:150),tono5(1:150),'c');
hold on
plot(tiempos(1:150),tono6(1:150),'k');
hold on
plot(tiempos(1:150),tono7(1:150),'s');
hold on
plot(tiempos(1:150),tono8(1:150));
hold on
xlabel('Tiempo');
ylabel('Amplitud');
title('Representacion de los tonos con sus desfases');



%%% Comprobamos que ocurre si sumamos dos señales desfasadas en pi/4
tonosumapi4=tono+tono1;
%%% Comprobamos que ocurre si sumamos dos señales desfasadas en pi/4
tonosumapi2=tono+tono2;
%%% Comprobamos que ocurre si sumamos dos señales desfasadas en pi/4
tonosumapi=tono+tono4;

%%% Representamos la suma de los tonos

figure;
subplot(3,1,1)
plot(tiempos(1:150),tonosumapi4(1:150));
title('Suma de tonos desfasados pi/4');
xlabel(' Tiempo');
ylabel('Amplitud');
subplot(3,1,2)
plot(tiempos(1:150),tonosumapi2(1:150));
title('Suma de tonos desfasados pi/2');
xlabel(' Tiempo');
ylabel('Amplitud');
subplot(3,1,3)
plot(tiempos(1:150),tonosumapi(1:150));
xlabel(' Tiempo');
ylabel('Amplitud');
title('Suma de tonos desfasados pi');
axis([0 tiempos(150) -2 2]);