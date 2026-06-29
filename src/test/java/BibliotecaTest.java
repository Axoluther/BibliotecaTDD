import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para la clase {@link Biblioteca}.
 * <p>
 * Cubre registro, búsqueda, préstamo y devolución de libros, así como los
 * escenarios de error relacionados (duplicados, no encontrado, no disponible).
 * </p>
 */
public class BibliotecaTest {

    private Biblioteca biblio;
    private Libro libro1;
    private Libro libro2;
    private Libro libro3;
    private Libro libro4;

    @BeforeEach
    void setUp() {
        biblio = new Biblioteca();
        libro1 = new Libro(
            "1234",
            "el mito de la caverna ex umbra in solem",
            "platon",
            230
        );
        libro2 = new Libro(
            "1234",
            "las mil y unas punto y coma s ",
            "aaron",
            2026
        );
        libro3 = new Libro(
            "asdasd",
            "100 años de computacion cientifica",
            "clavos de garcia",
            2040
        );
        libro4 = new Libro(
            "123rtrtr",
            "alicia en el pais del prestigio",
            "aaron",
            2040
        );
    }

    @Test
    void registrarLibroValido() {
        biblio.registrarLibro(libro1);

        Libro libro_found = biblio.buscarPorIsbn("1234").orElseThrow(() -> new LibroNoEncontradoException("1234"));
        assertEquals(libro1.getIsbn(), libro_found.getIsbn());
    }

    @Test
    void registrarLibroInvalido() {
        Libro libro_null = null;
        assertThrows(NullPointerException.class, () -> biblio.registrarLibro(libro_null));
    }

    @Test
    void testLibrosDuplicadosIsbn() {
        assertDoesNotThrow(() -> biblio.registrarLibro(libro1));
        assertThrows(LibroDuplicadoException.class, () -> biblio.registrarLibro(libro2));
    }

    @Test
    void testLibroNoEncontrado() {
        biblio.registrarLibro(libro1);
        biblio.registrarLibro(libro3);
        assertThrows(LibroNoEncontradoException.class, () -> biblio.buscarPorIsbn("9999").orElseThrow(() -> new LibroNoEncontradoException("9999")) );
        assertTrue(biblio.buscarPorTitulo("HOLA").isEmpty());
    }

    @Test
    void prestarLibros(){
        biblio.registrarLibro(libro1);
        biblio.registrarLibro(libro3);

        // existente
        assertDoesNotThrow(() -> biblio.prestarLibro(libro3.getIsbn()) );

        // ya prestado
        assertThrows(LibroNoDisponibleException.class, () -> biblio.prestarLibro(libro3.getIsbn()) );


        // devolverlo
        assertDoesNotThrow(() -> biblio.devolverLibro(libro3.getIsbn()));
        assertTrue( biblio.buscarPorIsbn( libro3.getIsbn()).orElseThrow(() -> new LibroNoEncontradoException(libro3.getIsbn())).getDisponible() );

        // inexistente
        assertThrows(LibroNoEncontradoException.class, ()-> biblio.prestarLibro("kkkasda") );
        assertThrows(LibroNoEncontradoException.class, ()-> biblio.devolverLibro("kkkasda") );

    };

    @Test
    void listarLibros(){
        List<Libro> libros_a_listar = new ArrayList<>();
        libros_a_listar.add(libro1);
        libros_a_listar.add(libro4);

        biblio.registrarLibro(libro1);
        biblio.registrarLibro(libro3);
        biblio.registrarLibro(libro4);

        biblio.prestarLibro(libro3.getIsbn());

        List<Libro> libros_disp = biblio.listarDisponibles();
        assertEquals(libros_a_listar, libros_disp);

    }

    @Test
    void buscarPorTitulo(){
        biblio.registrarLibro(libro1);
        biblio.registrarLibro(libro3);
        biblio.registrarLibro(libro4);

        // Buscar de manera parcial
        // y sin diferenciar por mayusculas o minusculas
        String word1 = "CompUTacIoN ";
        String word2 = "NoMeENcontr an";
        List<Libro> result_expected = new ArrayList<>();
        result_expected.add(libro3);

        List<Libro> libros = biblio.buscarPorTitulo(word1);
        List<Libro> libros_empty = biblio.buscarPorTitulo(word2);

        assertEquals(result_expected, libros);
        assertTrue(libros_empty.isEmpty());

    }
}
