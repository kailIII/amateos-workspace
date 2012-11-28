<%-- 
    Document   : index
    Created on : 25-ene-2012, 11:55:55
    Author     : Juan Antonio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <LINK href="css/estilo.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/funciones.js"></script>
        <title>paSOS - login</title>
    </head>
    <body>
    <center>

            <div id="title">
                pa<span id="sos" name="sos">SoS</span>
            </div>
        <br></br>
        <form ACTION="LoginServlet" METHOD="POST">

            <fieldset id="form">
                <legend>Introduzca sus datos de usuario</legend>
                <ol>
                    <li>
                        <label for="username">Usuario: </label>
                        <input TYPE="TEXT" NAME="username">
                    </li>
                    <li>
                        <label for="password">Contrase&ntilde;a:</label>
                        <span align="center"><input TYPE="PASSWORD" NAME="password"></span>
                    </li>
                </ol>
            </fieldset>
            <input type="hidden" name="action" value="login"/>    
            <input class="btn" TYPE="SUBMIT" NAME="Entrar" VALUE="Entrar"> </form>
    </center>
</body>
</html>