%% Apartado 2

%Abrimos la señal
x=abrir('extracto_orig.wav');

%Interpolacion de la señal con frec. *4
x=interp(x,4);

%Cuantizamos la señal con 1 bit (diferenciador de signo)
Nb=1;                       %Numero de bits
x=cuantizador(x,Nb);

%Decimamos y obtenemos la señal de salida
y2=decimate(x,4);