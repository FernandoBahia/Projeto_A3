package br.com.controleestoque.dao;

import br.com.controleestoque.model.Produto;
import br.com.controleestoque.model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco_unitario, unidade, quantidade_estoque, quantidade_minima, quantidade_maxima, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome=?, preco_unitario=?, unidade=?, quantidade_estoque=?, quantidade_minima=?, quantidade_maxima=?, categoria_id=? WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPrecoUnitario());
            stmt.setString(3, produto.getUnidade());
            stmt.setInt(4, produto.getQuantidadeEstoque());
            stmt.setInt(5, produto.getQuantidadeMinima());
            stmt.setInt(6, produto.getQuantidadeMaxima());
            stmt.setInt(7, produto.getCategoria().getId());
            stmt.setInt(8, produto.getId());
            stmt.executeUpdate();
        }
    }

    // Método salvar para inserir ou atualizar conforme id
    public void salvar(Produto produto) throws SQLException {
        if (produto.getId() == 0) {
            inserir(produto);
        } else {
            atualizar(produto);
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Produto> listar() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.*, c.nome AS categoria_nome FROM produto p LEFT JOIN categoria c ON p.categoria_id = c.id";
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setPrecoUnitario(rs.getDouble("preco_unitario"));
                p.setUnidade(rs.getString("unidade"));
                p.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                p.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                p.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));

                Categoria c = new Categoria();
                c.setId(rs.getInt("categoria_id"));
                c.setNome(rs.getString("categoria_nome"));
                p.setCategoria(c);

                produtos.add(p);
            }
        }
        return produtos;
    }

    public void reajustarPrecos(double percentual) throws SQLException {
        String sql = "UPDATE produto SET preco_unitario = preco_unitario * ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, 1 + (percentual / 100));
            stmt.executeUpdate();
        }
    }

    // Relatórios
    public List<Produto> produtosAbaixoMinimo() throws SQLException {
        String sql = "SELECT * FROM produto WHERE quantidade_estoque < quantidade_minima";
        return buscarPorQuery(sql);
    }

    public List<Produto> produtosAcimaMaximo() throws SQLException {
        String sql = "SELECT * FROM produto WHERE quantidade_estoque > quantidade_maxima";
        return buscarPorQuery(sql);
    }

    private List<Produto> buscarPorQuery(String sql) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                p.setQuantidadeMinima(rs.getInt("quantidade_minima"));
                p.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
                produtos.add(p);
            }
        }
        return produtos;
    }
}
