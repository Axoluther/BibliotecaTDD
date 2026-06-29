

/**
 * Excepción que indica que se intentó registrar un libro cuyo ISBN ya
 * existe en la biblioteca.
 */
public class LibroDuplicadoException extends RuntimeException {
    /**
     * Crea la excepción indicando el ISBN duplicado.
     *
     * @param isbn ISBN que ya estaba registrado.
     */
    public LibroDuplicadoException(String isbn){
        super("Libro duplicado isbn: " + isbn);
    };
}
