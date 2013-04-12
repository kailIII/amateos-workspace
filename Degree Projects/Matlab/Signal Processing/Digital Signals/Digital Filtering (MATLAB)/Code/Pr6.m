x=lee_mues2('senalsismica.raw',inf);
n=length(x);
%Hacemos el espectograma
figure
specgram(x,64,200,64,16);
title('Espectrograma')


%3.2 Implementacion del filtro FIR

%Frecuencia inferior wl, frecuencia superior wh

wl=5;
wh=20;

%Normalizamos las frecuencias
wn=[wl/200 wh/200];

%Hacemos el filtrado pasobanda
[b]=fir1(101,wn,'bandpass');

%Representamos la se?al en modulo y fase
figure
freqz(b,101);
title('Respuesta en frecuencia filtro FIR')

%Filtramos la se?al
y1=filter(b,1,x);
figure
plot(y1)
Title('Senal filtrada con filtro FIR y senal sin filtrar (amarillo)')
hold on
plot(x,'y')


%Espectrograma de la senal filtrada
figure
specgram(y1,64,200,64,16);
title('Espectrograma senal filtrada con filtro IIR')


%3.3 Implementacion del filtro FIR


%Hacemos el filtrado de
[b2,a2]=butter(6,wn,'bandpass');

%Representamos la se?al en modulo y fase
figure
freqz(b2,a2,101);
Title('Respuesta en frecuencia filtro IIR')

%Filtramos la se?al
y2=filter(b2,a2,x);
figure
plot(y2)
title('Senal filtrada con filtro FIR y senal sin filtrar (amarillo)')
hold on
plot(x,'y')


%Espectrograma de la senal filtrada
figure
specgram(y2,64,200,64,16);
title('Espectrograma senal filtrada con filtro FIR')

%Decimacion

y3=resample(y1,1,4);


%Resampleamos la senal y1
senal_res1=[];
for i=1:4:length(y1)
    
    senal_res1 = [senal_res1;y1(i)];
     
end

%Respresentacion senales sampleadas
figure
plot(senal_res1)
title('Senal resampleada por Matlab (azul) y por funcion (amarillo)')
hold on
plot(y3,'y')


%Resampleamos la senal y2
y4=resample(y2,1,4);


%Resampleamos la senal y2
senal_res2=[];
for i=1:4:length(y2)
    
    senal_res2 = [senal_res2;y2(i)];
     
end

%Representacion senales sampleadas
figure
plot(y4)
title('Senal resampleada por Matlab (azul) y por funcion (amarillo)')
hold on
plot(senal_res2,'y')

