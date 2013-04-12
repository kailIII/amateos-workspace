%% MODULACIÓN AM

function am = modAM(m,fc,t)

% ENTRADA:
% m -> señal mensaje
% fc -> frecuencia de la señal portadora
% t -> vector de tiempos con el que fue generada la señal moduladora
%
% SALIDA:
% am -> señal modulada en AM

am = (max(m)+m).*cos (2*pi*fc*t);  % La amplitud de la portadora es la misma que la de la señal mensaje
                                   % Si no se toma una amplitud de portadora >= que la de la señal mensaje,
                                   % se produce una distorsión.
