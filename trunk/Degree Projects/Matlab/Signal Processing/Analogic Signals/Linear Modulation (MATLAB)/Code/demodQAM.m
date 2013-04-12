%% DEMODULACIÓN QAM

function [rec1, rec2] = demodQAM(qam,fs,fc,t,BW)

% ENTRADA:
% qam -> señal modulada en QAM
% fs -> frecuencia de muestreo
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
% BW -> ancho de banda de la señal
%
% SALIDA:
% rec1/2 -> señales recuperadas tras la demodulación QAM

% Rectificación
dem1 = qam.*cos(2*pi*fc*t)*2;
dem2 = qam.*sin(2*pi*fc*t)*2;

% Filtrado de orden 40 para recuperar la señal
h=fir1(40, BW/fs);
rec1=filter(h,1,dem1);
rec2=filter(h,1,dem2);