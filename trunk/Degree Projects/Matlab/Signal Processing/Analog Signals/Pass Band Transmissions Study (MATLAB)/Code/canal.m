%% SIMULACIÓN DEL CANAL PASO BANDA

% ENTRADA:
% xI -> equivalente In-phase de la señal que se transmite
% xQ -> equivalente en Cuadratura de la señal que se transmite
% hI -> equivalente In-phase de la respuesta al impulso del filtro
% hQ -> equivalente en Cuadratura de la respuesta al impulso del filtro

% SALIDA:
% yI -> equivalente In-phase de la señal tras pasar por el canal
% yQ -> euuivalente en Cuadratura de la señal tras pasar por el canal

function [yI,yQ] = canal (xI,xQ,hI,hQ)


yI = (conv(xI,hI)-conv(xQ,hQ))/2;
yQ = (conv(xI,hQ)+conv(xQ,hI))/2;