%% CONVERSIÓN CUADRATURA (BANDA BASE) A PASO BANDA

% ENTRADA:
% fc -> frecuencia de los osciladores locales
% fs -> frecuencia de muestreo
% gI -> equivalente In-phase
% gQ -> equivalente en Cuadratura

% SALIDA:
% g -> señal paso banda
function [g] = cuadraturaAbanda (fc,fs,gI,gQ)

t=0:1/fs:0.2;

% Osciladores locales
oscSeno = sin(2*pi*fc*t);
oscCoseno = cos(2*pi*fc*t);

% Obtención de la señal paso banda

g = (gI.*oscCoseno) - (gQ.*oscSeno);
