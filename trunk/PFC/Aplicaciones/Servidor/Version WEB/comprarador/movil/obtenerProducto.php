<?php
header('Content-Type: text/xml;charset=iso-8859-1');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");


$busqueda = $_GET['texto'];
$codigo = $_GET['codigo'];
$cat = $_GET['cat'];
$subcat = $_GET['subcat'];
$subsubcat = $_GET['subsubcat'];

if($busqueda!=null){
	$sql = "SELECT * FROM `productos` WHERE (descripcion LIKE '%".$busqueda."%' OR marca LIKE '%".$busqueda."%')";
}elseif($codigo!=null){
	$sql = "SELECT * FROM `productos` WHERE (codigo_barras='$codigo')";
}else{
	$sql = "SELECT * FROM `productos` WHERE (categoria='$cat' AND subcategoria='$subcat' AND subsubcategoria='$subsubcat')";
}
	$consulta = mysql_query($sql);

	if(mysql_num_rows($consulta)==0){
		$xml = '<productos aciertos="no">';
	}else{
		$xml = '<productos aciertos="si">';

		while($fila = mysql_fetch_array($consulta)){
			$xml .= '<producto id="'.$fila['id'].'" desc="'.$fila['descripcion'].'" marca="'.$fila['marca'].'" ';		
			$xml .= 'img="'.$fila['imagen'].'" formato="'.$fila['formato'].'"/>';
		}
		mysql_free_result($consulta);
		}
		$xml .= '</productos>';


// se genera el nuevo menú en formato XML
echo $xml;


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>