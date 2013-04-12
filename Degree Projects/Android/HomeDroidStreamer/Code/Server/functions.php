<?php
function addUser($username, $password, $email) {
	
	$password = md5($password);
	
	$sql = "INSERT INTO users (`username`, `password`, `email`) VALUES (";
	$sql .= '"' .$username . '"'; 
	$sql .= ',"' . $password . '"';
	$sql .= ',"' . $email . '")';
	
	mysql_query($sql);
	?>
	<?php include 'webTop.php' ?>
	<?php echo "Registro realizado con &eacute;xito!";?>
	<?php include 'webBottom.php' ?>
	<?php
	
}

function checkUsername($username) {
	$sql = "SELECT id FROM users WHERE username=\"".$username. "\"";
	$result = mysql_query($sql);
	$row = mysql_fetch_array($result);
	mysql_free_result($result);	
	return $row;
}

function checkEmail($email) {
	$sql = "SELECT id FROM users WHERE email=\"".$email. "\"";
	$result = mysql_query($sql);
	$row = mysql_fetch_array($result);
	mysql_free_result($result);
	return $row;
}

function addServerIPtoUser($username, $password, $serverIP){
	$result = mysql_query("UPDATE `users` SET `server_ip` = \"".$serverIP. "\" WHERE `users`.`username` = \"".$username. "\" AND `users`.`password` = \"".$password. "\" LIMIT 1;");
}

function getIP($username) {
	$result = mysql_query("SELECT `server_ip` FROM `users` WHERE `username`=\"".$username. "\"");
	$row = mysql_fetch_array($result);
	$ip = $row["ip"];
	mysql_free_result($result);	
	return $ip;
}

?>