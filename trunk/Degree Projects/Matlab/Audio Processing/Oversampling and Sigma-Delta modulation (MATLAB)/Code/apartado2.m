%% Apartado 2

%Abrimos la se�al
x=abrir('extracto_orig.wav');

%Interpolacion de la se�al con frec. *4
x=interp(x,4);

%Cuantizamos la se�al con 1 bit (diferenciador de signo)
Nb=1;                       %Numero de bits
x=cuantizador(x,Nb);

%Decimamos y obtenemos la se�al de salida
y2=decimate(x,4);