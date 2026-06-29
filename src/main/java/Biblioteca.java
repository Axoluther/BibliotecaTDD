import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Servicio simple de gestión de libros en una biblioteca.
 * <p>
 * Permite registrar libros, buscar por ISBN o título, listar los disponibles,
 * prestar y devolver libros. Internamente mantiene un mapa de ISBN → {@link Libro}.
 * </p>
 *
 * @since 1.0
 */
public class Biblioteca {
    private final Map<String, Libro> libros = new HashMap<>();

    /**
     * Registra un nuevo libro en la biblioteca.
     *
     * @param libro Instancia de {@link Libro} a registrar (no puede ser {@code null}).
     * @throws NullPointerException     si {@code libro} es {@code null}.
     * @throws LibroDuplicadoException si ya existe un libro con el mismo ISBN.
     */
    public void registrarLibro(Libro libro) {
        Objects.requireNonNull(libro) ;

        String isbn = libro.getIsbn();
        if ( libros.putIfAbsent(isbn, libro) != null ){
            throw new LibroDuplicadoException(isbn);
        };

    }

    /**
     * Busca un libro por su ISBN.
     *
     * @param isbn ISBN a buscar (no puede ser {@code null}).
     * @return {@code Optional<Libro>} que contiene el libro si se encuentra,
     *         o {@code Optional.empty()} si no existe.
     * @throws NullPointerException si {@code isbn} es {@code null}.
     */
    public Optional<Libro> buscarPorIsbn(String isbn) {
        Objects.requireNonNull(isbn, "'isbn' cannot be null");

        return Optional.ofNullable(libros.get(isbn));
    }

    /**
     * Busca libros cuyo título contenga la cadena proporcionada. La búsqueda
     * no distingue entre mayúsculas y minúsculas y realiza una búsqueda
     * parcial (contains).
     *
     * @param titulo Subcadena a buscar en el título (no puede ser {@code null}).
     * @return Lista de libros cuyo título contiene la cadena proporcionada;
     *         devuelve una lista vacía si no hay coincidencias.
     */
    public List<Libro> buscarPorTitulo(String titulo) { 
        
        List<Libro> lista_libros = new ArrayList<>() ;

        titulo = titulo.toLowerCase();

        for ( Libro libro : libros.values() ){
            if ( libro.getTitulo().toLowerCase().contains(titulo) ){
                lista_libros.add(libro);
            } ;
        }
        return lista_libros ; 
    }

    /**
     * Lista todos los libros que actualmente están marcados como
     * disponibles para préstamo.
     *
     * @return Lista de libros disponibles; puede ser vacía si ninguno está
     *         disponible.
     */
    public List<Libro> listarDisponibles() { 
        List<Libro> disponibles = new ArrayList<>();
        for ( Libro libro : libros.values() ){
            if (libro.getDisponible()){
                disponibles.add(libro);
            };
        }
        return disponibles;
    }

    /**
     * Presta un libro identificado por su ISBN.
     * <p>
     * Verifica que el ISBN no sea {@code null}, que el libro exista y que
     * esté disponible; marca el libro como no disponible y actualiza el
     * registro interno.
     * </p>
     *
     * @param isbn ISBN del libro a prestar (no puede ser {@code null}).
     * @throws NullPointerException         si {@code isbn} es {@code null}.
     * @throws LibroNoEncontradoException  si no existe un libro con ese ISBN.
     * @throws LibroNoDisponibleException  si el libro ya está prestado.
     */
    public void prestarLibro(String isbn) {
        Objects.requireNonNull(isbn);

        Libro libro = buscarPorIsbn(isbn).orElseThrow(() -> new LibroNoEncontradoException(isbn)) ;

        if (!libro.getDisponible()){
            throw new LibroNoDisponibleException(isbn);
        };

        // Marca como pedido y actualiza el mapa
        libro.setPedido();
        libros.put(isbn, libro);
    }
    
    /**
     * Devuelve un libro identificado por su ISBN.
     * <p>
     * Verifica que el ISBN no sea {@code null}, que el libro exista y que
     * actualmente esté prestado; marca el libro como disponible y actualiza
     * el registro interno.
     * </p>
     *
     * @param isbn ISBN del libro a devolver (no puede ser {@code null}).
     * @throws NullPointerException        si {@code isbn} es {@code null}.
     * @throws LibroNoEncontradoException si no existe un libro con ese ISBN.
     * @throws LibroYaDisponible          si el libro ya estaba marcado como disponible.
     */
    public void devolverLibro(String isbn) {
        Objects.requireNonNull(isbn);

        Libro libro = buscarPorIsbn(isbn).orElseThrow(() -> new LibroNoEncontradoException(isbn));
        
        if ( libro.getDisponible() ){
            throw new LibroYaDisponible(isbn);
        }
        libro.setDisponible();
        libros.put(isbn,libro);
    }

}
