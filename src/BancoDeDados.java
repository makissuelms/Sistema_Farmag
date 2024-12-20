import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BancoDeDados {

    private static BancoDeDados instancia;

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultset = null;

    private BancoDeDados() {
    }

    public static BancoDeDados getInstancia() {
        if (instancia == null) {
            instancia = new BancoDeDados();
        }
        return instancia;
    }

    public void conectar() {
        String servidor = "jdbc:mysql://localhost:3306/sistema_farmag";
        String usuario = "root";
        String senha = "M@k1s5u3l#";
        String driver = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(servidor, usuario, senha);
            this.statement = this.connection.createStatement();
            System.out.println("Conexão bem-sucedida!");
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean estaConectado() {
        return this.connection != null;
    }

    public void listarClientes() {
        try {
            String query = "SELECT * FROM Cliente ORDER BY nome";
            this.resultset = this.statement.executeQuery(query);
            while (this.resultset.next()) {
                System.out.println(String.format("CPF: %s - Nome: %s - Telefone: %s - Endereço: %s",
                        this.resultset.getString("CPF"),
                        this.resultset.getString("nome"),
                        this.resultset.getString("telefone"),
                        this.resultset.getString("endereco")));
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            try {
                if (this.resultset != null) this.resultset.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar ResultSet: " + e.getMessage());
            }
        }
    }

    public void inserirCliente(String CPF, String nome, String telefone, String endereco) {
        try {
            String query = "INSERT INTO Cliente (CPF, nome, telefone, endereco) VALUES ('" + CPF + "','" + nome + "', '" + telefone + "','" + endereco + "')";
            this.statement.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void editarCliente(String CPF, String nome, String telefone, String endereco) {
        try {
            String query = String.format(
                    "UPDATE Cliente SET nome = '%s', telefone = '%s', endereco = '%s' WHERE CPF = '%s'",
                    nome, telefone, endereco, CPF);
            this.statement.executeUpdate(query);
            System.out.println("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void apagarCliente(String CPF) {
        try {
            String query = "DELETE FROM Cliente WHERE CPF = '" + CPF + "'";
            int linhasAfetadas = this.statement.executeUpdate(query);

            if (linhasAfetadas > 0) {
                System.out.println("Cliente com CPF " + CPF + " apagado com sucesso!");
            } else {
                System.out.println("Nenhum cliente encontrado com o CPF " + CPF + ".");
            }
        } catch (Exception e) {
            System.out.println("Erro ao apagar cliente: " + e.getMessage());
        }
    }

    public void relatorioVendasPorCliente(String cpfCliente) {
        try {
            String query = String.format("""
            SELECT
                v.nota_fiscal,
                v.data_venda,
                v.valor_total,
                p.nome AS produto,
                iv.quantidade,
                iv.preco_unitario
            FROM
                Venda v
            JOIN
                Item_Venda iv ON v.nota_fiscal = iv.Venda_nota_fiscal
            JOIN
                Produto p ON iv.Produto_cod_prod = p.cod_prod
            WHERE
                v.Cliente_CPF = '%s'
            ORDER BY
                v.data_venda;
        """, cpfCliente);

            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Relatório de Vendas do Cliente: " + cpfCliente);
            while (resultSet.next()) {
                System.out.println(String.format(
                        "Nota Fiscal: %d, Data: %s, Total: R$ %.2f, Produto: %s, Quantidade: %d, Preço Unitário: R$ %.2f",
                        resultSet.getInt("nota_fiscal"),
                        resultSet.getDate("data_venda"),
                        resultSet.getDouble("valor_total"),
                        resultSet.getString("produto"),
                        resultSet.getInt("quantidade"),
                        resultSet.getDouble("preco_unitario")
                ));
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void relatorioProdutosPorFornecedor(String cnpjFornecedor) {
        try {
            String query = String.format("""
            SELECT
                f.nome AS fornecedor,
                p.nome AS produto,
                p.descricao,
                p.preco,
                p.qtd_estoque,
                p.data_validade
            FROM
                Fornecedor f
            JOIN
                Produto_Fornecedor pf ON f.CNPJ = pf.Fornecedor_CNPJ
            JOIN
                Produto p ON pf.Produto_cod_prod = p.cod_prod
            WHERE
                f.CNPJ = '%s'
            ORDER BY
                p.nome;
        """, cnpjFornecedor);

            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("Relatório de Produtos do Fornecedor: " + cnpjFornecedor);
            while (resultSet.next()) {
                System.out.println(String.format(
                        "Fornecedor: %s, Produto: %s, Descrição: %s, Preço: R$ %.2f, Estoque: %d, Validade: %s",
                        resultSet.getString("fornecedor"),
                        resultSet.getString("produto"),
                        resultSet.getString("descricao"),
                        resultSet.getDouble("preco"),
                        resultSet.getInt("qtd_estoque"),
                        resultSet.getDate("data_validade")
                ));
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            if (this.statement != null) this.statement.close();
            if (this.connection != null) this.connection.close();
            System.out.println("Conexão encerrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao desconectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
