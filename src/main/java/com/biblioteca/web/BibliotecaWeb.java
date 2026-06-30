package com.biblioteca.web;

import com.biblioteca.Biblioteca;
import com.biblioteca.Libro;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BibliotecaWeb {
    private static final int PUERTO = 8080;
    private static final Biblioteca biblioteca = new Biblioteca();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PUERTO), 0);
        server.createContext("/", BibliotecaWeb::mostrarInicio);
        server.createContext("/registrar", BibliotecaWeb::registrar);
        server.createContext("/buscar-isbn", BibliotecaWeb::buscarPorIsbn);
        server.createContext("/buscar-titulo", BibliotecaWeb::buscarPorTitulo);
        server.createContext("/prestar", BibliotecaWeb::prestar);
        server.createContext("/devolver", BibliotecaWeb::devolver);
        server.createContext("/disponibles", BibliotecaWeb::listarDisponibles);
        server.start();

        System.out.println("Biblioteca web disponible en http://localhost:" + PUERTO);
    }

    private static void mostrarInicio(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        responder(exchange, 200, leerIndexHtml(), "text/html; charset=utf-8");
    }

    private static void registrar(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        Map<String, String> parametros = leerParametros(exchange);
        String isbn = parametros.getOrDefault("isbn", "");
        String titulo = parametros.getOrDefault("titulo", "");
        String anioPublicacion = parametros.getOrDefault("anioPublicacion", "");

        if (estaVacio(isbn)) {
            responderRuta(exchange, "POST", "El ISBN es obligatorio.", parametros);
            return;
        }

        if (estaVacio(titulo)) {
            responderRuta(exchange, "POST", "El titulo es obligatorio.", parametros);
            return;
        }

        if (estaVacio(anioPublicacion)) {
            responderRuta(exchange, "POST", "El anio de publicacion es obligatorio.", parametros);
            return;
        }

        try {
            Libro libro = new Libro(
                isbn,
                titulo,
                parametros.getOrDefault("autor", ""),
                Integer.parseInt(anioPublicacion)
            );
            biblioteca.registrarLibro(libro);
            responderRuta(exchange, "POST", "Libro registrado correctamente.", parametros);
        } catch (NumberFormatException ex) {
            responderRuta(exchange, "POST", "El anio de publicacion debe ser un numero.", parametros);
        } catch (RuntimeException ex) {
            responderRuta(exchange, "POST", "No se pudo registrar el libro: " + ex.getMessage(), parametros);
        }
    }

    private static void buscarPorIsbn(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        Map<String, String> parametros = leerParametros(exchange);
        String isbn = parametros.getOrDefault("isbn", "");

        if (estaVacio(isbn)) {
            responderResultado(exchange, "Resultado de busqueda por ISBN", "<p>El ISBN es obligatorio.</p>");
            return;
        }

        String resultado = biblioteca.buscarPorIsbn(isbn)
            .map(BibliotecaWeb::formatearLibro)
            .orElse("<p>No se encontro un libro con ese ISBN.</p>");

        responderResultado(exchange, "Resultado de busqueda por ISBN", resultado);
    }

    private static void buscarPorTitulo(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        Map<String, String> parametros = leerParametros(exchange);
        String titulo = parametros.getOrDefault("titulo", "");

        if (estaVacio(titulo)) {
            responderResultado(exchange, "Resultado de busqueda por titulo", "<p>El titulo es obligatorio.</p>");
            return;
        }

        String resultado = formatearListaLibros(
            biblioteca.buscarPorTitulo(titulo),
            "No se encontraron libros con ese titulo."
        );

        responderResultado(exchange, "Resultado de busqueda por titulo", resultado);
    }

    private static void prestar(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        Map<String, String> parametros = leerParametros(exchange);
        String isbn = parametros.getOrDefault("isbn", "");

        if (estaVacio(isbn)) {
            responderResultado(exchange, "Prestamo de libro", "<p>El ISBN es obligatorio.</p>");
            return;
        }

        try {
            biblioteca.prestarLibro(isbn);
            responderResultado(exchange, "Prestamo de libro", "<p>Libro prestado correctamente.</p>");
        } catch (RuntimeException ex) {
            responderResultado(
                exchange,
                "Prestamo de libro",
                "<p>No se pudo prestar el libro: " + escaparHtml(ex.getMessage()) + "</p>"
            );
        }
    }

    private static void devolver(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        Map<String, String> parametros = leerParametros(exchange);
        String isbn = parametros.getOrDefault("isbn", "");

        if (estaVacio(isbn)) {
            responderResultado(exchange, "Devolucion de libro", "<p>El ISBN es obligatorio.</p>");
            return;
        }

        try {
            biblioteca.devolverLibro(isbn);
            responderResultado(exchange, "Devolucion de libro", "<p>Libro devuelto correctamente.</p>");
        } catch (RuntimeException ex) {
            responderResultado(
                exchange,
                "Devolucion de libro",
                "<p>No se pudo devolver el libro: " + escaparHtml(ex.getMessage()) + "</p>"
            );
        }
    }

    private static void listarDisponibles(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        String resultado = formatearListaLibros(
            biblioteca.listarDisponibles(),
            "No hay libros disponibles."
        );

        responderResultado(exchange, "Libros disponibles", resultado);
    }

    private static void responderResultado(
        HttpExchange exchange,
        String titulo,
        String contenido
    ) throws IOException {
        String cuerpo = """
            <!doctype html>
            <html lang="es">
            <head>
                <meta charset="utf-8">
                <title>Biblioteca</title>
            </head>
            <body>
                <h1>Biblioteca</h1>
                <h2>%s</h2>
                %s
                <p><a href="/">Volver</a></p>
            </body>
            </html>
            """.formatted(escaparHtml(titulo), contenido);

        responder(exchange, 200, cuerpo, "text/html; charset=utf-8");
    }

    private static String formatearLibro(Libro libro) {
        return """
            <ul>
                <li><strong>ISBN:</strong> %s</li>
                <li><strong>Titulo:</strong> %s</li>
                <li><strong>Autor:</strong> %s</li>
                <li><strong>Anio:</strong> %s</li>
                <li><strong>Disponible:</strong> %s</li>
            </ul>
            """.formatted(
                escaparHtml(libro.getIsbn()),
                escaparHtml(libro.getTitulo()),
                escaparHtml(String.valueOf(libro.getAutor())),
                libro.getAnioPublicacion(),
                libro.getDisponible() ? "Si" : "No"
            );
    }

    private static String formatearListaLibros(Iterable<Libro> libros, String mensajeVacio) {
        StringBuilder resultado = new StringBuilder("<ul>");
        boolean tieneLibros = false;

        for (Libro libro : libros) {
            tieneLibros = true;
            resultado.append("<li>")
                .append(escaparHtml(libro.getTitulo()))
                .append(" - ISBN: ")
                .append(escaparHtml(libro.getIsbn()))
                .append(" - Autor: ")
                .append(escaparHtml(String.valueOf(libro.getAutor())))
                .append(" - ")
                .append(libro.getDisponible() ? "Disponible" : "No disponible")
                .append("</li>");
        }

        resultado.append("</ul>");

        if (!tieneLibros) {
            return "<p>" + escaparHtml(mensajeVacio) + "</p>";
        }

        return resultado.toString();
    }

    private static void responderRuta(
        HttpExchange exchange,
        String metodoEsperado,
        String mensaje,
        Map<String, String> parametros
    ) throws IOException {
        if (!metodoEsperado.equals(exchange.getRequestMethod())) {
            responder(exchange, 405, "Metodo no permitido", "text/plain; charset=utf-8");
            return;
        }

        String parametrosHtml = parametros.isEmpty()
            ? "<p>No se recibieron datos del formulario.</p>"
            : "<ul>" + parametros.entrySet().stream()
                .map(entry -> "<li><strong>" + escaparHtml(entry.getKey()) + ":</strong> "
                    + escaparHtml(entry.getValue()) + "</li>")
                .collect(Collectors.joining()) + "</ul>";

        String cuerpo = """
            <!doctype html>
            <html lang="es">
            <head>
                <meta charset="utf-8">
                <title>Biblioteca</title>
            </head>
            <body>
                <h1>Biblioteca</h1>
                <p>%s</p>
                <h2>Datos recibidos</h2>
                %s
                <p><a href="/">Volver</a></p>
            </body>
            </html>
            """.formatted(mensaje, parametrosHtml);

        responder(exchange, 200, cuerpo, "text/html; charset=utf-8");
    }

    private static Map<String, String> leerParametros(HttpExchange exchange) throws IOException {
        String datos = "";

        if ("GET".equals(exchange.getRequestMethod())) {
            datos = exchange.getRequestURI().getRawQuery();
        } else if ("POST".equals(exchange.getRequestMethod())) {
            datos = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        }

        return parsearParametros(datos);
    }

    private static Map<String, String> parsearParametros(String datos) {
        Map<String, String> parametros = new LinkedHashMap<>();

        if (datos == null || datos.isBlank()) {
            return parametros;
        }

        String[] pares = datos.split("&");
        for (String par : pares) {
            String[] partes = par.split("=", 2);
            String nombre = decodificar(partes[0]);
            String valor = partes.length > 1 ? decodificar(partes[1]) : "";
            parametros.put(nombre, valor);
        }

        return parametros;
    }

    private static String decodificar(String texto) {
        return URLDecoder.decode(texto, StandardCharsets.UTF_8);
    }

    private static boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private static String escaparHtml(String texto) {
        return texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    private static String leerIndexHtml() throws IOException {
        try (InputStream input = BibliotecaWeb.class.getResourceAsStream("/web/index.html")) {
            if (input == null) {
                return "<!doctype html><html><body><h1>Biblioteca</h1><p>No se encontro index.html.</p></body></html>";
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static void responder(
        HttpExchange exchange,
        int estado,
        String cuerpo,
        String contentType
    ) throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(estado, bytes.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
