package br.com.fiap.dao;

import br.com.fiap.bean.ImpactoAmbiental;
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
 * da entidade ImpactoAmbiental no banco de dados Oracle.
 *
 * Observação: co2_economizado e arvores_equivalentes são valores
 * derivados (calculados automaticamente pela própria classe bean a
 * partir dos km percorridos), por isso não têm setters próprios —
 * são recalculados sempre que o objeto é reconstruído a partir do banco.
 *
 * @author Equipe EcoLoop
 * @version 1.0
 */
public class ImpactAmbientalDAO {

    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    /**
     * Insere um novo registro de impacto ambiental no banco de dados.
     *
     * @param impacto registro a ser inserido (o id já deve estar definido)
     * @throws SQLException se ocorrer erro na operação
     */
    public void inserir(ImpactoAmbiental impacto) throws SQLException {
        String sql = "INSERT INTO ImpactoAmbiental "
                + "(id_impacto, km_percorridos, co2_economizado, arvores_equivalentes, "
                + "data_registro, Usuario_id_usuario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, impacto.getId());
            stmt.setDouble(2, impacto.getKmPercorridos());
            stmt.setDouble(3, impacto.getCo2Economizado());
            stmt.setInt(4, impacto.getArvoresEquivalentes());
            stmt.setDate(5, DataUtil.paraSqlDate(impacto.getDataRegistro()));
            stmt.setInt(6, impacto.getUsuarioId());

            stmt.executeUpdate();
            System.out.println("Impacto ambiental inserido com sucesso (id " + impacto.getId() + ")");
        }
    }

    // -------------------------------------------------------------------------
    // READ
    // -------------------------------------------------------------------------
    /**
     * Busca um registro de impacto ambiental pelo id.
     *
     * @param id identificador do registro
     * @return o registro encontrado, ou null se não existir
     * @throws SQLException se ocorrer erro na operação
     */
    public ImpactoAmbiental buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_impacto, km_percorridos, data_registro, Usuario_id_usuario "
                + "FROM ImpactoAmbiental WHERE id_impacto = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarImpacto(rs);
                }
                return null;
            }
        }
    }

    /**
     * Lista todos os registros de impacto ambiental cadastrados no banco.
     *
     * @return lista de registros (vazia se não houver nenhum)
     * @throws SQLException se ocorrer erro na operação
     */
    public List<ImpactoAmbiental> listarTodos() throws SQLException {
        String sql = "SELECT id_impacto, km_percorridos, data_registro, Usuario_id_usuario "
                + "FROM ImpactoAmbiental ORDER BY id_impacto";

        List<ImpactoAmbiental> impactos = new ArrayList<>();

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                impactos.add(montarImpacto(rs));
            }
        }
        return impactos;
    }

    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------
    /**
     * Atualiza os dados de um registro de impacto ambiental já existente
     * (identificado pelo id). Recalcula co2_economizado e arvores_equivalentes
     * a partir da nova quilometragem.
     *
     * @param impacto registro com os dados atualizados
     * @throws SQLException se ocorrer erro na operação
     */
    public void atualizar(ImpactoAmbiental impacto) throws SQLException {
        String sql = "UPDATE ImpactoAmbiental SET km_percorridos = ?, co2_economizado = ?, "
                + "arvores_equivalentes = ? WHERE id_impacto = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setDouble(1, impacto.getKmPercorridos());
            stmt.setDouble(2, impacto.getCo2Economizado());
            stmt.setInt(3, impacto.getArvoresEquivalentes());
            stmt.setInt(4, impacto.getId());

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Impacto ambiental atualizado com sucesso (id " + impacto.getId() + ")"
                    : "Nenhum registro encontrado com id " + impacto.getId());
        }
    }

    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    /**
     * Remove um registro de impacto ambiental do banco pelo id.
     *
     * @param id identificador do registro a ser removido
     * @throws SQLException se ocorrer erro na operação
     */
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM ImpactoAmbiental WHERE id_impacto = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Impacto ambiental removido com sucesso (id " + id + ")"
                    : "Nenhum registro encontrado com id " + id);
        }
    }

    // -------------------------------------------------------------------------
    // Métodos auxiliares
    // -------------------------------------------------------------------------
    /**
     * Gera o próximo id disponível para um novo registro de impacto ambiental,
     * consultando o maior id atualmente cadastrado no banco.
     *
     * @return próximo id disponível
     * @throws SQLException se ocorrer erro na operação
     */
    public int gerarProximoId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_impacto), 0) + 1 AS proximo_id FROM ImpactoAmbiental";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt("proximo_id");
        }
    }

    /**
     * Monta um objeto ImpactoAmbiental a partir da linha atual de um ResultSet.
     * O CO2 economizado e as árvores equivalentes são recalculados
     * automaticamente pelo construtor da classe bean.
     *
     * @param rs ResultSet posicionado em uma linha válida
     * @return registro montado
     * @throws SQLException se ocorrer erro na leitura das colunas
     */
    private ImpactoAmbiental montarImpacto(ResultSet rs) throws SQLException {
        return new ImpactoAmbiental(
                rs.getInt("id_impacto"),
                rs.getInt("Usuario_id_usuario"),
                rs.getDouble("km_percorridos"),
                DataUtil.paraTexto(rs.getDate("data_registro"))
        );
    }
}