package br.com.controleestoque.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import br.com.controleestoque.model.RelatorioCategoria;

public class RelatorioDAO {

    public List<RelatorioCategoria> obterQuantidadeProdutosPorCategoria() {
        List<RelatorioCategoria> relatorio = new ArrayList<>();
        String sql = "SELECT c.nome AS categoria, COUNT(p.id) AS quantidade " +
                     "FROM categoria c LEFT JOIN produto p ON c.id = p.categoria_id " +
                     "GROUP BY c.nome";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nomeCategoria = rs.getString("categoria");
                int quantidade = rs.getInt("quantidade");
                relatorio.add(new RelatorioCategoria(nomeCategoria, quantidade));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return relatorio;
    }
}
