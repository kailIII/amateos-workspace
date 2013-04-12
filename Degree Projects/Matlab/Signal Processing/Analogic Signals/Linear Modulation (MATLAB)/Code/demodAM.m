%% DEMODULACIÓN AM

function rec = demodAM(am,fs,BW)

% ENTRADA:
% am -> señal modulada en AM
% fs -> frecuencia de muestreo
% BW -> ancho de banda de la señal
%
% SALIDA:
% rec -> señal recuperada tras la demodulación DSB

% Detector cuadrático
dem=am.*(am>0);

% Filtrado de orden 40 para recuperar la señal
h=fir1(40, BW/fs);
rec=filter(h,1,dem);

%% LA SIGUIENTE SENTENCIA NO APARECE EN EL EJEMPLO DEL LATHI
rec=rec*pi; % La salida del detector tiene una atenuación de 1/pi -> debemos
            % amplificar la señal para obtener la misma amplitud inicial.