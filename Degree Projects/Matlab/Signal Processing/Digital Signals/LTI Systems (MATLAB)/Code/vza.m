% N�mero de bits para la simulaci�n
b=5

%vector vacio para los cuantos de las diferentes amplitudes
vcuantos=[];
%vector vacio para las varianzas de las diferentes amplitudes
vvarianzas=[];

for A=1:10
   
% Cuanto correspondiente a la se�al para amplitud A
cuanto=2*A/2^b

% A�adir valor cuanto al vector
vcuantos=[vcuantos,cuanto]

% Simulamos el modelo
sim('bits8')

%Varianza de la se�al obtenida
Ex2=(1/length(x))*(x'*x);

% A�adir el valor de la varianza al vector
vvarianzas=[vvarianzas,Ex2];
end

% Representaci�n gr�fica
plot(vcuantos,vvarianzas)
xlabel('cuanto')
ylabel('varianza')