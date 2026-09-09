package br.com.fiap.dao;

import br.com.fiap.bean.Voucher;
import br.com.fiap.conexao.ConexaoBancoDados;
import br.com.fiap.util.DataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {

    // Create
    public void inserir(Voucher voucher) throws SQLException {
        String sql = "INSERT INTO Voucher "
                + "(id_voucher, codigo_voucher, valor_credito_voucher, data_emissao, "
                + "data_validade, status_voucher, nome_operador_transporte, ConversaoPontos_id_conversao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, voucher.getId());
            stmt.setString(2, voucher.getCodigo());
            stmt.setDouble(3, voucher.getValorCredito());
            stmt.setDate(4, DataUtil.paraSqlDate(voucher.getDataEmissao()));
            stmt.setDate(5, DataUtil.paraSqlDate(voucher.getDataValidade()));
            stmt.setString(6, voucher.getStatus());
            stmt.setString(7, voucher.getOperadorTransporte());
            stmt.setInt(8, voucher.getConversaoId());

            stmt.executeUpdate();
            System.out.println("Voucher inserido com sucesso: " + voucher.getCodigo());
        }
    }

    // Read
    public Voucher buscarPorId(int id) throws SQLException {
        String sql = "SELECT v.id_voucher, v.codigo_voucher, v.valor_credito_voucher, "
                + "v.data_emissao, v.data_validade, v.status_voucher, v.nome_operador_transporte, "
                + "v.ConversaoPontos_id_conversao, c.Usuario_id_usuario "
                + "FROM Voucher v "
                + "JOIN ConversaoPontos c ON v.ConversaoPontos_id_conversao = c.id_conversao "
                + "WHERE v.id_voucher = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarVoucher(rs);
                }
                return null;
            }
        }
    }

    public List<Voucher> listarTodos() throws SQLException {
        String sql = "SELECT v.id_voucher, v.codigo_voucher, v.valor_credito_voucher, "
                + "v.data_emissao, v.data_validade, v.status_voucher, v.nome_operador_transporte, "
                + "v.ConversaoPontos_id_conversao, c.Usuario_id_usuario "
                + "FROM Voucher v "
                + "JOIN ConversaoPontos c ON v.ConversaoPontos_id_conversao = c.id_conversao "
                + "ORDER BY v.id_voucher";

        List<Voucher> vouchers = new ArrayList<>();

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vouchers.add(montarVoucher(rs));
            }
        }
        return vouchers;
    }

    // Update
    public void atualizar(Voucher voucher) throws SQLException {
        String sql = "UPDATE Voucher SET valor_credito_voucher = ?, data_validade = ?, "
                + "status_voucher = ?, nome_operador_transporte = ? WHERE id_voucher = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setDouble(1, voucher.getValorCredito());
            stmt.setDate(2, DataUtil.paraSqlDate(voucher.getDataValidade()));
            stmt.setString(3, voucher.getStatus());
            stmt.setString(4, voucher.getOperadorTransporte());
            stmt.setInt(5, voucher.getId());

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Voucher atualizado com sucesso: " + voucher.getCodigo()
                    : "Nenhum voucher encontrado com id " + voucher.getId());
        }
    }

    // Delete
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Voucher WHERE id_voucher = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Voucher removido com sucesso (id " + id + ")"
                    : "Nenhum voucher encontrado com id " + id);
        }
    }

    // Métodos auxiliares
    public int gerarProximoId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_voucher), 0) + 1 AS proximo_id FROM Voucher";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt("proximo_id");
        }
    }

    private Voucher montarVoucher(ResultSet rs) throws SQLException {
        return new Voucher(
                rs.getInt("id_voucher"),
                rs.getInt("Usuario_id_usuario"),
                rs.getInt("ConversaoPontos_id_conversao"),
                rs.getString("codigo_voucher"),
                rs.getDouble("valor_credito_voucher"),
                DataUtil.paraTexto(rs.getDate("data_emissao")),
                DataUtil.paraTexto(rs.getDate("data_validade")),
                rs.getString("nome_operador_transporte")
        );
    }
}