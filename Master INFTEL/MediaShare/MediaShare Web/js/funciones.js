var tweets;
var nombresUsuarios = new Array();
var musica = new Array();
var videos = new Array();
var imagenes = new Array();
var usuarios = new Array();

$(document).ready(function(){

	mostrarLoading();
	
	//se obtiene la información de Twitter
	obtenerInformacionTwitter();
    
	$("#right").hide();
	
	// se asignan los eventos
	$(".flipR").click(function(){
        $("#right").animate({width: 'toggle'});
        
    });


	$("#menu_musica").click(function(){
		agregarMusicaLeft();
	});	
	
	$("#menu_videos").click(function(){
		agregarVideosLeft();
	});
	
	$("#menu_imagenes").click(function(){
		agregarImagenesLeft();
	});
	
	$("#menu_localizaciones").click(function(){
		$("#left").empty().append("<p></p>");
		$("#right").empty();
		$("#right").attr("style","display:none");
		mapaLocalizacion();
	});
	
	$("#menu_estadisticas").click(function(){
		$("#left").empty().append("<p></p>");
		$("#right").empty();
		$("#right").attr("style","display:none");
		estadisticas();
	});
	
});

/////////////////////////////////////////////////////////////////////////
/////////////// Muestra en el panel de la izquierda los enlaces de música
/////////////////////////////////////////////////////////////////////////
function agregarMusicaLeft(){
	$("#main").empty();
	$("#right").empty();
	$("#right").attr("style","display:none");
	$("#left").empty().append("<div id='accordion'>");
	for (var i=0;i<musica.length;i++){
		var item = "<h3><a href='#'>"+musica[i].nombre+"</a></h3><div><p>Recomendado por: "+musica[i].autor+"</p><p>Fecha: "+musica[i].fecha+"</p><img src='imagenes/play.png' id='playMusica-"+i+"' style='display: block; margin-left: auto; margin-right: auto;' /></div>";
		$("#accordion").append(item);
		$("#playMusica-"+i).bind('click', function() {
		  buscarEnYoutube(musica[$(this).attr('id').split("-")[1]].nombre);
		 
		});
		
	}
	$("#left").append("</div>");

	$("#accordion").accordion();
}

/////////////////////////////////////////////////////////////////////////
/////////////// Muestra en el panel de la izquierda los enlaces de vídeos
/////////////////////////////////////////////////////////////////////////
function agregarVideosLeft(){
	$("#main").empty();
	$("#right").empty();
	$("#right").attr("style","display:none");
	$("#left").empty().append("<div id='accordion'>");
	for (var i=0;i<videos.length;i++){
		var item = "<h3><a href='#'>"+videos[i].nombre+"</a></h3><div><p>Recomendado por: "+videos[i].autor+"</p><p>Fecha: "+videos[i].fecha+"</p><img src='imagenes/play.png' id='playvideo-"+i+"' style='display: block; margin-left: auto; margin-right: auto;' /></div>";
		$("#accordion").append(item);
		$("#playvideo-"+i).bind('click', function() {
		  buscarEnYoutube(videos[$(this).attr('id').split("-")[1]].nombre);
		 
		});
		
	}
	$("#left").append("</div>");

	$("#accordion").accordion();
}

/////////////////////////////////////////////////////////////////////////
/////////////// Muestra en el panel de la izquierda los enlaces de imágenes
/////////////////////////////////////////////////////////////////////////
function agregarImagenesLeft(){
	$("#main").empty();
	$("#right").empty();
	$("#right").attr("style","display:none");
	$("#left").empty().append("<div id='accordion'>");
	for (var i=0;i<imagenes.length;i++){
		var item = "<h3><a href='#'>"+imagenes[i].nombre+"</a></h3><div><p>Recomendado por: "+imagenes[i].autor+"</p><p>Fecha: "+imagenes[i].fecha+"</p><img src='imagenes/play.png' id='playImagen-"+i+"' style='display: block; margin-left: auto; margin-right: auto;'/></div>";
		$("#accordion").append(item);
		$("#playImagen-"+i).bind('click', function() {
		  flickr(imagenes[$(this).attr('id').split("-")[1]].nombre);
		 
		});
		
	}
	$("#left").append("</div>");

	$("#accordion").accordion();
}

/////////////////////////////////////////////////////////////////////////
/////////////// Elimina los usuarios repetidos del array que almacena los
/////////////// nombres de usuarios que han compartido archivos
/////////////////////////////////////////////////////////////////////////
function eliminarUsuariosRepetidos(nombres){
	
	        nombres.forEach(function(value) {
	            if (nombresUsuarios.indexOf(value) == -1) {
	                nombresUsuarios.push(value);
	            }
	        });
	
}


/////////////////////////////////////////////////////////////////////////
/////////////// Realiza la petición a Twitter para obtener la información
/////////////// sobre los tweets publicados
/////////////////////////////////////////////////////////////////////////
function obtenerInformacionTwitter(){
	
	
	$.ajax({
        type: "GET",
        url: "http://search.twitter.com/search.json?q=%23mediashare&rpp=100",
        dataType: "jsonp",
        complete: function(resp, status) {
            //alert(resp.responseText);
        }, 
        success: function(json) {
        	
			// se almacenan todos los tweets con el tag #mediashare en la variable tweets
        	tweets=json["results"];
        	
			var nombres = new Array();
        	for (var i=0;i<tweets.length; i++){
				
				if(i==tweets.length-1){
					esconderLoading();
				}
				
				//se clasifica la informaci�n obtenida seg�n sea m�sica, v�deo o imagen y se almacena en los arrays correspondientes
        		switch(extraerTipoContenido(tweets[i]["text"])){

	        		case "musica":
	        			musica.push({"nombre":extraerNombreContenido(tweets[i]["text"]),"autor":tweets[i]["from_user"],"fecha":tweets[i]["created_at"]});       		
	        			break;
	
	        		case "video":
	        			videos.push({"nombre":extraerNombreContenido(tweets[i]["text"]),"autor":tweets[i]["from_user"],"fecha":tweets[i]["created_at"]});       		
	        			break;
	
	        		case "imagen":
	        			imagenes.push({"nombre":extraerNombreContenido(tweets[i]["text"]),"autor":tweets[i]["from_user"],"fecha":tweets[i]["created_at"]});       		
	        			
						break;
        		}// fin switch
        		
				//se almacenan todos los usuarios que han compartido archivos multimedia
        		nombres.push(tweets[i]["from_user"]);

        	}// fin for

			//se eliminan los usuarios repetidos y se almacenan en el array nombresUsuarios
			eliminarUsuariosRepetidos(nombres);
			
			//se obtiene la información de cada uno de los usuarios y se muestran los usuarios en la sección people
			for(var j=0;j<nombresUsuarios.length;j++){
				extraerUsuario(nombresUsuarios[j]);
			}
        	      	        	
        }// fin success
	}); //close $.ajax(   
		
}

/////////////////////////////////////////////////////////////////////////
/////////////// Realiza la petición a Twitter para obtener la información
/////////////// de los usuarios y se muestra en la sección people
/////////////////////////////////////////////////////////////////////////
function extraerUsuario(nombreUsuario){
	
		$.ajax({
	        type: "GET",	
	        url: "http://api.twitter.com/1/users/show.json?screen_name="+nombreUsuario,
	        dataType: "jsonp",
	        complete: function(resp, status) {
	        	//alert(resp.responseText);
	        }, 
	        success: function(json) {
	        	// se agrega el usuario obtenido al vector usuarios
	        	usuarios.push({"id":json["id"], "apodo":json["screen_name"], "nombre":json["name"], "descripcion":json["description"], "url":json["url"], "localizacion":json["location"], "imagen":json["profile_image_url"]});  
					
				// se agrega la informaci�n del usuario al footer
				i = usuarios.length-1;
				var html = "<div class='usuario' id='usuarioFooter-"+i+"'><a href='http://twitter.com/#!/"+usuarios[i].apodo+"' target='_blank'><img src='"+usuarios[i].imagen+"' class='imagenUsuarioFooter' /><p>@"+usuarios[i].apodo+"</p></a></div>";		
				var masInfo = "<div><a href='http://twitter.com/#!/"+usuarios[i].apodo+"'><img src='"+usuarios[i].imagen+"' /> <span class='masInfoUsuarioHeader'> @"+usuarios[i].apodo+"</span></a></div>";
				masInfo += "<div class='masInfoUsuarioFooter'><b>Nombre real:</b> "+usuarios[i].nombre+"<br>";
				masInfo += "<b>Descripci&oacute;n:</b> "+usuarios[i].descripcion+"<br>";
				masInfo += "<b>Web: </b>"+usuarios[i].url+"<br>";
				masInfo += "<b>Localizaci&oacute;n:</b> "+usuarios[i].localizacion+"</div>";

				$('#people').append(html);
				$('#usuarioFooter-'+i).tipsy({gravity: $.fn.tipsy.autoNS,fallback: masInfo,html:'true',fade: true});	
	      	
	        }// fin success
		}); //close $.ajax(   	
}



/////////////////////////////////////////////////////////////////////////
/////////////// Obtiene el tipo de contenido compartido de un tweet a 
/////////////// partir del hashtag utilizado
/////////////////////////////////////////////////////////////////////////
function extraerTipoContenido(texto){
	var aux = texto.split("#")[2].split(" ");
	var tipo = aux[0];

	return tipo;
}


/////////////////////////////////////////////////////////////////////////
/////////////// Obtiene el texto del archivo multimedia compartido
/////////////////////////////////////////////////////////////////////////
function extraerNombreContenido(texto){
	var aux = texto.split("#")[2].split(" ");
	
	var nombre = "";
	for (var i=1;i<aux.length;i++){
		nombre += aux[i]+" ";
	}
	return nombre;
	
}

/////////////////////////////////////////////////////////////////////////
/////////////// Busca en Youtube el texto que se le pasa como parámetro
/////////////// y obtiene información acerca del primer vídeo encontrado.
/////////////// Posteriormente genera el reproductor en la sección main.
/////////////////////////////////////////////////////////////////////////
function buscarEnYoutube(texto){

	$.ajax({
        type: "GET",	
        url: "http://gdata.youtube.com/feeds/api/videos?q="+texto+"&alt=json&max-results=1",
        dataType: "json",
        complete: function(resp, status) {
        }, 
        success: function(json) {

        	var id = json["feed"]["entry"][0]["id"]["$t"];
 			id = id.replace('http://gdata.youtube.com/feeds/api/videos/','');
        	var titulo = json["feed"]["entry"][0]["title"]["$t"];
        	var descripcion = json["feed"]["entry"][0]["content"]["$t"];
        	var autor = json["feed"]["entry"][0]["author"][0]["name"]["$t"];
        	var publicado = json["feed"]["entry"][0]["published"]["$t"];
        	var actualizado = json["feed"]["entry"][0]["updated"]["$t"];
        	var vistas = json["feed"]["entry"][0]["yt$statistics"]["viewCount"];
        	
		  	// Se genera el reproductor y se agrega la información oportuna a la sección de la derecha (+info)
			var reproductor ='<iframe class="youtube-player" type="text/html" width="640" height="385" src="http://www.youtube.com/embed/'+id+'" frameborder="0"></iframe>';
			$("#main").empty().append(reproductor);
                        $("#main").append("<a href='#' id='masInfo'>+info</>");

                        $("#masInfo").click(function(){

                            var html="<h3>T&iacute;tulo: "+titulo+"</h3>";
                                html+="<p>Descripci&oacute;n: "+descripcion+"</p>";
                                html+="<p>By: "+autor+"</p>";
                                html+="<p>Fecha Publicaci&oacute;n: "+publicado+"</p>";
                                html+="<p>Fecha Actualizaci&oacute;n: "+actualizado+"</p>";
                                html+="<p>N&uacute;mero de Visitas: "+vistas+"</p>";
                                $("#right").empty();
                                $("#right").append(html);

                                if($("#masInfo").text()=="+info"){
                                    $("#masInfo").text("-info");
                                    $("#right").animate({width: 'toggle'});
                                }else{
                                    $("#masInfo").text("+info");
                                    $("#right").animate({width: 'toggle'});
                                }
                        });
        }// fin success
	}); //close $.ajax(   

}

/////////////////////////////////////////////////////////////////////////
/////////////// Representa un mapa de google maps en la sección main 
/////////////// con la localización de los diferentes usuarios.
/////////////////////////////////////////////////////////////////////////
function mapaLocalizacion(){
	
	var localizaciones = new Array();

	for(var i=0;i<usuarios.length;i++){
		var codigo = "<div><a href='http://twitter.com/#!/"+usuarios[i].apodo+"'><img src='"+usuarios[i].imagen+"' /> <span class='masInfoUsuarioHeader'> @"+usuarios[i].apodo+"</span></a></div>";
		codigo += "<div class='masInfoUsuario'><b>Nombre real:</b> "+usuarios[i].nombre+"<br>";
		codigo += "<b>Descripci&oacute;n:</b> "+usuarios[i].descripcion+"<br>";
		codigo += "<b>Web:</b> <a href='"+usuarios[i].url+"'>"+usuarios[i].url+"</a><br>";
		codigo += "<b>Localizaci&oacute;n:</b> "+usuarios[i].localizacion+"</div>";
		localizaciones.push({address: usuarios[i].localizacion, html: codigo});
	}
	$("#main").empty().append("<div id='mapa' style='height:400px'></div>");
	$("#mapa").gMap({markers: localizaciones,zoom: 4});
	
	
}


/////////////////////////////////////////////////////////////////////////
/////////////// Obtiene estadísticas acerca del uso de la web y de la
/////////////// participación de los usuarios y la representa en dos gráficas
/////////////////////////////////////////////////////////////////////////
function estadisticas(){

	$("#main").empty().append('<canvas id="grafico1" width="600" height="400" style="display: block;margin-left: auto;margin-right: auto; padding-top:10px;">[No canvas support]</canvas><canvas id="grafico2" width="600" height="400" style="display: block;margin-left: auto;margin-right: auto; padding-bottom:10px;">[No canvas support]</canvas>');

	var data = [usuarios.length,tweets.length,videos.length,musica.length,imagenes.length];


	/// Gráfica de uso de la web
	var bar = new RGraph.Bar('grafico1', data);
	bar.Set('chart.labels', ['Usuarios', 'Archivos totales', 'Videos', 'Musica', 'Imagenes']);
	bar.Set('chart.gutter.left', 45);
	bar.Set('chart.background.barcolor1', 'rgba(255,255,255,0.8)');
	bar.Set('chart.background.barcolor2', 'rgba(248,236,236,0.8)');
	bar.Set('chart.background.grid', false);
	bar.Set('chart.shadow', true);
	bar.Set('chart.shadow.color', '#aaa');
	bar.Set('chart.colors', ['rgba(206,42,42,0.8)', 'rgba(41,164,88,0.8)', 'rgba(81,117,188,0.8)', 'rgba(165,98,178,0.8)', 'rgba(137,231,192,0.8)']);
	bar.Set('chart.colors.sequential',true);
	bar.Set('chart.title',"Estadisticas de uso");
	bar.Set('chart.title.color','#2A4694');
	bar.Set('chart.text.color','#FF8000');
	bar.Set('chart.labels.above', true);

	RGraph.Effects.jQuery.Reveal(bar);
	RGraph.Effects.Bar.Grow(bar);

	var labelUsuarios = new Array();
	var datos = new Array();
	var tooltips = new Array();

	for(var i=0;i<usuarios.length;i++){

		labelUsuarios.push(usuarios[i].apodo);
		tooltips.push('videos','musica','imagenes');

		var usuarioMusica=0;
		var usuarioVideos=0;
		var usuarioImagenes=0;

		for(var j=0;j<musica.length;j++){
			if(musica[j].autor==usuarios[i].apodo){
				usuarioMusica ++;
			}
		}
		for(var j=0;j<videos.length;j++){
			if(videos[j].autor==usuarios[i].apodo){
				usuarioVideos ++;
			}
		}
		for(var j=0;j<imagenes.length;j++){
			if(imagenes[j].autor==usuarios[i].apodo){
				usuarioImagenes ++;
			}
		}

		datos.push([usuarioVideos,usuarioMusica,usuarioImagenes]);
	}


	//gráfica de los usuarios    
	var bar2 = new RGraph.Bar('grafico2', datos);
	var grad = bar2.context.createLinearGradient(275,0,900, 0);
	grad.addColorStop(0, 'white');
	grad.addColorStop(1, 'blue'); 
	bar2.Set('chart.title', 'Estadisticas de usuarios');
	bar2.Set('chart.title.color','#2A4694');
	bar2.Set('chart.colors', ['rgba(41,164,88,0.8)', 'rgba(81,117,188,0.8)', 'rgba(165,98,178,0.8)']);
	bar2.Set('chart.gutter.left', 40);
	bar2.Set('chart.gutter.right', 5);
	bar2.Set('chart.gutter.top', 40);
	bar2.Set('chart.gutter.bottom', 50);
	bar2.Set('chart.shadow', true);
	bar2.Set('chart.shadow.color', '#aaa');
	bar2.Set('chart.background.barcolor1', 'rgba(255,255,255,0.8)');
	bar2.Set('chart.background.barcolor2', 'rgba(248,236,236,0.8)');
	bar2.Set('chart.background.grid', false);
	bar2.Set('chart.grouping', 'stacked');
	bar2.Set('chart.labels', labelUsuarios);
	bar2.Set('chart.labels.above', true);
	bar2.Set('chart.text.color','#FF8000');
	bar2.Set('chart.key', ['Videos', 'Musica', 'Imagenes']);
	bar2.Set('chart.text.angle', 30);
	bar2.Set('chart.strokestyle', 'rgba(0,0,0,0)');
	bar2.Set('chart.tooltips', tooltips);
	bar2.Set('chart.tooltips.effect', 'expand');
	bar2.Set('chart.tooltips.event', 'onmousemove'); 
	RGraph.Effects.jQuery.Reveal(bar2);
	RGraph.Effects.Bar.Grow(bar2);
}


/////////////////////////////////////////////////////////////////////////
/////////////// Realiza una búsqueda en flickr del texto que se le pasa y
/////////////// obtiene 12 imágenes que se representan en la galería.
/////////////////////////////////////////////////////////////////////////
function flickr(texto){
	$("#main").empty().append("<div id='galeria'></div>");
	
    $.getJSON("http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=fddd7bc223fe37295faace93a4f2d8fc&tags="+texto+"&per_page=12&format=json&nojsoncallback=1",
						function(data) {
							$.each(data.photos.photo, function(i, rPhoto){						
							
							var basePhotoURL = 'http://farm' + rPhoto.farm + '.static.flickr.com/'
							+ rPhoto.server + '/' + rPhoto.id + '_' + rPhoto.secret;         
							var thumbPhotoURL = basePhotoURL + '_s.jpg';
							var mediumPhotoURL = basePhotoURL + '.jpg';
            
							var photoStringStart = '<div class="contenedorfoto"><span>'+rPhoto.title+'</span><br/>';
							var photoStringEnd = '<a title="' + rPhoto.title + '"  class="lightbox" href="'+ 
							mediumPhotoURL +'"><img src="' + mediumPhotoURL + '" alt="' + 
					         	rPhoto.title + '" style="max-height:120px;max-width:210px;"  /></a><br /></div>;'                
                            var photoString=photoStringStart+photoStringEnd;
							$(photoString).appendTo("#galeria");
                                                        

 
 
    });

	// se aplica el plugin lightbox para mostrar las imágenes
	$('a.lightbox').lightBox(); 
    
  });

}

/////////////////////////////////////////////////////////////////////////
/////////////// Muestra un gif indicando al usuario que se está obteniendo
/////////////// la información e impide que éste interactue con la página.
/////////////////////////////////////////////////////////////////////////
function mostrarLoading(){
	$("#body").append("<div class='loading' id='loading'><img src='imagenes/loading.gif' height='100' width='100'></img></div>");
	$("#loading").fadeIn();
	
}
/////////////////////////////////////////////////////////////////////////
/////////////// Esconde el gif de loading
/////////////////////////////////////////////////////////////////////////
function esconderLoading(){

	$("#loading").remove();
}
