package br.com.controleestoque.dao;

import br.com.controleestoque.model.Categoria;
import br.com.controleestoque.util.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

public class CategoriaDAO {

// Insere nova categoria
public void inserir(Categoria categoria) throws SQLException {
String sql = "INSERT INTO categoria (nome, tamanho, embalagem) VALUES (?, ?, ?)";

try (Connection conn = Conexao.getConnection();
PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

stmt.setString(1, categoria.getNome());
stmt.setString(2, categoria.getTamanho());
stmt.setString(3, categoria.getEmbalagem());
stmt.executeUpdate();

// Recuperar o ID gerado
try (ResultSet rs = stmt.getGeneratedKeys()) {
if (rs.next()) {
categoria.setId(rs.getInt(1));
}
}
}
}

// Atualiza categoria
public void atualizar(Categoria categoria) throws SQLException {
String sql = "UPDATE categoria SET nome = ?, tamanho = ?, embalagem = ? WHERE id = ?";
try (Connection conn = Conexao.getConnection();
PreparedStatement stmt = conn.prepareStatement(sql)) {

stmt.setString(1, categoria.getNome());
stmt.setString(2, categoria.getTamanho());
stmt.setString(3, categoria.getEmbalagem());
stmt.setInt(4, categoria.getId());
stmt.executeUpdate();
}
}

// Exclui categoria
public void excluir(int id) throws SQLException {
String sql = "DELETE FROM categoria WHERE id = ?";
try (Connection conn = Conexao.getConnection();
PreparedStatement stmt = conn.prepareStatement(sql)) {

stmt.setInt(1, id);
stmt.executeUpdate();
}
}

// Lista todas as categorias
public List<Categoria> listarTodos() throws SQLException {
List<Categoria> lista = new ArrayList<>();
String sql = "SELECT id, nome, tamanho, embalagem FROM categoria ORDER BY nome";

try (Connection conn = Conexao.getConnection();
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql)) {

while (rs.next()) {
Categoria c = new Categoria();
c.setId(rs.getInt("id"));
c.setNome(rs.getString("nome"));
c.setTamanho(rs.getString("tamanho"));
c.setEmbalagem(rs.getString("embalagem"));
lista.add(c);
}
}
return lista;
}

// Relatório: Quantidade de produtos por categoria
public List<CategoriaQuantidade> quantidadePorCategoria() throws SQLException {
List<CategoriaQuantidade> lista = new ArrayList<>();
String sql = "SELECT c.id, c.nome, COUNT(p.id) AS quantidade " +
"FROM categoria c " +
"LEFT JOIN produto p ON c.id = p.categoria_id " +
"GROUP BY c.id, c.nome " +
"ORDER BY c.nome";

try (Connection conn = Conexao.getConnection();
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql)) {

while (rs.next()) {
lista.add(new CategoriaQuantidade(
rs.getString("nome"),
rs.getInt("quantidade")
));
}
}
return lista;
}

// Classe interna para armazenar categoria + quantidade
public static class CategoriaQuantidade {
private final String categoria;
private final int quantidade;

public CategoriaQuantidade(String categoria, int quantidade) {
this.categoria = categoria;
this.quantidade = quantidade;
}

public String getCategoria() {
return categoria;
}

public int getQuantidade() {
return quantidade;
}
}
}
