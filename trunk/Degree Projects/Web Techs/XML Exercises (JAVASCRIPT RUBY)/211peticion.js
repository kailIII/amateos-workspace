var request;

function peticion() {
	
	request = new XMLHttpRequest();
	var partido=document.getElementById('partido').value;
	var resultado=document.getElementById('resultado').value;
	
	var peticion_str = '/cgi-bin/211quiniela.cgi?partido='+encodeURIComponent(partido)+'&resultado='+resultado;
	request.open('GET', peticion_str , true);
	request.onreadystatechange= respuesta;
	request.send(null);
}

function respuesta(){
	if ( request.readyState == 4 ) {
		if ( request.status == 200 ) {
			
			var doc = request.responseXML; 
			var partido = doc.getElementsByTagName("partido")[0].childNodes[0].nodeValue;
			var resultado = doc.getElementsByTagName("resultado")[0].childNodes[0].nodeValue;	
			
			//Una vez extraídos los datos que envía el servidor, se muestran por pantalla los cambios.	
			document.getElementById('cambios').innerHTML= "El partido n&uacute;mero "+partido+" ha actualizado su resultado a "+resultado+" correctamente.";
			document.getElementById(partido).innerHTML = resultado;
			
		}
	}
}