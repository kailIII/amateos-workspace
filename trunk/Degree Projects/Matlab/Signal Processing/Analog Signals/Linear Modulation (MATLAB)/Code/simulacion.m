%% SIMULACION

%% Frecuencias y vector de tiempos

fs = 15000000; % Frecuencia muestreo -> 15 MHz. Tiene que ser muy grande para que la forma de la señal sea "analógica".
fc = 100000; % Frecuencia portadora -> Se utilizan dos frecuencias: 1 MHz y 100 KHz.
f = 100; % Frecuencia de la señal -> 100 Hz.
t=-0.03:1/fs:0.03; % Vector de tiempos

%% Generación de señales
nombreSenales = {'Armónico Puro: Amp=1. ','Armónico Puro: Amp=10. ','Triple Sinc: Amp=1. ','Triple Sinc: Amp=10. ','Pulsos: Amp=1. ','Pulsos: Amp=10. '};
signals = generacion(f, t);

%% Niveles de ruido del canal
nivel1=0;
nivel2=20;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SIMULACIÓN CON MODULACIÓN DSB %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% MODULACIÓN:
% La matriz signalsModuladas almacena en cada fila las muestras de cada
% señal modulada mediante DSB.

signalsModuladas = zeros (length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    signalsModuladas(i,:) = modDSB(signals(i,:),fc,t);
end

% EFECTO DEL CANAL:
% La matriz signalsCanal almacena en cada fila las muestras de cada señal
% modulada tras pasar por el canal ruidoso. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. Además, se calcula la SNR a la salida
% del canal.

signalsCanal = zeros (2*length(signals(:,1)),length(t)); 
snrDSB = zeros (length(signals(:,1)),2);
for i=1:length(signals(:,1))
    signalsCanal(i,:) = canalRuidoso(signalsModuladas(i,:),nivel1);
    signalsCanal(i+6,:) = canalRuidoso(signalsModuladas(i,:),nivel2);
end

% RECUPERACIÓN
% La matriz signalsRecuperadasDSB almacena en cada fila las muestras de las
% señales recuperadas tras realizar la demodulación. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. 

signalsRecuperadasDSB = zeros (2*length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    % El ancho de banda del filtro a usar será de 1000 Hz para las señales de
    % pulsos y de 150 Hz para las demás señales.
    if i==5 || i==6
        BW = 1000;
    else
        BW = 150;
    end

    signalsRecuperadasDSB(i,:) = demodDSB(signalsCanal(i,:),fs,fc,t,BW);
    signalsRecuperadasDSB(i+6,:) = demodDSB(signalsCanal(i+6,:),fs,fc,t,BW);
end

disp('Fin simulación DSB')

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SIMULACIÓN CON MODULACIÓN SSB %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% MODULACIÓN:
% La matriz signalsModuladas almacena en cada fila las muestras de cada
% señal modulada mediante SSB.

signalsModuladas = zeros (length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    signalsModuladas(i,:) = modSSB(signals(i,:),fc,t);
end

% EFECTO DEL CANAL:
% La matriz signalsCanal almacena en cada fila las muestras de cada señal
% modulada tras pasar por el canal ruidoso. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. Además, se calcula la SNR a la salida
% del canal.

signalsCanal = zeros (2*length(signals(:,1)),length(t)); 
snrSSB = zeros (length(signals(:,1)),2);
for i=1:length(signals(:,1))
    signalsCanal(i,:) = canalRuidoso(signalsModuladas(i,:),nivel1);
    signalsCanal(i+6,:) = canalRuidoso(signalsModuladas(i,:),nivel2);
end

% RECUPERACIÓN
% La matriz signalsRecuperadasDSB almacena en cada fila las muestras de las
% señales recuperadas tras realizar la demodulación. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. 

signalsRecuperadasSSB = zeros (2*length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    % El ancho de banda del filtro a usar será de 1000 Hz para las señales de
    % pulsos y de 150 Hz para las demás señales.
    if i==5 || i==6
        BW = 1000;
    else
        BW = 150;
    end

    signalsRecuperadasSSB(i,:) = demodSSB(signalsCanal(i,:),fs,fc,t,BW);
    signalsRecuperadasSSB(i+6,:) = demodSSB(signalsCanal(i+6,:),fs,fc,t,BW);
end

disp('Fin simulación SSB')

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SIMULACIÓN CON MODULACIÓN AM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% MODULACIÓN:
% La matriz signalsModuladas almacena en cada fila las muestras de cada
% señal modulada mediante AM.

signalsModuladas = zeros (length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    signalsModuladas(i,:) = modAM(signals(i,:),fc,t);
end

% EFECTO DEL CANAL:
% La matriz signalsCanal almacena en cada fila las muestras de cada señal
% modulada tras pasar por el canal ruidoso. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. Además, se calcula la SNR a la salida
% del canal.

signalsCanal = zeros (2*length(signals(:,1)),length(t)); 
snrAM = zeros (length(signals(:,1)),2);
for i=1:length(signals(:,1))
	signalsCanal(i,:) = canalRuidoso(signalsModuladas(i,:),nivel1);
    signalsCanal(i+6,:) = canalRuidoso(signalsModuladas(i,:),nivel2);
end

% RECUPERACIÓN
% La matriz signalsRecuperadasAM almacena en cada fila las muestras de las
% señales recuperadas tras realizar la demodulación. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. 

signalsRecuperadasAM = zeros (2*length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    % El ancho de banda del filtro a usar será de 1000 Hz para las señales de
    % pulsos y de 150 Hz para las demás señales.
    if i==5 || i==6
        BW = 1000;
    else
        BW = 150;
    end

    signalsRecuperadasAM(i,:) = demodAM(signalsCanal(i,:),fs,BW);
    signalsRecuperadasAM(i+6,:) = demodAM(signalsCanal(i+6,:),fs,BW);
end

disp('Fin simulación AM')

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SIMULACIÓN CON MODULACIÓN QAM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% MODULACIÓN:
% La matriz signalsModuladas almacena en cada fila las muestras de cada
% señal QAM. Las dos señales moduladas son la misma siempre.

signalsModuladas = zeros (length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    signalsModuladas(i,:) = modQAM(signals(i,:),signals(i,:),fc,t);
end

% EFECTO DEL CANAL:
% La matriz signalsCanal almacena en cada fila las muestras de cada señal
% modulada tras pasar por el canal ruidoso. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. Además, se calcula la SNR a la salida
% del canal.

signalsCanal = zeros (2*length(signals(:,1)),length(t)); 
snrQAM = zeros (length(signals(:,1)),2);
for i=1:length(signals(:,1))
	signalsCanal(i,:) = canalRuidoso(signalsModuladas(i,:),nivel1);
    signalsCanal(i+6,:) = canalRuidoso(signalsModuladas(i,:),nivel2);
end

% RECUPERACIÓN
% La matriz signalsRecuperadasQAM almacena en cada fila las muestras de las
% señales recuperadas tras realizar la demodulación. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. 

signalsRecuperadasQAM1 = zeros (2*length(signals(:,1)),length(t));  
signalsRecuperadasQAM2 = zeros (2*length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    % El ancho de banda del filtro a usar será de 1000 Hz para las señales de
    % pulsos y de 150 Hz para las demás señales.
    if i==5 || i==6
        BW = 1000;
    else
        BW = 150;
    end

    [signalsRecuperadasQAM1(i,:),signalsRecuperadasQAM2(i,:)] = demodQAM(signalsCanal(i,:),fs,fc,t,BW);
    [signalsRecuperadasQAM1(i+6,:),signalsRecuperadasQAM2(i+6,:)] = demodQAM(signalsCanal(i+6,:),fs,fc,t,BW);
end

disp('Fin simulación QAM')