import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Biblioteca {
    private final Map<String, Libro> libros = new HashMap<>();

    public void registrarLibro(Libro libro) {
        Libro libro_nn = Objects.requireNonNull(libro) ;
        String isbn = libro_nn.getIsbn();
        Optional<Libro> res = Optional.ofNullable(libros.get(isbn));
        if ( res.isPresent() ){
            throw new LibroDuplicadoException(isbn);
        };
        libros.put(libro.getIsbn(), libro_nn) ;
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return Optional.ofNullable(libros.get(Objects.requireNonNull(isbn, "'isbn' cannot be null")));
    }

    public List<Libro> buscarPorTitulo(String titulo) { return null; }

    public List<Libro> listarDisponibles() { return null; }

    public void prestarLibro(String isbn) { }

    public void devolverLibro(String isbn) { }

}
