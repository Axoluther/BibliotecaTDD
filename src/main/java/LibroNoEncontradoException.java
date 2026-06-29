

public class LibroNoEncontradoException extends RuntimeException {
        public LibroNoEncontradoException(String titulo_or_isbn){
            super("Libro No Encontrado: " + titulo_or_isbn);
        };
    }