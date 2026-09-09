package br.com.fiap.model;

public class Usuario {

    // Atributos
    private int    id;
    private String nome;
    private String email;
    private String senha;
    private int    pontos;
    private String dataCadastro;

    // Construtor
    public Usuario(int id, String nome, String email, String senha, int pontos, String dataCadastro) {
        this.id           = id;
        this.nome         = nome;
        this.email        = email;
        this.senha        = senha;
        this.pontos       = pontos;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public int getPontos() { return pontos; }
    public void setPontos(int pontos) { this.pontos = pontos; }

    public String getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(String dataCadastro) { this.dataCadastro = dataCadastro; }

    // Métodos da classe
    public void adicionarPontos(int quantidade) {
        if (quantidade > 0) {
            this.pontos += quantidade;
        }
    }

    public boolean debitarPontos(int quantidade) {
        if (quantidade > 0 && this.pontos >= quantidade) {
            this.pontos -= quantidade;
            return true;
        }
        return false;
    }

    public boolean validarSenha(String senhaInformada) {
        return this.senha.equals(senhaInformada);
    }

    public String toString() {
        return "Usuario{"
                + "id=" + id
                + ", nome='" + nome + "'"
                + ", email='" + email + "'"
                + ", senha='****'"
                + ", pontos=" + pontos
                + ", dataCadastro='" + dataCadastro + "'"
                + "}";
    }
}