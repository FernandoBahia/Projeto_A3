package br.com.controleestoque.view;
import br.com.controleestoque.dao.RelatorioDAO;
import br.com.controleestoque.model.RelatorioCategoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormRelatorio extends JFrame {
    private JTable tabelaRelatorio;
    private RelatorioDAO relatorioDAO;

    public FormRelatorio() {
        relatorioDAO = new RelatorioDAO();
        
        setTitle("Relatório de Produtos por Categoria");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] colunas = {"Categoria", "Quantidade"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        tabelaRelatorio = new JTable(model);
        add(new JScrollPane(tabelaRelatorio), BorderLayout.CENTER);

        carregarDados();
    }

    private void carregarDados() {
        List<RelatorioCategoria> relatorios = relatorioDAO.obterQuantidadeProdutosPorCategoria();
        DefaultTableModel model = (DefaultTableModel) tabelaRelatorio.getModel();
        
        for (RelatorioCategoria r : relatorios) {
            model.addRow(new Object[]{r.getNomeCategoria(), r.getQuantidade()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormRelatorio().setVisible(true));
    }
}
