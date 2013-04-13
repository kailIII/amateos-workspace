%% Representación de resultados: MODULACIÓN FM
rangoSenales = [-0.03 0.03 -1.5 1.5; -0.03 0.03 -11 11; -0.03 0.03 -0.1 1.8; -0.03 0.03 -1 11; -0.03 0.03 -1.5 1.5; -0.03 0.03 -11 11;];
rangoEspectros = [-500 500 0 400000; -500 500 0 4000000; -500 500 0 160000; -500 500 0 1600000; -3000 3000 0 500000; -3000 3000 0 5000000;];   

for i=1:length(signals(:,1))

    
        % NIVEL DE RUIDO 1
        h1=figure;
        titulo=strcat(nombreSenales(i),'FM: ', 'SNR=0dB');
        subplot(121)
        plot(t,signalsRecuperadasFM(i,:))
        hold on
        plot(t,signals(i,:),'red')
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
       

        [f,mx] = espectro(signals(i,:),fs);
        [f2,mx2] = espectro(signalsRecuperadasFM(i,:),fs);
        
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
        titulo=strcat(nombreSenales(i),'FM: ', 'SNR=20dB');
        subplot(121)
        plot(t,signalsRecuperadasFM(i+6,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f2,mx2] = espectro(signalsRecuperadasFM(i+6,:),fs);

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


%% Representación de resultados: MODULACIÓN PM
rangoSenales = [-0.03 0.03 -1.5 1.5; -0.03 0.03 -11 11; -0.03 0.03 -0.1 1.8; -0.03 0.03 -1 11; -0.03 0.03 -1.5 1.5; -0.03 0.03 -11 11;];
rangoEspectros = [-500 500 0 400000; -500 500 0 4000000; -500 500 0 160000; -500 500 0 1600000; -3000 3000 0 500000; -3000 3000 0 5000000;];   

for i=1:length(signals(:,1))

    
        % NIVEL DE RUIDO 1
        h1=figure;
        titulo=strcat(nombreSenales(i),'PM: ', 'SNR=0dB');
        subplot(121)
        plot(t,signalsRecuperadasPM(i,:))
        hold on
        plot(t,signals(i,:),'red')
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
       

        [f,mx] = espectro(signals(i,:),fs);
        [f2,mx2] = espectro(signalsRecuperadasPM(i,:),fs);
        
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
        titulo=strcat(nombreSenales(i),'PM: ', 'SNR=20dB');
        subplot(121)
        plot(t,signalsRecuperadasPM(i+6,:))
        hold on
        plot(t,signals(i,:),'red','Linewidth',2)
        axis(rangoSenales(i,:));
        title(titulo,'FontWeight','bold')
        xlabel('Tiempo (s)')
        ylabel('Amplitud')
        

        [f2,mx2] = espectro(signalsRecuperadasPM(i+6,:),fs);

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
