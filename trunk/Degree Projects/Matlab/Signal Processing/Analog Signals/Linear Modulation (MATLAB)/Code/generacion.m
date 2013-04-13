%% GENERACIÓN DE LAS SEÑALES

function [signals] = generacion(fc,t)
%
% ENTRADA:
% fc -> frecuencia de la señal
% t -> vector de tiempos
%
% SALIDA:
% signals -> matriz que contiene todas las señales generadas. Cada fila es
% una señal.

signals = zeros(6,length(t)); 

%% ArmonicoPuro 

% Amplitud 1
signals(1,:) = sin(2*pi*fc*t);

% Amplitud 10
signals(2,:) = 10*sin(2*pi*fc*t);

%% Triple Sinc
Ta=0.01;
sig_1=sinc(2*t/Ta);
sig_2=sinc (2*t/Ta-1);
sig_3=sinc(2*t/Ta+1);

% Amplitud 1
signals(3,:)=sig_1+0.5*sig_2+0.5*sig_3;

% Amplitud 10
signals(4,:)=10*(sig_1+0.5*sig_2+0.5*sig_3);

%% Pulsos

% Amplitud 1
signals(5,:) = square(2*pi*fc*t);

% Amplitud 10
signals(6,:) = 10*square(2*pi*fc*t);
