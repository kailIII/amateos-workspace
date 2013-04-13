%% MODULACIÓN SSB (USB)

function ssb = modSSB(m,fc,t)

% ENTRADA:
% m -> señal mensaje
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
%
% SALIDA:
% ssb -> señal modulada en SSB

%% EL CÓDIGO UTILIZADO NO ES EL MISMO QUE PROPORCIONA LATHI, ESTÁ REALIZADO
%% A PARTIR DEL DIAGRAMA DE LA TRANSPARENCIA 34 DEL TEMA 3. 

mh = imag(hilbert(m));     %Transformada de Hilbert

ssb = m.*cos(2*pi*fc*t) - mh.*sin(2*pi*fc*t);  % Obtención de USB
