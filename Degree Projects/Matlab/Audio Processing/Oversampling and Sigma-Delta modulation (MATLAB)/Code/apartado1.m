%% Apartado 1

%Abrimos la se�al
x=abrir('extracto_orig.wav');

%% Cuantizador uniforme
%Cuantizamos la se�al
Nb=4;                       %Numero de bits
y1=cuantizador(x,Nb);


