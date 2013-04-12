function [s_cuantizada]=cuantizar(signal,nb)
N=length(signal);
s_cuantizada=zeros(1,N);
cuanto=(max(signal)-min(signal))/(2^nb);
if (nb<2)
    for i=1:N
       if(signal(i)>0)
            s_cuantizada(i)=0.5;
       else
            s_cuantizada(i)=-0.5;
       end
    end
else

    for i=1:N
        s_cuantizada(i)=floor(signal(i)/cuanto)*cuanto+cuanto/2;
    end

end