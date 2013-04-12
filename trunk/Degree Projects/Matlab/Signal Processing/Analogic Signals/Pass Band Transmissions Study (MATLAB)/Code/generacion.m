%% GENERACIÓN DE LAS SEÑALES
%
% ENTRADA:
% fc -> frecuencia de la portadora
% fs -> frecuencia de muestreo a utilizar
%
% SALIDA:
% signals -> matriz que contiene todas las señales generadas. Cada fila es
% una señal.

function [signals] = generacion(fc, fs)

t=0:1/fs:0.2;

signals = zeros(6,length(t)); 
%armonicoPuro 
signals(1,:) = sin(2*pi*fc*t);
%sumaArmonicos
signals(2,:) = sin(2*pi*fc*t) + 0.5*sin(2*pi*(fc-1000)*t);
%diente
signals(3,:) = sawtooth(2*pi*fc*t);
%triangular
signals(4,:) = sawtooth(2*pi*fc*t,0.5);
%pulsos
signals(5,:) = square(2*pi*fc*t);
%AM
moduladora = sin(2*pi*100*t); % La señal moduladora es una senoidal a 100 Hz
signals(6,:) = ammod(moduladora, fc,fs);
%FM
signals(7,:) = fmmod(moduladora, fc,fs,300); 
