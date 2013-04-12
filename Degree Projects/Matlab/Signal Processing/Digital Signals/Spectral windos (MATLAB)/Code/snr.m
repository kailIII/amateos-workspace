function [resultado] = snr(x,e)

% Entrada:
% x-> Vector de valores de la señal cuatizada
% e-> Vector de valores del error

% Salida:
% resultado-> Valor de la operacion de Snr

% Iniciamos los contadores

Sumatoriox=0;
Sumatorioe=0;

% Represenacion grafica

%Representacion grafica primera frecuencia de x(n)

subplot(3,1,1)
plot(x)
title('Señal cuatizada')
xlabel('Variable discreta n');
ylabel('x(n)');

%Representacion grafica primera frecuencia de e(n)

subplot(3,1,3)
plot(e)
title('Error')
xlabel('Variable discreta n');
ylabel('e(n)');



% Almacenamos los valores

for i=1:length(x)
    
    Sumatoriox=Sumatoriox + x(i)^2;
    Sumatorioe=Sumatorioe + e(i)^2;
    
end

%Aseguamos que el error no es cero

if Sumatorioe~=0 && Sumatoriox/Sumatorioe > 0
   
    resultado=10*(log10(Sumatoriox/Sumatorioe));
else
   
    resultado=0;
end
    
