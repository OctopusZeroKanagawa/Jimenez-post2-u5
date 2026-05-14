package com.ejemplo.servlet;

import com.ejemplo.model.CarritoItem;
import com.ejemplo.model.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/carrito")
public class CarritoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String accion = req.getParameter("accion");

        HttpSession session = req.getSession();

        @SuppressWarnings("unchecked")
        Map<Integer, CarritoItem> carrito =
                (Map<Integer, CarritoItem>)
                        session.getAttribute("carrito");

        if (carrito == null) {

            carrito = new LinkedHashMap<>();

            session.setAttribute("carrito", carrito);
        }

        if ("agregar".equals(accion)) {

            int idProducto =
                    Integer.parseInt(req.getParameter("idProducto"));

            Producto producto = obtenerProducto(idProducto);

            if (producto != null) {

                carrito.merge(
                        idProducto,
                        new CarritoItem(producto, 1),

                        (existente, nuevo) -> {

                            existente.setCantidad(
                                    existente.getCantidad() + 1
                            );

                            return existente;
                        }
                );
            }

        } else if ("limpiar".equals(accion)) {

            carrito.clear();
        }

        resp.sendRedirect(
                req.getContextPath() +

                        ("verCarrito".equals(accion)
                                ? "/carrito"
                                : "/catalogo")
        );
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        @SuppressWarnings("unchecked")
        Map<Integer, CarritoItem> carrito =
                session != null
                        ? (Map<Integer, CarritoItem>)
                        session.getAttribute("carrito")
                        : null;

        req.setAttribute(
                "items",
                carrito != null
                        ? carrito.values()
                        : Collections.emptyList()
        );

        req.getRequestDispatcher("/WEB-INF/views/carrito.jsp")
                .forward(req, resp);
    }

    private Producto obtenerProducto(int id) {

        List<Producto> catalogo =
                (List<Producto>) getServletContext()
                        .getAttribute("catalogo");

        return catalogo.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}