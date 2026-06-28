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

        assertEquals("978-3-16-148410-0", libro.getIsbn());
        assertEquals("El Quijote", libro.getTitulo());
        assertEquals("Miguel de Cervantes", libro.getAutor());
        assertEquals(1605, libro.getAnioPublicacion());
        assertTrue(libro.getDisponible());
    }

    @Test
    void testCrearLibroInvalido(){
        IllegalArgumentException ex_isbn_null = assertThrows(
            IllegalArgumentException.class,
            () -> new Libro( 
                null, 
                "hola", 
                "aaron", 
                3000
            )
        );
        assertEquals("'isbn' argument cannot be null", ex_isbn_null.getMessage());

        IllegalArgumentException ex_titulo_null = assertThrows(
            IllegalArgumentException.class,
            () -> new Libro(
                "123123-123-123-12-3",
                null,
                "aaron",
                1231)
        );
        assertEquals("'titulo' argument cannot be null", ex_titulo_null.getMessage());
    };
}