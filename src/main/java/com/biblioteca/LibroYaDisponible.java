package com.biblioteca;

/**
 * Excepción lanzada cuando se intenta devolver un libro que ya estaba marcado
 * como disponible (no fue prestado previamente).
 */
public class LibroYaDisponible extends RuntimeException {
    /**
     * Construye la excepción indicando el ISBN del libro.
     *
     * @param isbn ISBN del libro que ya está disponible.
     */
    public LibroYaDisponible(String isbn){
        super("Este libro nunca se pidio: " + isbn);
    }
    
}
