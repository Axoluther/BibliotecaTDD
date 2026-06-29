/**
 * Excepción lanzada cuando se intenta prestar un libro que ya está
 * prestado (no disponible).
 */
public class LibroNoDisponibleException extends RuntimeException {
    /**
     * Construye la excepción indicando el ISBN del libro no disponible.
     *
     * @param isbn ISBN del libro que no puede prestarse.
     */
    public LibroNoDisponibleException(String isbn){
        super("libro "+ isbn +" no disponible");
    }
    
}
