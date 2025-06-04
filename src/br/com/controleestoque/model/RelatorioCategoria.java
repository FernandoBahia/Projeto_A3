package br.com.controleestoque.model;

public class RelatorioCategoria {
    private String nomeCategoria;
    private int quantidade;

    public RelatorioCategoria(String nomeCategoria, int quantidade) {
        this.nomeCategoria = nomeCategoria;
        this.quantidade = quantidade;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
