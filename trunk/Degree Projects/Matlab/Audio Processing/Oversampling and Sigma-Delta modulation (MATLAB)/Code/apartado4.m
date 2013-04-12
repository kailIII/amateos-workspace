%% Apartado 4
%Abrimos la señal
x=abrir('extracto_orig.wav');

%Interpolacion de la señal con frec. *4
x=interp(x,4);

%Cuantizamos la señal
x_cuant=delta_sigma2(x);

%Decimamos
y4=decimate(x_cuant,4);