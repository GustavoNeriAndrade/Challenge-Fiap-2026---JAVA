package br.com.fiap.dao;

import br.com.fiap.model.ConversaoPontos;
import br.com.fiap.conexao.ConexaoBancoDados;
import br.com.fiap.util.DataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Equipe EcoLoop
 * @version 1.0
 */
public class ConversaoPontosDAO {

    // Create
    public void inserir(ConversaoPontos conversao) throws SQLException {
        String sql = "INSERT INTO ConversaoPontos "
                + "(id_conversao, pontos_utilizados, valor_credito, data_conversao, status, Usuario_id_usuario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, conversao.getId());
            stmt.setInt(2, conversao.getPontosUtilizados());
            stmt.setDouble(3, conversao.getValorCredito());
            stmt.setDate(4, DataUtil.paraSqlDate(conversao.getDataConversao()));
            stmt.setString(5, conversao.getStatus());
            stmt.setInt(6, conversao.getUsuarioId());

            stmt.executeUpdate();
            System.out.println("Conversão de pontos inserida com sucesso (id " + conversao.getId() + ")");
        }
    }

    // Read
    public ConversaoPontos buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_conversao, pontos_utilizados, data_conversao, status, Usuario_id_usuario "
                + "FROM ConversaoPontos WHERE id_conversao = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarConversao(rs);
                }
                return null;
            }
        }
    }

    public List<ConversaoPontos> listarTodos() throws SQLException {
        String sql = "SELECT id_conversao, pontos_utilizados, data_conversao, status, Usuario_id_usuario "
                + "FROM ConversaoPontos ORDER BY id_conversao";

        List<ConversaoPontos> conversoes = new ArrayList<>();

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                conversoes.add(montarConversao(rs));
            }
        }
        return conversoes;
    }

    // Update
    public void atualizar(ConversaoPontos conversao) throws SQLException {
        String sql = "UPDATE ConversaoPontos SET pontos_utilizados = ?, valor_credito = ?, "
                + "status = ? WHERE id_conversao = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, conversao.getPontosUtilizados());
            stmt.setDouble(2, conversao.getValorCredito());
            stmt.setString(3, conversao.getStatus());
            stmt.setInt(4, conversao.getId());

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Conversão de pontos atualizada com sucesso (id " + conversao.getId() + ")"
                    : "Nenhuma conversão encontrada com id " + conversao.getId());
        }
    }

    // Delete
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM ConversaoPontos WHERE id_conversao = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Conversão de pontos removida com sucesso (id " + id + ")"
                    : "Nenhuma conversão encontrada com id " + id);
        }
    }

    // Métodos auxiliares

    public int gerarProximoId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_conversao), 0) + 1 AS proximo_id FROM ConversaoPontos";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt("proximo_id");
        }
    }


    private ConversaoPontos montarConversao(ResultSet rs) throws SQLException {
        ConversaoPontos conversao = new ConversaoPontos(
                rs.getInt("id_conversao"),
                rs.getInt("Usuario_id_usuario"),
                rs.getInt("pontos_utilizados"),
                DataUtil.paraTexto(rs.getDate("data_conversao"))
        );
        conversao.setStatus(rs.getString("status"));
        return conversao;
    }
}