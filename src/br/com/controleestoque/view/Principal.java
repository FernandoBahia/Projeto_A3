package br.com.controleestoque.view;

import br.com.controleestoque.dao.CategoriaDAO;
import br.com.controleestoque.dao.ProdutoDAO;
import br.com.controleestoque.model.Categoria;
import br.com.controleestoque.model.Produto;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Principal extends JFrame {

    private ProdutoDAO produtoDAO = new ProdutoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    private JTable tabelaProdutos;
    private JTable tabelaCategorias;

    private DefaultTableModel modelProdutos;
    private DefaultTableModel modelCategorias;

    public Principal() {
        setTitle("Sistema de Controle de Estoque");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();

        atualizarTabelaProdutos();
        atualizarTabelaCategorias();
    }

    private void initComponents() {
        JPanel painelBotoes = new JPanel();

        JButton btnProdutos = new JButton("Produtos");
        btnProdutos.addActionListener(e -> abrirFormProduto());

        JButton btnCategorias = new JButton("Categorias");
        btnCategorias.addActionListener(e -> abrirFormCategoria());

        JButton btnRelatorios = new JButton("Relatórios");
        btnRelatorios.addActionListener(e -> mostrarRelatorios());

        JButton btnExcluirProduto = new JButton("Excluir Produto");
        btnExcluirProduto.addActionListener(e -> excluirProduto());

        painelBotoes.add(btnProdutos);
        painelBotoes.add(btnCategorias);
        painelBotoes.add(btnRelatorios);
        painelBotoes.add(btnExcluirProduto);

        // Tabela de produtos (com ID oculto na coluna 0)
        modelProdutos = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Unidade", "Categoria"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(modelProdutos);
        tabelaProdutos.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer());
        tabelaProdutos.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaProdutos.getColumnModel().getColumn(0).setMaxWidth(0);

        // Tabela de categorias
        modelCategorias = new DefaultTableModel(new Object[]{"Nome", "Tamanho", "Embalagem"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCategorias = new JTable(modelCategorias);

        // Painel principal com abas
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Produtos", new JScrollPane(tabelaProdutos));
        abas.addTab("Categorias", new JScrollPane(tabelaCategorias));

        add(painelBotoes, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }

    public void atualizarTabelaProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listar();
            modelProdutos.setRowCount(0);
            for (Produto p : produtos) {
                modelProdutos.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getPrecoUnitario(),
                    p.getUnidade(),
                    p.getCategoria().getNome()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    public void atualizarTabelaCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.listar();
            modelCategorias.setRowCount(0);
            for (Categoria c : categorias) {
                modelCategorias.addRow(new Object[]{
                    c.getNome(),
                    c.getTamanho(),
                    c.getEmbalagem()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void abrirFormProduto() {
        FormProduto form = new FormProduto(this);
        form.setVisible(true);
    }

    private void abrirFormCategoria() {
        FormCategoria form = new FormCategoria(this);
        form.setVisible(true);
    }

    private void mostrarRelatorios() {
        FormRelatorio formRelatorio = new FormRelatorio();
        formRelatorio.setVisible(true);
    }

    private void excluirProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
            return;
        }

        String nomeProduto = (String) tabelaProdutos.getValueAt(linhaSelecionada, 1);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir o produto \"" + nomeProduto + "\"?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int idProduto = (int) tabelaProdutos.getValueAt(linhaSelecionada, 0);
            try {
                produtoDAO.excluir(idProduto);
                atualizarTabelaProdutos();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static class CurrencyRenderer extends DefaultTableCellRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Number) {
                value = NumberFormat.getCurrencyInstance().format(value);
            }
            super.setValue(value);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Principal().setVisible(true));
    }
}
