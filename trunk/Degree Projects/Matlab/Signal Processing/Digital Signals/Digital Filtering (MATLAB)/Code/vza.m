% Número de bits para la simulación
b=5

%vector vacio para los cuantos de las diferentes amplitudes
vcuantos=[];
%vector vacio para las varianzas de las diferentes amplitudes
vvarianzas=[];

for A=1:10
   
% Cuanto correspondiente a la señal para amplitud A
cuanto=2*A/2^b

% Añadir valor cuanto al vector
vcuantos=[vcuantos,cuanto]

% Simulamos el modelo
sim('bits8')

%Varianza de la señal obtenida
Ex2=(1/length(x))*(x'*x);

% Añadir el valor de la varianza al vector
vvarianzas=[vvarianzas,Ex2];
end

% Representación gráfica
plot(vcuantos,vvarianzas)
xlabel('cuanto')
ylabel('varianza')