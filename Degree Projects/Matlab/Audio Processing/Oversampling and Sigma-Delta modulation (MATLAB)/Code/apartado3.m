%% Apartado 3
%Abrimos la se�al
x=abrir('extracto_orig.wav');

%Interpolacion de la se�al con frec. *4
x=interp(x,4);

%Cuantizamos la se�al
x_cuant=delta_sigma(x);

%Decimamos
y3=decimate(x_cuant,4);

