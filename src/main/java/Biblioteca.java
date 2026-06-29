import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Biblioteca {
    private final Map<String, Libro> libros = new HashMap<>();

    public void registrarLibro(Libro libro) {
        Objects.requireNonNull(libro) ;

        String isbn = libro.getIsbn();
        if ( libros.putIfAbsent(isbn, libro) != null ){
            throw new LibroDuplicadoException(isbn);
        };

    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        Objects.requireNonNull(isbn, "'isbn' cannot be null");

        return Optional.ofNullable(libros.get(isbn));
    }

    public List<Libro> buscarPorTitulo(String titulo) { 
        List<Libro> lista_libros = new ArrayList<>() ;

        for ( Libro libro : libros.values() ){
            if (titulo == libro.getTitulo()){
                lista_libros.add(libro);
            } ;
        }
        return lista_libros ; 
    }

    public List<Libro> listarDisponibles() { return null; }

    public void prestarLibro(String isbn) {
        Objects.requireNonNull(isbn);
        
        Libro libro = buscarPorIsbn(isbn).orElseThrow(() -> new LibroNoEncontradoException(isbn)) ;

        if (!libro.getDisponible()){
            throw new LibroNoDisponibleException(isbn);
        };

        libro.setPedido();
        // override
        libros.put(isbn, libro);
    }

    public void devolverLibro(String isbn) { }

}
