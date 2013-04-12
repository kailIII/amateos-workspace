function [salida_cuant]=delta_sigma(entrada)

%salida_integrador=zeros(length(entrada),1);
salida_cuant=zeros(length(entrada),1);

%Caso inicial: y(n-1)=0 -> salida integrador es y(n)=x(n)
salida_integrador=entrada(1);
salida_cuant(1)=floor(salida_integrador)+(1/2);       
   
%Demas casos        
for i=2:length(entrada)
    
    salida_integrador=(entrada(i)-salida_cuant(i-1))+salida_integrador;
    salida_cuant(i)=2*(floor(salida_integrador)+(1/2));       %Cuantizamos multiplicando por 2 para coger todo el rango dinamico
    
end