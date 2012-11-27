var URLSERVER = "http://192.168.0.101:8080/comprarador/rest/";
var URLIMAGENES = "http://192.168.0.101:8080/comprarador/imagenes_productos/";

/*******************************************************************************
 * PETICIONES SERVIDOR
 */
console.log("COMIENZA EL TEST!");
// /////// OBTENCIÓN DE CATEGORIAS /////////////////////////////////////////
module('OBTENCIÓN CATEGORÍAS');
test("obtención de menúes en formato XML", 6, function() {
	// start();
	// expect(6);

	/*
	 * Se verifica el número de categorías obtenidas y el nombre de una de ellas
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "categorias",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var numCategorias = $(xml).find('categorias').length;

			var cat = 0;
			var nombreCategoria;
			$(xml).find('categorias').each(function() {
				cat += 1;
				if (cat == 10) {
					nombreCategoria = $(this).find('nombre').text();
				}
			});

			var aciertosMercadona = $(xml).find('aciertosMercadona').text();
			ok(numCategorias == 21, "el número de categorías debe ser 21");
			ok(nombreCategoria == "Congelados y helados", "la categoría #10 debe tener el nombre 'Congelados y helados'");
			start();
		}
	});

	/*
	 * Se verifica el número de subcategorías obtenidas para la categoría 1 y el
	 * nombre de una de ellas
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "subcategorias/1",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var numSubcategorias = $(xml).find('subcategorias').length;

			var cat = 0;
			var nombreSubcategoria;
			$(xml).find('subcategorias').each(function() {
				cat += 1;
				if (cat == 4) {
					nombreSubcategoria = $(this).find('nombre').text();
				}
			});

			var aciertosMercadona = $(xml).find('aciertosMercadona').text();
			ok(numSubcategorias == 6, "el número de subcategorías debe ser 8 para la categoría 1");
			ok(nombreSubcategoria == "Alimentos peces y tortugas", "la subcategoría #4 debe tener el nombre 'Alimentos peces y tortugas'");
			start();
		}
	});

	/*
	 * Se verifica el número de subsubcategorías obtenidas para la categoría 1 y
	 * subcategoria 2 y el nombre de una de ellas
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "subsubcategorias/1/2",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var numSubsubcategorias = $(xml).find('subsubcategorias').length;

			var cat = 0;
			var nombreSubsubcategoria;
			$(xml).find('subsubcategorias').each(function() {
				cat += 1;
				if (cat == 2) {
					nombreSubsubcategoria = $(this).find('nombre').text();
				}
			});

			var aciertosMercadona = $(xml).find('aciertosMercadona').text();
			ok(numSubsubcategorias == 2, "el número de subsubcategorías debe ser 2 para la categoría 1 y subcategoría 2");
			ok(nombreSubsubcategoria == "Seco", "la subsubcategoría #4 debe tener el nombre 'Seco'");
			start();
		}
	});

});

// /////// OBTENCIÓN DE PRODUCTOS /////////////////////////////////////////
module('OBTENCIÓN PRODUCTOS');
test("obtención de productos", 26, function() {

	/*
	 * Se verifica el número de productos obtenidos y el nombre de uno de ellos
	 * para la categoria 6, subcategoría 2 y subsubcategoría 1
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/6/2/1",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var numProductos = $(xml).find('productos').length;

			var p = 0;
			var descripcionProducto;
			var marcaProducto;
			var formatoProducto;
			var idProducto;

			$(xml).find('productos').each(function() {
				p += 1;
				if (p == 10) {
					descripcionProducto = $(this).find('descripcion').text();
					marcaProducto = $(this).find('marca').text();
					formatoProducto = $(this).find('formato').text();
					idProducto = $(this).find('id').text();
				}
			});

			ok(numProductos == 30, "el número de productos debe ser 30 para la categoría 6, subcategoría 2, subsubcategoría 1");
			ok(descripcionProducto == "El Cantero De Letur Batido De Cacao Procedente De GanaderÍa EcolÓgica Botella 1 L",
					"el producto #10 debe tener descripción 'El Cantero De Letur Batido De Cacao Procedente De GanaderÍa EcolÓgica Botella 1 L'");
			ok(marcaProducto == "El Cantero De Letur", "el producto #10 debe tener marca 'El Cantero De Letur'");
			ok(formatoProducto == "1x1000ml", "el producto #10 debe tener formato '1x1000ml'");
			ok(idProducto == 123, "el producto #10 debe tener id '123'");
			start();
		}
	});

	/*
	 * Se verifican los datos obtenidos para el producto con id=881
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/881",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {

			var descripcionProducto;
			var marcaProducto;
			var formatoProducto;
			var cat;
			var subcat;
			var subsubcat;

			$(xml).find('productos').each(function() {
				descripcionProducto = $(this).find('descripcion').text();
				marcaProducto = $(this).find('marca').text();
				formatoProducto = $(this).find('formato').text();
				cat = $(this).find('categoria').text();
				subcat = $(this).find('subcategoria').text();
				subsubcat = $(this).find('subsubcategoria').text();
			});

			ok(descripcionProducto == "Cerveza Abadía Carrefour - 33 Cl.", "el producto con id=881 debe tener descripción 'Cerveza Abadía Carrefour - 33 Cl.'");
			ok(marcaProducto == "Carrefour", "el producto con id=881 debe tener marca 'Carrefour'");
			ok(formatoProducto == "1x330ml", "el producto con id=881 debe tener formato '1x330ml'");
			ok(cat == 6, "el producto con id=881 debe pertenecer a la categoría 6");
			ok(subcat == 5, "el producto con id=881 debe pertenecer a la subcategoría 5");
			ok(subsubcat == 7, "el producto con id=881 debe pertenecer a la subsubcategoría 7");
			start();
		}
	});

	/*
	 * Se verifican los datos obtenidos para la búsqueda de producto "pila"
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/query/agua sabor",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {

			var numProductos = $(xml).find('productos').length;
			var descripcionProducto;
			var marcaProducto;
			var formatoProducto;
			var idProducto;
			var cat;
			var subcat;
			var subsubcat;

			var p = 0;
			$(xml).find('productos').each(function() {
				p += 1;
				if (p == 2) {
					descripcionProducto = $(this).find('descripcion').text();
					marcaProducto = $(this).find('marca').text();
					formatoProducto = $(this).find('formato').text();
					idProducto = $(this).find('id').text();
					cat = $(this).find('categoria').text();
					subcat = $(this).find('subcategoria').text();
					subsubcat = $(this).find('subsubcategoria').text();
				}
			});

			ok(numProductos == 3, "el número de productos obtenidos para la búsqueda 'Agua Sabor' debe ser 3");
			ok(descripcionProducto == "Agua Sabor Mandarina Font Vella - Botella De 1,25 Cl.", "el producto #2 para la búsqueda 'Agua Sabor' debe tener descripción 'Agua Sabor Mandarina Font Vella - Botella De 1,25 Cl.'");
			ok(marcaProducto == "Font Vella", "el producto #2 para la búsqueda 'Agua Sabor' debe tener marca 'Font Vella'");
			ok(formatoProducto == "1x1250ml", "el producto #2 para la búsqueda 'Agua Sabor' debe tener formato '1x1250ml'");
			ok(idProducto == 28, "el producto #2 para la búsqueda 'Agua Sabor' debe id=28");
			ok(cat == 6, "el producto #2 para la búsqueda 'Agua Sabor' debe pertenecer a la categoría 6");
			ok(subcat == 1, "el producto #2 para la búsqueda 'Agua Sabor' debe pertenecer a la subcategoría 1");
			ok(subsubcat == 2, "el producto #2 para la búsqueda 'Agua Sabor' debe pertenecer a la subsubcategoría 2");
			start();
		}
	});

	/*
	 * Se verifican los datos obtenidos para la búsqueda de producto con código
	 * de barras: 4902505154300
	 */
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/code/4902505154300",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {

			var descripcionProducto;
			var marcaProducto;
			var formatoProducto;
			var idProducto;
			var cat;
			var subcat;
			var subsubcat;

			$(xml).find('productos').each(function() {
				descripcionProducto = $(this).find('descripcion').text();
				marcaProducto = $(this).find('marca').text();
				formatoProducto = $(this).find('formato').text();
				idProducto = $(this).find('id').text();
				cat = $(this).find('categoria').text();
				subcat = $(this).find('subcategoria').text();
				subsubcat = $(this).find('subsubcategoria').text();
			});

			ok(descripcionProducto == "Carlsberg Cerveza Rubia Danesa Lata 33 Cl", "el producto con código de barras 4902505154300 debe tener descripción 'Carlsberg Cerveza Rubia Danesa Lata 33 Cl'");
			ok(marcaProducto == "Carlsberg", "el producto con código de barras 4902505154300 debe tener marca 'Carlsberg'");
			ok(formatoProducto == "1x330ml", "el producto con código de barras 4902505154300 debe tener formato '1x330ml'");
			ok(idProducto == 540, "el producto con código de barras 4902505154300 debe id=114");
			ok(cat == 6, "el producto con código de barras 4902505154300 debe pertenecer a la categoría 6");
			ok(subcat == 5, "el producto con código de barras 4902505154300 debe pertenecer a la subcategoría 5");
			ok(subsubcat == 3, "el producto con código de barras 4902505154300 debe pertenecer a la subsubcategoría 3");
			start();
		}
	});

});

// /////// COMPROBACIÓN DE PRECIOS /////////////////////////////////////////
module('COMPROBACIÓN PRECIOS');
test("comprobar precio de 2 productos para cada supermercado", 5, function() {
	// start();
	// expect(5);

	// MERCADONA
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/check/521,532",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var mercadona = $(xml).find('totalMercadona').text();
			var aciertosMercadona = $(xml).find('aciertosMercadona').text();
			ok(mercadona == 7.29 && aciertosMercadona == 2, "debe obtenerse un precio de 7.29€ y 2 aciertos para Mercadona");
			start();
		}
	});

	// CARREFOUR
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/check/522,523",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var carrefour = $(xml).find('totalCarrefour').text();
			var aciertosCarrefour = $(xml).find('aciertosCarrefour').text();
			ok(carrefour == 0.74 && aciertosCarrefour == 2, "debe obtenerse un precio de 0.74€ y 2 aciertos para Carrefour");
			start();
		}
	});

	// HIPERCOR
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/check/565,566",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var hipercor = $(xml).find('totalHipercor').text();
			var aciertosHipercor = $(xml).find('aciertosHipercor').text();
			ok(hipercor == 2.2 && aciertosHipercor == 2, "debe obtenerse un precio de 2.2€ y 2 aciertos para Hipercor");
			start();
		}
	});

	// CORTE INGLÉS
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/check/565,566",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var corteIngles = $(xml).find('totalCorteIngles').text();
			var aciertosCorteIngles = $(xml).find('aciertosCorteIngles').text();
			ok(corteIngles == 2.4 && aciertosCorteIngles == 2, "debe obtenerse un precio de 2.4€ y 2 aciertos para Corte Inglés");
			start();
		}
	});

	// TODOS SUPERMERCADOS
	$.ajax({
		type : "GET",
		url : URLSERVER + "productos/check/521,532,522,523,565,566",
		dataType : "xml",
		beforeSend : function(xhr) {
			stop();
		},
		error : function(request, status, error) {
			start();
		},
		success : function(xml) {
			var mercadona = $(xml).find('totalMercadona').text();
			var carrefour = $(xml).find('totalCarrefour').text();
			var hipercor = $(xml).find('totalHipercor').text();
			var corteIngles = $(xml).find('totalCorteIngles').text();

			ok(mercadona == 7.29 && carrefour == 0.74 && hipercor == 2.2 && corteIngles == 2.4,
					"debe obtenerse un precio de 7.29€, 0.74€, 2.2€ y 2.4€ para Mercadona, Carrefour, Hipercor y Corte Inglés respectivamente ");
			start();
		}
	});
});
