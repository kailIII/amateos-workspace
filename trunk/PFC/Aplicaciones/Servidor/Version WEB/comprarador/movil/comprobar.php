<?php
header('Content-Type: text/xml; charset=iso-8859-1');  

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");

//USO: comprobar.php?ids[]=0&ids[]=1
$ids = $_GET['ids'];

// variables que contendrán la suma de precios para cada supermercado
/*$mercadona = 0;
$carrefour = 0;
$hipercor = 0;
$corteIngles = 0;*/
$total = array("mercadona" => "", "carrefour" => "", "hipercor" => "", "corteIngles" => "");

// variables que contendrán el número de aciertos para cada supermercado
/*$aciertosMercadona = 0;
$aciertosCarrefour = 0;
$aciertosHipercor = 0;
$aciertosCorteIngles = 0;*/
$aciertos = array("mercadona" => "0", "carrefour" => "0", "hipercor" => "0", "corteIngles" => "0");


for ($i=0; $i<sizeof($ids);$i++){

	//$sql = "SELECT * FROM `productos` WHERE id='$ids[$i]'";
	
	$sql = "SELECT * FROM `productos` WHERE id='$ids[$i]'";
	$consulta = mysql_query($sql);	

	while($fila = mysql_fetch_array($consulta)){
		
		// MERCADONA
		$precio_mercadona = $fila['precio_mercadona'];
		if($precio_mercadona != "0.00"){
			$aciertos['mercadona'] += 1;
			
			//se convierte el valor a decimal sumando 0.00
			$precio_mercadona += 0.00;
			$total['mercadona'] += $precio_mercadona;
		}
		
		// CARREFOUR
		$precio_carrefour = $fila['precio_carrefour'];
		if($precio_carrefour != "0.00"){
			$aciertos['carrefour'] += 1;
			
			//se convierte el valor a decimal sumando 0.00
			$precio_carrefour += 0.00;
			$total['carrefour'] += $precio_carrefour;
		}
		
		// MERCADONA
		$precio_hipercor = $fila['precio_hipercor'];
		if($precio_hipercor != "0.00"){
			$aciertos['hipercor'] += 1;
			
			//se convierte el valor a decimal sumando 0.00
			$precio_hipercor += 0.00;
			$total['hipercor'] += $precio_hipercor;
		}
		
		// MERCADONA
		$precio_corteIngles = $fila['precio_corteIngles'];
		if($precio_corteIngles != "0.00"){
			$aciertos['corteIngles'] += 1;
			
			//se convierte el valor a decimal sumando 0.00
			$precio_corteIngles += 0.00;
			$total['corteIngles'] += $precio_corteIngles;
		}
				
	}//fin while
	mysql_free_result($consulta);
} //fin for



// CREACIÓN DEL XML
$xml = '<?xml version="1.0"?>';
$xml .= '<resultado>';
$xml .= '<mercadona total="'.$total['mercadona'].'" aciertos="'.$aciertos['mercadona'].'" />';
$xml .= '<carrefour total="'.$total['carrefour'].'" aciertos="'.$aciertos['carrefour'].'" />';
$xml .= '<hipercor total="'.$total['hipercor'].'" aciertos="'.$aciertos['hipercor'].'" />';
$xml .= '<corteIngles total="'.$total['corteIngles'].'" aciertos="'.$aciertos['corteIngles'].'" />';
$xml .= '</resultado>';

echo $xml;

// se genera el nuevo menú en formato XML
//echo $xml;
/*echo "total-> ".$total['mercadona']."<br>";
echo "aciertos-> ".$aciertos['mercadona']."<br>";
echo "id 0-> ".$ids[0]."<br>";
echo "id 1-> ".$ids[1]."<br>";
echo sizeof($ids);*/


// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>