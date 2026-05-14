<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <title>Catálogo</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css">
</head>

<body>

<h1>Catálogo de Productos</h1>

<form method="get"
      action="${pageContext.request.contextPath}/catalogo">

    <input type="text"
           name="q"
           value="${busqueda}"
           placeholder="Buscar producto...">

    <select name="cat">

        <option value="">Todas las categorías</option>

        <c:forEach var="cat" items="${categorias}">

            <option value="${cat}"
                ${cat == catActual ? "selected" : ""}>

                    ${cat}

            </option>

        </c:forEach>

    </select>

    <button type="submit">Buscar</button>

    <a href="${pageContext.request.contextPath}/carrito">
        Ver carrito
    </a>

</form>

<table>

    <thead>

    <tr>
        <th>Nombre</th>
        <th>Categoría</th>
        <th>Precio</th>
        <th>Stock</th>
        <th></th>
    </tr>

    </thead>

    <tbody>

    <c:choose>

        <c:when test="${empty productos}">

            <tr>
                <td colspan="5">
                    No se encontraron productos.
                </td>
            </tr>

        </c:when>

        <c:otherwise>

            <c:forEach var="p" items="${productos}">

                <tr>

                    <td>${p.nombre}</td>

                    <td>${p.categoria}</td>

                    <td>
                        <fmt:formatNumber
                                value="${p.precio}"
                                type="currency"
                                currencySymbol="$"/>
                    </td>

                    <td>${p.stock}</td>

                    <td>

                        <form method="post"
                              action="${pageContext.request.contextPath}/carrito">

                            <input type="hidden"
                                   name="accion"
                                   value="agregar">

                            <input type="hidden"
                                   name="idProducto"
                                   value="${p.id}">

                            <button type="submit">
                                + Carrito
                            </button>

                        </form>

                    </td>

                </tr>

            </c:forEach>

        </c:otherwise>

    </c:choose>

    </tbody>

</table>

</body>
</html>