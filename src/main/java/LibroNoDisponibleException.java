public class LibroNoDisponibleException extends RuntimeException {
    public LibroNoDisponibleException(String isbn){
        super("libro "+ isbn +" no disponible");
    }
    
}
