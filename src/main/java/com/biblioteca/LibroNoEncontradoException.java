package com.biblioteca;

/**
 * Excepción lanzada cuando no se encuentra un libro por ISBN o título.
 */
public class LibroNoEncontradoException extends RuntimeException {
        /**
         * Crea la excepción indicando la clave buscada (ISBN o título).
         *
         * @param titulo_or_isbn Identificador buscado que no fue encontrado.
         */
        public LibroNoEncontradoException(String titulo_or_isbn){
            super("Libro No Encontrado: " + titulo_or_isbn);
        };
    }