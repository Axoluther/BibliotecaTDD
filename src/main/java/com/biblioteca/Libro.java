package com.biblioteca;

import java.util.Objects;

/**
 * Representa un libro en la biblioteca.
 * <p>
 * Cada instancia contiene el ISBN, título, autor, año de publicación y el
 * estado de disponibilidad del libro.
 * </p>
 *
 * @since 1.0
 */
public class Libro {
    // req minimos
    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;

    // extra
    private boolean disponible = true;
    
    /**
     * Construye un nuevo {@code Libro}.
     *
     * @param isbn            Identificador único del libro (no puede ser {@code null}).
     * @param titulo          Título del libro (no puede ser {@code null}).
     * @param autor           Nombre del autor; puede ser {@code null} si se desconoce.
     * @param anioPublicacion Año de publicación del libro.
     * @throws NullPointerException si {@code isbn} o {@code titulo} es {@code null}.
     */
    public Libro(String isbn, String titulo, String autor, int anioPublicacion){
        this.isbn = Objects.requireNonNull(isbn,"'isbn' argument cannot be null");
        if (this.isbn.isEmpty()) {
            throw new IllegalArgumentException("'isbn' argument cannot be empty");
        }
        this.titulo = Objects.requireNonNull(titulo, "'titulo' argument cannot be null");
        if (this.titulo.isEmpty()) {
            throw new IllegalArgumentException("'titulo' argument cannot be empty");
        }
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
    };

    /**
     * Devuelve el ISBN del libro.
     *
     * @return el ISBN como {@code String}.
     */
    public String getIsbn() {
        return this.isbn;
    };

    /**
     * Devuelve el título del libro.
     *
     * @return el título como {@code String}.
     */
    public String getTitulo(){
        return this.titulo;
    };
    
    /**
     * Devuelve el autor del libro.
     *
     * @return el autor como {@code String}, o {@code null} si no está disponible.
     */
    public String getAutor(){
        return this.autor;
    };

    /**
     * Devuelve el año de publicación del libro.
     *
     * @return el año de publicación como {@code int}.
     */
    public int getAnioPublicacion(){
        return this.anioPublicacion;
    };

    /**
     * Indica si el libro está disponible para préstamo.
     *
     * @return {@code true} si está disponible, {@code false} si está prestado.
     */
    public boolean getDisponible(){
        return this.disponible;
    };

    /**
     * Marca el libro como pedido (no disponible).
     */
    public void setPedido(){
        this.disponible = false;
    }

    /**
     * Marca el libro como disponible.
     */
    public void setDisponible(){
        this.disponible = true;
    }
}
