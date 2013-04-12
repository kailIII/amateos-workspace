<?php include 'db.php' ?>
<?php include 'functions.php' ?>

<?php 

if(trim($_POST['username']) != "" && trim($_POST['email']) != "" && trim($_POST['password']) != "")
{
	
	if(checkUsername($_POST['username'])){?>
		<?php include 'webTop.php' ?>
		<?php echo "Error: el nombre de usuario ya est&aacute; en uso."; ?>
		<?php include 'webBottom.php' ?>
		<?php
	}else if(checkEmail($_POST['email'])){	?>
			<?php include 'webTop.php' ?>
			<?php echo "Error: el email ya est&aacute; en uso.";?>
			<?php include 'webBottom.php' ?>
			<?php
	}else{
		addUser($_POST['username'],$_POST['password'],$_POST['email']);
	}
	
	//mysql_free_result($result);
} else {	?>
		<?php include 'webTop.php' ?>
		<?php echo "Debe rellenar todos los campos para realizar el registro.";	?>
		<?php include 'webBottom.php' ?>
		<?php
}
mysql_close();
?>