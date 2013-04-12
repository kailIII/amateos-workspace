%% SIMULACION

%% Frecuencias de muestreo y portadora

fs=500000; % Frecuencia muestreo -> 500 kHz. Tiene que ser muy grande para que la forma de la señal sea "analógica".
fc = 10000; % Frecuencia portadora -> 10 kHz.
t=0:1/fs:0.2;

%% Generación de señales
nombreSenales = {'Señal-> Armónico Puro. ','Señal-> Suma de Armónicos. ','Señal-> Diente de Sierra. ','Señal-> Triangular. ','Señal-> Pulsos. ','Señal-> AM. ','Señal-> FM. '};
nombreFiltros = {'Canal-> Butterworth BP: n=2 Fs1=5kHz Fs2=15kHz.','Canal-> Butterworth BP: n=4 Fs1=5kHz Fs2=15kHz.','Canal-> Butterworth BP: n=10 Fs1=5kHz Fs2=15kHz.','Canal-> Chebyshev BP: n=2 Fs1=5kHz Fs2=15kHz.','Canal-> Chebyshev BP: n=4 Fs1=5kHz Fs2=15kHz.','Canal-> Chebyshev BP: n=10 Fs1=5kHz Fs2=15kHz.','Canal-> Elíptico BP: n=2 Fs1=5kHz Fs2=15kHz.','Canal-> Elíptico BP: n=4 Fs1=5kHz Fs2=15kHz.','Canal-> Elíptico BP: n=10 Fs1=5kHz Fs2=15kHz.','Canal-> Butterworth BP: n=2 Fs1=9kHz Fs2=11kHz.','Canal-> Butterworth BP: n=4 Fs1=9kHz Fs2=11kHz.','Canal-> Butterworth BP: n=10 Fs1=9kHz Fs2=11kHz.','Canal-> Chebyshev BP: n=2 Fs1=9kHz Fs2=11kHz.','Canal-> Chebyshev BP: n=4 Fs1=9kHz Fs2=11kHz.','Canal-> Chebyshev BP: n=10 Fs1=9kHz Fs2=11kHz.','Canal-> Elíptico BP: n=2 Fs1=9kHz Fs2=11kHz.','Canal-> Elíptico BP: n=4 Fs1=9kHz Fs2=11kHz.','Canal-> Elíptico BP: n=10 Fs1=9kHz Fs2=11kHz.','Canal-> Butterworth BP: n=2 Fs1=0kHz Fs2=20kHz.','Canal-> Butterworth BP: n=4 Fs1=0kHz Fs2=20kHz.','Canal-> Butterworth BP: n=10 Fs1=0kHz Fs2=20kHz.','Canal-> Chebyshev BP: n=2 Fs1=0kHz Fs2=20kHz.','Canal-> Chebyshev BP: n=4 Fs1=0kHz Fs2=20kHz.','Canal-> Chebyshev BP: n=10 Fs1=0kHz Fs2=20kHz.','Canal-> Elíptico BP: n=2 Fs1=0kHz Fs2=20kHz.','Canal-> Elíptico BP: n=4 Fs1=0kHz Fs2=20kHz.','Canal-> Elíptico BP: n=10 Fs1=0kHz Fs2=20kHz.'};

signals = generacion(fc, fs);

%% Conversión de la señal a equivalentes en cuadratura
                                                             
signalsCuadratura = bandaAcuadratura(fc,fs,signals);% La mitad de arriba de las filas contiene las equivalentes
                                                    %in phase y la mitad de abajo contiene las equivalentes en cuadratura


%% Carga de filtros

load('filtros/canal/respuestasCanal.mat'); %Las respuestas han sido obtenidas 
                                           %mediante la función filtros.m 
                                           %y guardadas en el fichero
                                           %respuestasCanal.mat para que la
                                           %simulación se más rápida

%% Simulación del canal
% En la mitad de arriba de la matriz signalsCanal se almacena la señal
% equivalente Inphase obtenida tras pasar por el canal para cada tipo de
% señal de entrada. En la mitad de abajo de la matriz signalsCanal se 
% almacena la señal equivalente en Cuadratura obtenida tras pasar por el 
% canal para cada tipo de señal de entrada.

signalsCanal = zeros (2*length(signals(:,1)),(2*length(t)-1));

k=27; % Indica el filtro a utilizar para la simulación del canal.

nombreFiltros(k) % Muestra por la línea de comandos el nombre del filtro que se está simulando.

% Se realiza la simulacion del canal, conversión a paso banda y obtención
% de resultados tantas veces como tipos de filtro tenemos. Estas
% operaciones se podrían haber realizado mediante un bucle for pero se ha
% preferido modificar el filtro a utilizar para la simulación del canal
% (valor de k) "manualmente" para poder guardar el workspace en un fichero
% .mat para la posterior representación de los datos.
                                    
                                    
    for i=1:1:length(signals(:,1))
        [signalsCanal(i,:), signalsCanal(i+length(signals(:,1)),:)] = canal(signalsCuadratura(i,:), signalsCuadratura(i+length(signals(:,1)),:),respuestasInphase(k,:), respuestasCuadratura(k,:));
    end
    
    %% Conversion de la señal de equivalentes en cuadratura a paso banda
    
    signalsBanda = zeros (length(signals(:,1)),length(t)); % Almacena el resultado de la simulación
                                                           % es decir, las señales paso banda a la 
                                                           % salida de canal
    
    for i=1:length(signals(:,1))
        long=length(signalsCanal(i,:));
        % Para obtener un resultado correcto hay que quedarse con la
        % segunda mitad de las señales en cuadratura, ya que, por efecto de
        % la operación de convolución realizada en la simulación del canal
        % se obtienen señales que tienen el doble de muestras.
        gI=signalsCanal(i,long/2:long);
        gQ=signalsCanal(i+length(signals(:,1)),long/2:long);
        signalsBanda(i,:) = cuadraturaAbanda(fc,fs,gI,gQ);
    end

    
    %% Simulación de la transmisión sin efecto del canal
    signalsNoCanal = zeros (length(signals(:,1)),length(t)); 
    
    for i=1:length(signals(:,1))
        aux = signalsCuadratura; 
        signalsNoCanal(i,:) = cuadraturaAbanda(fc,fs,signalsCuadratura(i,:),signalsCuadratura(i+length(signals(:,1)),:));
    end