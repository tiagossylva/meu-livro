package meusLivros.view;

import meusLivros.controller.LivroController;
import meusLivros.model.Livro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CadastroLivrosView extends JFrame {

    private final LivroController controller;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtAno;
    private JComboBox<String> cbGenero;
    private JCheckBox chkLido; // Caixinha de marcação
    private DefaultTableModel modeloTabela;

    public CadastroLivrosView() {
        super("Gerenciador de Livros - Padrão MVC");
        this.controller = new LivroController();
        configurarJanela();
        inicializarComponentes();
        carregarTabela();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Cadastro de Livro"),
                new EmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painelForm.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 3;
        txtTitulo = new JTextField();
        painelForm.add(txtTitulo, gbc);

        // Autor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        painelForm.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.gridwidth = 3;
        txtAutor = new JTextField();
        painelForm.add(txtAutor, gbc);

        // ISBN
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        painelForm.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.5;
        txtIsbn = new JTextField();
        painelForm.add(txtIsbn, gbc);

        // Ano
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        painelForm.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0.5;
        txtAno = new JTextField();
        painelForm.add(txtAno, gbc);

        // Gênero
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painelForm.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.5;
        String[] generos = {"Ficção Científica", "Fantasia", "Romance", "Terror", "Técnico", "Biografia", "Outro"};
        cbGenero = new JComboBox<>(generos);
        painelForm.add(cbGenero, gbc);

        // Checkbox "Lido"
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0.5; gbc.gridwidth = 2;
        chkLido = new JCheckBox("Livro já foi lido");
        painelForm.add(chkLido, gbc);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnLimpar = new JButton("Limpar");
        JButton btnSalvar = new JButton("Salvar no Arquivo");
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSalvar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        gbc.insets = new Insets(10, 5, 0, 5);
        painelForm.add(painelBotoes, gbc);

        // Tabela com a nova coluna "Status"
        String[] colunas = {"Título", "Autor", "ISBN", "Ano", "Gênero", "Lido?"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tabela = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Livros Armazenados"));

        // Eventos
        btnSalvar.addActionListener(this::acaoCadastrar);
        btnLimpar.addActionListener(e -> limparFormulario());

        add(painelForm, BorderLayout.NORTH);
        add(scrollTabela, BorderLayout.CENTER);
    }

    private void carregarTabela() {
        try {
            modeloTabela.setRowCount(0);
            List<Livro> livros = controller.carregarLivros();
            for (Livro l : livros) {
                modeloTabela.addRow(new Object[]{
                        l.getTitulo(),
                        l.getAutor(),
                        l.getIsbn(),
                        l.getAno(),
                        l.getGenero(),
                        l.isLido() ? "Sim" : "Não"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoCadastrar(ActionEvent e) {
        try {
            controller.cadastrarLivro(
                    txtTitulo.getText(),
                    txtAutor.getText(),
                    txtIsbn.getText(),
                    txtAno.getText(),
                    (String) cbGenero.getSelectedItem(),
                    chkLido.isSelected() // Captura o estado da caixinha (true/false)
            );

            JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarTabela();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro de gravação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtAno.setText("");
        cbGenero.setSelectedIndex(0);
        chkLido.setSelected(false);
        txtTitulo.requestFocus();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new CadastroLivrosView().setVisible(true));
    }
}