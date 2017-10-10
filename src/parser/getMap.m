function getMap()
    fileID=fopen('stockholm_graph.txt','r');
    sizeA = [2 Inf];
    formatSpec = '%f %f';
    A = fscanf(fileID,formatSpec,sizeA);
    
    G=graph();
    for i=30:121
        if fix(A(1,i))<fix(A(2,i))
%             fprintf('%d %d\n',fix(A(1,i)), fix(A(2,i)));
           
            G = addedge(G,fix(A(1,i)),fix(A(2,i)));  
        end
    end
    
    latitude=A(1,2:29);
    longitude=A(2,2:29);
    
%     plot(G,'XData', longitude, 'YData', latitude)
    plot(G,'Layout','auto')
    
end
