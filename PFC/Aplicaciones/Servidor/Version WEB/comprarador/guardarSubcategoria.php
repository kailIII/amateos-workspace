<?php
header('Content-Type: text/html; charset=UTF-8');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");



/**********************************************************/
/********** DEFINICIÓN DE FUNCIONES
/**********************************************************/

// verifica si existe la subcategoria en la base de datos
function buscarSubcategoria($nombre,$categoria) {
	
	// confección de la consulta
	$sql = "SELECT * FROM subcategorias WHERE nombre='$nombre' AND categoria='$categoria'";

	// se genera la consulta
	$result = mysql_query($sql);
	
	// se obtiene el resultado
	$row = mysql_fetch_array($result);
	
	// se liberan recursos
	mysql_free_result($result);	
	return $row;
}

// actualiza los valores de la subcategoria
function actualizarSubcategoria($id,$posicion) {
	
	// confección de la consulta
	$sql = "UPDATE subcategorias SET posicion='$posicion' WHERE id='$id'";
	
	// se genera la consulta
	mysql_query($sql);
		
	echo "actualizada posicion";
}

// agrega una nueva SUBcategoria a la base de datos
function agregarSubcategoria($nombre,$posicion,$categoria){

	// confección de la consulta
	$sql = "INSERT INTO subcategorias (`nombre`, `posicion`, `categoria`) VALUES (";
	$sql .= "'$nombre', '$posicion', '$categoria')";
	
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
$categoria = $_GET['categoria'];
	
// se comprueba si la subcategoria existe o no
$busqueda = buscarSubcategoria($nombre,$categoria);

if($busqueda){ // si la categoria existe -> actualizar

	if($posicion==$busqueda["posicion"]){
		
		// la subcategoria no ha cambiado
		echo "no modificada"
	}else{
		actualizarSubcategoria($busqueda["id"],$posicion,$categoria);
	}
}else{ // si la categoria no existe -> agregar categoria
	
	agregarSubcategoria($nombre,$posicion,$categoria);
	
}


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>