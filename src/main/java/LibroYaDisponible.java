public class LibroYaDisponible extends RuntimeException {
    public LibroYaDisponible(String isbn){
        super("Este libro nunca se pidio: " + isbn)
    }
    
}
