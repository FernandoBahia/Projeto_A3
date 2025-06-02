package br.com.controleestoque.view;

import br.com.controleestoque.dao.CategoriaDAO;
import br.com.controleestoque.model.Categoria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class FormCategoria extends JDialog {

private final Principal principal;
private Categoria categoria;
private JTextField txtNome;
private JComboBox<String> comboBoxTamanho;
private JComboBox<String> comboBoxEmbalagem;

public FormCategoria(Principal principal) {
this(principal, null);
}

public FormCategoria(Principal principal, Categoria categoria) {
super(principal, true);
this.principal = principal;
this.categoria = categoria;
initComponents();
if (categoria != null) {
preencherFormulario();
}
}

private void initComponents() {
setTitle(categoria == null ? "Nova Categoria" : "Editar Categoria");
setSize(400, 250);
setLocationRelativeTo(null);
setDefaultCloseOperation(DISPOSE_ON_CLOSE);

JPanel painel = new JPanel(new GridBagLayout());
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(8, 8, 8, 8);
gbc.fill = GridBagConstraints.HORIZONTAL;

// Linha 1 - Nome
gbc.gridx = 0;
gbc.gridy = 0;
painel.add(new JLabel("Nome:"), gbc);

gbc.gridx = 1;
txtNome = new JTextField(20);
painel.add(txtNome, gbc);

// Linha 2 - Tamanho
gbc.gridx = 0;
gbc.gridy = 1;
painel.add(new JLabel("Tamanho:"), gbc);

gbc.gridx = 1;
comboBoxTamanho = new JComboBox<>(new String[]{"Pequeno", "Médio", "Grande"});
painel.add(comboBoxTamanho, gbc);

// Linha 3 - Embalagem
gbc.gridx = 0;
gbc.gridy = 2;
painel.add(new JLabel("Embalagem:"), gbc);

gbc.gridx = 1;
comboBoxEmbalagem = new JComboBox<>(new String[]{"Lata", "Vidro", "Plástico"});
painel.add(comboBoxEmbalagem, gbc);

// Linha 4 - Botões
JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
JButton btnSalvar = new JButton("Salvar");
btnSalvar.addActionListener(this::salvarCategoria);
painelBotoes.add(btnSalvar);

JButton btnCancelar = new JButton("Cancelar");
btnCancelar.addActionListener(e -> dispose());
painelBotoes.add(btnCancelar);

gbc.gridx = 0;
gbc.gridy = 3;
gbc.gridwidth = 2;
painel.add(painelBotoes, gbc);

add(painel);
}

private void preencherFormulario() {
txtNome.setText(categoria.getNome());
comboBoxTamanho.setSelectedItem(categoria.getTamanho());
comboBoxEmbalagem.setSelectedItem(categoria.getEmbalagem());
}

private void salvarCategoria(ActionEvent e) {
String nome = txtNome.getText().trim();
String tamanho = (String) comboBoxTamanho.getSelectedItem();
String embalagem = (String) comboBoxEmbalagem.getSelectedItem();

if (nome.isEmpty()) {
JOptionPane.showMessageDialog(this, "Por favor, preencha o nome da categoria.");
return;
}

if (categoria == null) {
categoria = new Categoria();
}

categoria.setNome(nome);
categoria.setTamanho(tamanho);
categoria.setEmbalagem(embalagem);

try {
CategoriaDAO dao = new CategoriaDAO();
dao.salvar(categoria);
JOptionPane.showMessageDialog(this, "Categoria salva com sucesso!");
dispose();
principal.atualizarTabelaCategorias(); // <- Atualiza a tabela na tela principal
} catch (SQLException ex) {
ex.printStackTrace();
JOptionPane.showMessageDialog(this, "Erro ao salvar categoria:\n" + ex.getMessage());
}
}
}
