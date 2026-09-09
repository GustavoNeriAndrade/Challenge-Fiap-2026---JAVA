package br.com.fiap.main;

import br.com.fiap.bean.ConversaoPontos;
import br.com.fiap.bean.ImpactoAmbiental;
import br.com.fiap.bean.Missao;
import br.com.fiap.bean.Usuario;
import br.com.fiap.bean.Voucher;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Equipe: EcoLoop
 * Integrantes:
 *Gustavo Neri Andrade         (RM 572722)
 *Miguel Vieira Martins        (RM 571978)
 *Carlos Americo M. Brambilla  (RM 571250)
 *Thiago Vendrami Luca         (RM 572942)
 *Murilo da Silva Lourenco     (RM 573959)
 *@author Equipe EcoLoop
 *@version 1.0
 */
public class Main {

    // Formatador de data padrão do sistema
    static DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Arrays globais de usuários, missões e vouchers
    static Usuario[]  usuarios = new Usuario[50];
    static Missao[]   missoes  = new Missao[50];
    static Voucher[]  vouchers = new Voucher[50];

    static int qtdUsuarios = 0;
    static int qtdMissoes  = 0;
    static int qtdVouchers = 0;

    // Contadores de ID para cada entidade
    static int idConversao     = 1;
    static int idImpacto       = 1;
    static int idVoucher       = 1;

    //  MENU PRINCIPAL
    public static void main(String[] args) {

        JOptionPane.showMessageDialog(
                null,
                "Bem-vindo ao SoulUp EcoLoop!\n\n"
                        + "Converta seus pontos sustentáveis em\n"
                        + "créditos de transporte público.\n\n"
                        + "Equipe EcoLoop – FIAP 2026",
                "SoulUp EcoLoop",
                JOptionPane.INFORMATION_MESSAGE
        );

        boolean executando = true;

        while (executando) {

            String[] opcoes = {
                    "1 - Cadastrar usuário",
                    "2 - Cadastrar missão",
                    "3 - Concluir missão (ganhar pontos)",
                    "4 - Consultar saldo",
                    "5 - Converter pontos em voucher",
                    "6 - Registrar impacto ambiental",
                    "7 - Ranking de usuários",
                    "0 - Sair"
            };

            String escolha = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecione uma opção:",
                    "SoulUp EcoLoop — Menu Principal",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0]
            );

            if (escolha == null || escolha.startsWith("0")) {
                executando = false;
                JOptionPane.showMessageDialog(
                        null,
                        "Obrigado por usar o SoulUp EcoLoop!\n\nEquipe EcoLoop – FIAP 2026",
                        "Até logo!",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else if (escolha.startsWith("1")) {
                cadastrarUsuario();

            } else if (escolha.startsWith("2")) {
                cadastrarMissao();

            } else if (escolha.startsWith("3")) {
                concluirMissao();

            } else if (escolha.startsWith("4")) {
                consultarSaldo();

            } else if (escolha.startsWith("5")) {
                converterPontos();

            } else if (escolha.startsWith("6")) {
                registrarImpacto();

            } else if (escolha.startsWith("7")) {
                exibirRanking();
            }
        }
    }

    //  1 - CADASTRAR USUÁRIO
    static void cadastrarUsuario() {

        if (qtdUsuarios >= usuarios.length) {
            JOptionPane.showMessageDialog(
                    null,
                    "Limite de usuários atingido.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                null,
                "Cadastro de novo usuário.",
                "Cadastrar Usuário",
                JOptionPane.INFORMATION_MESSAGE
        );

        String nome = JOptionPane.showInputDialog(
                null, "Nome completo:", "Cadastrar Usuário", JOptionPane.QUESTION_MESSAGE
        );
        if (nome == null || nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "Nome não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String email = JOptionPane.showInputDialog(
                null, "E-mail:", "Cadastrar Usuário", JOptionPane.QUESTION_MESSAGE
        );

        String senha = JOptionPane.showInputDialog(
                null, "Crie uma senha:", "Cadastrar Usuário", JOptionPane.QUESTION_MESSAGE
        );

        String pontosStr = JOptionPane.showInputDialog(
                null, "Pontos iniciais (ex: 0):", "Cadastrar Usuário", JOptionPane.QUESTION_MESSAGE
        );

        int pontos = Integer.parseInt(pontosStr);
        String dataCadastro = LocalDate.now().format(formato);

        usuarios[qtdUsuarios] = new Usuario(
                qtdUsuarios + 1, nome, email, senha, pontos, dataCadastro
        );
        qtdUsuarios++;

        JOptionPane.showMessageDialog(
                null,
                "Usuário cadastrado com sucesso!\n\n" + usuarios[qtdUsuarios - 1].toString(),
                "Usuário Cadastrado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //  2 - CADASTRAR MISSÃO
    static void cadastrarMissao() {

        if (qtdMissoes >= missoes.length) {
            JOptionPane.showMessageDialog(
                    null, "Limite de missões atingido.", "Erro", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                null, "Cadastro de nova missão sustentável.",
                "Cadastrar Missão", JOptionPane.INFORMATION_MESSAGE
        );

        String titulo = JOptionPane.showInputDialog(
                null,
                "Título da missão:\n(ex: Selfie no ônibus, Andar de metrô)",
                "Cadastrar Missão", JOptionPane.QUESTION_MESSAGE
        );
        if (titulo == null || titulo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "Título não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String descricao = JOptionPane.showInputDialog(
                null, "Descrição da missão:", "Cadastrar Missão", JOptionPane.QUESTION_MESSAGE
        );

        String pontosStr = JOptionPane.showInputDialog(
                null, "Pontos de recompensa (ex: 50):",
                "Cadastrar Missão", JOptionPane.QUESTION_MESSAGE
        );
        int pontosRecompensa = Integer.parseInt(pontosStr);

        String[] categorias = {"Transporte", "Reciclagem", "Energia", "Alimentacao", "Outro"};
        String categoria = (String) JOptionPane.showInputDialog(
                null, "Selecione a categoria:",
                "Categoria", JOptionPane.QUESTION_MESSAGE,
                null, categorias, categorias[0]
        );
        if (categoria == null) categoria = "Outro";

        missoes[qtdMissoes] = new Missao(
                qtdMissoes + 1, titulo.trim(), descricao.trim(), pontosRecompensa, categoria
        );
        qtdMissoes++;

        JOptionPane.showMessageDialog(
                null,
                "Missão cadastrada com sucesso!\n\n" + missoes[qtdMissoes - 1].resumo(),
                "Missão Cadastrada",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //  3 - CONCLUIR MISSÃO
    static void concluirMissao() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(
                    null, "Nenhum usuário cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (qtdMissoes == 0) {
            JOptionPane.showMessageDialog(
                    null, "Nenhuma missão cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Montar lista de usuários
        String[] nomesUsuarios = new String[qtdUsuarios];
        for (int i = 0; i < qtdUsuarios; i++) {
            nomesUsuarios[i] = (i + 1) + " - " + usuarios[i].getNome()
                    + " (" + usuarios[i].getPontos() + " pts)";
        }

        String escolhaUsuario = (String) JOptionPane.showInputDialog(
                null, "Selecione o usuário:",
                "Concluir Missão", JOptionPane.QUESTION_MESSAGE,
                null, nomesUsuarios, nomesUsuarios[0]
        );
        if (escolhaUsuario == null) return;

        int idxUsuario = Integer.parseInt(escolhaUsuario.split(" - ")[0]) - 1;

        // Montar lista de missões
        String[] nomesMissoes = new String[qtdMissoes];
        for (int i = 0; i < qtdMissoes; i++) {
            nomesMissoes[i] = missoes[i].resumo();
        }

        String escolhaMissao = (String) JOptionPane.showInputDialog(
                null, "Selecione a missão concluída:",
                "Concluir Missão", JOptionPane.QUESTION_MESSAGE,
                null, nomesMissoes, nomesMissoes[0]
        );
        if (escolhaMissao == null) return;

        int idxMissao = 0;
        for (int i = 0; i < qtdMissoes; i++) {
            if (nomesMissoes[i].equals(escolhaMissao)) {
                idxMissao = i;
                break;
            }
        }

        String resultado = missoes[idxMissao].concluirMissao(usuarios[idxUsuario]);

        JOptionPane.showMessageDialog(
                null, resultado, "Missão Concluída!", JOptionPane.INFORMATION_MESSAGE
        );
    }

    //  4 - CONSULTAR SALDO
    static void consultarSaldo() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(
                    null, "Nenhum usuário cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] nomesUsuarios = new String[qtdUsuarios];
        for (int i = 0; i < qtdUsuarios; i++) {
            nomesUsuarios[i] = (i + 1) + " - " + usuarios[i].getNome();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null, "Selecione o usuário:",
                "Consultar Saldo", JOptionPane.QUESTION_MESSAGE,
                null, nomesUsuarios, nomesUsuarios[0]
        );
        if (escolha == null) return;

        int idx = Integer.parseInt(escolha.split(" - ")[0]) - 1;
        Usuario u = usuarios[idx];

        // Buscar vouchers do usuário
        StringBuilder infoVouchers = new StringBuilder();
        for (int i = 0; i < qtdVouchers; i++) {
            if (vouchers[i].getUsuarioId() == u.getId()) {
                infoVouchers.append("\n  ").append(vouchers[i].exibirDetalhes());
            }
        }

        String info = "=== Saldo do Usuário ===\n"
                + "  Nome   : " + u.getNome() + "\n"
                + "  E-mail : " + u.getEmail() + "\n"
                + "  Pontos : " + u.getPontos() + " pts\n"
                + "  Crédito: R$ " + String.format("%.2f",
                ConversaoPontos.calcularCredito(u.getPontos())) + "\n"
                + "========================"
                + (infoVouchers.length() > 0
                ? "\n\nVouchers:\n" + infoVouchers
                : "\n\nNenhum voucher emitido.");

        JOptionPane.showMessageDialog(
                null, info, "Saldo de " + u.getNome(), JOptionPane.INFORMATION_MESSAGE
        );
    }

    //  5 - CONVERTER PONTOS EM VOUCHER
    static void converterPontos() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(
                    null, "Nenhum usuário cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] nomesUsuarios = new String[qtdUsuarios];
        for (int i = 0; i < qtdUsuarios; i++) {
            nomesUsuarios[i] = (i + 1) + " - " + usuarios[i].getNome()
                    + " (" + usuarios[i].getPontos() + " pts)";
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null, "Selecione o usuário:",
                "Converter Pontos", JOptionPane.QUESTION_MESSAGE,
                null, nomesUsuarios, nomesUsuarios[0]
        );
        if (escolha == null) return;

        int idx = Integer.parseInt(escolha.split(" - ")[0]) - 1;
        Usuario usuarioConversao = usuarios[idx];

        if (usuarioConversao.getPontos() < ConversaoPontos.PONTOS_POR_REAL) {
            JOptionPane.showMessageDialog(
                    null,
                    "Saldo insuficiente!\nMínimo: " + ConversaoPontos.PONTOS_POR_REAL + " pts.",
                    "Saldo Insuficiente",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String pontosStr = JOptionPane.showInputDialog(
                null,
                "Quantos pontos deseja converter?\n"
                        + "Saldo: " + usuarioConversao.getPontos() + " pts\n"
                        + "Taxa : " + ConversaoPontos.PONTOS_POR_REAL + " pts = R$ 1,00",
                "Converter Pontos",
                JOptionPane.QUESTION_MESSAGE
        );
        if (pontosStr == null) return;

        int pontosConverter = Integer.parseInt(pontosStr);

        String dataConversao = LocalDate.now().format(formato);

        ConversaoPontos conversao = new ConversaoPontos(
                idConversao++, usuarioConversao.getId(), pontosConverter, dataConversao
        );

        String resultadoConversao = conversao.aprovar(usuarioConversao);

        int tipoMsg = "APROVADO".equals(conversao.getStatus())
                ? JOptionPane.INFORMATION_MESSAGE
                : JOptionPane.WARNING_MESSAGE;

        JOptionPane.showMessageDialog(
                null, resultadoConversao, "Resultado da Conversão", tipoMsg
        );

        if ("APROVADO".equals(conversao.getStatus())) {

            String[] operadores = {"SPTrans", "Metro SP", "EMTU", "CPTM", "Outro"};
            String operador = (String) JOptionPane.showInputDialog(
                    null, "Selecione o operador de transporte:",
                    "Operador", JOptionPane.QUESTION_MESSAGE,
                    null, operadores, operadores[0]
            );
            if (operador == null) operador = "SPTrans";

            String dataEmissao  = LocalDate.now().format(formato);
            String dataValidade = LocalDate.now().plusYears(1).format(formato);
            String codigo       = "ECO-" + usuarioConversao.getId()
                    + "-" + (System.currentTimeMillis() % 100000);

            Voucher voucher = new Voucher(
                    idVoucher++, usuarioConversao.getId(), conversao.getId(),
                    codigo, conversao.getValorCredito(),
                    dataEmissao, dataValidade, operador
            );

            vouchers[qtdVouchers++] = voucher;

            JOptionPane.showMessageDialog(
                    null, voucher.exibirDetalhes(), "Voucher Gerado!", JOptionPane.INFORMATION_MESSAGE
            );

            int usar = JOptionPane.showConfirmDialog(
                    null, "Deseja simular a utilização do voucher agora?",
                    "Utilizar Voucher", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
            );

            if (usar == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                        null, voucher.utilizar(), "Voucher Utilizado", JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    //  6 - REGISTRAR IMPACTO AMBIENTAL
    static void registrarImpacto() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(
                    null, "Nenhum usuário cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] nomesUsuarios = new String[qtdUsuarios];
        for (int i = 0; i < qtdUsuarios; i++) {
            nomesUsuarios[i] = (i + 1) + " - " + usuarios[i].getNome();
        }

        String escolha = (String) JOptionPane.showInputDialog(
                null, "Selecione o usuário:",
                "Impacto Ambiental", JOptionPane.QUESTION_MESSAGE,
                null, nomesUsuarios, nomesUsuarios[0]
        );
        if (escolha == null) return;

        int idx = Integer.parseInt(escolha.split(" - ")[0]) - 1;

        String kmStr = JOptionPane.showInputDialog(
                null,
                "Quantos km foram percorridos via transporte público?\n\n"
                        + "Referências:\n"
                        + "  Carro    : " + ImpactoAmbiental.EMISSAO_CARRO_KG_KM + " kg CO2/km\n"
                        + "  Ônibus   : " + ImpactoAmbiental.EMISSAO_ONIBUS_KG_KM + " kg CO2/km\n"
                        + "  Economia : " + ImpactoAmbiental.ECONOMIA_CO2_KG_KM + " kg CO2/km",
                "Quilometragem",
                JOptionPane.QUESTION_MESSAGE
        );
        if (kmStr == null) return;

        double km = Double.parseDouble(kmStr.replace(",", "."));
        String dataImpacto = LocalDate.now().format(formato);

        ImpactoAmbiental impacto = new ImpactoAmbiental(
                idImpacto++, usuarios[idx].getId(), km, dataImpacto
        );

        JOptionPane.showMessageDialog(
                null, impacto.gerarRelatorio(),
                "Relatório de Impacto Ambiental", JOptionPane.INFORMATION_MESSAGE
        );
    }

    //  7 - RANKING DE USUÁRIOS
    static void exibirRanking() {

        if (qtdUsuarios == 0) {
            JOptionPane.showMessageDialog(
                    null, "Nenhum usuário cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Copia os usuários para ordenar por pontos (bubble sort simples)
        Usuario[] ranking = new Usuario[qtdUsuarios];
        for (int i = 0; i < qtdUsuarios; i++) {
            ranking[i] = usuarios[i];
        }

        for (int i = 0; i < qtdUsuarios - 1; i++) {
            for (int j = 0; j < qtdUsuarios - 1 - i; j++) {
                if (ranking[j].getPontos() < ranking[j + 1].getPontos()) {
                    Usuario temp  = ranking[j];
                    ranking[j]    = ranking[j + 1];
                    ranking[j + 1] = temp;
                }
            }
        }

        StringBuilder sb = new StringBuilder("=== Ranking de Usuários ===\n\n");
        String[] medalhas = {"1o", "2o", "3o"};

        for (int i = 0; i < qtdUsuarios; i++) {
            String pos = i < 3 ? medalhas[i] : (i + 1) + "o";
            sb.append(String.format("  %s  %-25s  %5d pts  R$ %6.2f%n",
                    pos,
                    ranking[i].getNome(),
                    ranking[i].getPontos(),
                    ConversaoPontos.calcularCredito(ranking[i].getPontos())
            ));
        }

        JOptionPane.showMessageDialog(
                null, sb.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE
        );
    }
}