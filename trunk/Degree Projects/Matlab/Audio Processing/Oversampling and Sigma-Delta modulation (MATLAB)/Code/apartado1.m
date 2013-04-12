%% Apartado 1

%Abrimos la señal
x=abrir('extracto_orig.wav');

%% Cuantizador uniforme
%Cuantizamos la señal
Nb=4;                       %Numero de bits
y1=cuantizador(x,Nb);


