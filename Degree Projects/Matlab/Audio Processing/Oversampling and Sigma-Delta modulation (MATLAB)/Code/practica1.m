%% PRACTICA 1: SOBREMUESTREO Y MODULACIÓN DELTA - SIGMA

%% Apartado 1:
snrq_uniforme=zeros(1,20);
snrq_sobremuestreo=zeros(1,20);
snrq_delta1=zeros(1,20);
snrq_delta2=zeros(1,20);

for i=1:20
    L=i;
    snrq_uniforme(i)=6.02*4+1.76;
    snrq_sobremuestreo(i)=6.02*(1+0.5*log2(L))+1.76;
    snrq_delta1(i)=6.02*(1+1.5*log2(L))-3.41;
    snrq_delta2(i)=6.02*(1+2.5*log2(L))-11.14;
end

%Representamos las snrqs obtenidas en funcion de L
plot (snrq_uniforme,'r','LineWidth',2)
hold on;
plot (snrq_sobremuestreo,'g','LineWidth',2)
hold on;
plot (snrq_delta1,'b','LineWidth',2)
hold on;
plot (snrq_delta2,'y','LineWidth',2)
hold on;

title('SNRQ');
xlabel('L');
ylabel('SNR (dB)');
legend('uniforme','sobremuestreo','delta-sigma','delta-sigma 2');

%% Apartado 2:
signal8=load('signal_8khz.txt');
signal8=signal8/max(signal8);
uniforme=cuantizador(signal8,4);
%%%%%%%%%%%%%%%%%%%%%%L=4:
x=interp(signal8,4);

% Cuantizador uniforme con sobremuestreo
x_cuant=cuantizador(x,1);
sobremuestreo_8_4=decimate(x_cuant,4);

% Delta-sigma
x_cuant=delta_sigma(x);
delta_8_4=decimate(x_cuant,4);

% Delta-sigma 2º orden
x_cuant=delta_sigma2(x);
delta2_8_4=decimate(x_cuant,4);

%%%%%%%%%%%%%%%%%%%%%L=8:
x=interp(signal8,8);

% Cuantizador uniforme con sobremuestreo
x_cuant=cuantizador(x,1);
sobremuestreo_8_8=decimate(x_cuant,8);

% Delta-sigma
x_cuant=delta_sigma(x);
delta_8_8=decimate(x_cuant,8);

% Delta-sigma 2º orden
x_cuant=delta_sigma2(x);
delta2_8_8=decimate(x_cuant,8);

%%%% resultados %%%%
'Señal 8 kHz uniforme 4 bits' %#ok<NOPTS>
pause;
sound(uniforme,8000);
'Señal 8 kHz sobremuestreo L=4' %#ok<NOPTS>
pause;
sound(sobremuestreo_8_4,8000);
'Señal 8 kHz Delta-Sigma L=4' %#ok<NOPTS>
pause;
sound(delta_8_4,8000);
'Señal 8 kHz Delta-Sigma 2º orden L=4' %#ok<NOPTS>
pause;
sound(delta2_8_4,8000);

'Señal 8 kHz sobremuestreo L=8' %#ok<NOPTS>
pause;
sound(sobremuestreo_8_8,8000);
'Señal 8 kHz Delta-Sigma L=8' %#ok<NOPTS>
pause;
sound(delta_8_8,8000);
'Señal 8 kHz Delta-Sigma 2º orden L=8' %#ok<NOPTS>
pause;
sound(delta2_8_8,8000);

%% Apartado 3:
signal44=abrir('extracto_orig.wav');
%uniforme
uniforme2=cuantizador(signal44,4);

x=interp(signal44,4);

% Delta-sigma 1º orden
x_cuant=delta_sigma(x);
delta_44=decimate(x_cuant,4);

% Delta-sigma 2º orden
x_cuant=delta_sigma2(x);
delta2_44=decimate(x_cuant,4);

%%%% resultados %%%%
'Señal 44.1 kHz Cuant. uniforme 4 bits' %#ok<NOPTS>
pause;
sound(uniforme2,44100);
'Señal 44.1 kHz Delta-Sigma 1º orden L=4' %#ok<NOPTS>
pause;
sound(delta_44,44100);
'Señal 44.1 kHz Delta-Sigma 2º orden L=4' %#ok<NOPTS>
pause;
sound(delta2_44,44100);