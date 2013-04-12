function [salida,maximos]=normalizar(senal)

salida=zeros(1,length(senal));
maximos=zeros(1,ceil(length(senal)/128));

for i=1:ceil(length(senal)/128)-1
    inferior=(i-1)*128 +1;
    superior=i*128;
    maximos(i)=max(abs(senal(inferior:superior)));
    salida(inferior:superior)=senal(inferior:superior)/maximos(i);
end

maximos(ceil(length(senal)/128))=max(abs(senal((ceil(length(senal)/128)-1)*128 +1:length(senal))));
salida((ceil(length(senal)/128)-1)*128 +1:length(senal))=senal((ceil(length(senal)/128)-1)*128 +1:length(senal))/maximos(ceil(length(senal)/128));