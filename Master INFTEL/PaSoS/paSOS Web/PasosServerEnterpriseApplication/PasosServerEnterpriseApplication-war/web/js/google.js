/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var peticion = null;
var peticion2 = null;
var map;
var markersArrayProtegida = null;
var markersArrayMaltratador = null;
var i=0;
var j =0;
var circle = null;

$(document).ready(function() {
        $("#Enviar").click(function(){
            mensaje = $("#trama").val();
            $.post('FrameHandlerServlet?trama='+mensaje,function(data){alert("Frame sent!")});
        });
        $("#Alarma").click(function(){
            mensaje = $("#trama").val();
            mensaje = "*$AU11"+mensaje.substring(4,mensaje.Length);            
            $.post('FrameHandlerServlet?trama='+mensaje,function(data){alert("Frame sent!")});
        });
        initialize();
        peticion = inicializa_xhr();
        if(peticion) {
            peticion.onreadystatechange = muestraZonasProtegida;
            peticion.open("GET", "js/coordenadasProtegida.json?nocache="+Math.random(), true);
            peticion.send(null);
        }
        peticion2 = inicializa_xhr();
        if(peticion2) {
            peticion2.onreadystatechange = muestraZonasMaltratador;
            peticion2.open("GET", "js/coordenadasMaltratador.json?nocache="+Math.random(), true);
            peticion2.send(null);
        }
        document.getElementById("latAltProtegida").onchange = cargaCoordenadasProtegida;
        document.getElementById("latAltMaltratador").onchange = cargaCoordenadasMaltratador;
    });

function inicializa_xhr() {
  if (window.XMLHttpRequest) {
    return new XMLHttpRequest(); 
  } else if (window.ActiveXObject) {
    return new ActiveXObject("Microsoft.XMLHTTP"); 
  } 
}
function cargaCoordenadasProtegida(){
    var lista = document.getElementById("latAltProtegida");
  var tipoComida = lista.options[lista.selectedIndex].value;
  
  if(!isNaN(tipoComida)) {
    peticion = inicializa_xhr();
    if (peticion) {
      peticion.onreadystatechange = muestraUbicacionProtegida;
      peticion.open("GET", "js/coordenadasProtegida.json?nocache=" + Math.random(), true);
      peticion.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      peticion.send(null);
    }
  } 
    
}
function cargaCoordenadasMaltratador(){
    var lista = document.getElementById("latAltMaltratador");
  var tipoComida = lista.options[lista.selectedIndex].value;
  
  if(!isNaN(tipoComida)) {
    peticion2 = inicializa_xhr();
    if (peticion2) {
      peticion2.onreadystatechange = muestraUbicacionMaltratador;
      peticion2.open("GET", "js/coordenadasMaltratador.json?nocache=" + Math.random(), true);
      peticion2.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      peticion2.send(null);
    }
  } 
    
}

function muestraUbicacionProtegida() {
  if(i==1){  
    borradoMarkerProtegida();  
  }
  var myLatlng;
  if (peticion.readyState == 4) {
    if (peticion.status == 200) {
      var coordenadas = eval('(' + peticion.responseText + ')');
      var prov = document.getElementById("latAltProtegida").value;
      ln = ConvertDDToDMS(coordenadas[prov].lng);
      lt = ConvertDDToDMS(coordenadas[prov].lat);
      document.getElementById("trama").value="*$TE"+
          "%26LD"+muestraLD()+
          "%26LH"+muestraLH()+
          "%26LN"+ln+
          "%26LT"+lt+
          "%26RD358987010052195";
      
       myLatlng = new google.maps.LatLng(coordenadas[prov].lat , coordenadas[prov].lng);
        marker1 = new google.maps.Marker({
        position: myLatlng, 
              map: map
        });
        markersArrayProtegida=marker1;
        i=1;
    }
  }
}
function muestraUbicacionMaltratador() {
  if (j==1){
    borradoMarkerMaltratador();  
  }
  var myLatlng;
  if (peticion2.readyState == 4) {
    if (peticion2.status == 200) {
      var coordenadas = eval('(' + peticion2.responseText + ')');
      var prov = document.getElementById("latAltMaltratador").value;
      ln = ConvertDDToDMS(coordenadas[prov].lng);
      lt = ConvertDDToDMS(coordenadas[prov].lat);
      document.getElementById("trama").value="*$ZN"+
          "%26LD"+muestraLD()+
          "%26LH"+muestraLH()+
          "%26LN"+ln+
          "%26LT"+lt+
          "%26RD358987010052665";
      
      
       myLatlng = new google.maps.LatLng(coordenadas[prov].lat , coordenadas[prov].lng);
        marker1 = new google.maps.Marker({
        position: myLatlng, 
              map: map
        });
        markersArrayMaltratador=marker1;
        var populationOptions = {
                strokeColor: "#FF0000",
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: "#FF0000",
                fillOpacity: 0.35,
                map: map,
                center: myLatlng,
                radius: 200
        };
      circle = new google.maps.Circle(populationOptions);        
        j=1;
    }
  }
}

function muestraZonasProtegida() {
  if (peticion.readyState == 4) {
    if (peticion.status == 200) {
      var lista = document.getElementById("latAltProtegida");
      var zonas = eval('(' + peticion.responseText + ')');
      lista.options[0] = new Option("- selecciona -");
      var i=1;
      for(var codigo in zonas) {
        lista.options[i] = new Option(zonas[codigo].nombre, codigo);
        i++;
      }
    }
  }
}
function muestraZonasMaltratador() {
  if (peticion2.readyState == 4) {
    if (peticion2.status == 200) {
      var lista = document.getElementById("latAltMaltratador");
      var zonas = eval('(' + peticion2.responseText + ')');
      lista.options[0] = new Option("- selecciona -");
      var i=1;
      for(var codigo in zonas) {
        lista.options[i] = new Option(zonas[codigo].nombre, codigo);
        i++;
      }
    }
  }
}
 
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
 function borradoMarkerProtegida(){
    
              markersArrayProtegida.setMap(null);  
        
    
   }
   function borradoMarkerMaltratador(){
      
              markersArrayMaltratador.setMap(null);  
              circle.setMap(null);
    }
 google.maps.event.addDomListener(window, 'load', initialize);
 
 
// function dec2gms(valor)
//{
//        
//        grados    = Math.abs(parseInt(valor));   
//        
//        minutos   = (parseInt(valor) - grados) * 60;
//        diezMilesimas  = minutos;
//        minutos   = Math.abs(parseInt(minutos));
//        diezMilesimas  = Math.round((diezMilesimas - minutos) * 6000);
//        signo     = (valor > 0) ? 1 : 2;        
//        dd = (grados>10) ? grados:"0"+grados;
//        mm = (minutos>10) ? minutos:"0"+minutos;        
//        if (diezMilesimas < 10){
//            nnnn = "000" + diezMilesimas;
//        }
//        if (diezMilesimas < 100){
//            nnnn = "00" + diezMilesimas;
//        }
//        if (diezMilesimas < 1000){
//            nnnn = "0" + diezMilesimas;
//        }
//        if (diezMilesimas >= 1000){
//            nnnn =  diezMilesimas;
//        }        
//        gsm = signo+""+dd+ "" + mm+"" + nnnn;
//        return gsm;      
//}
function ConvertDDToDMS(D){
    dir = (D> 0) ? 1 : 2;
    dd = 0|(D<0?D=-D:D);
    mm = 0|D%1*60 ;
    ssss =(0|D*60%1*6000);
    if (dd<10){
        dd = '0'+dd;        
    }
    if (mm <10){
        mm = '0' + mm;
    }
    if (ssss<10){
        ssss = ssss + '000';
    }
    if (ssss<100){
        ssss = ssss +'00';
    }
    if (ssss <1000){
        ssss =  ssss +'0';
    }
    trama = dir +""+dd+""+mm+""+ssss;
    return trama;
}

function muestraLH()  
  {  
  LH=new Date();  
  segundos = LH.getSeconds();  
  minutos  = LH.getMinutes(); 
  horas = LH.getHours();
  hh = (horas>10) ? horas:"0"+horas;
  mm= (minutos>10) ? minutos:"0"+minutos;
  ss = (segundos>10) ? segundos:"0"+segundos;
  h= hh+""+mm+""+ss;
  
  return h;
  }  
function muestraLD(){
    LD=new Date();
    dia = LD.getDate();
    mes= LD.getMonth() + 1;
    año = LD.getFullYear();
    if (mes < 10)  
        mes = '0' + mes  
  
    if (dia < 10)  
        dia = '0' + dia  
    d= año +"" + mes + "" + dia;
    return d;
} 
