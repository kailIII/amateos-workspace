<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>paSOS - login</title>
    </head>
    <body>
        <center>
        <h1>Login</h1>
        <form ACTION="LoginServlet" METHOD="POST">
         <table BORDER=0 WIDTH="50%" >       
        <tr> 
            <td> <b>Usuario </b> </td>
            <td> <input TYPE="TEXT" NAME="user" SIZE="20" MAXLENGTH="20"> </td> </tr>
        <tr>
            <td> <b> Contrase&ntilde;a:</b> </td> 
            <td> <input TYPE="PASSWORD" NAME="password"> </td>
        </tr> 
        </table> 
        <input TYPE="SUBMIT" NAME="Entrar" VALUE="Entrar"> </form>
        </center>
    <p><b>El usuario y/o la contrase&ntilde;a introducida no son válidos.</b></p>
    </body>
</html>