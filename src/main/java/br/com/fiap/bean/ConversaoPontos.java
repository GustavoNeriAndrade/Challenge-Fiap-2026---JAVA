package br.com.fiap.bean;

public class ConversaoPontos {

    // Constante de conversão
    public static final int PONTOS_POR_REAL = 100;

    // Atributos
    private int    id;
    private int    usuarioId;
    private int    pontosUtilizados;
    private double valorCredito;
    private String dataConversao;
    private String status;

    // Construtor
    public ConversaoPontos(int id, int usuarioId, int pontosUtilizados, String dataConversao) {
        this.id               = id;
        this.usuarioId        = usuarioId;
        this.pontosUtilizados = pontosUtilizados;
        this.valorCredito     = calcularCredito(pontosUtilizados);
        this.dataConversao    = dataConversao;
        this.status           = "PENDENTE";
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getPontosUtilizados() { return pontosUtilizados; }
    public void setPontosUtilizados(int pontosUtilizados) {
        this.pontosUtilizados = pontosUtilizados;
        this.valorCredito     = calcularCredito(pontosUtilizados);
    }

    public double getValorCredito() { return valorCredito; }

    public String getDataConversao() { return dataConversao; }
    public void setDataConversao(String dataConversao) { this.dataConversao = dataConversao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Métodos da classe
    public static double calcularCredito(int pontos) {
        return (double) pontos / PONTOS_POR_REAL;
    }

    public String aprovar(Usuario usuario) {
        if (!"PENDENTE".equals(this.status)) {
            return "Conversão já processada. Status atual: " + this.status;
        }
        boolean debitado = usuario.debitarPontos(pontosUtilizados);
        if (debitado) {
            this.status = "APROVADO";
            return "Conversão APROVADA!\n"
                    + "  Pontos debitados : " + pontosUtilizados + " pts\n"
                    + "  Crédito gerado   : R$ " + String.format("%.2f", valorCredito) + "\n"
                    + "  Saldo restante   : " + usuario.getPontos() + " pts";
        } else {
            this.status = "CANCELADO";
            return "Conversão CANCELADA: saldo insuficiente.\n"
                    + "  Necessário : " + pontosUtilizados + " pts\n"
                    + "  Disponível : " + usuario.getPontos() + " pts";
        }
    }

    public String toString() {
        return "ConversaoPontos{"
                + "id=" + id
                + ", usuarioId=" + usuarioId
                + ", pontosUtilizados=" + pontosUtilizados
                + ", valorCredito=R$" + String.format("%.2f", valorCredito)
                + ", data='" + dataConversao + "'"
                + ", status='" + status + "'"
                + "}";
    }
}