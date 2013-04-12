function [salida]=desescalar(signal,vector_max,tam)
tam=128;
N=length(signal);
numero_segmentos=ceil(N/tam);
salida=zeros(1,N);
   
    for i=1:numero_segmentos-1
        limite_inferior=(i-1)*tam +1;
        limite_superior=i*tam;
        salida(limite_inferior:limite_superior)=signal(limite_inferior:limite_superior)*vector_max(i);
    end
    limite_inferior=(numero_segmentos-1)*tam +1;
    limite_superior=N;
    salida(limite_inferior:limite_superior)=signal(limite_inferior:limite_superior)*vector_max(numero_segmentos);

