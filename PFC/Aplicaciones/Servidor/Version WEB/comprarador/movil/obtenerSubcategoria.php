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


function creacionXml($categoria){

	$xml = '<?xml version="1.0"?>';
	$xml .= '<menu>';

	$consultaSubcategorias = mysql_query("SELECT * FROM subcategorias WHERE categoria='$categoria'");
	while($filaSubcategoria = mysql_fetch_array($consultaSubcategorias)){
		
		$nombre = caracteresErroneos($filaSubcategoria['nombre']);
		
		$xml .= '<subcategoria nombre="'.$nombre.'" pos="'.$filaSubcategoria['posicion'].'">';
				
		$xml .='</subcategoria>';
		
	}
	mysql_free_result($consultaSubcategorias);
	
	$xml .= '</menu>';

	return $xml;
}
$cat = $_GET['cat'];
// se genera el nuevo menú en formato XML
$menu = creacionXml($cat);

echo $menu;


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>