package com.ejemplo.servlet;

import com.ejemplo.model.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private List<Producto> catalogo;

    @Override
    public void init() {

        catalogo = new ArrayList<>(Arrays.asList(

                new Producto(1,
                        "Laptop Dell",
                        "Computadores",
                        2500000,
                        5),

                new Producto(2,
                        "Mouse Logitech",
                        "Periféricos",
                        85000,
                        20),

                new Producto(3,
                        "Teclado Mecánico",
                        "Periféricos",
                        220000,
                        10),

                new Producto(4,
                        "Monitor 24",
                        "Computadores",
                        650000,
                        8),

                new Producto(5,
                        "Audífonos Sony",
                        "Audio",
                        180000,
                        15),

                new Producto(6,
                        "Webcam HD",
                        "Periféricos",
                        95000,
                        12)

        ));

        // Guardar catálogo en contexto global
        getServletContext().setAttribute("catalogo", catalogo);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String busqueda = req.getParameter("q");
        String categoria = req.getParameter("cat");

        List<Producto> resultado = catalogo.stream()

                .filter(p ->
                        busqueda == null
                                || busqueda.isBlank()
                                || p.getNombre()
                                .toLowerCase()
                                .contains(busqueda.toLowerCase()))

                .filter(p ->
                        categoria == null
                                || categoria.isBlank()
                                || p.getCategoria().equals(categoria))

                .collect(Collectors.toList());

        // Categorías únicas
        List<String> categorias = catalogo.stream()
                .map(Producto::getCategoria)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        req.setAttribute("productos", resultado);
        req.setAttribute("categorias", categorias);
        req.setAttribute("busqueda", busqueda);
        req.setAttribute("catActual", categoria);

        req.getRequestDispatcher("/WEB-INF/views/catalogo.jsp")
                .forward(req, resp);
    }

    public List<Producto> getCatalogo() {
        return catalogo;
    }
}