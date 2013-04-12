%%SCRIPT DEL MODELO ATM 2X2 SIN MEMORIA A LA SALIDA
Tsim=10000;         %tiempo de simulacion
throughput=[];      %vector que almacena el throughput de salida del conmutador oara cada valor de utilizacion
retardo=[];         %vector que almacena el retardo de una celula para cada valor de utilizacion
prob_perdidas=[];   %vector que almacena la probabilidad de descarte de una celula para cada valor de utilizacion
throug_teorico=[];

%%ahora definimos todos los valores de utilizacion que vamos a evaluar
util1=0.01:0.05:0.9;     
util2=0.9:0.03:0.999;     
utilizacion=[util1, util2];
throughput=zeros(1,length(utilizacion));
retardo=zeros(1,length(utilizacion));
prob_perdidas=zeros(1,length(utilizacion));
throug_teorico=zeros(1,length(utilizacion));

for i=1:length(utilizacion)
    
    %asignamos las probabilidades de los destinos de cada celula. Incluimos
    %el destino 0 para representar celulas vacias
    p=utilizacion(i);
    %simulamos con los parametros correspondientes
    sim('conmutador',Tsim);
    
    %una vez finalizada la simulacion calculamos los datos de interes
    
  
    
    %almacenamos el throughput:suma de las utilizaciones de cada linea dividida entre el numero de lineas
    throughput(i)=(utilizacion1(length(utilizacion1))+utilizacion2(length(utilizacion2)))/2;
    %%obtenemos el throughput teorico
    throug_teorico(i)=(1-(1-(p/2))^2);
    
    %almacenamos el retardo
    retardo(i)=(retardo1(length(retardo1))+retardo2(length(retardo2)))/2;
    
    %calculamos la probabilidad de perdidas: celulas que se pierden entre las celulas totales

    
    [vacias1,vacias2]=vacias(destinos1,destinos2);
    prob_perdidas(i)= (2*Tsim-(vacias1+vacias2+salidas1(length(salidas1))+salidas2(length(salidas2))))/(2*Tsim);
end



%%por ultimo representamos los resultados obtenidos
figure;
plot(utilizacion,throughput);
title('Throughput vs Utilizacion','LineWidth',2);
xlabel('Utilizacion');
ylabel('Throughput');
hold on;
plot(utilizacion,throug_teorico,'r','LineWidth',2);

figure;
plot(utilizacion,retardo,'LineWidth',2);
title('retardo vs Utilizacion');
xlabel('Utilizacion');
ylabel('Retardo');

figure;
plot(utilizacion,prob_perdidas,'LineWidth',2);
title('Probabilidad de perdidas vs Utilizacion');
xlabel('Utilizacion');
ylabel('P');




