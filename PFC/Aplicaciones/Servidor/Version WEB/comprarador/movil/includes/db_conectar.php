<?php
$host = 'localhost';
$username = 'icompra';
$database = 'icompra';
$password = 'pfc';

$connect = mysql_connect($host,$username,$password) or die("Error al conectar con la base de datos! " . mysql_error());
mysql_select_db($database , $connect) or die("No se puede seleccionar la base de datos! " . mysql_error());
?>