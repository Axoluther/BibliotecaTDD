import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BibliotecaTest {

    private Biblioteca biblio;
    private Libro libro1;
    private Libro libro2;
    private Libro libro3;

    @BeforeEach
    void setUp() {
        biblio = new Biblioteca();
        libro1 = new Libro(
            "1234",
            "fantasias de don Fede",
            "Conocido de apellido Carrera",
            1930
        );
        libro2 = new Libro(
            "1234",
            "aventuras traviesas en el el jardin escondido en la chancha de la usm",
            "aaron",
            2026
        );
        libro3 = new Libro(
            "asdasd-asd-aas-da-s",
            "fin de la usm",
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

    };

    @Test
    void listarLibros(){}
}
