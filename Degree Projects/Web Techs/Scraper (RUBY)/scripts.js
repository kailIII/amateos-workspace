//********* DECLARACIÓN DE VARIABLES ************
// Variables para la realización de peticiones AJAX
var request;
var requestBusqueda;

// Variables para el buen funcionamiento del menú y la web
var ultimaSeleccion;
var supermercadoElegido;

// Variables utilizadas para la búsqueda de productos a través del buscador
var supermercadoBusqueda;
var opcionBusqueda;
var textoBusqueda;
var textoFinalBusqueda;

// Vector asociativo id -> nombre
var supermercados = new Array();
supermercados['1'] = 'Mercadona';
supermercados['2'] = 'Carrefour';
supermercados['3'] = 'Hipercor';
supermercados['4'] = 'CorteIngles';
//************************************************


//************* FUNCIONES ************************

// peticion -> Realiza la petición a consulta.cgi para obtener todos los productos relativos a la opción del menú y supermercado seleccionados
function peticion(ruta) {
	if (supermercadoElegido != null){
		ultimaSeleccion = ruta;
		request = new XMLHttpRequest();
		var peticion_str = '/cgi-bin/consulta.cgi?ruta='+ruta+'&supermercado='+supermercadoElegido;
		request.open('GET', peticion_str , true);
		request.onreadystatechange=respuesta;
		request.send(null);
		
		// Se muestra el gif de 'cargando'
		document.getElementById('cargando').innerHTML ="<img src='imagenes/cargando.gif' alt='Cargando...'></img>";
	}else{
		alert('Debe seleccionar un supermercado.')
	}
} // FIN PETICION

// respuesta -> Gestiona la respuesta procedente de consulta.cgi tras la petición realizada a través del menú de la web
function respuesta(){
	if ( request.readyState == 4 ) {
		if ( request.status == 200 ) {
			var doc = request.responseXML; 			
			var productos = doc.getElementsByTagName("producto");
			
			// Se crea la tabla con los datos de los productos obtenidos tras la respuesta del cgi	
			tabla = "<table><TR><TH></TH><TH><font color='#4b52c4' size=5>Marca</font></TH><TH><font color='#4b52c4' size=5>Precio € (Precio €/litro)</font></TH><TH><font color='#4b52c4' size=5>Descripción</font></TH></TR>";
			i=0;
			while(i<productos.length){
				marca = productos[i].childNodes[1].childNodes[0].nodeValue;
				descripcion = productos[i].childNodes[3].childNodes[0].nodeValue;
				precio = productos[i].childNodes[5].childNodes[0].nodeValue;
				precioRelativo = productos[i].childNodes[7].childNodes[0].nodeValue;
				imagen = productos[i].childNodes[9].childNodes[0].nodeValue;
				tabla = agregarFila(tabla,imagen,marca,precio,precioRelativo,descripcion);
				i++;
			}
			
			tabla += "</table>";
			// Se quita el gif de 'cargando'
			document.getElementById('cargando').innerHTML ="";
			
			// Se muestran los productos obtenidos en el div resultados		
			document.getElementById('resultados').innerHTML= tabla;
			
		}
	}
} // FIN FUNCIÓN RESPUESTA


// peticionBusqueda -> Realiza la petición de búsqueda de productos a la BBDD a través de busqueda.cgi
function peticionBusqueda(opcion, textoABuscar, supermercado){
	requestBusqueda = new XMLHttpRequest();
	var peticion_str = '/cgi-bin/busqueda.cgi?tipo='+opcion+'&q='+textoABuscar+'&super='+supermercado;
	requestBusqueda.open('GET', peticion_str , true);
	requestBusqueda.onreadystatechange=respuestaBusqueda;
	requestBusqueda.send(null);
} // FIN FUNCIÓN BÚSQUEDA


// respuestaBusqueda -> Gestiona la respuesta procedente de busqueda.cgi tras la petición de búsqueda de un producto
function respuestaBusqueda(){
	
	if ( requestBusqueda.readyState == 4 ) {
		if ( requestBusqueda.status == 200 ) {
			var doc = requestBusqueda.responseXML; 			
			var productos = doc.getElementsByTagName("producto");
			
			// Se crea la tabla con los datos de los productos obtenidos tras la respuesta del cgi
			tabla = "<table><TR><TH></TH><TH><font color='#4b52c4' size=5>Marca</font></TH><TH><font color='#4b52c4' size=5>Precio € (Precio €/litro)</font></TH><TH><font color='#4b52c4' size=5>Descripción</font></TH></TR>";
			i=0;
			while(i<productos.length){
				marca = productos[i].childNodes[1].childNodes[0].nodeValue;
				descripcion = productos[i].childNodes[3].childNodes[0].nodeValue;
				precio = productos[i].childNodes[5].childNodes[0].nodeValue;
				precioRelativo = productos[i].childNodes[7].childNodes[0].nodeValue;
				imagen = productos[i].childNodes[9].childNodes[0].nodeValue;
				tabla = agregarFila(tabla,imagen,marca,precio,precioRelativo,descripcion);
				i++;
			}
			tabla += "</table>";
			
			// Se agrega la tabla y la imagen del supermercado correspondiente al texto final que se mostrará
			textoFinalBusqueda += '<img src="imagenes/'+supermercados[supermercadoBusqueda]+'.jpg"></img><br><br>'+tabla+'<br>';
			
			// Si no se ha realizado la búsqueda para los 4 supermercados se realiza la llamada de búsqueda para el siguiente supermercado
			if(supermercadoBusqueda<4){
				supermercadoBusqueda ++;
				peticionBusqueda(opcionBusqueda, textoBusqueda, supermercados[supermercadoBusqueda]);
			}else{	
				// Se quita el gif de 'cargando'
				document.getElementById('cargando').innerHTML ="";
				
				// Se muestran los productos obtenidos en el div resultados		
				document.getElementById('resultados').innerHTML+=textoFinalBusqueda;
			} // fin if		
		}// fin if status
	}// fin if readyState
} // FIN FUNCIÓN RESPUESTA BUSQUEDA


// busqueda -> Gestiona la función de búsqueda de la web e inicia la realización de peticiones de búsqueda.
function busqueda(formulario){
	// Ponemos en blanco ciertas zonas de la web y se resetean las variables apropiadas
	document.getElementById('resultados').innerHTML="";
	textoFinalBusqueda = "";
	document.getElementById('supermercado').innerHTML ="";
	ultimaSeleccion=null;
	
	// Se muestra el gif de 'cargando'
	document.getElementById('cargando').innerHTML ="<img src='imagenes/cargando.gif' alt='Cargando...'></img>";
	
	// Se obtienen los datos de la búsqueda a partir del formulario
	opcionBusqueda=formulario.opcion.value;
	textoBusqueda=formulario.entrada.value;
	
	//Se hace la llamada para comenzar la búsqueda
	supermercadoBusqueda = 1;
	peticionBusqueda(opcionBusqueda, textoBusqueda, supermercados[supermercadoBusqueda]);	
}// FIN BUSQUEDA

// agregarFila -> Agrega una fila a la tabla HTML en la que se mostrarán los datos relativos a cada producto
function agregarFila(tabla,imagen,marca,precio,relativo,desc){
	tabla +="<TR><TH><img src="+imagen+"></img></TH><TH>"+marca+"</TH><TH>"+precio+" ("+relativo+")</TH><TH>"+desc+"</TH></TR>"; 
	return tabla;
} // FIN AGREGAR FILA

// seleccionSuper -> Gestiona la selección de un supermercado a través del menú
function seleccionSuper(seleccion){
	supermercadoElegido = supermercados[seleccion];
	document.getElementById('supermercado').innerHTML = '<img src="imagenes/'+supermercados[seleccion]+'.jpg"></img>';
	
	// Si se están mostrando productos seleccionados mediante el menú se realiza una nueva petición para mostrar dichos productos
	// pero en el nuevo supermercado
	if(ultimaSeleccion != null){
		peticion(ultimaSeleccion);
	}
	
} // FIN SELECCION SUPER