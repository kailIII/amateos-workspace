x=load('senal.txt');
soundsc(x);
%Seleccionamos un segmento

y=x(4200:4500);

%Dibujamos ambas señales

figure

plot(x);
title('Representacion de la señal original')
xlabel('Variable');
ylabel('x');

figure

plot(y);
title('Representacion de un segmento de la señal original')
xlabel('Variable');
ylabel('x');

%Funcion de autocorrelacion

rxx=0;
N=301

for m=0:55
    rxx=0;
    for n=1:N-m
     
        rxx=rxx+(y(n)*y(n+m));
  end
  vectorcorr(m+1)=rxx;
end
vectorcorr=vectorcorr/N;


%Normalizacion de la correlacion

vectorpxx=vectorcorr./vectorcorr(1);
pxx=rxx/vectorcorr(1);
rxxteori=autocorr(y,55)

%Comprobacion

if autocorr(y,55)==vectorpxx'
    print('La correlacion y el resultado de Matlab son iguales');
end

L=[0:55];

figure

plot(L,vectorpxx,'r');
hold on;
plot(L,rxxteori,'g');
title('Correlacion con programa y matlab')
xlabel('Variable');
ylabel('x');

%Determinar el periodo de la señal

y2=autocorr(y,55);
maximo=-2;%La señal va de -1 a 1
periodo=0;
%no tomamos los extremos

for i=2:length(y2)-1
    if y2(i)>maximo&&y2(i)>y2(i-1)&&y2(i)<y2(i+1)
        
        periodo=i;
        maximo=y2(i);
    end
end
periodo


%Diseño de la señal con ruido k=100

ruido100=100*randn(length(y),1);
maximo100=-2;
periodo100=0;
yk100=y+ruido100;
y100=autocorr(yk100,55);

for i=2:length(y100)-1
    if y100(i)>maximo100&&y100(i)>y100(i-1)&&y100(i)<y100(i+1)
        periodo100=i;
        maximo100=y100(i);
    end
end

periodo100
maximo100

%Diseño de la señal con ruido k=250


ruido250=250*randn(length(y),1);
maximo250=-2;
periodo250=0;
yk250=y+ruido250;
y250=autocorr(yk250,55);

for i=2:length(y250)-1
    if y250(i)>maximo250&&y250(i)>y250(i-1)&&y250(i)<y250(i+1)
        
        periodo250=i;
        maximo250=y250(i);
    end
end
periodo250
maximo250

%Diseño de la señal con ruido k=500

ruido500=500*randn(length(y),1);
yk500=y+ruido500;
maximo500=-2;
periodo500=0;
y500=autocorr(yk500,55);

for i=2:length(y500)-1
    if y500(i)>maximo500&&y500(i)>y500(i-1)&&y500(i)<y500(i+1)
        
        periodo500=i;
        maximo500=y500(i);
    end
end
periodo500
maximo500