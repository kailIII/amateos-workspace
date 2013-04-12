%% REPRESENTACIÓN DE SEÑALES DE ENTRADA

rangoSenales = [-0.03 0.03 -1.1 1.1; -0.03 0.03 -11 11; -0.03 0.03 -0.1 1.1; -0.03 0.03 -1 11; -0.03 0.03 -1.1 1.1; -0.03 0.03 -11 11;];
rangoEspectros = [-500 500 0 90000; -500 500 0 900000; -500 500 0 5500; -500 500 0 55000; -3000 3000 0 110000; -3000 3000 0 1100000;];

for i=1:length(signals(:,1))

        h1=figure;
        subplot(121)
        plot(t,signals(i,:))
        axis(rangoSenales(i,:));
        title(nombreSenales(i),'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f,mx] = espectro(signals(i,:),fs);
        
        subplot(122)
        plot(f,mx)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
%  saveas(h1,num2str(i),'png')
%  close;
end

%% Representación de resultados: MODULACIÓN DSB
rangoSenales = [-0.03 0.03 -1.5 1.5; -0.03 0.03 -11 11; -0.03 0.03 -0.1 1.8; -0.03 0.03 -1 11; -0.03 0.03 -1.5 1.5; -0.03 0.03 -11 11;];
rangoEspectros = [-500 500 0 400000; -500 500 0 4000000; -500 500 0 160000; -500 500 0 1600000; -3000 3000 0 500000; -3000 3000 0 5000000;];   

for i=1:length(signals(:,1))

    
        % NIVEL DE RUIDO 1
        h1=figure;
        titulo=strcat(nombreSenales(i),'DSB: ', 'SNR=0dB');
        subplot(121)
        plot(t,signalsRecuperadasDSB(i,:))
        hold on
        plot(t,signals(i,:),'red')
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
       

        [f,mx] = espectro(signals(i,:),fs);
        [f2,mx2] = espectro(signalsRecuperadasDSB(i,:),fs);
        
        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h1,num2str(i),'png')
 close;
        
        % NIVEL DE RUIDO 2
        h2=figure;
        titulo=strcat(nombreSenales(i),'DSB: ', 'SNR=20dB');
        subplot(121)
        plot(t,signalsRecuperadasDSB(i+6,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f2,mx2] = espectro(signalsRecuperadasDSB(i+6,:),fs);

        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h2,num2str(i+6),'png')
 close;
end

%% Representación de resultados: MODULACIÓN SSB

for i=1:length(signals(:,1))

    
        % NIVEL DE RUIDO 1
        h1=figure;
        titulo=strcat(nombreSenales(i),'SSB: ', 'SNR=0dB');
        subplot(121)
        plot(t,signalsRecuperadasSSB(i,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
       

        [f,mx] = espectro(signals(i,:),fs);
        [f2,mx2] = espectro(signalsRecuperadasSSB(i,:),fs);
        
        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h1,num2str(i),'png')
 close;
        
        % NIVEL DE RUIDO 2
        h2=figure;
        titulo=strcat(nombreSenales(i),'SSB: ', 'SNR=20dB');
        subplot(121)
        plot(t,signalsRecuperadasSSB(i+6,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f2,mx2] = espectro(signalsRecuperadasSSB(i+6,:),fs);

        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h2,num2str(i+6),'png')
 close;
end



%% Representación de resultados: MODULACIÓN QAM

for i=1:length(signals(:,1))

    
        % NIVEL DE RUIDO 1
        h1=figure;
        titulo=strcat(nombreSenales(i),'QAM: ', 'SNR=0dB');
        subplot(121)
        plot(t,signalsRecuperadasQAM1(i,:),'green')
        hold on
        plot(t,signalsRecuperadasQAM2(i,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
       

        [f,mx] = espectro(signals(i,:),fs);
        [f2,mx2] = espectro(signalsRecuperadasQAM1(i,:),fs);
        [f3,mx3] = espectro(signalsRecuperadasQAM2(i,:),fs);
        
        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2,'green','Linewidth',2)
        hold on
        plot(f3,mx3)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h1,num2str(i),'png')
 close;
        
        % NIVEL DE RUIDO 2
        h2=figure;
        titulo=strcat(nombreSenales(i),'QAM ', 'SNR=20dB');
        subplot(121)
        plot(t,signalsRecuperadasQAM1(i+6,:))
        hold on
        plot(t,signalsRecuperadasQAM2(i+6,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f2,mx2] = espectro(signalsRecuperadasQAM1(i+6,:),fs);
        [f3,mx3] = espectro(signalsRecuperadasQAM2(i+6,:),fs);
        
        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2,'green','Linewidth',2)
        hold on
        plot(f3,mx3)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h2,num2str(i+6),'png')
 close;
end

%% Representación de resultados: MODULACIÓN AM
rangoSenales = [-0.03 0.03 -1.1 2.5; -0.03 0.03 -11 25; -0.03 0.03 -0.1 2.6; -0.03 0.03 -1 26; -0.03 0.03 -1.1 2.5; -0.03 0.03 -11 25;];
rangoEspectros = [-500 500 0 400000; -500 500 0 4000000; -500 500 0 700000; -500 500 0 7000000; -3000 3000 0 500000; -3000 3000 0 5000000;];   

for i=1:length(signals(:,1))

    
        % NIVEL DE RUIDO 1
        h1=figure;
        titulo=strcat(nombreSenales(i),'AM: ', 'SNR=0dB');
        subplot(121)
        plot(t,signalsRecuperadasAM(i,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
       

        [f,mx] = espectro(signals(i,:),fs);
        [f2,mx2] = espectro(signalsRecuperadasAM(i,:),fs);
        
        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h1,num2str(i),'png')
 close;
        
        % NIVEL DE RUIDO 2
        h2=figure;
        titulo=strcat(nombreSenales(i),'AM ', 'SNR=20dB');
        subplot(121)
        plot(t,signalsRecuperadasAM(i+6,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f2,mx2] = espectro(signalsRecuperadasAM(i+6,:),fs);

        subplot(122)
        plot(f,mx,'red')
        hold on
        plot(f2,mx2)
        axis(rangoEspectros(i,:));
        title('Espectro','FontWeight','bold')
        xlabel('Frecuencia (Hz)')
        ylabel('Módulo')
 %pause;
 saveas(h2,num2str(i+6),'png')
 close;
end