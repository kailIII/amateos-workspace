function crearMatriz(filas,columnas){
	
	var tabla="table"; 
	var celda="td"; 
	var fila="tr"; 
	var matriz = [1,2,3,5,8]; 
	print( "<"+tabla+">"); 
	
	var i;
	var j;
	
	for (i=0; i<filas; i++ ) { 
        print( "<"+fila+">"); 
        for (j=0; j<columnas; j++ ) { 
			print ("<"+celda+">"+matriz[i]*matriz[j]+"</"+celda+">"); 
        } 
		print ("</"+fila+">\n"); 
	} 
	
	print ("</"+tabla+">"); 
}


//Llamamos a la función
crearMatriz(2,5);