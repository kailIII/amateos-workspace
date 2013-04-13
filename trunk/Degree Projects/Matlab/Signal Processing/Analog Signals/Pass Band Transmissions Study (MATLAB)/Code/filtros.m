%% CARGA DE LOS FILTROS Y OBTENCIÓN DE LA RESPUESTA AL IMPULSO DE CADA UNO
% ENTRADA:
% fs -> frecuencia de muestreo
% fc -> frecuencia de portadora
%
% SALIDA:
% respuestasInphase -> matriz que contiene en cada fila la respuesta
% equivalente In phase para cada filtro
% respuestasCuadratura -> matriz que contiene en cada fila la respuesta
% equivalente en cuadratura para cada filtro

% function [respuestasInphase respuestasCuadratura] = filtros (fs,fc)

fc=10000;
fs=500000;
t=0:1/fs:0.2;
numFiltros = 27;

% Generación señal impulso
imp = [1 zeros(1, (length(t)-1))];

% Carga de filtros
load('filtros/canal/ab2.mat');
load('filtros/canal/ab4.mat');
load('filtros/canal/ab10.mat');
load('filtros/canal/ac2.mat');
load('filtros/canal/ac4.mat');
load('filtros/canal/ac10.mat');
load('filtros/canal/ae2.mat');
load('filtros/canal/ae4.mat');
load('filtros/canal/ae10.mat');

load('filtros/canal/bb2.mat');
load('filtros/canal/bb4.mat');
load('filtros/canal/bb10.mat');
load('filtros/canal/bc2.mat');
load('filtros/canal/bc4.mat');
load('filtros/canal/bc10.mat');
load('filtros/canal/be2.mat');
load('filtros/canal/be4.mat');
load('filtros/canal/be10.mat');

load('filtros/canal/cb2.mat');
load('filtros/canal/cb4.mat');
load('filtros/canal/cb10.mat');
load('filtros/canal/cc2.mat');
load('filtros/canal/cc4.mat');
load('filtros/canal/cc10.mat');
load('filtros/canal/ce2.mat');
load('filtros/canal/ce4.mat');
load('filtros/canal/ce10.mat');

% Obtención de respuestas al impulso
respuestas = zeros(numFiltros,length(t));

respuestas(1,:) = filter(ab2,imp);
respuestas(2,:) = filter(ab4,imp);
respuestas(3,:) = filter(ab10,imp);
respuestas(4,:) = filter(ac2,imp);
respuestas(5,:) = filter(ac4,imp);
respuestas(6,:) = filter(ac10,imp);
respuestas(7,:) = filter(ae2,imp);
respuestas(8,:) = filter(ae4,imp);
respuestas(9,:) = filter(ae10,imp);
respuestas(10,:) = filter(bb2,imp);
respuestas(11,:) = filter(bb4,imp);
respuestas(12,:) = filter(bb10,imp);
respuestas(13,:) = filter(bc2,imp);
respuestas(14,:) = filter(bc4,imp);
respuestas(15,:) = filter(bc10,imp);
respuestas(16,:) = filter(be2,imp);
respuestas(17,:) = filter(be4,imp);
respuestas(18,:) = filter(be10,imp);
respuestas(19,:) = filter(cb2,imp);
respuestas(20,:) = filter(cb4,imp);
respuestas(21,:) = filter(cb10,imp);
respuestas(22,:) = filter(cc2,imp);
respuestas(23,:) = filter(cc4,imp);
respuestas(24,:) = filter(cc10,imp);
respuestas(25,:) = filter(ce2,imp);
respuestas(26,:) = filter(ce4,imp);
respuestas(27,:) = filter(ce10,imp);

% figure
% stem(t,respuestas(1,:))
% title('respuesta')
% pause;
% 
% % Obtención de las preenvolventes
preenvolventes = zeros(numFiltros,length(t));
% 
% % La función Hilbert devuelve la señal analítica, no la transformada.
% % El siguiente código no se ha implementado con un bucle porque da problemas
% %la función hilbert.
preenvolventes(1,:) = hilbert(respuestas(1,:)); 
preenvolventes(2,:) = hilbert(respuestas(2,:));
preenvolventes(3,:) = hilbert(respuestas(3,:));
preenvolventes(4,:) = hilbert(respuestas(4,:));
preenvolventes(5,:) = hilbert(respuestas(5,:));
preenvolventes(6,:) = hilbert(respuestas(6,:));
preenvolventes(7,:) = hilbert(respuestas(7,:));
preenvolventes(8,:) = hilbert(respuestas(8,:));
preenvolventes(9,:) = hilbert(respuestas(9,:));
preenvolventes(10,:) = hilbert(respuestas(10,:));
preenvolventes(11,:) = hilbert(respuestas(11,:));
preenvolventes(12,:) = hilbert(respuestas(12,:));
preenvolventes(13,:) = hilbert(respuestas(13,:));
preenvolventes(14,:) = hilbert(respuestas(14,:));
preenvolventes(15,:) = hilbert(respuestas(15,:));
preenvolventes(16,:) = hilbert(respuestas(16,:));
preenvolventes(17,:) = hilbert(respuestas(17,:));
preenvolventes(18,:) = hilbert(respuestas(18,:));
preenvolventes(19,:) = hilbert(respuestas(19,:));
preenvolventes(20,:) = hilbert(respuestas(20,:));
preenvolventes(21,:) = hilbert(respuestas(21,:));
preenvolventes(22,:) = hilbert(respuestas(22,:));
preenvolventes(23,:) = hilbert(respuestas(23,:));
preenvolventes(24,:) = hilbert(respuestas(24,:));
preenvolventes(25,:) = hilbert(respuestas(25,:));
preenvolventes(26,:) = hilbert(respuestas(26,:));
preenvolventes(27,:) = hilbert(respuestas(27,:));
% 
% figure
% stem(t,preenvolventes(1,:))
% title('preen')
% pause;
% 
% % Obtención de las envolventes complejas
envolventesComplejas = zeros(numFiltros,length(t));
for p=1:numFiltros
   envolventesComplejas(p,:) = preenvolventes(p,:).*exp(-j*2*pi*fc*t);
end
% 
% % Obtención de las componentes Inphase y Cuadratura de las respuestas de
% % los filtros
% 
respuestasInphase = zeros(numFiltros,length(t));
respuestasCuadratura = zeros(numFiltros,length(t));
% 
for p=1:numFiltros
   respuestasInphase(p,:) = real(envolventesComplejas(p,:));
   respuestasCuadratura(p,:) = imag(envolventesComplejas(p,:));
end
% 
figure
stem(t,respuestasInphase(4,:))
title('Inphase')
figure
stem(t,respuestasCuadratura(4,:))
title('Cuadratura')

% 
% 
