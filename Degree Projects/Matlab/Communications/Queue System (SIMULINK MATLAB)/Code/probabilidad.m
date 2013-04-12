contador=0;

for i=1:length(tiempo.signals.values)
    if tiempo.signals.values(i)>10
        contador=contador+1;
    end
end

prob=contador/length(tiempo.signals.values)