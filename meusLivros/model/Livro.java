package meusLivros.model;

public class Livro {
    private String titulo;
    private String autor;
    private String isbn;
    private int ano;
    private String genero;
    private boolean lido; // Novo campo

    public Livro(String titulo, String autor, String isbn, int ano, String genero, boolean lido) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.ano = ano;
        this.genero = genero;
        this.lido = lido;
    }

    public String toCsv() {
        return String.format("\"%s\";\"%s\";\"%s\";%d;\"%s\";%b",
                titulo.replace("\"", "\"\""),
                autor.replace("\"", "\"\""),
                isbn.replace("\"", "\"\""),
                ano,
                genero.replace("\"", "\"\""),
                lido);
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getAno() { return ano; }
    public String getGenero() { return genero; }
    public boolean isLido() { return lido; }
}