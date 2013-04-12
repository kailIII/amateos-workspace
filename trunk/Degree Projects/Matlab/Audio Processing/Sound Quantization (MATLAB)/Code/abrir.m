function [salida]=abrir(signal)
x=wavread(signal);
x_left=x(:,1);              %Canal izquierdo del estereo
x_right=x(:,2);             %Canal derecho del estereo
salida=(x_left+x_right)/2;  %Señal media de los canales
