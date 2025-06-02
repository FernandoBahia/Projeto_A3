package src.br.com.controleestoque.model;

public class Categoria {
    private int id;
    private String nome;
    private String tamanho;
    private String embalagem;

    // Construtor
    public Categoria() {}

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
    public String getEmbalagem() { return embalagem; }
    public void setEmbalagem(String embalagem) { this.embalagem = embalagem; }
    
    @Override
    public String toString() {
        return nome;
    }
}