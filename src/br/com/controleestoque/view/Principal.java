package br.com.controleestoque.view;

import br.com.controleestoque.dao.CategoriaDAO;
import br.com.controleestoque.dao.ProdutoDAO;
import br.com.controleestoque.model.Categoria;
import br.com.controleestoque.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;

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

painelBotoes.add(btnProdutos);
painelBotoes.add(btnCategorias);
painelBotoes.add(btnRelatorios);

// Criar tabelas
modelProdutos = new DefaultTableModel(new Object[]{"Nome", "Preço", "Unidade", "Categoria"}, 0);
tabelaProdutos = new JTable(modelProdutos);
tabelaProdutos.getColumnModel().getColumn(1).setCellRenderer(new CurrencyRenderer());

modelCategorias = new DefaultTableModel(new Object[]{"Nome", "Tamanho", "Embalagem"}, 0);
tabelaCategorias = new JTable(modelCategorias);

// Painel principal com abas para Produtos e Categorias
JTabbedPane abas = new JTabbedPane();
abas.addTab("Produtos", new JScrollPane(tabelaProdutos));
abas.addTab("Categorias", new JScrollPane(tabelaCategorias));

add(painelBotoes, BorderLayout.NORTH);
add(abas, BorderLayout.CENTER);
}

// Método chamado pelo FormProduto para atualizar tabela na tela principal
public void atualizarTabelaProdutos() {
try {
List<Produto> produtos = produtoDAO.listar();
modelProdutos.setRowCount(0);
for (Produto p : produtos) {
modelProdutos.addRow(new Object[]{
p.getNome(),
NumberFormat.getCurrencyInstance().format(p.getPrecoUnitario()),
p.getUnidade(),
p.getCategoria().getNome()
});
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
}
}

// Método chamado pelo FormCategoria para atualizar tabela na tela principal
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
// Relatórios conforme implementado anteriormente (omitido aqui para foco)
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