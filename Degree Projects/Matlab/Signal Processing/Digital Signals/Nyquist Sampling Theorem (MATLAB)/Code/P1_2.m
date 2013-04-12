%2.1 Realice los siguientes ejercicios:

%- a) Generar y representar las siguientes secuencias: 

%Implementacion Se–al X1

L=21;%Longitud 
nn1=0:(L-1);%Vector de tiempos
imp1=zeros(L,1);%Matriz de cero con L filas y 1 columna
imp1(6)=0.9;%Localizamos el punto donde queremos el impulso, en nuestro caso sera la posicion 6 que representa a n=5


%Implementacion Se–al X2

L=11;%Longitud 
nn2=-10:0;%Vector de tiempos
imp2=zeros(L,1);%Matriz de cero con L filas y 1 columna
imp2(4)=4.5;%Localizamos el punto donde queremos el impulso, en nuestro caso sera la posicion 6 que representa a n=5

%Representacion grafica


subplot(2,1,1)
stem(nn1,imp1)
title('Representacion de la se–al X1')
xlabel('Variable discreta n');
ylabel('Amplitud');


subplot(2,1,2)
stem(nn2,imp2)
title('Representacion de la se–al X2')
xlabel('Variable discreta n');
ylabel('Amplitud');


%- b) Genere y represente graficamente un tren de impulsos periodicos cuya
%amplitud sea 2, periodo 5 y longitud 50.

%Tren de impulsos

M=50;%Longitud
A=2;%Amplitud
P=5;%Periodo
nn=(0:1:((M-1)*P));%Vector de tiempos, de 0 a M-1
imp=zeros(M,1);%Matriz de Ceros de M filas y una columna

%Bucle donde se genera un tren de pulsos de amplitud A(2), en intervalos de
%a 5, hasta M-1(Periodo), porque empezamos en 0 se le resta 1

for l=1:5:(M*P)
    imp(l)=A;
end

%Representacion grafica

stem(nn,imp);
title('Representacion del tren de impulsos')
xlabel('Variable discreta n');
ylabel('Amplitud');


