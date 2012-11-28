<%-- 
    Document   : main
    Created on : 26-ene-2012, 0:12:20
    Author     : albertomateos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>paSOS</title>
        <LINK href="css/estilo.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript"  src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <!--        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>-->
        <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>  
        <script type="text/javascript" src="js/funciones.js"></script>
    </head>
    <body>
    <center>
        <div id="title">
            pa<span id="sos" name="sos">SoS</span>
        </div>
    </center>
    <div id="user">
        Operador/a: <b>${username}</b>
        <span id="logout"><b><a href="#" id="desconectar">descontectar</a></b></span>
        <input id="atendida" type="button" value="atendida"></input>
    </div>
    <ul id="menu">
        <li><a href="#" id="menuBuscar">BUSCAR USUARIO</a></li>
        <li><a href="#" id="menuCrear">CREAR USUARIO</a></li>
        <li><a href="#" id="menuEstadisticas">ESTADISTICAS</a></li>
    </ul>

    <div id="content"></div>
    <frameset id="frameset">

        <iframe src ="comet?action=suscribe" id="iframe" name="iframeChat" width="100%" scrolling="auto">

        </iframe> 

    </frameset>
    
    <div id="mapa" style="position: absolute; left:45%; right: 2%; top:45%;bottom: 5%; overflow: hidden; z-index: 0;">                   
    </div> 
    

</body>
</html>
