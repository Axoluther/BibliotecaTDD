import java.util.Objects;

public class Libro {
    // req minimos
    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;

    // extra
    private boolean disponible = true;

    public Libro(String isbn, String titulo, String autor, int anioPublicacion){
        this.isbn = Objects.requireNonNull(isbn,"'isbn' argument cannot be null");
        this.titulo = Objects.requireNonNull(titulo, "'titulo' argument cannot be null");
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
    };

    public String getIsbn() {
        return this.isbn;
    };

    public String getTitulo(){
        return this.titulo;
    };
    
    public String getAutor(){
        return this.autor;
    };

    public int getAnioPublicacion(){
        return this.anioPublicacion;
    };

    public boolean getDisponible(){
        return this.disponible;
    };

    public void setPedido(){
        this.disponible = false;
    }
    public void setDisponible(){
        this.disponible = true;
    }
}
