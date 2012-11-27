var URLSERVER = "http://192.168.0.103:8080/comprarador/rest/";
var URLIMAGENES = "http://192.168.0.103:8080/comprarador/imagenes_productos/"; 

// variables que controlan que categoria, subcategoria y subsubcategoria han
// sido pulsadas en la busqueda de productos
var categoriaActual = -1;
var subcategoriaActual = -1;
var subsubcategoriaActual = -1;

// variable que controla la creacion/apertura de la base de datos
var bd = false;

// "constantes" que indican el modo en el que deben de manejarse las peticiones
// a la base de datos
var LISTAS_MOSTRADO = 1;
var LISTAS_SELECCION = 2;
var PRODUCTOS_MOSTRAR = 1;
var PRODUCTOS_COMPROBAR = 2;
var PRODUCTOS_COMPRA = 3;

// variables que controlan que lista ha sido seleccionada
var listaActual = {
	"id" : "-1",
	"nombre" : ""
};

// variable que controla que lista es la predeterminada para introducir un
// producto
var listaPredeterminadaAgregarProducto = "-";

// variable que controla que producto ha sido seleccionado
var productoActual = {
	"id" : "-1",
	"marca" : "",
	"descripcion" : "",
	"formato" : ""
};

// variable que almacena cual es el nombre de la lista que se ha seleccionado
// para ver en modo compra
var nombreListaModoCompraActual = "";

// variable que controla si se está visualizando un producto para permitir el
// uso del acelerometro
var vistaProducto = false;

// variable de inicializacion de la aceleracion del acelerometro
var prevX = 1.0;

// ****************************************************************************
// ********************** DEVICE READY ****************************************
// ****************************************************************************

document.addEventListener("deviceready", onDeviceReady, false);
function onDeviceReady() {
	iniciarAcelerometro();

	// evento botón atrás (sólo para Android y Blackberry)
	document.addEventListener("backbutton", function() {
		history.back();
	}, false);

}

// ****************************************************************************
// ********************* INICIALIZACION DEL DOCUMENTO *************************
// ****************************************************************************
$(document).ready(function() {

	$("#mensaje").hide();

	// se establece el mensaje para el gif loading
	$.mobile.loadingMessage = "cargando...";

	// se establece el texto que se muestra en la zona de
	// inserción de
	// texto para filtrado de listviews
	$.mobile.listview.prototype.options.filterPlaceholder = "Filtrar items...";

	// INICIALIZACIÓN DE LA BASE DE DATOS
	iniciarBD();
	crearTablas();

	// EVENTO BOTON CODIGO BARRAS
	$("#botonCodigoBarras").click(function() {
		escanearCodigoBarras();
	});

	// EVENTO BOTÓN MOSTRAR LISTA CATEGORIAS
	$("#botonPaginaListaCategorias").click(function() {
		mostrarCategorias();
	});

	// EVENTO BOTÓN MOSTRAR LISTAS
	$("#botonPaginaListas").click(function() {
		buscarListas(LISTAS_MOSTRADO);
	});

	// EVENTO BOTÓN MOSTRAR FAVORITOS
	$("#botonPaginaFavoritos").click(function() {
		buscarFavoritos();
	});

	// EVENTOS MENU CONTEXTUAL CREAR LISTA
	$("#botonCrearLista").click(function() {
		nombreLista = $("#nombreCrearLista").val();
		agregarLista(nombreLista);
		navigator.notification.vibrate(500);
		buscarListas(LISTAS_MOSTRADO);
	});
	$("#botonCancelarCrearLista").click(function() {
		$.mobile.changePage("#paginaListas");
	});

	// EVENTO BOTÓN BÚSQUEDA DE PRODUCTO
	$("#botonBuscarProducto").click(function() {
		eventoBuscarProductoPulsado($('#campoBuscarProducto').val());
	});

	// EVENTO BOTÓN SELECCIONAR LISTA
	$('#botonSeleccionarLista').click(function() {
		if ($('#contenidoPaginaSeleccionLista div select').val() != "" && $('#contenidoPaginaSeleccionLista div select').val() != "-") {
			listaPredeterminadaAgregarProducto = $('#contenidoPaginaSeleccionLista div select').val();
		}

		if (listaPredeterminadaAgregarProducto != "" && listaPredeterminadaAgregarProducto != "-") {
			agregarProducto(productoActual.id, listaPredeterminadaAgregarProducto, productoActual.marca, productoActual.descripcion, productoActual.formato);
			history.back();
		} else {
			mostrarMensaje("Error", "Debe seleccionar una lista para agregar el producto");
		}
	});

	// EVENTOS CAMBIO PÁGINA DESDE PÁGINA VISUALIZACIÓN DE
	// PRODUCTO.
	// si se sale de una página de vista de producto se
	// desactiva la
	// función del acelerómetro
	$('#paginaProducto').live('pagehide', function(event, ui) {
		vistaProducto = false;
	});

	$('#paginaProductoFavorito').live('pagehide', function(event, ui) {
		vistaProducto = false;
	});

	// EVENTOS BOTONES DE FOOTER
	$('#botonFooterAgregarALista').click(function() {
		buscarListas(LISTAS_SELECCION);
	});

	$('#botonFooterAgregarAFavoritos').click(function() {
		agregarFavorito(productoActual.id, productoActual.marca, productoActual.descripcion, productoActual.formato);
	});

	$('#botonFooterAgregarProductoListaAFavoritos').click(function() {
		agregarFavorito(productoActual.id, productoActual.marca, productoActual.descripcion, productoActual.formato);
	});

	$('#botonFooterAgregarFavoritoALista').click(function() {
		buscarListas(LISTAS_SELECCION);
	});

	$('#botonFooterEliminarFavorito').click(function() {
		eliminarFavorito(productoActual.id);
	});

	$('#botonFooterComprobar').click(function() {
		buscarProductosLista(listaActual.id, PRODUCTOS_COMPROBAR);
	});

	$('#botonFooterEliminarLista').click(function() {
		eliminarLista(listaActual.id);
	});

	$('#botonFooterModoCompra').click(function() {
		buscarProductosLista(listaActual.id, PRODUCTOS_COMPRA, listaActual.nombre);
	});

	// evento swipe right: permite volver a la pantalla anterior
	// deslizando el dedo sobre la pantalla
	$('div.ui-page').live("swiperight", function() {
		history.back();
	});
});

// ****************************************************************************
// ************** FUNCIONES NOTIFICACIONES Y MENSAJES *************************
// ****************************************************************************

// /////////////////////////////////////////////////////////////////////////
// MOSTRAR NOTIFICACION: muestra una notificación
// tipoMensaje = tipo de mensaje a mostrar
// /////////////////////////////////////////////////////////////////////////
function mostrarNotificacion(tipoMensaje) {

	var mensaje;

	// el dispositivo vibra durante 0.5 segundos
	navigator.notification.vibrate(500);

	switch (tipoMensaje) {
	case 1:
		mensaje = "El producto ha sido agregado a la lista";
		break;
	case 2:
		mensaje = "El producto ha sido agregado a favoritos";
		break;
	case 3:
		mensaje = "La lista ha sido eliminada";
		break;
	case 4:
		mensaje = "El producto ha sido eliminado de favoritos";
		break;
	case 5:
		mensaje = "El producto ha sido eliminado de la lista";
		break;
	default:
		mensaje = "";
	}

	// se muestra el mensaje en un cuadro durante 2 segundos
	$("#mensaje p").empty().append(mensaje);
	$("#mensaje").fadeIn(400).delay(2000).fadeOut(400);
}

// /////////////////////////////////////////////////////////////////////////
// MOSTRAR MENSAJE: muestra un mensaje de alerta
// titulo = titulo del mensaje
// mensaje = mensaje que se quiere mostrar
// /////////////////////////////////////////////////////////////////////////
function mostrarMensaje(titulo, mensaje) {
	navigator.notification.alert(mensaje, function() {
	}, titulo, 'Aceptar');
}

// ****************************************************************************
// ************** FUNCIONES CONSTRUCCIÓN DE PÁGINAS ***************************
// ****************************************************************************
// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA RESULTADOS: construye la página de resultados tras hacer
// click sobre la función "comprobar"
// /////////////////////////////////////////////////////////////////////////
function construirPaginaResultados(ids, xml) {
	var datos = new Array();
	var aciertos = new Array();

	// se obtienen los datos del XML de respuesta del servidor
	var totalMercadona = $(xml).find('totalMercadona').text();
	var aciertosMercadona = $(xml).find('aciertosMercadona').text();
	datos.push(parseFloat(totalMercadona));
	aciertos.push(aciertosMercadona + "/" + ids.length);
 
	var totalCarrefour = $(xml).find('totalCarrefour').text();
	var aciertosCarrefour = $(xml).find('aciertosCarrefour').text();
	datos.push(parseFloat(totalCarrefour));
	aciertos.push(aciertosCarrefour + "/" + ids.length);

	var totalHipercor = $(xml).find('totalHipercor').text();
	var aciertosHipercor = $(xml).find('aciertosHipercor').text();
	datos.push(parseFloat(totalHipercor));
	aciertos.push(aciertosHipercor + "/" + ids.length);

	var totalCorteIngles = $(xml).find('totalCorteIngles').text();
	var aciertosCorteIngles = $(xml).find('aciertosCorteIngles').text();
	datos.push(parseFloat(totalCorteIngles));
	aciertos.push(aciertosCorteIngles + "/" + ids.length);

	// Se representan los datos obtenidos
	representarGraficaResultados(datos, aciertos);

	// Se cambia la vista a la página de resultados
	$.mobile.changePage("#paginaResultados");

}

// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA BUSQUEDA: construye la página de resultados tras hacer
// una búsqueda de un producto por nombre
// /////////////////////////////////////////////////////////////////////////
function construirPaginaBusqueda(xml) {

	// se verifica que se han obtenido resultados
	productos = $(xml).find('productos');
	if (productos.length) {
		$('#contenidoBusquedaProductos ul').empty();

		// se recorren los productos y se agregan a la página
		productos.each(function() {
			var descripcion = $(this).find('descripcion').text();
			var marca = $(this).find('marca').text();
			var formato = $(this).find('formato').text();
			var id = $(this).find('id').text();

			textoAagregar = "<li><a href='#' id='enlaceBusquedaProductos-" + id + "'>";
			textoAagregar += "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
			textoAagregar += "<h3>" + marca + "</h3>";
			textoAagregar += "<p>" + "(" + formato + ")" + descripcion + "</p></a>";
			textoAagregar += "<a href='#' id='enlaceBusquedaProductosMenu-" + id + "'data-role='button' data-icon='gear' data-iconpos='notext' data-rel='dialog' data-transition='pop'>Menú</a>";
			textoAagregar += "</li>";

			$('#contenidoBusquedaProductos ul').append(textoAagregar);
			agregarEventoBuscarProducto(id, marca, descripcion, formato);

		}); // close each(
		$('#contenidoBusquedaProductos ul').listview("refresh");

	} else {
		mostrarMensaje("Error", "No se han encontrado coincidencias en la base de datos de productos.");
	}

}

// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA PRODUCTO CATEGORIA: construye la página que muestra la
// información de un producto tras hacer click sobre el producto en una vista
// de categorías
// /////////////////////////////////////////////////////////////////////////
function construirPaginaProductoCategoria(xml, html) {

	$(xml).find('productos').each(function() {

		// MERCADONA
		var precio_mercadona = $(this).find('precioMercadona').text();
		var precio_relativo_mercadona = $(this).find('precioRelativoMercadona').text();
		var oferta_mercadona = $(this).find('ofertaMercadona').text();

		html += "<p><img src='imagenes/mercadona.png' alt='Mercadona' />";

		if (precio_mercadona != "0.00") {
			html += "<span class='precio'> <b> " + precio_mercadona + " (" + precio_relativo_mercadona + ")</b></span>";
		}

		if (oferta_mercadona != "") {
			html += "<img id='ofertaMercadona' src='imagenes/oferta.png' alt='" + oferta_mercadona + "' />";
		}
		html += "</p>";

		var precio_carrefour = $(this).find('precioCarrefour').text();
		var precio_relativo_carrefour = $(this).find('precioRelativoCarrefour').text();
		var oferta_carrefour = $(this).find('ofertaCarrefour').text();

		// html += "<p><b>Carrefour: </b>";
		html += "<p><img src='imagenes/carrefour.png' alt='Carrefour' />";

		if (precio_carrefour != "0.00") {
			html += "<span class='precio'><b> " + precio_carrefour + " (" + precio_relativo_carrefour + ")</b></span>";
		}

		if (oferta_carrefour != "") {
			html += "<img id='ofertaCarrefour' src='imagenes/oferta.png' alt='" + oferta_carrefour + "' />";
		}
		html += "</p>";

		var precio_hipercor = $(this).find('precioHipercor').text();
		var precio_relativo_hipercor = $(this).find('precioRelativoHipercor').text();
		var oferta_hipercor = $(this).find('ofertaHipercor').text();

		html += "<p><img src='imagenes/hipercor.png' alt='Hipercor' />";

		if (precio_hipercor != "0.00") {
			html += "<span class='precio'><b> " + precio_hipercor + " (" + precio_relativo_hipercor + ")</b></span>";
		}

		if (oferta_hipercor != "") {
			html += "<img id='ofertaHipercor' src='imagenes/oferta.png' alt='" + oferta_hipercor + "' />";
		}
		html += "</p>";

		var precio_corteIngles = $(this).find('preciocorteIngles').text();
		var precio_relativo_corteIngles = $(this).find('preciorelativocorteIngles').text();
		var oferta_corteIngles = $(this).find('ofertacorteIngles').text();

		html += "<p><img src='imagenes/corteIngles.png' alt='Corte Ingles' />";

		if (precio_corteIngles != "0.00") {
			html += "<span class='precioCorte'><b> " + precio_corteIngles + " (" + precio_relativo_corteIngles + ")</b></span>";
		}

		if (oferta_corteIngles != "") {
			html += "<img id='ofertaCorteIngles' src='imagenes/oferta.png' alt='" + oferta_corteIngles + "' />";
		}
		html += "</p>";

	}); // close each(

	$('#contenidoPaginaProducto').empty().append(html);

	// se agregan los eventos a las imagenes de oferta
	$('#ofertaMercadona').click(function() {
		mostrarMensaje("Oferta", $(this).attr("alt"));
	});
	$('#ofertaCarrefour').click(function() {
		mostrarMensaje("Oferta", $(this).attr("alt"));
	});
	$('#ofertaHipercor').click(function() {
		mostrarMensaje("Oferta", $(this).attr("alt"));
	});
	$('#ofertaCorteIngles').click(function() {
		mostrarMensaje("Oferta", $(this).attr("alt"));
	});

	$.mobile.hidePageLoadingMsg();

	// se cambia de página
	$.mobile.changePage("#paginaProducto");
}

// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA CATEGORIAS: construye la página que muestra las categorías
// /////////////////////////////////////////////////////////////////////////
function construirPaginaCategorias(xml) {

	// se vacía la lista y se cambia la vista a la página de categorías
	$("#contenidoListaCategorias ul").empty();

	// se extrae la información del xml y se agrega a la página
	$(xml).find('categorias').each(function() {
		var nombre = $(this).find('nombre').text();
		var posicion = $(this).find('posicion').text();
		textoAagregar = "<li data-icon='arrow-r'><a href='#' id='enlaceCategoria-" + posicion + "'>" + nombre + "</a></li>";
		$('#contenidoListaCategorias ul').append(textoAagregar);
		$('#enlaceCategoria-' + posicion).click(function() {
			eventoCategoriaPulsada(posicion, nombre);
		});
	}); // close each(

	$.mobile.hidePageLoadingMsg();

	$.mobile.changePage("#paginaListaCategorias");

	// se refresca la lista para que sea visible
	$("#contenidoListaCategorias ul").listview("refresh");

}

// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA SUBCATEGORIAS: construye la página que muestra las
// subcategorias
// /////////////////////////////////////////////////////////////////////////
function construirPaginaSubcategorias(xml, nombreCategoria) {

	$("#paginaListaSubcategorias div h1").empty().append(nombreCategoria);

	$(xml).find('subcategorias').each(function() {
		var nombre = $(this).find('nombre').text();
		var posicion = $(this).find('posicion').text();
		textoAagregar = "<li data-icon='arrow-r'><a href='#' id='enlaceSubcategoria-" + posicion + "'>" + nombre + "</a></li>";
		$('#contenidoListaSubcategorias ul').append(textoAagregar);
		$('#enlaceSubcategoria-' + posicion).click(function() {
			eventoSubcategoriaPulsada(posicion, nombre);
		});
	}); // close each(

	$.mobile.hidePageLoadingMsg();
	$.mobile.changePage("#paginaListaSubcategorias");

	$("#contenidoListaSubcategorias ul").listview("refresh");
}

// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA SUBSUBCATEGORIAS: construye la página que muestra las
// subsubcategorias
// /////////////////////////////////////////////////////////////////////////
function construirPaginaSubsubcategorias(xml, nombreSubcategoria) {

	$("#paginaListaSubsubcategorias div h1").empty().append(nombreSubcategoria);

	$(xml).find('subsubcategorias').each(function() {
		var nombre = $(this).find('nombre').text();
		var posicion = $(this).find('posicion').text();
		textoAagregar = "<li data-icon='arrow-r'><a href='#' id='enlaceSubsubcategoria-" + posicion + "'>" + nombre + "</a></li>";
		$('#contenidoListaSubsubcategorias ul').append(textoAagregar);
		$('#enlaceSubsubcategoria-' + posicion).click(function() {
			eventoSubsubcategoriaPulsada(posicion, nombre);
		});
	}); // close each(

	$.mobile.hidePageLoadingMsg();
	$.mobile.changePage("#paginaListaSubsubcategorias");

	$("#contenidoListaSubsubcategorias ul").listview("refresh");
}

// /////////////////////////////////////////////////////////////////////////
// CONSTRUIR PAGINA PRODUCTOS CATEGORIA: construye la página que muestra los
// productos pertenecientes a una categoria, subcategoria y subsubcategoria
// /////////////////////////////////////////////////////////////////////////
function construirPaginaProductosCategoria(xml, nombre) {

	$('#contenidoListaProductos ul').empty();

	productos = $(xml).find('productos');
	if (productos.length) {
		productos.each(function() {
			var descripcion = $(this).find('descripcion').text();
			var marca = $(this).find('marca').text();
			var formato = $(this).find('formato').text();
			var id = $(this).find('id').text();

			textoAagregar = "<li><a href='#' id='enlaceListaProductos-" + id + "'>";
			textoAagregar += "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
			textoAagregar += "<h3>" + marca + "</h3>";
			textoAagregar += "<p>" + "(" + formato + ")" + descripcion + "</p></a>";
			textoAagregar += "<a href='#' id='enlaceListaProductosMenu-" + id + "'data-role='button' data-icon='gear' data-iconpos='notext' data-rel='dialog' data-transition='pop'>Menu</a>";
			textoAagregar += "</li>";

			$('#contenidoListaProductos ul').append(textoAagregar);
			agregarEventoProductoListaCategoria(id, marca, descripcion, formato);

		}); // close each(

		$.mobile.hidePageLoadingMsg();
		$.mobile.changePage("#paginaListaProductos");
		$('#contenidoListaProductos ul').listview("refresh");
		$("#paginaListaProductos div h1").empty().append(nombre);
	} else {
		$.mobile.hidePageLoadingMsg();
		mostrarMensaje("Error", "No existen productos en la base de datos");
	}
}

// ****************************************************************************
// ************** FUNCIONES COMPROBACIÓN DE LISTA *****************************
// ****************************************************************************
// /////////////////////////////////////////////////////////////////////////
// COMPROBAR LISTA: se ejecuta cuando se pulsa sobre el botón
// de comprobar una lista
// /////////////////////////////////////////////////////////////////////////
function comprobarLista(ids) {

	var params = "";

	for ( var i = 0; i < ids.length; i++) {
		params += ids[i];
		if (i < ids.length - 1) {
			params += ",";
		}
	}

	if (verificarConexion()) {
		// se realiza la petición al servidor
		$.ajax({
			type : "GET",
			url : URLSERVER + "productos/check/" + params,
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				$.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				$.mobile.hidePageLoadingMsg();
				construirPaginaResultados(ids, xml);
			}
		}); // close $.ajax(

	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion
}

// /////////////////////////////////////////////////////////////////////////
// REPRESENTAR GRAFICA RESULTADOS: crea la pagina de resultados insertando
// la grafica comparativa de resultados
// datos = datos a representar
// tooltips = datos que se mostrarán al hacer "click" sobre las gráficas
// /////////////////////////////////////////////////////////////////////////
function representarGraficaResultados(datos, tooltips) {

	// se crea el gráfico
	var hbar = new RGraph.HBar('grafico', datos);

	// se configuran las opciones de visualización del gráfico
	hbar.Set('chart.labels', [ 'Mercadona', 'Carrefour', 'Hipercor', 'Corte Ingles' ]);
	hbar.Set('chart.gutter.left', 0);
	hbar.Set('chart.background.barcolor1', 'rgba(230,230,230,0)');
	hbar.Set('chart.background.barcolor2', 'rgba(230,230,230,0)');
	hbar.Set('chart.background.grid', false);
	hbar.Set('chart.shadow', true);
	hbar.Set('chart.strokestyle', 'rgba(0,0,0,0)');
	hbar.Set('chart.colors', [ 'rgba(43,97,48,0.85)', 'rgba(2,45,124,0.85)', 'rgba(25,86,130,0.85)', 'rgba(0,166,80,0.85)' ]);
	hbar.Set('chart.colors.sequential', true);

	hbar.Set('chart.labels.above', true);
	hbar.Set('chart.labels.above.decimals', 2);
	hbar.Set('chart.text.color', '#a61d1d');
	hbar.Set('chart.text.size', 12);
	hbar.Set('chart.xlabels', false);
	hbar.Set('chart.tooltips', tooltips);

	RGraph.Effects.HBar.Grow(hbar);

}

// ****************************************************************************
// ************** FUNCIONES AGREGACION EVENTOS ********************************
// ****************************************************************************
// /////////////////////////////////////////////////////////////////////////
// AGREGAR EVENTO PRODUCTO LISTA MODO COMPRA: agrega el evento de clickado
// a un producto de una la lista que se esta visualizando en modo compra
// /////////////////////////////////////////////////////////////////////////
function agregarEventoProductoListaModoCompra(id) {
	$('#enlaceProductoModoCompra-' + id).click(function() {
		if ($(this).css("text-decoration") == "none") {
			$(this).addClass("tachado");
		} else {
			$(this).removeClass("tachado");
		}
	});
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR EVENTO LISTA: agrega el evento de clickado a la lista correspondiente
// por el usuario en la pagina de listas
// id = identificador de la lista en la base de datos
// nombre = nombre de la lista
// /////////////////////////////////////////////////////////////////////////
function agregarEventoLista(id, nombre) {

	$('#enlaceLista-' + id).click(function() {
		eventoListaPulsada(id, nombre);
	});
	$('#enlaceListaMenu-' + id).click(function() {

		listaActual.id = id;
		listaActual.nombre = nombre;

		// se muestra un dialog con las opciones posibles
		$(this).simpledialog({
			'mode' : 'bool',
			'prompt' : 'Elija una opcion',
			'buttons' : {
				'Ver' : {
					click : function() {
						eventoListaPulsada(id, nombre);
						return false;
					},
					icon : "",
					theme : "c"
				},
				'Eliminar' : {
					click : function() {
						if (confirm("Desea eliminar la lista?")) {
							eliminarLista(id);
						}
					},
					icon : "",
					theme : "c"
				},
				'Comprobar' : {
					click : function() {
						buscarProductosLista(listaActual.id, PRODUCTOS_COMPROBAR);
						return false;
					},
					icon : "",
					theme : "b"
				},
				'Modo compra' : {
					click : function() {
						buscarProductosLista(id, PRODUCTOS_COMPRA, nombre);
						return false;
					},
					icon : "",
					theme : "c"
				},
				'Cancelar' : {
					click : function() {
						return true;
					},
					icon : "delete",
					theme : "a",
				}
			// fin cancelar
			}
		// fin buttons
		});// fin simple dialog

	});

}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR EVENTO BUSCAR PRODUCTO: agrega el evento de clickado al
// producto obtenido tras busqueda por nombre
// /////////////////////////////////////////////////////////////////////////
function agregarEventoBuscarProducto(id, marca, descripcion, formato) {
	$('#enlaceBusquedaProductos-' + id).click(function() {
		eventoProductoListaCategoriaPulsado(id, marca, descripcion, formato);
	});
	$('#enlaceBusquedaProductosMenu-' + id).click(function() {

		// se muestra un dialog con las opciones posibles
		$(this).simpledialog({
			'mode' : 'bool',
			'prompt' : 'Elija una opcion',
			'buttons' : {
				'Ver' : {
					click : function() {
						eventoProductoListaCategoriaPulsado(id, marca, descripcion, formato);
						return false;
					},
					icon : "",
					theme : "c"
				},
				'Agregar a favoritos' : {
					click : function() {
						agregarFavorito(id, marca, descripcion, formato);
					},
					icon : "",
					theme : "c"
				},
				'Agregar a lista' : {
					click : function() {
						productoActual.id = id;
						productoActual.marca = marca;
						productoActual.descripcion = descripcion;
						productoActual.formato = formato;
						buscarListas(LISTAS_SELECCION);
					},
					icon : "",
					theme : "c"
				},
				'Cancelar' : {
					click : function() {
						return true;
					},
					icon : "delete",
					theme : "a"
				}
			// fin cancelar
			}
		// fin buttons
		});// fin simple dialog
	});
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR EVENTO PRODUCTO LISTA CATEGORIA: agrega el evento de clickado al
// producto de una lista de productos de tipo categoria
// /////////////////////////////////////////////////////////////////////////
function agregarEventoProductoListaCategoria(id, marca, descripcion, formato) {
	$('#enlaceListaProductos-' + id).click(function() {
		eventoProductoListaCategoriaPulsado(id, marca, descripcion, formato);
	});
	$('#enlaceListaProductosMenu-' + id).click(function() {

		// se muestra un dialog con las opciones posibles
		$(this).simpledialog({
			'mode' : 'bool',
			'prompt' : 'Elija una opcion',
			'buttons' : {
				'Ver' : {
					click : function() {
						eventoProductoListaCategoriaPulsado(id, marca, descripcion, formato);
						return false;
					},
					icon : "",
					theme : "c"
				},
				'Agregar a favoritos' : {
					click : function() {
						agregarFavorito(id, marca, descripcion, formato);
					},
					icon : "",
					theme : "c"
				},
				'Agregar a lista' : {
					click : function() {
						productoActual.id = id;
						productoActual.marca = marca;
						productoActual.descripcion = descripcion;
						productoActual.formato = formato;
						buscarListas(LISTAS_SELECCION);
					},
					icon : "",
					theme : "c"
				},
				'Cancelar' : {
					click : function() {
						return true;
					},
					icon : "delete",
					theme : "a"
				}
			// fin cancelar
			}
		// fin buttons
		});// fin simple dialog
	});
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR EVENTO FAVORITOS: agrega el evento de clickado al favorito
// correspondiente
// id = identificador de la lista en la base de datos
// nombre = nombre de la lista
// /////////////////////////////////////////////////////////////////////////
function agregarEventoFavoritos(id, marca, descripcion, formato) {

	$('#enlaceListaFavoritos-' + id).click(function() {
		eventoFavoritoPulsado(id, marca, descripcion, formato);
		vistaProducto = true;
	});
	$('#enlaceListaFavoritosMenu-' + id).click(function() {

		// se muestra un dialog con las opciones posibles
		$(this).simpledialog({
			'mode' : 'bool',
			'prompt' : 'Elija una opcion',
			'buttons' : {
				'Ver' : {
					click : function() {
						eventoFavoritoPulsado(id, marca, descripcion, formato);
						return false;
					},
					icon : "",
					theme : "c"
				},
				'Eliminar' : {
					click : function() {
						if (confirm("Desea eliminar el favorito?")) {
							eliminarFavorito(id);
						}
					},
					icon : "",
					theme : "c"
				},
				'Agregar a lista' : {
					click : function() {
						productoActual.id = id;
						productoActual.marca = marca;
						productoActual.descripcion = descripcion;
						productoActual.formato = formato;
						buscarListas(LISTAS_SELECCION);
					},
					icon : "",
					theme : "c"
				},
				'Cancelar' : {
					click : function() {
						return true;
					},
					icon : "delete",
					theme : "a"
				}
			// fin cancelar
			}
		// fin buttons
		});// fin simple dialog
	});// fin click
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR EVENTO PRODUCTO LISTA: agrega el evento de clickado al producto
// de una lista
// /////////////////////////////////////////////////////////////////////////
function agregarEventoProductoLista(id, marca, descripcion, formato) {

	$('#enlaceProductoLista-' + id).click(function() {
		eventoProductoListaPulsado(id, marca, descripcion, formato);
	});
	$('#enlaceProductoListaMenu-' + id).click(function() {

		// se muestra un dialog con las opciones posibles
		$(this).simpledialog({
			'mode' : 'bool',
			'prompt' : 'Elija una opcion',
			'buttons' : {
				'Ver' : {
					click : function() {
						eventoProductoListaPulsado(id, marca, descripcion, formato);
						return false;
					},
					icon : "",
					theme : "c"
				},
				'Eliminar' : {
					click : function() {
						if (confirm("Desea eliminar el producto de la lista?")) {
							eliminarProducto(id);
							// se obtienen las
							// listas
							buscarListas(LISTAS_MOSTRADO);
							// se hace click
							// sobre la lista
							// que se estaba
							// viendo
							// para volver a
							// mostrarla
							$('#enlaceLista-' + listaActual.id).click();
						}
					},
					icon : "",
					theme : "c"
				},
				'Agregar a favoritos' : {
					click : function() {
						agregarFavorito(id, marca, descripcion, formato);
					},
					icon : "",
					theme : "c"
				},
				'Cancelar' : {
					click : function() {
						return true;
					},
					icon : "delete",
					theme : "a"
				}
			// fin cancelar
			}
		// fin buttons
		});// fin simple dialog
	});
}

// ****************************************************************************
// ************** FUNCIONES ACCIONES EVENTOS **********************************
// ****************************************************************************

// /////////////////////////////////////////////////////////////////////////
// EVENTO BUSCAR PRODUCTO PULSADO: se ejecuta cuando se pulsa sobre el boton
// de buscar producto por nombre
// texto = texto a buscar
// /////////////////////////////////////////////////////////////////////////
function eventoBuscarProductoPulsado(texto) {

	if (verificarConexion()) {

		$.ajax({
			type : "GET",
			url : URLSERVER + "productos/query/" + texto,
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				$.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				$.mobile.hidePageLoadingMsg();
				construirPaginaBusqueda(xml);
			}
		}); // close $.ajax(
	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO CATEGORIA PULSADA: se ejecuta cuando se pulsa sobre una categoria
// id = identificador de la categoria
// nombre = nombre de la categoria
// /////////////////////////////////////////////////////////////////////////
function eventoListaPulsada(id, nombre) {
	listaActual.id = id;
	listaActual.nombre = nombre;
	buscarProductosLista(id, PRODUCTOS_MOSTRAR);
	$("#paginaLista div h1").empty().append(nombre);
	$.mobile.changePage("#paginaLista");
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO CATEGORIA PULSADA: se ejecuta cuando se pulsa sobre una categoria
// pos = indice de la categoria pulsada
// nombre = nombre de la categoria pulsada
// /////////////////////////////////////////////////////////////////////////
function eventoCategoriaPulsada(pos, nombreCategoria) {
	categoriaActual = pos;
	mostrarSubcategorias(pos, nombreCategoria);
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO SUBCATEGORIA PULSADA: se ejecuta cuando se pulsa sobre una
// subcategoria
// pos = indice de la subcategoria pulsada
// nombre = nombre de la subcategoria pulsada
// /////////////////////////////////////////////////////////////////////////
function eventoSubcategoriaPulsada(pos, nombre) {
	subcategoriaActual = pos;
	mostrarSubsubcategorias(pos, nombre);
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO SUBSUBCATEGORIA PULSADA: se ejecuta cuando se pulsa sobre una
// subcategoria
// pos = indice de la subcategoria pulsada
// nombre = nombre de la subcategoria pulsada
// /////////////////////////////////////////////////////////////////////////
function eventoSubsubcategoriaPulsada(pos, nombre) {
	subsubcategoriaActual = pos;
	obtenerProductosCategoria(nombre);
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO FAVORITO PULSADO: se ejecuta cuando se pulsa sobre un favorito
// en la lista de favoritos
// /////////////////////////////////////////////////////////////////////////
function eventoFavoritoPulsado(id, marca, descripcion, formato) {

	productoActual.id = id;
	productoActual.marca = marca;
	productoActual.descripcion = descripcion;
	productoActual.formato = formato;

	html = "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
	html += "<p><b>Marca: </b>" + marca + "</p>";
	html += "<p><b>Descripci&oacute;n: </b>" + descripcion + "</p>";
	html += "<p><b>Formato: </b>" + formato + "</p>";

	$('#contenidoPaginaProductoFavorito').empty().append(html);
	$.mobile.changePage("#paginaProductoFavorito");
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO PRODUCTO LISTA PULSADO: se ejecuta cuando se pulsa sobre un producto
// de una lista
// /////////////////////////////////////////////////////////////////////////
function eventoProductoListaPulsado(id, marca, descripcion, formato) {
	productoActual.id = id;
	productoActual.marca = marca;
	productoActual.descripcion = descripcion;
	productoActual.formato = formato;

	html = "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
	html += "<p><b>Marca: </b>" + marca + "</p>";
	html += "<p><b>Descripci&oacute;n: </b>" + descripcion + "</p>";
	html += "<p><b>Formato: </b>" + formato + "</p>";

	$('#contenidoPaginaProductoLista').empty().append(html);
	$.mobile.changePage("#paginaProductoLista");
}

// /////////////////////////////////////////////////////////////////////////
// EVENTO PRODUCTO LISTA CATEGORIA PULSADO: se ejecuta cuando se pulsa sobre
// un producto a partir del menú de categorías
// /////////////////////////////////////////////////////////////////////////
function eventoProductoListaCategoriaPulsado(id, marca, descripcion, formato) {

	vistaProducto = true;

	productoActual.id = id;
	productoActual.marca = marca;
	productoActual.descripcion = descripcion;
	productoActual.formato = formato;

	html = "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
	html += "<p><b>Marca: </b>" + marca + "</p>";
	html += "<p><b>Descripci&oacute;n: </b>" + descripcion + "</p>";
	html += "<p><b>Formato: </b>" + formato + "</p>";

	if (verificarConexion()) {

		$.ajax({
			type : "GET",
			url : URLSERVER + "productos/" + id,
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				// $.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				// $.mobile.hidePageLoadingMsg();
				construirPaginaProductoCategoria(xml, html);

			}// close success
		}); // close $.ajax(
	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion

}

// ****************************************************************************
// *************** FUNCIONES MOSTRADO DE MENU DE CATEGORIAS *******************
// ****************************************************************************

// /////////////////////////////////////////////////////////////////////////
// MOSTRAR CATEGORIAS: obtiene la lista de categorias del servidor y la
// muestra en la pagina correspondiente.
// /////////////////////////////////////////////////////////////////////////
function mostrarCategorias() {

	if (verificarConexion()) {

		$.ajax({
			type : "GET",
			url : URLSERVER + "categorias",
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				// $.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				// $.mobile.hidePageLoadingMsg();
				construirPaginaCategorias(xml);
			}
		}); // close $.ajax(
		// $.mobile.changePage("#paginaListaCategorias");

	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion

}

// //////////////////////////////////////////////////////////////////////////
// MOSTRAR SUBCATEGORIAS: obtiene la lista de subcategorias del servidor y la
// muestra en la pagina correspondiente.
// pos = indica el indice de la categoria padre pulsada
// nombreCategoria = nombre de la categoria padre pulsada
// /////////////////////////////////////////////////////////////////////////
function mostrarSubcategorias(pos, nombreCategoria) {

	// se eliminan los items de la listview
	$("#contenidoListaSubcategorias ul").empty();
	if (verificarConexion()) {

		// se obtienen las subcategorías correspondientes a la categoría pulsada
		$.ajax({
			type : "GET",
			url : URLSERVER + "subcategorias/" + pos,
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				$.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				// $.mobile.hidePageLoadingMsg();
				construirPaginaSubcategorias(xml, nombreCategoria);
			}
		}); // close $.ajax(
	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion
}

// //////////////////////////////////////////////////////////////////////////
// MOSTRAR SUBSUBCATEGORIAS: obtiene la lista de subsubcategorias del servidor
// y la muestra en la pagina correspondiente.
// pos = indica el indice de la categoria padre pulsada
// nombreSubcategoria = nombre de la subcategoria padre pulsada
// /////////////////////////////////////////////////////////////////////////
function mostrarSubsubcategorias(pos, nombreSubcategoria) {
	// se eliminan los items de la listview
	$("#contenidoListaSubsubcategorias ul").empty();

	if (verificarConexion()) {

		// se obtienen las subcategorías correspondientes a la categoría pulsada
		$.ajax({
			type : "GET",
			url : URLSERVER + "subsubcategorias/" + categoriaActual + "/" + pos,
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				// $.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				// $.mobile.hidePageLoadingMsg();

				// se comprueba que existen subsubcategorías
				if ($(xml).find('subsubcategorias').length) {
					construirPaginaSubsubcategorias(xml, nombreSubcategoria);

					// si no existen subsubcategorías se muestran los
					// productos de la subcategoría padre
				} else {
					subsubcategoriaActual = 0;
					$.mobile.hidePageLoadingMsg();
					obtenerProductosCategoria(nombreSubcategoria);

				}
			}
		}); // close $.ajax(
	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion
}

// /////////////////////////////////////////////////////////////////////////
// OBTENER PRODUCTOS CATEGORIA: realiza la peticion al servidor para obtener
// la lista de productos de una categoria, subcategoria y subsubcategoria
// /////////////////////////////////////////////////////////////////////////
function obtenerProductosCategoria(nombre) {

	if (verificarConexion()) {

		$.ajax({
			type : "GET",
			url : URLSERVER + "productos/" + categoriaActual + "/" + subcategoriaActual + "/" + subsubcategoriaActual,
			dataType : "xml",
			beforeSend : function() {
				$.mobile.showPageLoadingMsg();
			},
			complete : function(resp, status) {
				// $.mobile.hidePageLoadingMsg();
			},
			success : function(xml) {
				// $.mobile.hidePageLoadingMsg();
				construirPaginaProductosCategoria(xml, nombre);
			}// fin success
		}); // close $.ajax(

		// $.mobile.changePage("#paginaListaProductos");
	} else {
		mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.");
	} // fin verificar conexion
}

// ****************************************************************************
// ******************* FUNCIONES BASE DE DATOS ********************************
// ****************************************************************************

// /////////////////////////////////////////////////////////////////////////
// INICIAR BD: crea/abre la base de datos de la aplicacion
// /////////////////////////////////////////////////////////////////////////
function iniciarBD() {

	try {

		if (!window.openDatabase) {

			console.log('Base de datos no soportada');

		} else {

			var shortName = 'icompra';

			var version = '1.0';

			var displayName = 'Base de datos iCompra';

			var maxSize = 65536; // en bytes

			bd = openDatabase(shortName, version, displayName, maxSize);
		}

	} catch (e) {

		// Error handling code goes here.

		if (e == INVALID_STATE_ERR) {

			// Version number mismatch.
			console.log("Versi\u00f3n de la base de datos no v\u00e1lida.");
		} else {

			console.log("Error de acceso a base de datos desconocido: " + e);
		}

		return;

	}

}

// /////////////////////////////////////////////////////////////////////////
// ERROR HANDLER: se ejecuta cuando se produce un error en el acceso a la
// de datos
// /////////////////////////////////////////////////////////////////////////
function errorHandler(transaction, error) {

	console.log("Error al acceder a BD!-> " + error.message);
	// se devuelve true para continuar con la ejecución de la aplicación
	return true;

}

// /////////////////////////////////////////////////////////////////////////
// CREAR TABLAS: crea las tablas para la base de datos
// /////////////////////////////////////////////////////////////////////////
function crearTablas() {

	try {

		bd.transaction(

		function(transaction) {
			transaction.executeSql('CREATE TABLE listas(id INTEGER NOT NULL PRIMARY KEY , nombre TEXT NOT NULL DEFAULT "");', [], function() {
				console.log("Tabla listas creada.");
			}, errorHandler);
			transaction.executeSql(
					'CREATE TABLE productos(id INTEGER NOT NULL, lista INTEGER NOT NULL, marca TEXT NOT NULL DEFAULT "", descripcion TEXT NOT NULL DEFAULT "", formato TEXT NOT NULL DEFAULT "");', [],
					function() {
						console.log("Tabla productos creada.");
					}, errorHandler);
			transaction.executeSql('CREATE TABLE favoritos(id INTEGER NOT NULL PRIMARY KEY , marca TEXT NOT NULL DEFAULT "", descripcion TEXT NOT NULL DEFAULT "", formato TEXT NOT NULL DEFAULT "");',
					[], function() {
						console.log("Tabla favoritos creada.");
					}, errorHandler);
		});
	} catch (e) {
		console.log("Error al agregar tablas a la base de datos: " + e.message);
		return;
	}
}

// /////////////////////////////////////////////////////////////////////////
// BUSCAR LISTAS: ejecuta una busqueda de las listas en la base de datos.
// /////////////////////////////////////////////////////////////////////////
function buscarListas(opcion) {
	try {
		bd.transaction(function(transaction) {
			if (opcion == LISTAS_MOSTRADO) { // modo mostrado de listas

				transaction.executeSql('SELECT * FROM listas ORDER BY id', [], buscarListasHandler1, errorHandler);

			} else if (opcion == LISTAS_SELECCION) { // modo selección de
				// lista para agregar
				// producto

				// hay que ejecutar dos veces la sentencia sql para que el
				// resultado sea correcto
				transaction.executeSql('SELECT * FROM listas ORDER BY id', [], buscarListasHandler2, errorHandler);
				transaction.executeSql('SELECT * FROM listas ORDER BY id', [], buscarListasHandler2, errorHandler);
			}
		});
	} catch (e) {
		console.log("Error al buscar las listas en la base de datos: " + e.message);
	}
}

// /////////////////////////////////////////////////////////////////////////
// BUSCAR LISTAS HANDLER: manejador de los datos que devuelve la funci�n buscar
// listas
// /////////////////////////////////////////////////////////////////////////
function buscarListasHandler1(transaction, results) {

	// se eliminan las listas anteriores de la página
	$('#contenidoPaginaListas ol').empty();

	// se recorren las listas obtenidas
	for ( var i = 0; i < results.rows.length; i++) {

		var row = results.rows.item(i);
		id = row['id'];
		nombre = row['nombre'];

		html = "<li data-icon=arrow-r><a href='#' id='enlaceLista-" + id + "'>" + nombre + "</a><a href='#' id='enlaceListaMenu-" + id
				+ "'data-role='button' data-icon='gear' data-iconpos='notext' data-rel='dialog' data-transition='pop'>Menú</a></li>";

		// se agregan a la lista de la página de listas
		$('#contenidoPaginaListas ol').append(html);

		// se asigna el evento al item de lista correspondiente
		agregarEventoLista(id, nombre);

	} // fin for

	// se muestra la página de listas
	$.mobile.changePage("#paginaListas");
	$("#contenidoPaginaListas ol").listview("refresh");
}

function buscarListasHandler2(transaction, results) {

	// se eliminan las listas anteriores de la página
	$('#contenidoPaginaSeleccionLista div select').empty();
	$('#contenidoPaginaSeleccionLista div select').append("<option value='-' selected>-</option>");

	// se recorren las listas obtenidas
	for ( var i = 0; i < results.rows.length; i++) {

		var row = results.rows.item(i);
		id = row['id'];
		nombre = row['nombre'];

		html = "<option value='" + id + "' id='opcionSeleccionLista-" + id + "'>" + nombre + "</option>";

		// se agregan a la lista de la página de listas
		$('#contenidoPaginaSeleccionLista div select').append(html);

	} // fin for

	// se muestra la página de listas
	$.mobile.changePage("#paginaSeleccionLista");
}

// /////////////////////////////////////////////////////////////////////////
// BUSCAR PRODUCTOS LISTA: ejecuta una busqueda de los productos que pertenecen
// a la lista indicada
// /////////////////////////////////////////////////////////////////////////
function buscarProductosLista(idLista, opcion, nombre) {

	try {
		bd.transaction(function(transaction) {
			if (opcion == PRODUCTOS_MOSTRAR) { // modo mostrado de productos de
				// una lista
				transaction.executeSql('SELECT * FROM productos WHERE lista="' + idLista + '";', [], buscarProductosListaHandler1, errorHandler);
			} else if (opcion == PRODUCTOS_COMPROBAR) { // modo comprobar
				transaction.executeSql('SELECT * FROM productos WHERE lista="' + idLista + '";', [], buscarProductosListaHandler2, errorHandler);
			} else if (opcion == PRODUCTOS_COMPRA) { // modo compra
				nombreListaModoCompraActual = nombre + " (modo compra)";
				transaction.executeSql('SELECT * FROM productos WHERE lista="' + idLista + '";', [], buscarProductosListaHandler3, errorHandler);
			}

		});
	} catch (e) {
		console.log("Error al buscar los productos de una lista en la base de datos: " + e.message);
	}
}

// /////////////////////////////////////////////////////////////////////////
// BUSCAR PRODUCTOS LISTA HANDLER: manejador de los datos que devuelve la
// función buscarProductosLista
// /////////////////////////////////////////////////////////////////////////
function buscarProductosListaHandler1(transaction, results) {

	// se eliminan las listas anteriores de la página
	$('#contenidoPaginaLista ul').empty();

	// se recorren las listas obtenidas
	for ( var i = 0; i < results.rows.length; i++) {

		var row = results.rows.item(i);
		id = row['id'];
		marca = row['marca'];
		descripcion = row['descripcion'];
		formato = row['formato'];

		html = "<li><a href='#' id='enlaceProductoLista-" + id + "'>";
		html += "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
		html += "<h3>" + marca + "</h3>";
		html += "<p>(" + formato + ") " + descripcion + "</p></a>";
		html += "<a href='#' id='enlaceProductoListaMenu-" + id + "' data-role='button' data-icon='gear' data-iconpos='notext' data-rel='dialog' data-transition='pop'>Menú</a>";
		html += "</li>";

		// se agregan a la lista de la página de listas
		$('#contenidoPaginaLista ul').append(html);

		// se asigna el evento al item de lista correspondiente
		agregarEventoProductoLista(id, marca, descripcion, formato);

	} // fin for

	// se muestra la página de favoritos
	// $.mobile.changePage("#paginaLista");
	$("#contenidoPaginaLista ul").listview("refresh");
}

function buscarProductosListaHandler2(transaction, results) {
	var ids = [];

	// se recorren los productos obtenidos
	for ( var i = 0; i < results.rows.length; i++) {

		var row = results.rows.item(i);
		id = row['id'];
		ids.push(id);
	} // fin for

	comprobarLista(ids);

}

function buscarProductosListaHandler3(transaction, results) {

	// se eliminan las listas anteriores de la página
	$('#contenidoPaginaListaModoCompra ul').empty();

	// se recorren los productos obtenidos
	for ( var i = 0; i < results.rows.length; i++) {

		var row = results.rows.item(i);
		id = row['id'];
		marca = row['marca'];
		descripcion = row['descripcion'];
		formato = row['formato'];

		html = "<li id='enlaceProductoModoCompra-" + id + "'>";
		html += "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
		html += "<h3>" + marca + "</h3>";
		html += "<p>(" + formato + ") " + descripcion + "</p>";
		html += "</li>";

		// se agregan a la lista de la página de listas
		$('#contenidoPaginaListaModoCompra ul').append(html);

		// se asigna el evento al item de lista correspondiente
		agregarEventoProductoListaModoCompra(id);

	} // fin for

	// se muestra la página de favoritos
	$("#paginaListaModoCompra div h1").empty().append(nombreListaModoCompraActual);
	$.mobile.changePage("#paginaListaModoCompra");
	$("#contenidoPaginaListaModoCompra ul").listview("refresh");
}

// /////////////////////////////////////////////////////////////////////////
// BUSCAR FAVORITOS: ejecuta una busqueda de los productos favoritos en la base
// de datos.
// /////////////////////////////////////////////////////////////////////////
function buscarFavoritos() {
	try {
		bd.transaction(function(transaction) {
			transaction.executeSql('SELECT * FROM favoritos', [], buscarFavoritosHandler, errorHandler);
		});
	} catch (e) {
		console.log("Error al buscar los favoritos en la base de datos: " + e.message);
	}
}

// /////////////////////////////////////////////////////////////////////////
// BUSCAR FAVORITOS HANDLER: manejador de los datos que devuelve la
// función buscarFavoritos
// /////////////////////////////////////////////////////////////////////////
function buscarFavoritosHandler(transaction, results) {

	// se eliminan las listas anteriores de la página
	$('#contenidoPaginaFavoritos ol').empty();

	// se recorren las listas obtenidas
	for ( var i = 0; i < results.rows.length; i++) {

		var row = results.rows.item(i);
		id = row['id'];
		marca = row['marca'];
		descripcion = row['descripcion'];
		formato = row['formato'];

		html = "<li><a href='#' id='enlaceListaFavoritos-" + id + "'>";
		html += "<img src='" + URLIMAGENES + id + ".gif' title='" + descripcion + "'/>";
		html += "<h3>" + marca + "</h3>";
		html += "<p>(" + formato + ") " + descripcion + "</p></a>";
		html += "<a href='#' id='enlaceListaFavoritosMenu-" + id + "' data-role='button' data-icon='gear' data-iconpos='notext' data-rel='dialog' data-transition='pop'>Menú</a>";
		html += "</li>";

		// se agregan a la lista de la página de listas
		$('#contenidoPaginaFavoritos ol').append(html);

		// se asigna el evento al item de lista correspondiente
		agregarEventoFavoritos(id, marca, descripcion, formato);

	} // fin for

	// se muestra la página de favoritos
	$.mobile.changePage("#paginaFavoritos");
	$("#contenidoPaginaFavoritos ol").listview("refresh");
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR LISTA: agrega una lista a la tabla lista de la base de datos
// /////////////////////////////////////////////////////////////////////////
function agregarLista(nombre) {

	sql = "insert into listas (id,nombre) VALUES (NULL,'" + nombre + "');";
	try {

		bd.transaction(

		function(transaction) {
			transaction.executeSql(sql, [], function() {
				console.log("Lista insertada en la base de datos.");
			}, errorHandler);
		});
	} catch (e) {
		console.log("Error al agregar lista a la base de datos: " + e.message);
		return;

	}
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR FAVORITO: agrega un producto a la tabla de favoritos
// /////////////////////////////////////////////////////////////////////////
function agregarFavorito(id, marca, descripcion, formato) {

	try {
		bd.transaction(function(transaction) {
			// el comando replace primero chequea si el producto existe. Si
			// existe, actualiza sus valores. Si no existe, agrega el producto
			transaction.executeSql('replace into favoritos (id,marca,descripcion,formato) VALUES ("' + id + '","' + marca + '","' + descripcion + '","' + formato + '");', [], function() {
				mostrarNotificacion(2);
				console.log("Insertado favorito en la base de datos");
			}, errorHandler);
		});
	} catch (e) {
		console.log("Error al agregar favorito a la base de datos: " + e.message);
		return;
	}
}

// /////////////////////////////////////////////////////////////////////////
// AGREGAR PRODUCTO: agrega un producto a la tabla de productos
// /////////////////////////////////////////////////////////////////////////
function agregarProducto(id, lista, marca, descripcion, formato) {

	try {
		bd.transaction(function(transaction) {
			// el comando replace primero chequea si el producto existe. Si
			// existe, actualiza sus valores. Si no existe, agrega el producto
			transaction.executeSql('replace into productos (id,lista,marca,descripcion,formato) VALUES ("' + id + '","' + lista + '","' + marca + '","' + descripcion + '","' + formato + '");', [],
					function() {
						mostrarNotificacion(1);
						console.log("Insertado producto en la base de datos");
					}, errorHandler);
		});
	} catch (e) {
		console.log("Error al agregar producto a la base de datos: " + e.message);
		return;
	}
}

// /////////////////////////////////////////////////////////////////////////
// ELIMINAR PRODUCTO: elimina un producto de la tabla de productos
// /////////////////////////////////////////////////////////////////////////
function eliminarProducto(id) {

	sql = "DELETE FROM productos WHERE id =" + id + ";";
	try {
		bd.transaction(function(transaction) {
			transaction.executeSql(sql, [], function() {
				mostrarNotificacion(5);
				console.log("Producto eliminado de la base de datos");
			}, errorHandler);
		});
	} catch (e) {
		console.log("Error al eliminar favorito de la base de datos " + e.message);
		return;

	}
}

// /////////////////////////////////////////////////////////////////////////
// ELIMINAR LISTA: elimina una lista de la tabla lista de la base de datos
// /////////////////////////////////////////////////////////////////////////
function eliminarLista(id) {

	// sql para borrado de la lista
	sql = "DELETE FROM listas WHERE id =" + id + ";";

	// sql para borrado de todos los productos que pertenecen a la lista
	sql2 = "DELETE FROM productos WHERE lista =" + id + ";";

	try {
		bd.transaction(function(transaction) {
			transaction.executeSql(sql, [], function() {
				mostrarNotificacion(3);
				console.log("Lista eliminada de la base de datos");
			}, errorHandler);
			transaction.executeSql(sql2, [], function() {
				console.log("Eliminados productos de la lista");
			}, errorHandler);
		});

		// se muestran las listas de nuevo
		buscarListas(LISTAS_MOSTRADO);
	} catch (e) {
		console.log("Error al eliminar lista de la base de datos: " + e.message);
		return;

	}
}

// /////////////////////////////////////////////////////////////////////////
// ELIMINAR FAVORITO: elimina un favorito de la tabla favoritos de la base de
// datos
// /////////////////////////////////////////////////////////////////////////
function eliminarFavorito(id) {

	sql = "DELETE FROM favoritos WHERE id =" + id + ";";
	try {
		bd.transaction(function(transaction) {
			transaction.executeSql(sql, [], function() {
				mostrarNotificacion(4);
				console.log("Favorito eliminado de la base de datos");
			}, errorHandler);
		});

		// se muestran los favoritos de nuevo
		buscarFavoritos();
	} catch (e) {
		console.log("Error al eliminar favorito de la base de datos: " + e.message);
		return;
	}
}

// ****************************************************************************
// ***************** FUNCIONES DE ACELEROMETRO ********************************
// ****************************************************************************

// /////////////////////////////////////////////////////////////////////////
// INICIAR ACELEROMETRO: inicia la observacion del acelerometro. La aceleracion
// se verifica cada 200 ms
function iniciarAcelerometro() {

	// La aceleración se comprueba cada 200ms
	var options = {
		frequency : 200
	};

	acelerometroID = navigator.accelerometer.watchAcceleration(exitoAcelerometro, errorAcelerometro, options);
}

// /////////////////////////////////////////////////////////////////////////
// EXITO ACELEROMETRO: funcion que se ejecuta cada vez que el acelerometro
// comprueba el valor de la aceleracion
function exitoAcelerometro(Accel) {
	if (true === Accel.is_updating) {
		return;
	}

	if (vistaProducto) {
		// se obtiene la diferencia de aceleración
		var diffX = Math.abs(Accel.x) - prevX;

		// si la diferencia es superior a 10g se agrega el producto a una lista
		if (diffX >= 10) {
			vistaProducto = false;
			buscarListas(LISTAS_SELECCION);
		}
		prevX = Math.abs(Accel.x);
	}
}

// /////////////////////////////////////////////////////////////////////////
// ERROR ACELEROMETRO: funcion que se ejecuta cuando se produce un error en
// la lectura de la aceleracion
function errorAcelerometro() {
	console.log('Ha ocurrido un error en el aceler\u00f3metro del dispositivo.');
}

// ****************************************************************************
// ************************ OTRAS FUNCIONES ***********************************
// ****************************************************************************
// /////////////////////////////////////////////////////////////////////////
// ESCANEAR CODIGO BARRAS: hace una petición a la aplicación BarcodeScanner para
// obtener el número relativo al código de barras
// /////////////////////////////////////////////////////////////////////////
function escanearCodigoBarras() {

	window.plugins.barcodeScanner.scan(function(codigo) {

		// Se comprueba que el código leido es un código de
		// barras
		if (codigo.format == "EAN_13") {

			// se comprueba que se tiene conexión
			if (verificarConexion()) {
				$.ajax({
					type : "GET",
					url : URLSERVER + "productos/code/" + codigo.text,
					dataType : "xml",
					beforeSend : function() {
						$.mobile.showPageLoadingMsg();
					},
					complete : function(resp, status) {
						$.mobile.hidePageLoadingMsg();
					},
					success : function(xml) {
						$.mobile.hidePageLoadingMsg();

						$('#contenidoBusquedaProductos ul').empty();

						/*
						 * var acierto;
						 * 
						 * $(xml) .find('productos') .each( function() { acierto = $(
						 * this) .attr( 'aciertos'); });
						 * 
						 * if (acierto == "si") {
						 */

						$(xml).find('productos').each(function() {
							if ($(this).find('categoria').text() != "0") {
								var descripcion = $(this).find('descripcion').text();
								var marca = $(this).find('marca').text();
								var formato = $(this).find('formato').text();
								var id = $(this).find('id').text();

								// se
								// obtiene
								// el
								// precio
								// del
								// producto
								// y se
								// muestra
								eventoProductoListaCategoriaPulsado(id, marca, descripcion, formato);
								vistaProducto = true;

							} else {
								mostrarMensaje("Error", "El c\u00f3digo de barras no se encuentra en la base de datos.")
							}

						}); // close each(

					}
				}); // close $.ajax(
			} else {
				mostrarMensaje("Error", "El dispositivo no est\u00e1 conectado a Internet. Verifique su conexi\u00f3n.")
			} // fin verificar conexion
		} else {
			mostrarMensaje("Error", "El c\u00f3digo leido no es un c\u00f3digo de barras tipo EAN 13.")
		}
	}, function(error) {
		console.log("El escaneo del codigo de barras ha fallado: " + error);
	});
}

// /////////////////////////////////////////////////////////////////////////
// VERIFICAR CONEXION: verifica que el dispositivo está conectado a Internet
// /////////////////////////////////////////////////////////////////////////
function verificarConexion() {

	if (navigator.network.connection.type == Connection.NONE) {
		return false;
	} else {
		return true;
	}
}
