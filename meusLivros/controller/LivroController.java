package meusLivros.controller;

import meusLivros.dao.LivroDAO;
import meusLivros.model.Livro;

import java.io.IOException;
import java.util.List;

public class LivroController {

    private final LivroDAO livroDAO;

    public LivroController() {
        this.livroDAO = new LivroDAO();
    }

    public void cadastrarLivro(String titulo, String autor, String isbn, String anoStr, String genero, boolean lido) throws Exception {
        if (titulo == null || titulo.trim().isEmpty() ||
                autor == null || autor.trim().isEmpty() ||
                isbn == null || isbn.trim().isEmpty() ||
                anoStr == null || anoStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Todos os campos obrigatórios devem ser preenchidos.");
        }

        int ano;
        try {
            ano = Integer.parseInt(anoStr.trim());
            if (ano <= 0 || ano > 2100) {
                throw new IllegalArgumentException("Ano de publicação inválido.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ano deve ser um valor numérico válido.");
        }

        Livro novoLivro = new Livro(titulo.trim(), autor.trim(), isbn.trim(), ano, genero, lido);
        livroDAO.salvar(novoLivro);
    }

    public List<Livro> carregarLivros() throws IOException {
        return livroDAO.listarTodos();
    }
}