%% DEMODULACIÓN SSB

function rec = demodSSB(ssb,fs,fc,t,BW)

% ENTRADA:
% ssb -> señal modulada en SSB
% fs -> frecuencia de muestreo
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
% BW -> ancho de banda de la señal
%
% SALIDA:
% rec -> señal recuperada tras la demodulación DSB

dem = ssb.*cos(2*pi*fc*t)*2;

% Filtrado de orden 40 para recuperar la señal
h=fir1(40, BW/fs);
rec=filter(h,1,dem);

