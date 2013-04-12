function [salida,max]=norm(entrada)

salida=zeros(1,length(entrada));
max=zeros(1,(ceil(length(entrada)/128)));

for i=1:(ceil(length(entrada)/128))-1
    max(i)=max(abs(entrada(((i-1)*128 +1):(i*128))));
    salida(((i-1)*128 +1):(i*128))=entrada(((i-1)*128 +1):(i*128))/max(i);
end

max((ceil(length(entrada)/128)))=max(abs(entrada((((ceil(length(entrada)/128))-1)*128 +1):(length(entrada)))));
salida((((ceil(length(entrada)/128))-1)*128 +1):(length(entrada)))=entrada((((ceil(length(entrada)/128))-1)*128 +1):(length(entrada)))/max((ceil(length(entrada)/128)));