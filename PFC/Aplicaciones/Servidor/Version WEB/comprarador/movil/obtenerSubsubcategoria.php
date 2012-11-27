<?php
header('Content-Type: text/xml; charset=iso-8859-1');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");


// resuelve el problema de obtención de caracteres erróneos para ciertas letras
function caracteresErroneos($cadena){
	$correcto = preg_replace('/Ãº/','u',$cadena);
	$correcto = preg_replace('/Ã±/','ñ',$cadena);
	$correcto = preg_replace('/Ã³/','o',$cadena);
	$correcto = preg_replace('/Ã©/','e',$cadena);
	
	return $correcto;
}


function creacionXmlMenu($categoria,$subcategoria){
	$consultaCategorias = mysql_query("SELECT * FROM categorias");

	$xml = '<?xml version="1.0"?>';
	$xml .= '<menu>';

	$consultaSubsubcategorias = mysql_query("SELECT * FROM subsubcategorias WHERE categoria='$categoria' AND subcategoria='$subcategoria'");
	
	while($filaSubsubcategoria = mysql_fetch_array($consultaSubsubcategorias)){
		
		$nombre = caracteresErroneos($filaSubsubcategoria['nombre']);
		$xml .= '<subsubcategoria nombre="'.$nombre.'" pos="'.$filaSubsubcategoria['posicion'].'">';
		
		
		$xml .='</subsubcategoria>';
		
	}
	mysql_free_result($consultaSubsubcategorias);
	
	$xml .= '</menu>';

	return $xml;
}
$cat = $_GET['cat'];
$subcat = $_GET['subcat'];

// se genera el nuevo menú en formato XML
$menu = creacionXmlMenu($cat,$subcat);

echo $menu;


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>