import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        BancoDeDados bancoDeDados = BancoDeDados.getInstancia();
        bancoDeDados.conectar();

        if (bancoDeDados.estaConectado()) {
            System.out.println("Bem-vindo ao Sistema Farmag!");

            System.out.print("Digite o CPF do cliente para gerar o relatório de vendas: ");
            String cpfCliente = sc.nextLine();
            bancoDeDados.relatorioVendasPorCliente(cpfCliente);

            System.out.print("\nDigite o CNPJ do fornecedor para gerar o relatório de produtos: ");
            String cnpjFornecedor = sc.nextLine();
            bancoDeDados.relatorioProdutosPorFornecedor(cnpjFornecedor);

            bancoDeDados.desconectar();
        } else {
            System.out.println("Não foi possível conectar ao banco de dados.");
        }

        sc.close();
    }
}
