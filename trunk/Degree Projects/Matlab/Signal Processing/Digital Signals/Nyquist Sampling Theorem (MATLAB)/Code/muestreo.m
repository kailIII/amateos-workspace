function [nn,xn]= muestreo(A,f0,tita,tin,tfin,fmuestreo)
%Entrada:

%Amplitud->A
%Frecuencia->f0
%Fase->tita
%Tiempo final de muestreo->tfin
%Tiempo inicial de muestreo->tin
%Frecuencia de muestreo (fmuestreo*f0)->fmuestreo

nn=tin:(1/(fmuestreo*f0)):tfin;%Vector de tiempos
xn=A*cos(2*pi*f0*nn+tita);%Señal muestreada

%Salida:
%nn-> Vector de tiempos
%xn-> Señal muestreada
