public class Relatorios {

    public void exibirRelatorioVendasPorCliente() {
        String cpfCliente = "12345678901"; // Substitua pelo CPF desejado
        BancoDeDados banco = BancoDeDados.getInstancia();
        banco.conectar();

        System.out.println("=== Relatório de Vendas por Cliente ===");
        banco.relatorioVendasPorCliente(cpfCliente);

        banco.desconectar();
    }

    public void exibirRelatorioProdutosPorFornecedor() {
        String cnpjFornecedor = "12345678000101"; // Substitua pelo CNPJ desejado
        BancoDeDados banco = BancoDeDados.getInstancia();
        banco.conectar();

        System.out.println("\n=== Relatório de Produtos por Fornecedor ===");
        banco.relatorioProdutosPorFornecedor(cnpjFornecedor);

        banco.desconectar();
    }

    public static void main(String[] args) {
        Relatorios relatorios = new Relatorios();

        // Exibe o relatório de vendas por cliente
        relatorios.exibirRelatorioVendasPorCliente();

        // Exibe o relatório de produtos por fornecedor
        relatorios.exibirRelatorioProdutosPorFornecedor();
    }
}
