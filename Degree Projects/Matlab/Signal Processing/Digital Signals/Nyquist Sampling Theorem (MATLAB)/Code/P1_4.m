%4 Funciones exponenciales complejas

%4.1 Realice los siguientes ejercicios:

%- a)  Utilice la funcion genexp para generar una exponencial decreciente y
%despues sumar sus valores

y=genexp(3,0,12)%Generamos la funcion
suma=sum(y)%Sumamos los valores
n=1:12
stem(n,y)
title('Exponencial decreciente');
%- b) Representar graficamente las partes real e imaginaria asi como la
%parte real en funcion de la parte imaginaria de la funcion

nn=[-10:1:10];%Vector de tiempos
x=3*sin((pi/7)*nn)+j*4*cos((pi/7)*nn);%Implementacion de la funcion

%Representacion grafica

% Parte real
subplot(2,1,1);
stem(nn,real(x));
title('Parte Real')
xlabel('Variable discreta n');
ylabel('Parte real de x');

% Parte imaginaria
subplot(2,1,2);
stem(nn,imag(x));
title('Parte imaginaria')
xlabel('Variable discreta n');
ylabel('Parte imaginaria de x');

% Parte real Vs Parte imaginaria

stem(imag(x),real(x));
title('Parte Real Vs Parte Imaginaria')
xlabel('Parte imaginaria de x');
ylabel('Parte real de x');



