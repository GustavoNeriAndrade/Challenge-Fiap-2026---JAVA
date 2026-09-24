# 🌱 SoulUp EcoLoop

Sistema de pontos sustentáveis que converte engajamento ambiental em crédito real de transporte público. Desenvolvido em Java, com persistência em Oracle Database.

> 100 pontos sustentáveis = R$ 1,00 em crédito de transporte 🚌

---

## 📋 Sobre o projeto

O **SoulUp EcoLoop** é um módulo da plataforma SoulUp que permite aos usuários acumular pontos completando missões sustentáveis (usar transporte público, reciclar, economizar energia) e convertê-los em vouchers digitais de transporte, vinculados a operadores como SPTrans, Metrô SP, EMTU e CPTM.

O sistema também mede e reporta o impacto ambiental gerado por cada usuário, calculando CO2 economizado e sua equivalência em árvores.

## ✨ Funcionalidades

- 👤 Cadastro de usuários e acompanhamento de saldo de pontos
- 🎯 Missões sustentáveis categorizadas (Transporte, Reciclagem, Energia, Alimentação...)
- 🎫 Conversão de pontos em crédito de transporte (100 pts = R$ 1,00)
- 🧾 Geração de vouchers digitais com código único, validade e operador
- 🌳 Registro e cálculo de impacto ambiental (CO2 economizado, árvores equivalentes)
- 🏆 Ranking de usuários por pontos acumulados
- 💾 Persistência completa em banco Oracle via camada DAO (CRUD)

## 🛠️ Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 25 |
| Build | Maven |
| Banco de dados | Oracle Database (laboratório FIAP) |
| Driver JDBC | `com.oracle.database.jdbc:ojdbc11` — v23.5.0.24.07 |
| Interface | Java Swing (`JOptionPane`) |
| IDE recomendada | IntelliJ IDEA |

## 📂 Estrutura do projeto

```
src/main/java/br/com/fiap/
├── model/           # Classes de modelo (Usuario, Missao, ConversaoPontos, ImpactoAmbiental, Voucher)
├── conexao/        # ConexaoBancoDeDados — conexão JDBC com o Oracle
├── dao/            # Camada DAO — CRUD completo de cada entidade
├── util/           # DataUtil — conversão de datas (String ↔ java.sql.Date)
├── main/           # Main — menu interativo (JOptionPane)
└── teste/          # TesteDAO — classe de teste com main, valida todo o CRUD
```

### Camada de modelo (`model`)

| Classe | Responsabilidade |
|---|---|
| `Usuario` | Dados do usuário e saldo de pontos |
| `Missao` | Missões sustentáveis disponíveis |
| `ConversaoPontos` | Transações de conversão pontos → crédito |
| `ImpactoAmbiental` | Cálculo de CO2 economizado e árvores equivalentes |
| `Voucher` | Voucher digital de transporte gerado |

### Camada DAO (`dao`)

Cada entidade tem seu DAO com o mesmo contrato, usando exclusivamente `PreparedStatement`:

```java
inserir(entidade)       // CREATE
buscarPorId(id)          // READ
listarTodos()             // READ
atualizar(entidade)      // UPDATE
deletar(id)               // DELETE
gerarProximoId()          // sincroniza o próximo ID com o banco
```

## 🚀 Como rodar

### Pré-requisitos
- JDK 25 instalado
- IntelliJ IDEA (ou outra IDE com suporte a Maven)
- Conexão com a rede/VPN da FIAP (o banco é o Oracle do laboratório)

### Passo a passo

1. Clone o repositório e abra a pasta no IntelliJ: `File → Open`
2. Aguarde o Maven baixar as dependências automaticamente (ou `Reload Maven Project`)
3. Confirme que o Project SDK está em Java 25: `File → Project Structure → Project`
4. Confira as credenciais de conexão em `ConexaoBancoDeDados` (pacote `br.com.fiap.conexao`)
5. Rode a classe `TesteDAO` primeiro, pra validar que a conexão e o CRUD estão funcionando:
   ```
   botão direito em TesteDAO.java → Run 'TesteDAO.main()'
   ```
6. Rode o sistema completo pela classe `Main`:
   ```
   botão direito em Main.java → Run 'Main.main()'
   ```
7. Interaja pelas caixas de diálogo (`JOptionPane`) que vão abrindo

## 👥 Equipe EcoLoop

| Nome | RM |
|---|---|
| Gustavo Neri Andrade | 572722 |
| Miguel Vieira Martins | 571978 |
| Carlos Americo Machado Brambilla | 571250 |
| Thiago Vendrami Luca | 572942 |
| Murilo da Silva Lourenco | 573959 |

---

<p align="center">Feito com 💚 pela equipe EcoLoop — FIAP 2026</p>
