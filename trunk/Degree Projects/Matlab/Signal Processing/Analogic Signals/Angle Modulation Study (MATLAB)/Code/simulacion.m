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
%% SIMULACIÓN CON MODULACIÓN FM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% MODULACIÓN:
% La matriz signalsModuladas almacena en cada fila las muestras de cada
% señal modulada mediante FM.

desvFrec = 5000; %Desviación de la frecuencia

signalsModuladas = zeros (length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
      signalsModuladas(i,:) = fmmod(signals(i,:),fc,fs,desvFrec);
end


% EFECTO DEL CANAL:
% La matriz signalsCanal almacena en cada fila las muestras de cada señal
% modulada tras pasar por el canal ruidoso. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. Además, se calcula la SNR a la salida
% del canal.

signalsCanal = zeros (2*length(signals(:,1)),length(t)); 
for i=1:length(signals(:,1))
    signalsCanal(i,:) = canalRuidoso(signalsModuladas(i,:),nivel1);
    signalsCanal(i+6,:) = canalRuidoso(signalsModuladas(i,:),nivel2);
end

% RECUPERACIÓN
% La matriz signalsRecuperadasFM almacena en cada fila las muestras de las
% señales recuperadas tras realizar la demodulación. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. 

signalsRecuperadasFM = zeros (2*length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    
    if i==5 || i==6 % Elección del ancho de banda de la señal
        BW = 1000;
    else
        BW = 150;
    end
    
    signalsRecuperadasFM(i,:) = fmdemod(signalsCanal(i,:),fc,fs,desvFrec);
    signalsRecuperadasFM(i+6,:) = fmdemod(signalsCanal(i+6,:),fc,fs,desvFrec);
    
    h=fir1(40, BW/fs);  % Se realiza un filtrado paso baja porque el ruido 
                        % se concentra fuera del ancho de banda de la señal
                        % Asi la forma de la señal resultante se aprecia mejor
                        % y el espectro queda intacto en la zona que nos interesa.
                        
    signalsRecuperadasFM(i,:) = filter(h,1,signalsRecuperadasFM(i,:));
    signalsRecuperadasFM(i+6,:) = filter(h,1,signalsRecuperadasFM(i+6,:));
end
disp('-----------------')
disp('Fin simulación FM')
disp('-----------------')

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SIMULACIÓN CON MODULACIÓN PM %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Niveles de ruido del canal
nivel1=0;
nivel2=20;

% MODULACIÓN:
% La matriz signalsModuladas almacena en cada fila las muestras de cada
% señal modulada mediante PM.

desvFase = pi/2; %Desviación de la fase

signalsModuladas = zeros (length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
      signalsModuladas(i,:) = pmmod(signals(i,:),fc,fs,desvFase);
end


% EFECTO DEL CANAL:
% La matriz signalsCanal almacena en cada fila las muestras de cada señal
% modulada tras pasar por el canal ruidoso. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. Además, se calcula la SNR a la salida
% del canal.

signalsCanal = zeros (2*length(signals(:,1)),length(t)); 
for i=1:length(signals(:,1))
    signalsCanal(i,:) = canalRuidoso(signalsModuladas(i,:),nivel1);
    signalsCanal(i+6,:) = canalRuidoso(signalsModuladas(i,:),nivel2);
end

% RECUPERACIÓN
% La matriz signalsRecuperadasPM almacena en cada fila las muestras de las
% señales recuperadas tras realizar la demodulación. La mitad superior se
% corresponde con el nivel de ruido en canal bajo y la mitad inferior con
% el nivel de ruido en canal alto. 

signalsRecuperadasPM = zeros (2*length(signals(:,1)),length(t));  
for i=1:length(signals(:,1))
    
    signalsRecuperadasPM(i,:) = pmdemod(signalsCanal(i,:),fc,fs,desvFase);
    signalsRecuperadasPM(i+6,:) = pmdemod(signalsCanal(i+6,:),fc,fs,desvFase);
end
disp('-----------------')
disp('Fin simulación PM')
disp('-----------------')