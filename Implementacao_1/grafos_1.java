import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class grafos_1 {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o nome de um dos arquivos a seguir");
        System.out.println("graph-test-100-1.txt");
        System.out.println("graph-test-50000-1.txt");
        System.out.print("Nome do arquivo: ");

        String caminho = entrada.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();
            String[] valores_iniciais = linha.trim().split("\\s+");// espaçamento
            int vertices = Integer.parseInt(valores_iniciais[0]);
            System.out.println("Vértices identificados: " + vertices);
            int arestas = Integer.parseInt(valores_iniciais[1]);
            System.out.println("Arestas identificadas: " + arestas);
            int[] origem = new int[arestas + 1]; // ignorar o 0
            int[] destino = new int[arestas + 1];// ignorar o 0

            // descartar indice 0
            origem[0] = 0;
            destino[0] = 0;

            int pos_global = 1;

            // assumindo que os vértices estão ordenados
            System.out.println("[Status] -> Carregando vetores de destino e origem...");
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.trim().split("\\s+");
                if (valores.length == 2) {
                    try {
                        int val = Integer.parseInt(valores[0]);
                        origem[pos_global] = val;
                        val = Integer.parseInt(valores[1]);
                        destino[pos_global] = val;
                        pos_global++;
                    } catch (NumberFormatException e) {
                        System.err.println("Erro de formatação de número na linha: " + linha);
                    }
                }
            }
            System.out.println("[Status] -> Vetor carregado!");
            br.close();

            System.out.println("[Status] -> Processando ponteiros...");

            int[] pointer = new int[vertices + 2]; // +2 -> ignorar indice 0, e adicionar "total + 1" no final

            // adicionar os ponteiros no vetor
            int pos_atual_vet = 2; // posição atual que será inserido um valor no vetor origem
            int val = origem[1]; // obter o primeiro valor dos vértices de origem
            int pos_vetor = 1; // usada para percorrer o vetor pointer

            // assumir que os vértices estão ordenados
            int pos_ponteiro = 1;
            pointer[1] = pos_ponteiro; // atualizar o ponteiro para a primeira posição do vetor de destino

            while (pos_atual_vet < pointer.length - 1) {
                // se o próximo número no vetor de origem for igual ao número atual, então
                // incrementar ponteiro
                if (val == origem[pos_vetor]) {
                    pos_ponteiro++; // atualizar o ponteiro para a proxima posição no vetor de destino
                    pos_vetor++;// percorrer para a próxima posição do vetor de origem

                    // debug
                    // System.out.println("valores comparados -> " + val + " com " +
                    // origem[pos_vetor]);
                    // System.out.println("pos atual no vetor origem " + pos_vetor);
                } else {
                    val = origem[pos_vetor]; // atualizar para comparar o novo valor do vértice seguinte
                    pointer[pos_atual_vet] = pos_ponteiro; // posicionar ponteiro logo após o ultimo ponteiro inserido
                    // System.out.println("novo ponteiro em " + pos_atual_vet); // debug
                    pos_atual_vet++; // atualizar a posição do vetor de origem para inserir um novo endereço
                }
            }
            pointer[pos_atual_vet] = arestas + 1; // ultimo número = total de arestas + 1;

            System.out.println("[Status] -> Ponteiros processados!");
            System.out.println("[Status] -> Carregamento Concluído!");

            System.out.println("Digite o número de um vértice: ");

            int num = entrada.nextInt();
            System.out.println("Informações do vértice [" + num + "]:");
            if (num < pointer.length - 1) {
                int next = pointer[num];
                int max = pointer[num + 1] - next;//número de elementos entre o ponteiro atual e o próximo = grau de saída do vértice
                System.out.println("[INFO] -> Grau de Saída = " + max);

                int grau_entrada = 0;
                for (int i = 1; i < destino.length; i++){
                    if (destino[i] == num)
                        grau_entrada++;
                }

                System.out.println("[INFO] -> Grau de Entrada = " + grau_entrada);
                System.out.println("[INFO] -> Sucessores: ");
                while (max > 0) {
                    System.out.println("[" + num + "] -> " + destino[next]);
                    next++;
                    max--;
                }

            }
            System.out.println("Fim das informações desse vértice");

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }

        entrada.close();
    }
}