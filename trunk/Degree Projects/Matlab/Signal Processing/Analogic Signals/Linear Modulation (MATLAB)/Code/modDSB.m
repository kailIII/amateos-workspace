%% MODULACIÓN DSB

function dsb = modDSB(m,fc,t)

% ENTRADA:
% moduladora -> señal mensaje
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
%
% SALIDA:
% dsb -> señal modulada en DSB

dsb=m.*cos(2*pi*fc*t);