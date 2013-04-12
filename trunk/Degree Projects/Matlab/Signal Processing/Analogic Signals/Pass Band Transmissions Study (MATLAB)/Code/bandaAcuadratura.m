%% CONVERSIÓN PASOBANDA A CUADRATURA (BANDA BASE)

% ENTRADA:
% fc -> frecuencia de los osciladores locales
% fs -> frecuencia de muestreo
% g -> matriz de señales paso banda

% SALIDA:
% gI -> equivalente In-phase
% gQ -> equivalente Cuadratura

function [gCuadratura] = bandaAcuadratura (fc,fs,g)

t=0:1/fs:0.2;

gCuadratura = zeros(14, length(t));

% Osciladores locales
oscSeno = -2*sin(2*pi*fc*t);
oscCoseno = 2*cos(2*pi*fc*t);

filtrosConversor; % Carga los filtros que se van a utilizar para la conversión.

%% Obtención de los equivalentes en banda base - cuadratura

% ARMONICO PURO
gCuadratura(1,:)=filter(filtroArmonico,(oscCoseno.*g(1,:)));
gCuadratura(8,:)=filter(filtroArmonico,(oscSeno.*g(1,:)));

% SUMA DE ARMÓNICOS
gCuadratura(2,:)=filter(filtroSuma,(oscCoseno.*g(2,:)));
gCuadratura(9,:)=filter(filtroSuma,(oscSeno.*g(2,:)));

% DIENTE DE SIERRA
gCuadratura(3,:)=filter(filtroDiente,(oscCoseno.*g(3,:)));
gCuadratura(10,:)=filter(filtroDiente,(oscSeno.*g(3,:)));

% TRIANGULAR
gCuadratura(4,:)=filter(filtroTriangular,(oscCoseno.*g(4,:)));
gCuadratura(11,:)=filter(filtroTriangular,(oscSeno.*g(4,:)));

% CUADRADA
gCuadratura(5,:)=filter(filtroCuadrada,(oscCoseno.*g(5,:)));
gCuadratura(12,:)=filter(filtroCuadrada,(oscSeno.*g(5,:)));

% MODULACION AM
gCuadratura(6,:)=filter(filtroAM,(oscCoseno.*g(6,:)));
gCuadratura(13,:)=filter(filtroAM,(oscSeno.*g(6,:)));

% MODULACION FM
gCuadratura(7,:)=filter(filtroFM,(oscCoseno.*g(7,:)));
gCuadratura(14,:)=filter(filtroFM,(oscSeno.*g(7,:)));
