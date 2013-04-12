<?php include 'db.php' ?>
<?php include 'functions.php' ?>

<?php
$username = $_GET['user'];
$password = md5($_GET['pass']);

$result = mysql_query("SELECT password FROM users WHERE username=\"".$username. "\"");

if($row = mysql_fetch_array($result)){
	
	if($row["password"] == $password){		
		$result = mysql_query("SELECT `server_ip` FROM `users` WHERE `username`=\"".$username. "\"");
		while($row = mysql_fetch_array($result))
		{
			$output = array();
			//$data = array ('username'=>$username,'ip'=>$row['server_ip']);
			$data = array ('ip'=>$row['server_ip']);
			array_push($output, $data);
			//$output = array ('ip'=>$row['server_ip']);
			
		    //echo $output;
			//header('Content-type: application/json');
			echo json_encode($output);
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