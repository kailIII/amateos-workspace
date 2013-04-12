% filtros QMF del G.722
h1=[.366211e-3 -.134277e-2 -.134277e-2 .646973e-2 .146484e-2 -.190430e-1 .390625e-2 .44189e-1 -.256348e-1 -.98266e-1 .116089 .473145];
h1(13:24)=h1(12:-1:1);
N=length(h1);
[Mag1,w]=freqz(h1,1,512);
n=0:N-1;
h2=h1.*(-ones(1,N)).^n;
[Mag2,w]=freqz(h2,1,512);
M1=[w' abs(Mag1)']; M2=[w' abs(Mag2)'];
plot(w,abs(Mag1),w,abs(Mag2),'-')
f1=h1; f2=-h2;