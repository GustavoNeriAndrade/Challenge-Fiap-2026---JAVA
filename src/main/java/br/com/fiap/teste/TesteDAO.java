package br.com.fiap.teste;

import br.com.fiap.model.ConversaoPontos;
import br.com.fiap.model.ImpactoAmbiental;
import br.com.fiap.model.Missao;
import br.com.fiap.model.Usuario;
import br.com.fiap.model.Voucher;
import br.com.fiap.dao.ConversaoPontosDAO;
import br.com.fiap.dao.ImpactAmbientalDAO;
import br.com.fiap.dao.MissaoDAO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dao.VoucherDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Equipe EcoLoop
 * @version 1.0
 */
public class TesteDAO {

    static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {

        testarCrudUsuario();
        testarCrudMissao();
        testarCrudImpactoAmbiental();
        testarCrudConversaoEVoucher();
    }

    // Teste completo do CRUD de Usuario
    static void testarCrudUsuario() {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        try {
            System.out.println("\n=== TESTE: CRUD de Usuario ===\n");

            int proximoId = usuarioDAO.gerarProximoId();
            String dataCadastro = LocalDate.now().format(formato);

            Usuario novoUsuario = new Usuario(
                    proximoId, "Usuario Teste DAO", "teste.dao@ecoloop.com",
                    "senha123", 0, dataCadastro
            );
            usuarioDAO.inserir(novoUsuario);

            Usuario usuarioLido = usuarioDAO.buscarPorId(proximoId);
            System.out.println("Usuário lido do banco: " + usuarioLido);

            usuarioLido.setPontos(150);
            usuarioLido.setNome("Usuario Teste DAO (Atualizado)");
            usuarioDAO.atualizar(usuarioLido);

            Usuario usuarioAtualizado = usuarioDAO.buscarPorId(proximoId);
            System.out.println("Usuário após atualização: " + usuarioAtualizado);

            List<Usuario> todosUsuarios = usuarioDAO.listarTodos();
            System.out.println("Total de usuários cadastrados: " + todosUsuarios.size());

            usuarioDAO.deletar(proximoId);

            Usuario usuarioAposDelete = usuarioDAO.buscarPorId(proximoId);
            System.out.println("Usuário após exclusão (esperado null): " + usuarioAposDelete);

        } catch (SQLException e) {
            System.out.println("Erro ao testar o CRUD de Usuario.");
            e.printStackTrace();
        }
    }

    // Teste completo do CRUD de Missao
    static void testarCrudMissao() {

        MissaoDAO missaoDAO = new MissaoDAO();

        try {
            System.out.println("\n=== TESTE: CRUD de Missao ===\n");

            int proximoId = missaoDAO.gerarProximoId();

            Missao novaMissao = new Missao(
                    proximoId, "Missao Teste DAO", "Descricao de teste",
                    50, "Transporte"
            );
            missaoDAO.inserir(novaMissao);

            Missao missaoLida = missaoDAO.buscarPorId(proximoId);
            System.out.println("Missão lida do banco: " + missaoLida);

            missaoLida.setPontosRecompensa(80);
            missaoLida.setAtiva(false);
            missaoDAO.atualizar(missaoLida);

            Missao missaoAtualizada = missaoDAO.buscarPorId(proximoId);
            System.out.println("Missão após atualização: " + missaoAtualizada);

            List<Missao> todasMissoes = missaoDAO.listarTodos();
            System.out.println("Total de missões cadastradas: " + todasMissoes.size());

            missaoDAO.deletar(proximoId);

            Missao missaoAposDelete = missaoDAO.buscarPorId(proximoId);
            System.out.println("Missão após exclusão (esperado null): " + missaoAposDelete);

        } catch (SQLException e) {
            System.out.println("Erro ao testar o CRUD de Missao.");
            e.printStackTrace();
        }
    }

    // Teste completo do CRUD de ImpactoAmbiental
    static void testarCrudImpactoAmbiental() {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ImpactAmbientalDAO impactoDAO = new ImpactAmbientalDAO();

        try {
            System.out.println("\n=== TESTE: CRUD de ImpactoAmbiental ===\n");

            // Usuário temporário só pra satisfazer a FK
            int idUsuarioTeste = usuarioDAO.gerarProximoId();
            Usuario usuarioTeste = new Usuario(
                    idUsuarioTeste, "Usuario Temp Impacto", "temp.impacto@ecoloop.com",
                    "senha123", 0, LocalDate.now().format(formato)
            );
            usuarioDAO.inserir(usuarioTeste);

            int proximoId = impactoDAO.gerarProximoId();
            ImpactoAmbiental novoImpacto = new ImpactoAmbiental(
                    proximoId, idUsuarioTeste, 12.5, LocalDate.now().format(formato)
            );
            impactoDAO.inserir(novoImpacto);

            ImpactoAmbiental impactoLido = impactoDAO.buscarPorId(proximoId);
            System.out.println("Impacto lido do banco: " + impactoLido);

            impactoLido.setKmPercorridos(20.0);
            impactoDAO.atualizar(impactoLido);

            ImpactoAmbiental impactoAtualizado = impactoDAO.buscarPorId(proximoId);
            System.out.println("Impacto após atualização: " + impactoAtualizado);

            List<ImpactoAmbiental> todosImpactos = impactoDAO.listarTodos();
            System.out.println("Total de registros de impacto: " + todosImpactos.size());

            // Limpeza (ordem inversa: impacto antes do usuário, por causa da FK)
            impactoDAO.deletar(proximoId);
            usuarioDAO.deletar(idUsuarioTeste);

            ImpactoAmbiental impactoAposDelete = impactoDAO.buscarPorId(proximoId);
            System.out.println("Impacto após exclusão (esperado null): " + impactoAposDelete);

        } catch (SQLException e) {
            System.out.println("Erro ao testar o CRUD de ImpactoAmbiental.");
            e.printStackTrace();
        }
    }

    // Teste completo do CRUD de ConversaoPontos + Voucher
    static void testarCrudConversaoEVoucher() {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ConversaoPontosDAO conversaoDAO = new ConversaoPontosDAO();
        VoucherDAO voucherDAO = new VoucherDAO();

        try {
            System.out.println("\n=== TESTE: CRUD de ConversaoPontos e Voucher ===\n");

            // Usuário temporário com pontos suficientes pra conversão
            int idUsuarioTeste = usuarioDAO.gerarProximoId();
            Usuario usuarioTeste = new Usuario(
                    idUsuarioTeste, "Usuario Temp Conversao", "temp.conversao@ecoloop.com",
                    "senha123", 500, LocalDate.now().format(formato)
            );
            usuarioDAO.inserir(usuarioTeste);

            // Create ConversaoPontos
            int idConversaoTeste = conversaoDAO.gerarProximoId();
            ConversaoPontos novaConversao = new ConversaoPontos(
                    idConversaoTeste, idUsuarioTeste, 200, LocalDate.now().format(formato)
            );
            novaConversao.setStatus("APROVADO");
            conversaoDAO.inserir(novaConversao);

            ConversaoPontos conversaoLida = conversaoDAO.buscarPorId(idConversaoTeste);
            System.out.println("Conversão lida do banco: " + conversaoLida);

            conversaoDAO.atualizar(conversaoLida);

            List<ConversaoPontos> todasConversoes = conversaoDAO.listarTodos();
            System.out.println("Total de conversões cadastradas: " + todasConversoes.size());

            // Create Voucher
            int idVoucherTeste = voucherDAO.gerarProximoId();
            String codigo = "TESTE-" + idVoucherTeste;
            Voucher novoVoucher = new Voucher(
                    idVoucherTeste, idUsuarioTeste, idConversaoTeste, codigo,
                    conversaoLida.getValorCredito(), LocalDate.now().format(formato),
                    LocalDate.now().plusYears(1).format(formato), "SPTrans"
            );
            voucherDAO.inserir(novoVoucher);

            Voucher voucherLido = voucherDAO.buscarPorId(idVoucherTeste);
            System.out.println("Voucher lido do banco: " + voucherLido);

            voucherLido.setStatus("UTILIZADO");
            voucherDAO.atualizar(voucherLido);

            Voucher voucherAtualizado = voucherDAO.buscarPorId(idVoucherTeste);
            System.out.println("Voucher após atualização: " + voucherAtualizado);

            List<Voucher> todosVouchers = voucherDAO.listarTodos();
            System.out.println("Total de vouchers cadastrados: " + todosVouchers.size());

            // Limpeza
            voucherDAO.deletar(idVoucherTeste);
            conversaoDAO.deletar(idConversaoTeste);
            usuarioDAO.deletar(idUsuarioTeste);

            Voucher voucherAposDelete = voucherDAO.buscarPorId(idVoucherTeste);
            System.out.println("Voucher após exclusão (esperado null): " + voucherAposDelete);

        } catch (SQLException e) {
            System.out.println("Erro ao testar o CRUD de ConversaoPontos/Voucher.");
            e.printStackTrace();
        }
    }
}