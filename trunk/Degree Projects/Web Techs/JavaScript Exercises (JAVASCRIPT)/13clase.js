var tabla="table"; 
var celdas="td"; 
var filas="tr"; 
var numFilas=4;

function Fila (columnas) {
	
	this.contenido="";
	
	for (i=0; i<columnas; i++)
		this.contenido+="<"+celdas+">"+"celdas"+"</"+celdas+">";
	
}// Fin clase Fila

print( "<"+tabla+">"); 

for (j=0; j<numFilas; j++ ) { 
	print( "<"+filas+">"); 
	fila = new Fila(3);
	print (fila.contenido);
	print ("</"+filas+">\n"); 
}// Fin for 

print ("</"+tabla+">");