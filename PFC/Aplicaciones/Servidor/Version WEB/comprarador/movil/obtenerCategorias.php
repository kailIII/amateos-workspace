<?php
header('Content-Type: text/xml; charset=iso-8859-1');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");


// resuelve el problema de obtención de caracteres erróneos para ciertas letras
function caracteresErroneos($cadena){
	$correcto = preg_replace('/Ãº/','u',$cadena);
	$correcto = preg_replace('/Ã±/','ñ',$cadena);
	
	return $correcto;
}

function creacionXml(){
	$consultaCategorias = mysql_query("SELECT * FROM categorias");

	$xml = '<?xml version="1.0"?>';
	$xml .= '<menu>';

	while($filaCategoria = mysql_fetch_array($consultaCategorias)){
	
		$nombre = caracteresErroneos($filaCategoria['nombre']);
		
		$xml .= '<categoria pos="'.$filaCategoria['posicion'].'">';
		$xml .= $nombre;
		$xml .='</categoria>';

	}
	mysql_free_result($consultaCategorias);

	$xml .= '</menu>';

	return $xml;
}

// se genera el nuevo menú en formato XML
$menu = creacionXml();

echo $menu;


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>