%function [X,n] = lee_mues2(fichero,nmues)
% Lee ficheros de codificacion
% Por defecto lee todas las muestras

function [X,n] = lee_mues2(fichero,nmues)
f = fopen(fichero,'r','ieee-le');
if (nmues>0)
  X = fread(f,nmues,'int16');
else
  X = fread(f,Inf,'int16');
end
fclose(f);

