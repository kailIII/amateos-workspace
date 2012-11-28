<%-- 
    Document   : tablaAlarmas
    Created on : 26-ene-2012, 11:49:37
    Author     : Juan Antonio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<table>
    <tr>
        <td>Identificador</td>
        <td>Fecha</td>
        <td>Descripción</td>
        <td>Estado</td>
    </tr>
    <c:forEach var="alarma" items="${tablaAlarmasBean.alarmas}">
        <tr>
            <td>${alarma.idAlarma} </td>
            <td>${alarma.fechaHora}</td>
            <td>${alarma.descripcion}</td>
            <td>
                <c:choose>
                    <c:when test="${alarma.estadoAlarma eq 1 }">
                        Atendida
                    </c:when>
                    <c:otherwise>
                        No atendida
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
</table>


