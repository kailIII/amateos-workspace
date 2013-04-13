%PRACTICA 3 TAU: CODIFICADOR G.722

clear;
%% Datos iniciales
%se�al de voz
signal8=load('U:\E.T.S. TELECOMUNICACIONES\4�\Tecnologias de Audio\PracticasTAU\practica3 tau\signal_8khz.txt')';
signal8=signal8/max(signal8);
%filtros de sub-bandas
h1=[.366211e-3 -.134277e-2 -.134277e-2 .646973e-2 .146484e-2 -.190430e-1 .390625e-2 .44189e-1 -.256348e-1 -.98266e-1 .116089 .473145];
h1(13:24)=h1(12:-1:1);
N=length(h1);   n=0:N-1;
h2=h1.*(-ones(1,N)).^n;
%filtros de sintesis
f1=h1; f2=-h2;


%% Codificador
%primero filtramos las bandas
x1=conv(signal8,h1);    %banda superior 
x2=conv(signal8,h2);    %banda inferior
%reducimos la frecuencia de muestreo
y1=downsample(x1,2);    y2=downsample(x2,2);
%realizamos el factor de escala
[y1e,vector_max1]=escalar(y1,128);    [y2e,vector_max2]=escalar(y2,128);
%creamos las se�ales cuantizadas
y1q=zeros(3,length(y1e));   y2q=y1q;
B=[4 6 8];
for i=1:3
   y1q(i,:)=cuantizar(y1e,B(i));    y2q(i,:)=cuantizar(y2e,B(i));
end

%% Decodificador
% deshacemos el factor de escala y aumentamos la frecuencia de muestreo
y1u=zeros(3,2*length(y1q));   y2u=y1u;
for i=1:3
   y1u(i,:)=upsample(desescalar(y1q(i,:),vector_max1,128),2);
   y2u(i,:)=upsample(desescalar(y2q(i,:),vector_max2,128),2); 
end
% pasamos por el banco de filtros de sintesis
s1=zeros(3,length(y1u)+length(f1)-1);    s2=s1;      s=s1;
for i=1:3
   s1(i,:)=conv(y1u(i,:),f1);
   s2(i,:)=conv(y2u(i,:),f2);
   s(i,:)=s1(i,:)+s2(i,:);
end

%% Codificacion PCM
s_pcm=zeros(3,length(signal8));
for i=1:3
   s_pcm(i,:) =cuantizar(signal8,B(i));
end
