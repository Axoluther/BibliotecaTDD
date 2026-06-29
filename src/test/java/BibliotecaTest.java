import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BibliotecaTest {

    @Test
    void registrarLibroValido(){

        Libro libro = new Libro(
            "1234",
            "fantasias de don Fede",
            "Conodido de apellido Carrera",
            1930
        );

        Biblioteca biblio = new Biblioteca();
        biblio.registrarLibro(libro);

        // lanza error si no es presente .isPresent()
        Libro libro_found = biblio.buscarPorIsbn("1234").orElseThrow( () -> new LibroNoEncontradoException("1234"));
        assertEquals(libro.getIsbn(), libro_found.getIsbn());
    };

    @Test
    void registrarLibroInvalido(){
        // Libro libro_null = null;
    }

    @Test
    void testLibrosDuplicadosIsbn(){
        // TODO: Implementar Biblioteca para manejar los ISBN y duplicados
    };
    
    @Test
    void test(){};
}
