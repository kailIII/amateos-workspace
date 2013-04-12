function [salida]=desnormalizar(entrada,max)

salida=zeros(1,length(entrada));
   
    for i=1:ceil(length(entrada)/128)-1
        salida((i-1)*128 +1:i*128)=entrada((i-1)*128 +1:i*128)*max(i);
    end
salida((ceil(length(entrada)/128)-1)*128 +1:length(entrada))=entrada((ceil(length(entrada)/128)-1)*128 +1:length(entrada))*max(ceil(length(entrada)/128));

