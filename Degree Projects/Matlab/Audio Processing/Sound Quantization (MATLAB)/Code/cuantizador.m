function [salida]=cuantizador(signal,bits)
N=length(signal);
salida=zeros(N,1);            %Creamos el vector de salida
q=2/(2^bits);                 %Niveles de cuantizacion

%Cuantizaci�n con un s�lo bit (diferenciador de signo)
if bits<2
    for i=1:N
        if signal(i)<0
            salida(i)=-0.5;
        else
            salida(i)=0.5;
        end
    end
else %cuantizaci�n con m�s de un bit
    for i=1:N
        salida(i)=floor(signal(i)/q)*q+(q/2);
    end
end

