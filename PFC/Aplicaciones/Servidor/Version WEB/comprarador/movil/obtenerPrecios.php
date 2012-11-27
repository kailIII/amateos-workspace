<?php
header('Content-Type: text/xml; charset=iso-8859-1');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");


$id = $_GET['id'];

$sql = "SELECT * FROM `productos` WHERE id='$id'";
	$consulta = mysql_query($sql);

	$xml = '<?xml version="1.0"?>';
	$xml .= '<producto>';

	while($fila = mysql_fetch_array($consulta)){
	
		$xml .= '<mercadona precio="'.$fila['precio_mercadona'].'" relativo="'.$fila['precio_relativo_mercadona'].'" oferta="'.$fila['oferta_mercadona'].'" />';		
		$xml .= '<carrefour precio="'.$fila['precio_carrefour'].'" relativo="'.$fila['precio_relativo_carrefour'].'" oferta="'.$fila['oferta_carrefour'].'" />';		
		$xml .= '<hipercor precio="'.$fila['precio_hipercor'].'" relativo="'.$fila['precio_relativo_hipercor'].'" oferta="'.$fila['oferta_hipercor'].'" />';		
		$xml .= '<corteIngles precio="'.$fila['precio_corteIngles'].'" relativo="'.$fila['precio_relativo_corteIngles'].'" oferta="'.$fila['oferta_corteIngles'].'" />';		
		
	}
	mysql_free_result($consulta);

	$xml .= '</producto>';


// se genera el nuevo menú en formato XML
echo $xml;


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>