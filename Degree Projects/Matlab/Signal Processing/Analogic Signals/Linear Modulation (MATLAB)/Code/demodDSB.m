%% DEMODULACIÓN DSB

function rec = demodDSB(dsb,fs,fc,t,BW)

% ENTRADA:
% dsb -> señal modulada en DSB
% fs -> frecuencia de muestreo
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
% BW -> ancho de banda de la señal
%
% SALIDA:
% rec -> señal recuperada tras la demodulación DSB

dem = dsb.*cos(2*pi*fc*t)*2;

% Filtrado de orden 40 para recuperar la señal
h=fir1(40, BW/fs);
rec=filter(h,1,dem);


