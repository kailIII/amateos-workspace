var quiniela={jornada1:{Granada:2,Betis:0},jornada2:{Granada:3,BarcelonaB:2},jornada3:{Granada:1,Celta:0}}

for ( var i in quiniela ) {
	print(i);
	print("¨¨¨¨¨¨¨¨¨");
	
	for (var j in quiniela[i]){
		print(j + ":" + quiniela[i][j]);
	}
	print("\n");
}