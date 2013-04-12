<?php include 'db.php' ?>
<?php include 'functions.php' ?>

<?php

$username = $_GET['user'];
$password = md5($_GET['pass']);
$serverIP = $_GET['ip'];

$result = mysql_query("SELECT password FROM users WHERE username=\"".$username. "\"");

if($row = mysql_fetch_array($result)){
	
	if($row["password"] == $password){		
		addServerIPtoUser($username, $password, $serverIP);
		if($serverIP != ""){
			echo "Connected";
		}else{
			echo "Disconnected";
		}
	}else{
		echo "Incorrect password";
	}
}else{
	echo "User doesnt exists";
}
mysql_free_result($result);
mysql_close();

?>