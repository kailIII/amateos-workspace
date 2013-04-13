load('Partido.js');

var equipos= new Array('Madrid', 'Barça', 'Atleti', 'Geta', 'Betis', 'Depor', 'Sevilla', 'Graná');

var estadisticas = new Array('Victorias','Derrotas','Empates');
estadisticas['Victorias']=new Array;
estadisticas['Derrotas']=new Array;
estadisticas['Empates']=new Array;

function jornada( estosEquipos ) {
	
	var equiposAqui = new Array;
	equiposAqui = equiposAqui.concat(estosEquipos);
	var midsize = equiposAqui.length/2;
	var quiniela = new Array( midsize );
	var unox2 = new Array( '1','x','2');
	for ( var i=0; i < midsize ; i++ ) {
		var equipo1 = equiposAqui.splice(Math.floor( equiposAqui.length*Math.random()) , 1);
		var equipo2 = equiposAqui.splice(Math.floor( equiposAqui.length*Math.random()), 1);
		quiniela[i] = new Partido( equipo1, equipo2 );
		quiniela[i].setResultado( unox2[Math.floor( 3*Math.random()) ]);
	}
	return quiniela;
}


var quinielas = new Array;
for ( var i = 0; i < 10; i ++ ) {
	quinielas[i] = jornada( equipos ); 
}

var resultados= new Array;
for ( var i in equipos ) {
	//Inicialización de los vectores a 0
	resultados[equipos[i]]=0;
	estadisticas['Victorias'][equipos[i]]=0;
	estadisticas['Derrotas'][equipos[i]]=0;
	estadisticas['Empates'][equipos[i]]=0;
}

for ( var i = 0; i < quinielas.length; i ++ ) {
	for ( var j = 0;j < quinielas[i].length; j ++ ) {
		var local = quinielas[i][j].local;
		var visitante = quinielas[i][j].visitante;
		var resultado = quinielas[i][j].resultado;
		if ( resultado == 1 ) {
			resultados[local]+=3;
			estadisticas['Victorias'][local]+=1;
			estadisticas['Derrotas'][visitante]+=1;
		} else if ( resultado == 'x' ) {
			resultados[local]+=1;
			resultados[visitante]+=1;
			estadisticas['Empates'][local]+=1;
			estadisticas['Empates'][visitante]+=1;			
		} else { // resultado == 2
			resultados[visitante]+=3
			estadisticas['Victorias'][visitante]+=1;
			estadisticas['Derrotas'][local]+=1;
		}
	}
}

//Impresión de resultados

print("Equipo\t"+"Puntos\t"+"Victorias  "+"Empates   "+"Derrotas\n");
print("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨");

for (i=0; i<equipos.length;i++){
	print( equipos[i] + "\t" + resultados[equipos[i]] +  "\t   " + estadisticas['Victorias'][equipos[i]] + "\t      " + estadisticas['Empates'][equipos[i]] + "\t\t  " + estadisticas['Derrotas'][equipos[i]] + "\n");
}

