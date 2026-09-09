package br.com.fiap.dao;

import br.com.fiap.model.Missao;
import br.com.fiap.conexao.ConexaoBancoDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de CRUD
 * da entidade Missao no banco de dados Oracle.
 *
 * Observação: o atributo booleano "ativa" da classe bean é armazenado
 * no banco como CHAR(1) ('S' ou 'N'), então essa conversão acontece
 * dentro dos métodos inserir/atualizar/montarMissao.
 *
 * @author Equipe EcoLoop
 * @version 1.0
 */
public class MissaoDAO {

    // Create
    public void inserir(Missao missao) throws SQLException {
        String sql = "INSERT INTO Missao "
                + "(id_missao, titulo_missao, descricao_missao, pontos_recompensa, categoria_missao, ativa) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, missao.getId());
            stmt.setString(2, missao.getTitulo());
            stmt.setString(3, missao.getDescricao());
            stmt.setInt(4, missao.getPontosRecompensa());
            stmt.setString(5, missao.getCategoria());
            stmt.setString(6, missao.isAtiva() ? "S" : "N");

            stmt.executeUpdate();
            System.out.println("Missão inserida com sucesso: " + missao.getTitulo());
        }
    }

    // READ
    public Missao buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_missao, titulo_missao, descricao_missao, "
                + "pontos_recompensa, categoria_missao, ativa FROM Missao WHERE id_missao = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarMissao(rs);
                }
                return null;
            }
        }
    }

    public List<Missao> listarTodos() throws SQLException {
        String sql = "SELECT id_missao, titulo_missao, descricao_missao, "
                + "pontos_recompensa, categoria_missao, ativa FROM Missao ORDER BY id_missao";

        List<Missao> missoes = new ArrayList<>();

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                missoes.add(montarMissao(rs));
            }
        }
        return missoes;
    }

    // UPDATE
    public void atualizar(Missao missao) throws SQLException {
        String sql = "UPDATE Missao SET titulo_missao = ?, descricao_missao = ?, "
                + "pontos_recompensa = ?, categoria_missao = ?, ativa = ? WHERE id_missao = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, missao.getTitulo());
            stmt.setString(2, missao.getDescricao());
            stmt.setInt(3, missao.getPontosRecompensa());
            stmt.setString(4, missao.getCategoria());
            stmt.setString(5, missao.isAtiva() ? "S" : "N");
            stmt.setInt(6, missao.getId());

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Missão atualizada com sucesso: " + missao.getTitulo()
                    : "Nenhuma missão encontrada com id " + missao.getId());
        }
    }

    // DELETE
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Missao WHERE id_missao = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Missão removida com sucesso (id " + id + ")"
                    : "Nenhuma missão encontrada com id " + id);
        }
    }

    // Métodos auxiliares
    public int gerarProximoId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_missao), 0) + 1 AS proximo_id FROM Missao";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt("proximo_id");
        }
    }

    private Missao montarMissao(ResultSet rs) throws SQLException {
        Missao missao = new Missao(
                rs.getInt("id_missao"),
                rs.getString("titulo_missao"),
                rs.getString("descricao_missao"),
                rs.getInt("pontos_recompensa"),
                rs.getString("categoria_missao")
        );
        missao.setAtiva("S".equals(rs.getString("ativa")));
        return missao;
    }
}