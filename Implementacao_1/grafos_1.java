import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class grafos_1 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o nome do arquivo (sem a extensão.txt)");
        System.out.println("graph-test-100-1");
        System.out.println("graph-test-50000-1");
        System.out.print("Nome do arquivo: ");
        
        String caminho = entrada.nextLine() + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();
            String[] valores_iniciais = linha.trim().split("  ");//espaçamento
            int vertices = Integer.parseInt(valores_iniciais[0]);
            System.out.println("Vértices identificados: " + vertices);
            int arestas = Integer.parseInt(valores_iniciais[1]);
            System.out.println("Arestas identificadas: " + arestas);
            int[] origem = new int[vertices + 1];
            int[] destino = new int[arestas + 1];
            origem[0] = 0;
            destino[0] = 0;
            int pos_origem = 1;
            int pos_destino = 1;
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.trim().split("   ");//espaçamento entre os vértices
                System.out.println(valores);
            }
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }
        entrada.close();
    }
}