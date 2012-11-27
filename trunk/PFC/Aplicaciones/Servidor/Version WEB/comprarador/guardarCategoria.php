<?php
header('Content-Type: text/html; charset=UTF-8');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");

/**********************************************************/
/********** DEFINICIÓN DE FUNCIONES
/**********************************************************/

// verifica si existe la categoria en la base de datos
function buscarCategoria($nombre) {
	
	// confección de la consulta
	$sql = "SELECT * FROM categorias WHERE nombre='$nombre'";

	// se genera la consulta
	$result = mysql_query($sql);
	
	// se obtiene el resultado
	$row = mysql_fetch_array($result);
	
	// se liberan recursos
	mysql_free_result($result);	
	return $row;
}

// actualiza los valores de la categoria
function actualizarCategoria($id,$posicion) {
	
	// confección de la consulta
	$sql = "UPDATE categorias SET posicion='$posicion' WHERE id='$id'";
	
	// se genera la consulta
	mysql_query($sql);
		
	echo "actualizada posicion";
}

// agrega una nueva categoria a la base de datos
function agregarCategoria($nombre,$posicion){
	
	
	// confección de la consulta
	$sql = "INSERT INTO categorias (`nombre`, `posicion`) VALUES (";
	$sql .= "'$nombre', '$posicion')";
	
	// se genera la consulta
	mysql_query($sql);

	echo "insertada";
}


/**********************************************************/
/********** EJECUCIÓN
/**********************************************************/

// Obtención de parámetros de URL
$nombre = $_GET['nombre'];
$posicion = $_GET['posicion'];
	
// se comprueba si la categoria existe o no
$busqueda = buscarCategoria($nombre);

if($busqueda){ // si la categoria existe
	
	if($posicion==$busqueda["posicion"]){
		
		// la categoria no ha cambiado
		echo "no modificada"
	}else{
		
		// la categoria ha cambiado de posición
		actualizarCategoria($busqueda["id"],$posicion);
	}
	
}else{ // si la categoria no existe -> agregar categoria
	
	agregarCategoria($nombre,$posicion);
	
}


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>