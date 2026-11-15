
package Modelo;

public class libro {

    private int idLibro;
    private String titulo;
    private int idAutor;
    private int idEditorial;
    private int idGenero;
    private String isbn;
    private String foto;
    private int estatus;

    public int getIdLibro() {
        return idLibro;
    }
    
    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdAutor() {
        return idAutor; 
    }
    
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdEditorial() { 
        return idEditorial;
    }
    
    public void setIdEditorial(int idEditorial) { 
        this.idEditorial = idEditorial; 
    }

    public int getIdGenero() {
        return idGenero; 
    }
    
    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero; 
    }

    public String getIsbn() {
        return isbn; 
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getFoto() { 
        return foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getEstatus() {
        return estatus;
    }
    
    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
}