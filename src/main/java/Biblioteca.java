import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class Biblioteca {
    private final Map<String, Libro> libros = new HashMap<>();

    public void registrarLibro(Libro libro) {
        libros.put(libro.getIsbn(), libro) ;
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        // trucaso xd
        return Optional.ofNullable(libros.get(isbn));
    }

    public List<Libro> buscarPorTitulo(String titulo) { return null; }

    public List<Libro> listarDisponibles() { return null; }

    public void prestarLibro(String isbn) { }

    public void devolverLibro(String isbn) { }

}
