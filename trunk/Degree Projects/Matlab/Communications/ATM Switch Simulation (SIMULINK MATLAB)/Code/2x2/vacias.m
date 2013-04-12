function [vacias1,vacias2]=contar_vacias(destinos1,destinos2)
vacias1=0;
vacias2=0;
for i=1:length(destinos1)
    if destinos1(i)==0
        vacias1=vacias1+1;
    end
    if destinos2(i)==0
        vacias2=vacias2+1;
    end
end
