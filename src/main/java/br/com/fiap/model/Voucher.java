package br.com.fiap.model;

public class Voucher {

    // Atributos
    private int    id;
    private int    usuarioId;
    private int    conversaoId;
    private String codigo;
    private double valorCredito;
    private String dataEmissao;
    private String dataValidade;
    private String status;
    private String operadorTransporte;

    // Construtor
    public Voucher(int id, int usuarioId, int conversaoId, String codigo,
                   double valorCredito, String dataEmissao, String dataValidade,
                   String operadorTransporte) {
        this.id                 = id;
        this.usuarioId          = usuarioId;
        this.conversaoId        = conversaoId;
        this.codigo             = codigo;
        this.valorCredito       = valorCredito;
        this.dataEmissao        = dataEmissao;
        this.dataValidade       = dataValidade;
        this.status             = "GERADO";
        this.operadorTransporte = operadorTransporte;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getConversaoId() { return conversaoId; }
    public void setConversaoId(int conversaoId) { this.conversaoId = conversaoId; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public double getValorCredito() { return valorCredito; }
    public void setValorCredito(double valorCredito) { this.valorCredito = valorCredito; }

    public String getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(String dataEmissao) { this.dataEmissao = dataEmissao; }

    public String getDataValidade() { return dataValidade; }
    public void setDataValidade(String dataValidade) { this.dataValidade = dataValidade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOperadorTransporte() { return operadorTransporte; }
    public void setOperadorTransporte(String operadorTransporte) { this.operadorTransporte = operadorTransporte; }

    // Métodos da classe
    public String utilizar() {
        if ("GERADO".equals(this.status)) {
            this.status = "UTILIZADO";
            return "Voucher " + codigo + " utilizado com sucesso!\n"
                    + "  Crédito aplicado : R$ " + String.format("%.2f", valorCredito) + "\n"
                    + "  Operador         : " + operadorTransporte;
        }
        return "Voucher não pode ser utilizado. Status atual: " + this.status;
    }

    public String exibirDetalhes() {
        return "=== Voucher de Transporte ===\n"
                + "  Código      : " + codigo + "\n"
                + "  Valor       : R$ " + String.format("%.2f", valorCredito) + "\n"
                + "  Operador    : " + operadorTransporte + "\n"
                + "  Emissão     : " + dataEmissao + "\n"
                + "  Validade    : " + dataValidade + "\n"
                + "  Status      : " + status + "\n"
                + "============================";
    }

    public String toString() {
        return "Voucher{"
                + "id=" + id
                + ", codigo='" + codigo + "'"
                + ", valorCredito=R$" + String.format("%.2f", valorCredito)
                + ", operador='" + operadorTransporte + "'"
                + ", status='" + status + "'"
                + ", validade='" + dataValidade + "'"
                + "}";
    }
}