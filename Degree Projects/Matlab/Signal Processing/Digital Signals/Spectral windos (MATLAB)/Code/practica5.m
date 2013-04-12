
%Tomamos un vector que sera el eje de freuencias(x) normalizado que nos vale para toda
%la practica
eje_norm=0:1:512-1;
eje_norm=eje_norm*2*pi/512;




%VENTANA RECTANGULAR:
ventana1=ones(1,9);

%Transformada
transformada1=fft(ventana1,512);
trans1=10*log10(abs(transformada1));

%Representacion ventana rectangular
subplot(2,1,1);
plot(ventana1)
title('ventana rectangular')
subplot(2,1,2);
plot(eje_norm,trans1)
title('fft')

%Busqueda de la anchura del lobulo central
m=find(trans1<3);%Buscamos los puntos de menos de 3dB
anchura1=2*eje_norm(m(1))%Para buscar la anchura cogemos y buscamos la muestra en el eje normalizado de modo que la anchura nos sale normalizada



%Busqueda de la altura del lobulo lateral
posicion=0;
altura1=0;
for i=2:1:512
   if (trans1(i)>trans1(i-1) & trans1(i)>trans1(i+1))
      if(trans1(i)>altura1)
         altura1=trans1(i);
         posicion=i;
      end
   end
end
posicion;
altura1;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%VENTANA TRIANGULAR
ventana2=triang(9);

%Transformada
transformada2=fft(ventana2,512);
trans2=10*log10(abs(transformada2));

%Representacion ventana triangular
figure;
subplot(2,1,1);
plot(ventana2)
title('ventana triangular')
subplot(2,1,2);
plot(eje_norm, trans2)
title('fft')

%Busqueda de la anchura del lobulo central
n=find(trans2<3);
anchura2=2*eje_norm(n(1))

%Busqueda de la altura del lobulo lateral
posicion2=0;
altura2=-100;%Ponemos un valor muy peque?o porque la altura puede que sea menor que 0
for i=2:1:511
   if (trans2(i)>trans2(i-1) & trans2(i)>trans2(i+1))
      if(trans2(i)>altura2)
         altura2=trans2(i);
         posicion2=i;
      end
   end
end
posicion2;
altura2

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%VENTANA HAMMING

ventana3=hamming(9);

%Transformada
transformada3=fft(ventana3,512);
trans3=10*log10(abs(transformada3));

%Representacion ventana HAMMING
figure;
subplot(2,1,1);
plot(ventana3)
title('ventana hamming')
subplot(2,1,2);
plot(eje_norm, trans3)
title('fft')

%Busqueda de la anchura lobulo central
p=find(trans3<3);
anchura3=2*eje_norm(p(1))

%Busqueda de la altura del lobulo lateral
posicion3=0;
altura3=-100;%Ponemos un valor muy peque?o porque la altura puede que sea menor que 0
for i=2:1:511
   if (trans3(i)>trans3(i-1) & trans3(i)>trans3(i+1))
      if(trans3(i)>altura3)
         altura3=trans3(i);
         posicion3=i;
      end
   end
end
posicion3;
altura3


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%VENTANA HANNING

ventana4=hanning(9);

%Transformada
transformada4=fft(ventana4,512);
trans4=10*log10(abs(transformada4));

%Representacion ventana hanning
figure;
subplot(2,1,1);
plot(ventana4)
title('ventana hanning')
subplot(2,1,2);
plot(eje_norm, trans4)
title('fft')

%Busqueda de la anchura del lobulo central 
q=find(trans4<3);
anchura4=2*eje_norm(q(1))

%Busqueda de la altura del lobulo lateral
posicion4=0;
altura4=-100;%Ponemos un valor muy peque?o porque la altura puede que sea menor que 0
for i=2:1:511
   if (trans4(i)>trans4(i-1) & trans4(i)>trans4(i+1))
      if(trans4(i)>altura4)
         altura4=trans4(i);
         posicion4=i;
      end
   end
end
posicion4;
altura4


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%VENTANA BLACKMAN
ventana5=blackman(9);


%Transformada
transformada5=fft(ventana5,512);
trans5=10*log10(abs(transformada5));

%Representacion ventana blackman
figure;
subplot(2,1,1);
plot(ventana5)
title('ventana blackman')
subplot(2,1,2);
plot(eje_norm, trans5)
title('fft')

%Busqueda de la anchura de la ventana blackman
r=find(trans5<3);
anchura5=2*eje_norm(r(1))

%Busqueda de la altura de la ventana blackman
posicion5=0;
altura5=-100;%Ponemos un valor muy peque?o porque la altura puede que sea menor que 0
for i=2:1:511
   if (trans5(i)>trans5(i-1) & trans5(i)>trans5(i+1))
      if(trans5(i)>altura5)
         altura5=trans5(i);
         posicion5=i;
      end
   end
end
posicion5;
altura5