package br.com.fiap.dao;

import br.com.fiap.bean.ConversaoPontos;
import br.com.fiap.conexao.ConexaoBancoDados;
import br.com.fiap.util.DataUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de CRUD
 * da entidade ConversaoPontos no banco de dados Oracle.
 *
 * @author Equipe EcoLoop
 * @version 1.0
 */
public class ConversaoPontosDAO {

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    /**
     * Insere uma nova conversão de pontos no banco de dados.
     *
     * @param conversao conversão a ser inserida (o id já deve estar definido)
     * @throws SQLException se ocorrer erro na operação
     */
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

    // -------------------------------------------------------------------------
    // READ
    // -------------------------------------------------------------------------
    /**
     * Busca uma conversão de pontos pelo id.
     *
     * @param id identificador da conversão
     * @return a conversão encontrada, ou null se não existir
     * @throws SQLException se ocorrer erro na operação
     */
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

    /**
     * Lista todas as conversões de pontos cadastradas no banco.
     *
     * @return lista de conversões (vazia se não houver nenhuma)
     * @throws SQLException se ocorrer erro na operação
     */
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

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------
    /**
     * Atualiza os dados mutáveis de uma conversão já existente (identificada
     * pelo id) — tipicamente usado após aprovar() ou cancelar() a conversão
     * na camada de negócio.
     *
     * @param conversao conversão com os dados atualizados
     * @throws SQLException se ocorrer erro na operação
     */
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

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    /**
     * Remove uma conversão de pontos do banco pelo id.
     *
     * @param id identificador da conversão a ser removida
     * @throws SQLException se ocorrer erro na operação
     */
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

    // -------------------------------------------------------------------------
    // Métodos auxiliares
    // -------------------------------------------------------------------------
    /**
     * Gera o próximo id disponível para uma nova conversão, consultando o
     * maior id atualmente cadastrado no banco.
     *
     * @return próximo id disponível
     * @throws SQLException se ocorrer erro na operação
     */
    public int gerarProximoId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_conversao), 0) + 1 AS proximo_id FROM ConversaoPontos";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt("proximo_id");
        }
    }

    /**
     * Monta um objeto ConversaoPontos a partir da linha atual de um ResultSet.
     * O construtor da classe bean recalcula o valor do crédito automaticamente
     * e define o status inicial como PENDENTE; em seguida sobrescrevemos o
     * status com o valor real vindo do banco.
     *
     * @param rs ResultSet posicionado em uma linha válida
     * @return conversão montada
     * @throws SQLException se ocorrer erro na leitura das colunas
     */
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