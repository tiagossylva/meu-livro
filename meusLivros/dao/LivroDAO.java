package meusLivros.dao;

import meusLivros.model.Livro;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private static final String CAMINHO_ARQUIVO = "/home/guest/Documentos/livros.csv";
    // Atualizado com a coluna Lido
    private static final String CABECALHO = "Titulo;Autor;ISBN;Ano;Genero;Lido";

    public LivroDAO() {
        inicializarArquivo();
    }

    private void inicializarArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            try {
                if (arquivo.getParentFile() != null) {
                    arquivo.getParentFile().mkdirs();
                }
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8))) {
                    writer.write(CABECALHO);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Erro ao inicializar arquivo de dados: " + e.getMessage());
            }
        }
    }

    public void salvar(Livro livro) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(CAMINHO_ARQUIVO, true), StandardCharsets.UTF_8))) {
            writer.write(livro.toCsv());
            writer.newLine();
        }
    }

    public List<Livro> listarTodos() throws IOException {
        List<Livro> livros = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) return livros;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {

            String linha = reader.readLine(); // Pula cabeçalho
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] colunas = parseCsvLine(linha);
                if (colunas.length >= 5) {
                    try {
                        String titulo = colunas[0];
                        String autor = colunas[1];
                        String isbn = colunas[2];
                        int ano = Integer.parseInt(colunas[3]);
                        String genero = colunas[4];
                        // Lê o booleano (se o arquivo antigo não tiver a coluna, assume false por padrão)
                        boolean lido = colunas.length >= 6 && Boolean.parseBoolean(colunas[5]);
                        livros.add(new Livro(titulo, autor, isbn, ano, genero, lido));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return livros;
    }

    private String[] parseCsvLine(String linha) {
        String[] partes = linha.split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < partes.length; i++) {
            partes[i] = partes[i].trim().replaceAll("^\"|\"$", "").replace("\"\"", "\"");
        }
        return partes;
    }
}