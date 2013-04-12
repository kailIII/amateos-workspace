%3 Generacion de se–ales sinusoidales

%3.1 Realice los siguientes ejercicios: Genere y represente graficamente
%cada una de las siguientes secuencias.

%Creamos los vectores de tiempos:
nn1=[0:1:25];

nn2=[-10:1:10];

nn3=[0:1:50];

%Creamos las se–ales

x1=sin((pi/17)*nn1);

x2=sin((pi/2)+3*pi*nn2);

x3=cos((pi/sqrt(23))*nn3);

%Representacion grafica Introducimos dentro del stem la funcion, donde
%variamos n con la creada anteriormente

subplot(2,2,1)
stem(nn1,x1);
title('Representacion X1')
xlabel('Variable discreta n');
ylabel('Amplitud');


subplot(2,2,2)
stem(nn2,x2);
title('Representacion X2')
xlabel('Variable discreta n');
ylabel('Amplitud');


subplot(2,2,3)
stem(nn3,x3);
title('Representacion X3')
xlabel('Variable discreta n');
ylabel('Amplitud');



