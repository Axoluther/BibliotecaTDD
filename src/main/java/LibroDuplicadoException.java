

public class LibroDuplicadoException extends RuntimeException {
    public LibroDuplicadoException(String isbn){
        super("Libro duplicado isbn: " + isbn);
    };
}
