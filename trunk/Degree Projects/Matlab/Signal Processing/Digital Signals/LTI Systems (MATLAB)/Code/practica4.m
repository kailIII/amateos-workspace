a=zeros(1,3);
b=zeros(1,3);

%Vector coeficientes x(n)
a(1)=0.3;
a(2)=0.6;
a(3)=0.3;

%Vector coeficientes y(n)
b(1)=1;
b(2)=0;
b(3)=0.9;

%Funcion impulso
impulso=zeros(1,128);
impulso(64)=1;

%Respuesta al impulso
y=filter(a,b,impulso)

%Funcion escalon
escalon=zeros(1,512);
for i=256:512,
   escalon(i)=3;
end
stem(escalon)

%Respuesta al escalon
n=-255:1:256
ye=filter(a,b,escalon)


%Respuesta en regimen permanente
g0=ye(512)%1.8947

%Repuesta transitoria
yt=zeros(1,50)
for j=1:50,
   yt(j)=ye(j+256)-g0;
end

%Representacion de las respuestas
figure;
subplot(3,1,1);
n=-63:1:64
stem(n,y)
title('respuesta al impulso');
subplot(3,1,2);
n=-255:1:256
stem(n,ye)
title('respuesta al escalon');
subplot(3,1,3);
stem(yt)
title('respuesta transitoria');

%EJERCICIO FUNCION FREQZ
%Representacion toda la circunferencia
figure
freqz(a, b, 512,'WHOLE')
%Representacion mitad superior de la circunferencia
figure
freqz(a, b, 512)

%El filtro es de tipo paso baja con polo doble complejo




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Repetimos lo anterior pero con otras ecuaciones en diferencias:

%Ecuacion a:
a1=zeros(1,3);
b1=zeros(1,3);

%Vector coeficientes x(n)
a1(1)=0.6;
a1(2)=-0.48;
a1(3)=0.48;
a1(4)=-1.6;

%Vector coeficientes y(n)
b1(1)=1;
b1(2)=0.13;
b1(3)=0.2;
b1(4)=0.3;


%Respuesta al impulso
y1=filter(a1,b1,impulso)


%Respuesta al escalon
n=-255:1:256
ye1=filter(a1,b1,escalon)


%Respuesta en regimen permanente
g0=ye1(512)
%Repuesta transitoria
yt1=zeros(1,50)
for j=1:50,
   yt1(j)=ye1(j+256)-g0;
end

%Representacion de las respuestas
figure;
subplot(3,1,1);
n=-63:1:64
stem(n,y1)
title('respuesta al impulso');
subplot(3,1,2);
n=-255:1:256
stem(n,ye1)
title('respuesta al escalon');
subplot(3,1,3);
stem(yt1)
title('respuesta transitoria');

%EJERCICIO FUNCION FREQZ
figure
freqz(a1, b1, 512,'WHOLE')
figure
freqz(a1, b1, 512)

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Ecuacion b:
a2=zeros(1,3);
b2=zeros(1,3);

%Vector coeficientes x(n)
a2(1)=10;
a2(2)=-5;
a2(3)=1;

%Vector coeficientes y(n)
b2(1)=1;
b2(2)=-5;
b2(3)=10;

%Respuesta al impulso
y2=filter(b2,a2,impulso)

%Respuesta al escalon
n=-255:1:256
ye2=filter(b2,a2,escalon)

%Respuesta en regimen permanente
g0=ye2(512)
%Repuesta transitoria
yt2=zeros(1,50)
for j=1:50,
   yt2(j)=ye2(j+256)-g0;
end

%Representacion de las respuestas
figure;
subplot(3,1,1);
n=-63:1:64
stem(n,y2)
title('respuesta al impulso');
subplot(3,1,2);
n=-255:1:256
stem(n,ye2)
title('respuesta al escalon');
subplot(3,1,3);
stem(yt2)
title('respuesta transitoria');

%EJERCICIO  FUNCION FREQZ
figure
freqz(b2, a2, 512,'WHOLE')
figure
freqz(b2, a2, 512)


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Ejercicio descomposicion en fracciones simples

%coeficientes a
a3=zeros(1,4);
a3(1)=1;
a3(2)=-0.8741;
a3(3)=0.9217;
a3(4)=0.2672;
%coeficientes b
b3=zeros(1,4);
b3(1)=0.1866;
b3(2)=0.2336;
b3(3)=0.2336;
b3(4)=0.1866;


%descomponemos en fracciones simples
[R,P,k]=residuez(b3,a3);

for n=1:100
    
   trans_inver(n) = R(1)*P(1)^n + R(2)*P(2)^n + R(3)*P(3)^n + k;
    
end

%calculamos los polos y los ceros
[ceros,polos,c]=tf2zp(b3,a3);

%respuesta en frecuencia compleja
figure
freqz(b3,a3,512,'whole');

%respuesta al impulso
impulso=zeros(1,100);
impulso(1)=1;

ri=filter(b3,a3,impulso);



figure
subplot(2,1,1)
plot(ri)
title('respuesta impulso');
subplot(2,1,2)
stem(trans_inver)
title('respuesta con factores simples');
