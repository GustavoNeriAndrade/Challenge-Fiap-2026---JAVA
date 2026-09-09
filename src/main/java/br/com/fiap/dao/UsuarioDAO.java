package br.com.fiap.dao;

import br.com.fiap.bean.Usuario;
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
 * da entidade Usuario no banco de dados Oracle.
 *
 * @author Equipe EcoLoop
 * @version 1.0
 */
public class UsuarioDAO {

    // Create
    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario "
                + "(id_usuario, nome_usuario, email_usuario, senha_usuario, pontos_usuario, data_cadastro) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, usuario.getId());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setInt(5, usuario.getPontos());
            stmt.setDate(6, DataUtil.paraSqlDate(usuario.getDataCadastro()));

            stmt.executeUpdate();
            System.out.println("Usuário inserido com sucesso: " + usuario.getNome());
        }
    }

    // Read
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_usuario, nome_usuario, email_usuario, senha_usuario, "
                + "pontos_usuario, data_cadastro FROM Usuario WHERE id_usuario = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarUsuario(rs);
                }
                return null;
            }
        }
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT id_usuario, nome_usuario, email_usuario, senha_usuario, "
                + "pontos_usuario, data_cadastro FROM Usuario ORDER BY id_usuario";

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        }
        return usuarios;
    }

    // Update
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET nome_usuario = ?, email_usuario = ?, "
                + "senha_usuario = ?, pontos_usuario = ? WHERE id_usuario = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setInt(4, usuario.getPontos());
            stmt.setInt(5, usuario.getId());

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Usuário atualizado com sucesso: " + usuario.getNome()
                    : "Nenhum usuário encontrado com id " + usuario.getId());
        }
    }

    // Delete
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            System.out.println(linhas > 0
                    ? "Usuário removido com sucesso (id " + id + ")"
                    : "Nenhum usuário encontrado com id " + id);
        }
    }

    // Métodos auxiliares
    public int gerarProximoId() throws SQLException {
        String sql = "SELECT NVL(MAX(id_usuario), 0) + 1 AS proximo_id FROM Usuario";

        try (Connection conexao = ConexaoBancoDados.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt("proximo_id");
        }
    }

    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nome_usuario"),
                rs.getString("email_usuario"),
                rs.getString("senha_usuario"),
                rs.getInt("pontos_usuario"),
                DataUtil.paraTexto(rs.getDate("data_cadastro"))
        );
    }
}
