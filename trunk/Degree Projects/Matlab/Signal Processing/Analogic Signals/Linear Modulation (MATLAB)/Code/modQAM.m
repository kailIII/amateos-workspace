%% MODULACIÓN AM

function qam = modQAM(m1,m2,fc,t)

% ENTRADA:
% m1/2 -> señales mensaje
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
%
% SALIDA:
% qam -> señal modulada en QAM

qam=m1.*cos(2*pi*fc*t)+m2.*sin(2*pi*fc*t);
