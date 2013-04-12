%% PRACTICA 3

%cargamos la señal
senal=abrir('extracto_orig.wav');
senal=senal/max(senal);
%filtros de sub-bandas
h1=[.366211e-3 -.134277e-2 -.134277e-2 .646973e-2 .146484e-2 -.190430e-1 .390625e-2 .44189e-1 -.256348e-1 -.98266e-1 .116089 .473145];
h1(13:24)=h1(12:-1:1);
N=length(h1);   n=0:N-1;
h2=h1.*(-ones(1,N)).^n;
%filtros de sintesis
f1=h1; f2=-h2;


bits1=4;
bits2=8;

bitspcm=6;

%% PCM
pcm=cuantizador(senal,bits);


%% Codificador
%convolucionamos la señal con los filtros H
y1=conv(senal,h1);   
y2=conv(senal,h2);    

%Hacemos el downsample
y1=downsample(y1,2);
y2=downsample(y2,2);

%normalizamos las señales
[y1norm,max1]=normalizar(y1);    
[y2norm,max2]=normalizar(y2);

%cuantizamos las señales
y1cuant=zeros(1,length(y1norm));
y2cuant=y1cuant; 

y1cuant=cuantizador(y1norm,bits1);    
y2cuant=cuantizador(y2norm,bits2);


%% Decodificador

%Desnormalizamos la señal de entrada
y1de=zeros(1,length(y1cuant)); 
y2de=y1de; 

y1de=desnormalizar(y1cuant,max1);
y2de=desnormalizar(y2cuant,max2);


%Subimos la frecuencia
y1cod=zeros(1,2*length(y1cuant)); 
y2cod=y1cod; 

y1cod=upsample(y1de,2);
y2cod=upsample(y2de,2);


% Filtramos la señal con el filtro F
y1sal=zeros(1,length(y1cod)+length(f1)-1);
y2sal=y1sal; 

y1sal=conv(y1cod,f1); y2sal=conv(y2cod,f2);
ysal=y1sal+y2sal;


%% Resampleado y guardado en wav
%ysal=resample(ysal,48000,8000);
%pcm=resample(pcm,48000,8000);

ysal=resample(ysal,160,147);
pcm=resample(pcm,160,147);

wavwrite(ysal,48000,16,'cod.wav');

wavwrite(pcm,48000,16,'pcm.wav');

