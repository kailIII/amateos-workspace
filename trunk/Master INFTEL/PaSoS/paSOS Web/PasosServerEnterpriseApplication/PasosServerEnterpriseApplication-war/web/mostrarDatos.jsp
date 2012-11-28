<%-- 
    Document   : mostrarDatos
    Created on : 25-ene-2012, 15:36:21
    Author     : Juan Antonio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="cont_foto" >
        <img src="http://localhost:8080/PasosServerEnterpriseApplication-war/ImagenServlet?id=${ProtegidoInfoBean.protegido.idProtegido}" id="foto"/>
</div>
<div id="cont_texto">
    <p id="nombre"><b>Nombre: ${ProtegidoInfoBean.protegido.nombre}</b></p>
    <p id="apellidos"><b>Apellidos: ${ProtegidoInfoBean.protegido.apellidos}</b></p>
    <p id="fecha_nacimiento"><b>Fecha de nacimiento: ${ProtegidoInfoBean.protegido.fechaNacimiento}</b></p>
    <p id="telefono"><b>Telefono: ${ProtegidoInfoBean.protegido.telefonoMovil}</b></p>
</div>

