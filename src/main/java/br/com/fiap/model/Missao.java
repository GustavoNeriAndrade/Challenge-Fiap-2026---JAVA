package br.com.fiap.model;

public class Missao {

    // Atributos
    private int     id;
    private String  titulo;
    private String  descricao;
    private int     pontosRecompensa;
    private String  categoria;
    private boolean ativa;

    // Construtor
    public Missao(int id, String titulo, String descricao, int pontosRecompensa, String categoria) {
        this.id               = id;
        this.titulo           = titulo;
        this.descricao        = descricao;
        this.pontosRecompensa = pontosRecompensa;
        this.categoria        = categoria;
        this.ativa            = true;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getPontosRecompensa() { return pontosRecompensa; }
    public void setPontosRecompensa(int pontosRecompensa) { this.pontosRecompensa = pontosRecompensa; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }

    // Métodos da classe
    public String concluirMissao(Usuario usuario) {
        if (!ativa) {
            return "Missão '" + titulo + "' está inativa e não pode ser concluída.";
        }
        usuario.adicionarPontos(pontosRecompensa);
        return "Parabéns, " + usuario.getNome() + "! Missão '" + titulo
                + "' concluída. +" + pontosRecompensa + " pontos."
                + "\nSaldo atual: " + usuario.getPontos() + " pts.";
    }

    public String resumo() {
        return "[" + categoria + "] " + titulo + " | Recompensa: " + pontosRecompensa + " pts";
    }

    public String toString() {
        return "Missao{"
                + "id=" + id
                + ", titulo='" + titulo + "'"
                + ", categoria='" + categoria + "'"
                + ", pontosRecompensa=" + pontosRecompensa
                + ", ativa=" + ativa
                + "}";
    }
}