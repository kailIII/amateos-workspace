<?php

// se incluye el archivo que abre la conexión con la base de datos
include("includes/db_conectar.php");
// se incluye la clase que descarga, redimensiona y guarda las imágenes
include("includes/resize-class.php");  

/**********************************************************/
/********** DEFINICIÓN DE FUNCIONES
/**********************************************************/

// busca un producto en la base de datos verificando si la descripción
// marca, imagen y formato coinciden con la de alguno de los productos almacenados
function buscarProducto($desc,$marca,$img,$formato) {
	
	
	// Si no hay imagen, no se compara ésta.
	if($img!=null){
		$sql = "SELECT id, imagen_src FROM productos WHERE descripcion='$desc' AND marca='$marca' AND imagen_src='$img' AND formato='$formato'";
	}else{
		$sql = "SELECT id, imagen_src FROM productos WHERE descripcion='$desc' AND marca='$marca' AND formato='$formato'";
	}
	$result = mysql_query($sql);
	$row = mysql_fetch_array($result);
	mysql_free_result($result);	
	return $row;
}

// actualiza los precios e imagen asociados a un producto ya existente en la base de datos
function actualizarProducto($id,$categoria,$subcategoria,$subsubcategoria,$imagen_src,$imagen_src_guardada,$mercadona,$carrefour,$hipercor,$corteIngles,$mercadona_relativo,$carrefour_relativo,$hipercor_relativo,$corteIngles_relativo,$oferta_mercadona,$oferta_carrefour,$oferta_hipercor,$oferta_corteIngles) {

	$sql = "UPDATE productos SET categoria='$categoria', subcategoria='$subcategoria', subsubcategoria='$subsubcategoria', precio_mercadona='$mercadona', precio_carrefour='$carrefour', precio_hipercor='$hipercor', precio_corteIngles='$corteIngles', precio_relativo_mercadona='$mercadona_relativo', precio_relativo_carrefour='$carrefour_relativo', precio_relativo_hipercor='$hipercor_relativo', precio_relativo_corteIngles='$corteIngles_relativo', oferta_mercadona='$oferta_mercadona', oferta_carrefour='$oferta_carrefour', oferta_hipercor='$oferta_hipercor', oferta_corteIngles='$oferta_corteIngles' ";
	$sql .= "imagen_src='$imagen_src', imagen='$imagen'";
	
	// si se incluye imagen -> guardar la imagen con el nombre de su id en el directorio imagenes_productos
	if($imagen_src != null && strcmp($imagen_src_guardada,$imagen_src)!==0){
		guardarImagen($imagen_src,$id);
		$imagen = 1;
		
	}else{
		$imagen = 0;
	}
	
	
	$sql .= " WHERE id='$id'";
	mysql_query($sql);
		
	echo "Actualizado";
}


function agregarProducto($marca,$descripcion,$formato,$categoria,$subcategoria,$subsubcategoria,$imagen_src,$imagen,$mercadona,$carrefour,$hipercor,$corteIngles,$mercadona_relativo,$carrefour_relativo,$hipercor_relativo,$corteIngles_relativo,$oferta_mercadona,$oferta_carrefour,$oferta_hipercor,$oferta_corteIngles){
	
	// confección de la consulta
	$sql = "INSERT INTO productos (`marca`, `descripcion`, `formato`,`categoria`, `subcategoria`, `subsubcategoria`,";
	$sql .= " `imagen_src`, `imagen`, `precio_mercadona`, `precio_carrefour`, `precio_hipercor`, `precio_corteIngles`,";
	$sql .= " `precio_relativo_mercadona`, `precio_relativo_carrefour`, `precio_relativo_hipercor`, `precio_relativo_corteIngles`,`oferta_mercadona`,`oferta_carrefour`,`oferta_hipercor`,`oferta_corteIngles`) VALUES (";
	$sql .= "'$marca', '$descripcion', '$formato', '$categoria', '$subcategoria', '$subsubcategoria',";
	$sql .= "'$imagen_src', '$imagen', '$mercadona', '$carrefour', '$hipercor', '$corteIngles',"; 
	$sql .= "'$mercadona_relativo', '$carrefour_relativo', '$hipercor_relativo', '$corteIngles_relativo', ";
	$sql .= "'$oferta_mercadona','$oferta_carrefour','$oferta_hipercor','$oferta_corteIngles')";
	
	// se genera la consulta para introducir el producto
	mysql_query($sql);

	// si el producto tiene imagen -> guardar la imagen con el nombre de su id en el directorio imagenes_productos
	if($imagen == 1){
		guardarImagen($imagen_src,mysql_insert_id());	
	}

	echo "Guardado";
}

// guarda una imagen con tamaño máximo de 150x150 píxeles y nombre id.jpg a partir de la url que se le pasa
function guardarImagen($url,$id) {
	    $c = curl_init();
	    curl_setopt($c,CURLOPT_URL,$url);
	    curl_setopt($c,CURLOPT_HEADER,0);
	    curl_setopt($c,CURLOPT_RETURNTRANSFER,true);
	    $s = curl_exec($c);
	    curl_close($c);
	    $f = fopen('imagenes_productos/'.$id.'.gif', 'wb');
	    $z = fwrite($f,$s);
}

/**********************************************************/
/********** EJECUCIÓN
/**********************************************************/

// Obtención de parámetros de URL
$marca = $_GET['m'];
$descripcion = $_GET['d'];
$imagen_src = $_GET['im'];
$formato = $_GET['f'];
$categoria = $_GET['cat'];
$subcategoria = $_GET['subcat'];
$subsubcategoria = $_GET['subsubcat'];
$mercadona = $_GET['merc'];
$carrefour = $_GET['carr'];
$hipercor = $_GET['hip'];
$corteIngles = $_GET['cort'];
$mercadona_relativo = $_GET['merc_rel'];
$carrefour_relativo = $_GET['carr_rel'];
$hipercor_relativo = $_GET['hip_rel'];
$corteIngles_relativo = $_GET['cort_rel'];
$oferta_mercadona = $_GET['of_merc'];
$oferta_carrefour = $_GET['of_carr'];
$oferta_hipercor = $_GET['of_hip'];
$oferta_corteIngles = $_GET['of_cort'];
	
// se comprueba si el producto existe o no
$busqueda = buscarProducto($descripcion,$marca,$imagen_src,$formato);

if($busqueda){ // si el producto existe -> actualizar precios
	actualizarProducto($busqueda["id"],$categoria,$subcategoria,$subsubcategoria,$imagen_src,$busqueda["imagen_src"],$mercadona,$carrefour,$hipercor,$corteIngles,$mercadona_relativo,$carrefour_relativo,$hipercor_relativo,$corteIngles_relativo,$oferta_mercadona,$oferta_carrefour,$oferta_hipercor,$oferta_corteIngles);
	
}else{ // si el producto no existe -> agregar producto
	// se comprueba si se introduce imagen o no
	if($imagen_src != null){
		$imagen = 1;
	}else{
		$imagen = 0;
	}
	
	agregarProducto($marca,$descripcion,$formato,$categoria,$subcategoria,$subsubcategoria,$imagen_src,$imagen,$mercadona,$carrefour,$hipercor,$corteIngles,$mercadona_relativo,$carrefour_relativo,$hipercor_relativo,$corteIngles_relativo,$oferta_mercadona,$oferta_carrefour,$oferta_hipercor,$oferta_corteIngles);
	
}

// se incluye el archivo que cierra la conexión con la base de datos
include("includes/db_desconectar.php");
?>