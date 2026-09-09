package br.com.fiap.bean;

public class ImpactoAmbiental {
    /** Emissão média de CO2 (kg) por km em carro particular */
    public static final double EMISSAO_CARRO_KG_KM  = 0.21;

    /** Emissão média de CO2 (kg) por km em transporte público por passageiro */
    public static final double EMISSAO_ONIBUS_KG_KM = 0.04;

    /** Economia de CO2 (kg) por km ao trocar carro por transporte público */
    public static final double ECONOMIA_CO2_KG_KM   = EMISSAO_CARRO_KG_KM - EMISSAO_ONIBUS_KG_KM;

    // Atributos
    private int    id;
    private int    usuarioId;
    private double kmPercorridos;
    private double co2Economizado;
    private int    arvoresEquivalentes;
    private String dataRegistro;

    // Construtor
    public ImpactoAmbiental(int id, int usuarioId, double kmPercorridos, String dataRegistro) {
        this.id                  = id;
        this.usuarioId           = usuarioId;
        this.kmPercorridos       = kmPercorridos;
        this.dataRegistro        = dataRegistro;
        this.co2Economizado      = calcularCO2Economizado(kmPercorridos);
        this.arvoresEquivalentes = calcularArvoresEquivalentes(this.co2Economizado);
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public double getKmPercorridos() { return kmPercorridos; }
    public void setKmPercorridos(double kmPercorridos) {
        this.kmPercorridos       = kmPercorridos;
        this.co2Economizado      = calcularCO2Economizado(kmPercorridos);
        this.arvoresEquivalentes = calcularArvoresEquivalentes(this.co2Economizado);
    }

    public double getCo2Economizado() { return co2Economizado; }

    public int getArvoresEquivalentes() { return arvoresEquivalentes; }

    public String getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(String dataRegistro) { this.dataRegistro = dataRegistro; }

    // Métodos da classe
    public static double calcularCO2Economizado(double km) {
        return km * ECONOMIA_CO2_KG_KM;
    }

    public static int calcularArvoresEquivalentes(double co2Kg) {
        return (int) (co2Kg / 21.0);
    }

    public String gerarRelatorio() {
        return "=== Relatório de Impacto Ambiental ===\n"
                + "  Usuário ID      : " + usuarioId + "\n"
                + "  Data            : " + dataRegistro + "\n"
                + "  KM percorridos  : " + String.format("%.1f", kmPercorridos) + " km\n"
                + "  CO2 economizado : " + String.format("%.2f", co2Economizado) + " kg\n"
                + "  Árvores equiv.  : " + arvoresEquivalentes + " árvore(s)\n"
                + "======================================";
    }

    /**
     * Retorna uma representação textual do impacto ambiental.
     *
     * @return String com os dados do registro
     */
    public String toString() {
        return "ImpactoAmbiental{"
                + "id=" + id
                + ", usuarioId=" + usuarioId
                + ", kmPercorridos=" + kmPercorridos
                + ", co2Economizado=" + String.format("%.2f", co2Economizado) + "kg"
                + ", arvoresEquivalentes=" + arvoresEquivalentes
                + ", dataRegistro='" + dataRegistro + "'"
                + "}";
    }
}