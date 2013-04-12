function integ = integrador(x)

integ(1)=x(1);
for n=2:length(x)
integ = x(n)-integ(n-1);
end