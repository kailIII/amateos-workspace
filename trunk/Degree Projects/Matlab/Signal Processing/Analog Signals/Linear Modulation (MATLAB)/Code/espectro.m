%% ESPECTRO DE UNA SEÑAL

function [f, mx] = espectro(x,fs)

% ENTRADA:
% x -> señal de la que queremos obtener el espectro
% fs -> frecuencia de muestreo de la señal
%
% SALIDA:
% f -> vector de frecuencias (para representar)
% mx -> módulo del espectro

Lfft=length(x);
Lfft=2^ceil(log2(Lfft));

mx=abs(fftshift(fft(x,Lfft)));
f=(-Lfft/2:Lfft/2-1)/(Lfft/fs);
