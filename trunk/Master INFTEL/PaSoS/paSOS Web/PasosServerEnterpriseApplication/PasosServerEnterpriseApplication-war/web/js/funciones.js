/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var marker;

var map;

$(document).ready(function(){
   
    /*
    * EVENTOS MENU
    */

    $("#iframe").hide();
    $("#atendida").click(function(){
        $.post("comet?action=atendido");
        $("#mapa").hide();
        //atendida();
    });
    
    $("#menuBuscar").click(function(){
        $("#content").load("http://localhost:8080/PasosServerEnterpriseApplication-war/buscarProtegido.jsp");
    });
    
    $("#menuCrear").click(function(){
        $("#content").load("http://localhost:8080/PasosServerEnterpriseApplication-war/crearUsuarios.jsp");
    });

    $("#menuEstadisticas").click(function(){
        $("#content").load("http://localhost:8080/PasosServerEnterpriseApplication-war/estadisticas.jsp");
    });
    
    $("#desconectar").click(function(){
        $.post("comet?action=logout");
        $(document).load("index.jsp");
    });
    
        
    /*
     * EVENTO BOTÓN FINALIZACIÓN ATENCIÓN ALARMA
     */
   
    //Load map
    initialize(); 
    $("#mapa").hide();
     
});

/*
 * DECLARACIÓN DE FUNCIONES
 */

function buscarUsuario(){
    
    var nombre=$("#nombre").val();
    var apellidos=$("#apellidos").val();
    var url = "SearchServlet?nombre="+nombre+"&apellidos="+apellidos;
    
    $.ajax({
        url: url,
        beforeSend: function(){
            $("#datos").empty().append("<img src='imagenes/loading.gif'>");
        },
        success: function(data){
            $("#datos").empty().append(data);
        }
    });    
}


function crearUsuario(){
     
    var opcion = $('#tipouser').val();
    
    if(opcion == 1){
        
        tipo = 1;
        nombreP = $("#nombreP").val();
        apellidosP = $("#apellidosP").val();
        fechanacP = $("#fechanacP").val();
        telefonoP = $("#telefonoP").val();
        imeiP = $("#imeiP").val();
        nombreC1 = $("#nombreC1").val();
        movilC1 = $("#movilC1").val();
        emailC1 = $("#emailC1").val();
        nombreC2 = $("#nombreC2").val();
        movilC2 = $("#movilC2").val();
        emailC2 = $("#emailC2").val();
        url="CreateUserServlet?tipo=1&nombreP="+nombreP+"&apellidosP="+apellidosP+"&fechanacP="+fechanacP+"&telefonoP="+telefonoP+"&imeiP="+imeiP+
        "&nombreC1="+nombreC1+"&movilC1="+movilC1+"&emailC1="+emailC1+"&nombreC2="+nombreC2+"&movilC2="+movilC2+"&emailC2="+emailC2;
    }else{
         
        tipo=2;
        nombreA = $("#nombreA").val();
        apellidosA = $("#apellidosA").val();
        dispositivoA = $("#dispositivoA").val();
        distanciaA = $("#distanciaA").val();
        imeiA = $("#imeiA").val();
        url="CreateUserServlet?tipo=2&&nombreA="+nombreA+"&apellidosA="+apellidosA+"&dispositivoA="+dispositivoA+"&distanciaA="+distanciaA+"&imeiA="+imeiA;
    }
        
        
    /*url="CreateUserServlet?nombreP="+nombreP+"&apellidosP="+apellidosP+"&fechanacP="+fechanacP+"&telefonoP="+telefonoP+
    "&imagenP="+imagenP+"&nombreC1="+nombreC1+"&movilC1="+movilC1+"&emailC1="+emailC1+"&nombreC2="+nombreC2+"&movilC2="+movilC2+"&emailC2="+emailC2+
    "&nombreA="+nombreA+"&apellidosA="+apellidosA+"&dispositivoA="+dispositivoA+"&distanciaA="+distanciaA+"&imagenA="+imagenA;
        */
    $.ajax({
                    
        type: 'POST',
        url: url,
        beforeSend: function(){
            alert(url); 
            $("#content").empty().append("<img src='imagenes/loading.gif'>");
        },                
        success: function(respuesta) {
            $("#content").empty().append(respuesta);
        },
        error: function() {
            alert("Los datos no han podido ser almacenados correctamente");
        }
    });      
                    
}
              

function alarma(){
    alert("alarmaaaaa");    
    $("#alarma").empty().append(info);
    parent.$("#iframe").show();
    parent.$("#mapa").show();
    parent.initialize();
    parent.mostrarMarker(LT,LN);  
}

function mostrarMarker(LT,LN){
        
        myLatlng = new google.maps.LatLng(LT ,LN);
        marker1 = new google.maps.Marker({
        position: myLatlng, 
              map: map
        });
        marker = marker1;
}
function borradoMarker(){    
    markersArrayProtegida.setMap(null);         
    
}

//Crear mapa
function initialize() {
    directionsDisplay = new google.maps.DirectionsRenderer();  
    var myLatlng1 = new google.maps.LatLng(36.717696,-4.463711);   
    var myOptions = {
        zoom: 15,
        center: myLatlng1,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("mapa"), myOptions);
    directionsDisplay.setMap(map);
}
