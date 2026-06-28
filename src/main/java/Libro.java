public class Libro {
    // req minimos
    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;

    // extra
    private boolean disponible = true;

    public Libro(String isbn, String titulo, String autor, int anioPublicacion){
        if (isbn == null) {
            throw new IllegalArgumentException("'isbn' argument cannot be null");
        }
        if (titulo == null){
            throw new IllegalArgumentException("'titulo' argument cannot be null");
        }
        this.isbn = isbn;
        this.titulo = titulo;
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
}
