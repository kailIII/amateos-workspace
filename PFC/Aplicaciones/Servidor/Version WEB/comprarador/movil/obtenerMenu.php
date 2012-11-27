<?php
header('Content-Type: text/xml; charset=iso-8859-1');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");


function getSubcategorias($categoria){
	
	$consultaSubcategorias = mysql_query("SELECT * FROM subcategorias WHERE categoria='$categoria'");
	
	$xml = "";
	
	while($filaSubcategoria = mysql_fetch_array($consultaSubcategorias)){
		$xml .= '<subcategoria nombre="'.$filaSubcategoria['nombre'].'" pos="'.$filaSubcategoria['posicion'].'">';
		
		$xml .= getSubsubcategorias($categoria,$filaSubcategoria['posicion']);
		
		$xml .='</subcategoria>';
		
	}
	mysql_free_result($consultaSubcategorias);
	
	return $xml;
	
}


function getSubsubcategorias($categoria,$subcategoria){
	
	$consultaSubsubcategorias = mysql_query("SELECT * FROM subsubcategorias WHERE categoria='$categoria' AND subcategoria='$subcategoria'");
	
	$xml = "";
	
	while($filaSubsubcategoria = mysql_fetch_array($consultaSubsubcategorias)){
		$xml .= '<subsubcategoria nombre="'.$filaSubsubcategoria['nombre'].'" pos="'.$filaSubsubcategoria['posicion'].'">';
		
		
		$xml .='</subsubcategoria>';
		
	}
	mysql_free_result($consultaSubsubcategorias);
	
	return $xml;
	
}

function creacionXmlMenu(){
	$consultaCategorias = mysql_query("SELECT * FROM categorias");

	$xml = '<?xml version="1.0"?>';
	$xml .= '<menu>';

	while($filaCategoria = mysql_fetch_array($consultaCategorias)){
	
		$xml .= '<categoria nombre="'.$filaCategoria['nombre'].'" pos="'.$filaCategoria['posicion'].'">';

		//$xml .= getSubcategorias($filaCategoria['posicion']);
	
	
		$xml .='</categoria>';

	}
	mysql_free_result($consultaCategorias);

	$xml .= '</menu>';

	return $xml;
}

// se genera el nuevo menú en formato XML
$menu = creacionXmlMenu();

echo $menu;


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>