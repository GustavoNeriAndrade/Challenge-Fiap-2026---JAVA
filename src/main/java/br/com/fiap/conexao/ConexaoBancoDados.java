package br.com.fiap.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBancoDados {

    // Parâmetros de conexão (Oracle do laboratório da FIAP)
    private static final String SERVER = "oracle.fiap.com.br";
    private static final String PORT   = "1521";
    private static final String SID    = "orcl";
    private static final String USER   = "RM572722";
    private static final String PASSWD = "fiap26";

    private static final String URL =
            "jdbc:oracle:thin:@" + SERVER + ":" + PORT + ":" + SID;

    // Impede instanciar a classe: só possui métodos estáticos
    private ConexaoBancoDados() {
    }

    // Métodos da classe
    public static Connection getConnection() throws SQLException {
        System.out.println("Conectando ao Banco de Dados...");
        Connection conexao = DriverManager.getConnection(URL, USER, PASSWD);
        System.out.println("Conexão efetuada com sucesso!");
        return conexao;
    }

    public static void closeConnection(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar a conexão com o banco de dados.");
                e.printStackTrace();
            }
        }
    }
}
