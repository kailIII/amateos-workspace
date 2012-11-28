<%-- 
    Document   : estadisticas
    Created on : 27-ene-2012, 14:07:26
    Author     : Juan Antonio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="js/estadisticas.js"></script>
    </head>
    <body>
        <div id="buscarEstadisiticas">
            <select id="listaAnios" name="listaAnios">
                <option value="todos">Todos</option>
                <option value="2011">2011</option>
                <option value="2010">2010</option>
                <option value="2009">2009</option>
                <option value="2008">2008</option>           
            </select>
            <input type="button" id="buscarEstadisticas" value="Buscar" name="buscarEstadisticas" />
        </div>
        <div id="grafico">
            
        </div>
    </body>
</html>
