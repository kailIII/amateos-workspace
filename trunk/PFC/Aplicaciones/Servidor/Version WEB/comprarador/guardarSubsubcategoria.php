<?php
header('Content-Type: text/html; charset=UTF-8');  
// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");

/**********************************************************/
/********** DEFINICIÓN DE FUNCIONES
/**********************************************************/

// verifica si existe la subsubcategoria en la base de datos
function buscarSubsubcategoria($nombre,$categoria,$subcategoria) {
	
	// confección de la consulta
	$sql = "SELECT * FROM subsubcategorias WHERE nombre='$nombre' AND categoria='$categoria' AND subcategoria='$subcategoria'";

	// se genera la consulta
	$result = mysql_query($sql);
	
	// se obtiene el resultado
	$row = mysql_fetch_array($result);
	
	// se liberan recursos
	mysql_free_result($result);	
	return $row;
}

// actualiza los valores de la subsubcategoria
function actualizarSubsubcategoria($id,$posicion,$categoria,$subcategoria) {

	// confección de la consulta
	$sql = "UPDATE subsubcategorias SET posicion='$posicion' WHERE id='$id'";
	
	// se genera la consulta
	mysql_query($sql);
		
	echo "actualizada posicion";
}

// agrega una nueva Subsubcategoria a la base de datos
function agregarSubsubcategoria($nombre,$posicion,$categoria,$subcategoria){

	// confección de la consulta
	$sql = "INSERT INTO subsubcategorias (`nombre`, `posicion`, `categoria`, `subcategoria`) VALUES (";
	$sql .= "'$nombre', '$posicion', '$categoria', '$subcategoria')";
	
	// se genera la consulta
	mysql_query($sql);

	echo "insertada";
}



/**********************************************************/
/********** EJECUCIÓN
/**********************************************************/

// Obtención de parámetros de URL
$nombre = utf8_encode($_GET['nombre']);
$posicion = $_GET['posicion'];
$categoria = $_GET['categoria'];
$subcategoria = $_GET['subcategoria'];
	
// se comprueba si la subcategoria existe o no
$busqueda = buscarSubsubcategoria($nombre,$categoria,$subcategoria);

if($busqueda){ // si la categoria existe -> actualizar
	if($posicion==$busqueda["posicion"]){
		
		// la subsubcategoria no ha cambiado
		echo "no modificada"
	}else{
		actualizarSubsubcategoria($busqueda["id"],$posicion,$categoria,$subcategoria);
	}
}else{ // si la categoria no existe -> agregar categoria
	
	agregarSubsubcategoria($nombre,$posicion,$categoria,$subcategoria);
	
}

// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>