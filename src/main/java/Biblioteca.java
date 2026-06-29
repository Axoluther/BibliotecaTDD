import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Biblioteca {
    private final Map<String, Libro> libros = new HashMap<>();

    public void registrarLibro(Libro libro) {
        libros.put(libro.getIsbn(), Objects.requireNonNull(libro)) ;
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return Optional.ofNullable(libros.get(Objects.requireNonNull(isbn, "'isbn' cannot be null")));
    }

    public List<Libro> buscarPorTitulo(String titulo) { return null; }

    public List<Libro> listarDisponibles() { return null; }

    public void prestarLibro(String isbn) { }

    public void devolverLibro(String isbn) { }

}
