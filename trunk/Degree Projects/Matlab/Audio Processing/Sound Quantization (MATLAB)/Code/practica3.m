%% PRACTICA 3

%cargamos la señal
senal=load('signal_8khz.txt');
senal=senal/max(senal);
%filtros de sub-bandas
h1=[.366211e-3 -.134277e-2 -.134277e-2 .646973e-2 .146484e-2 -.190430e-1 .390625e-2 .44189e-1 -.256348e-1 -.98266e-1 .116089 .473145];
h1(13:24)=h1(12:-1:1);
N=length(h1);   n=0:N-1;
h2=h1.*(-ones(1,N)).^n;
%filtros de sintesis
f1=h1; f2=-h2;


%% PCM
pcm4=zeros(1,length(senal));
pcm6=pcm4; pcm8=pcm4;

pcm4=cuantizador(senal,4);
pcm6=cuantizador(senal,6);
pcm8=cuantizador(senal,8);


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
y1cuant4=zeros(1,length(y1norm));
y2cuant4=y1cuant4; y1cuant6=y1cuant4; y2cuant6=y1cuant4; y1cuant8=y1cuant4; y2cuant8=y1cuant4;

%4 bits
y1cuant4=cuantizador(y1norm,4);    
y2cuant4=cuantizador(y2norm,4);
%6 bits
y1cuant6=cuantizador(y1norm,6);    
y2cuant6=cuantizador(y2norm,6);
%8 bits
y1cuant8=cuantizador(y1norm,8);    
y2cuant8=cuantizador(y2norm,8);


%% Decodificador

%Desnormalizamos la señal de entrada
y1des4=zeros(1,length(y1cuant4)); 
y2des4=y1des4; y1des6=y1des4; y2des6=y1des4; y1des8=y1des4; y2des8=y1des4;

%4 bits
y1des4=desnormalizar(y1cuant4,max1);
y2des4=desnormalizar(y2cuant4,max2);
%6 bits
y1des6=desnormalizar(y1cuant6,max1);
y2des6=desnormalizar(y2cuant6,max2);
%8 bits
y1des8=desnormalizar(y1cuant8,max1);
y2des8=desnormalizar(y2cuant8,max2);


%Subimos la frecuencia
y1cod4=zeros(1,2*length(y1cuant4)); 
y2cod4=y1cod4; y1cod6=y1cod4; y2cod6=y1cod4; y1cod8=y1cod4; y2cod8=y1cod4;

%4 bits
y1cod4=upsample(y1des4,2);
y2cod4=upsample(y2des4,2);
%6 bits
y1cod6=upsample(y1des6,2);
y2cod6=upsample(y2des6,2);
%8bits
y1cod8=upsample(y1des8,2);
y2cod8=upsample(y2des8,2);


% Filtramos la señal con el filtro F
y1sal=zeros(1,length(y1cod4)+length(f1)-1);
y2sal=y1sal; ysal4=y1sal; ysal6=y1sal; ysal8=y1sal;

%4 bits
y1sal=conv(y1cod4,f1); y2sal=conv(y2cod4,f2);
ysal4=y1sal+y2sal;

%6 bits
y1sal=conv(y1cod6,f1); y2sal=conv(y2cod6,f2);
ysal6=y1sal+y2sal;

%4 bits
y1sal=conv(y1cod8,f1); y2sal=conv(y2cod8,f2);
ysal8=y1sal+y2sal;

%% Resampleado y guardado en wav
ysal4=resample(ysal4,48000,8000);
ysal6=resample(ysal6,48000,8000);
ysal8=resample(ysal8,48000,8000);

pcm4=resample(pcm4,48000,8000);
pcm6=resample(pcm6,48000,8000);
pcm8=resample(pcm8,48000,8000);

wavwrite(ysal4,48000,16,'cod4.wav');
wavwrite(ysal6,48000,16,'cod6.wav');
wavwrite(ysal8,48000,16,'cod8.wav');

wavwrite(pcm4,48000,16,'pcm4.wav');
wavwrite(pcm6,48000,16,'pcm6.wav');
wavwrite(pcm8,48000,16,'pcm8.wav');