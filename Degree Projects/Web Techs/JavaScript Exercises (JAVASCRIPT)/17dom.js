<script type="application/javascript">function putBloque() {
	
	var h2 = document.body.getElementsByTagName('h2');
	
	for (i=0;i<h2.length;i++){
		var titulo=h2[i];
		document.getElementById('resultado1').innerHTML+="<p>"+titulo.textContent+"<\p>";
	}
}
</script>