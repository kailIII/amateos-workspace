<?php
$host = 'localhost';
$username = 'hodrost';
$database = 'hodrost';
$password = 'telematicaAplicada';

$connect = mysql_connect($host,$username,$password) or die("Error connecting to Database! " . mysql_error());
mysql_select_db($database , $connect) or die("Cannot select database! " . mysql_error());
?>