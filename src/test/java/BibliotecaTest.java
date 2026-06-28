import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BibliotecaTest {

    @Test
    void testCrearLibroValido() {
        // prueba Red?
        Libro libro = new Libro(
            "978-3-16-148410-0",
            "El Quijote",
            "Miguel de Cervantes",
            1605
        );

        // assertTrue(true);

        assertEquals("978-3-16-148410-0", libro.getIsbn());
        assertEquals("El Quijote", libro.getTitulo());
        assertEquals("Miguel de Cervantes", libro.getAutor());
        assertEquals(1605, libro.getAnioPublicacion());
    }
}