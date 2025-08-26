import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
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
            String[] valores_iniciais = linha.trim().split("\\s+");// espaçamento
            int vertices = Integer.parseInt(valores_iniciais[0]);
            System.out.println("Vértices identificados: " + vertices);
            int arestas = Integer.parseInt(valores_iniciais[1]);
            System.out.println("Arestas identificadas: " + arestas);
            int[] origem = new int[vertices + 2];
            int[] destino = new int[arestas + 1];
            origem[0] = 0;
            // origem[vertices + 1] = vertices + 1;
            destino[0] = 0;
            int pos_destino = 1;
            int pos_origem = 1;

            // assumindo que os vértices estão ordenados
            System.out.println("[Status] -> Carregando vetor de destino...");
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.trim().split("\\s+");
                if (valores.length == 2) {
                    try {
                        int val = Integer.parseInt(valores[0]);
                        if (val > pos_origem)
                            pos_origem++;
                        origem[pos_origem] = val;
                        val = Integer.parseInt(valores[1]);
                        destino[pos_destino] = val;
                        pos_destino++;
                    } catch (NumberFormatException e) {
                        System.err.println("Erro de formatação de número na linha: " + linha);
                    }
                }
            }
            System.out.println("[Status] -> Vetor carregado!");
            br.close();

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }
        entrada.close();
    }
}