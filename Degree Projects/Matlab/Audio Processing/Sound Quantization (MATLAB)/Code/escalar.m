function [signal_e,vector_max]=escalar(signal,tam)

N=length(signal);
numero_segmentos=ceil(N/tam);
vector_max=zeros(1,numero_segmentos);
signal_e=zeros(1,N);

for i=1:numero_segmentos-1
    limite_inferior=(i-1)*tam +1;
    limite_superior=i*tam;
    vector_max(i)=max(abs(signal(limite_inferior:limite_superior)));
    signal_e(limite_inferior:limite_superior)=signal(limite_inferior:limite_superior)/vector_max(i);
end
limite_inferior=(numero_segmentos-1)*tam +1;
limite_superior=N;
vector_max(numero_segmentos)=max(abs(signal(limite_inferior:limite_superior)));
signal_e(limite_inferior:limite_superior)=signal(limite_inferior:limite_superior)/vector_max(numero_segmentos);