function [salida_cuant]=delta_sigma2(entrada)

salida_cuant=zeros(length(entrada),1);

%Caso inicial:
salida_integrador1=entrada(1);
salida_integrador2=entrada(1);
salida_cuant(1)=sign(salida_integrador2);

%Demas casos        
for i=2:length(entrada)
   salida_integrador1=entrada(i)-salida_cuant(i-1)+salida_integrador1;
   salida_integrador2=salida_integrador1-salida_cuant(i-1)+salida_integrador2;
   salida_cuant(i)=sign(salida_integrador2);
    
end