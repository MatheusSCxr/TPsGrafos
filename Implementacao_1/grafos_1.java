import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

// classe auxiliar para ordenar as arestas pelo destino ou origem
class Aresta {
    int origem;
    int destino;

    // construtor recebendo sempre uma origem e um destino
    Aresta(int origem, int destino) {
        this.origem = origem;
        this.destino = destino;
    }
}

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
            String[] valores_iniciais = linha.trim().split("\\s+"); // espaçamento entre os valores dos vértices e das arestas

            //obter individualmente os valores
            int vertices = Integer.parseInt(valores_iniciais[0]);
            int arestas = Integer.parseInt(valores_iniciais[1]);

            Aresta[] arestas_vet = new Aresta[arestas + 1]; // ignorar o índice 0

            arestas_vet[0] = new Aresta(0, 0); // ignorar o índice 0

            int pos_global = 1;

            // assumindo que os vértices estão ordenados

            while ((linha = br.readLine()) != null) {
                String[] valores = linha.trim().split("\\s+");
                if (valores.length == 2) {
                    try {
                        int val1 = Integer.parseInt(valores[0]); // origem
                        int val2 = Integer.parseInt(valores[1]); // destino
                        arestas_vet[pos_global] = new Aresta(val1, val2);
                        pos_global++;
                    } catch (NumberFormatException e) {
                        System.err.println("Erro de formatação de número na linha: " + linha);
                    }
                }
            }
            br.close(); // fechar BufferedReader

            int[] pointer = new int[vertices + 2]; // +2 -> ignorar indice 0, e adicionar "total + 1" no final

            // adicionar os ponteiros no vetor
            int pos_atual_vet = 2; // posição atual que será inserido um valor no vetor origem
            int val = arestas_vet[1].origem; // obter o primeiro valor dos vértices de origem
            int pos_vetor = 1; // usada para percorrer o vetor pointer

            // assumir que os vértices estão ordenados pela origem
            int pos_ponteiro = 1; //posição do ponteiro no vetor de destino
            pointer[1] = pos_ponteiro; // atualizar o ponteiro para a primeira posição do vetor de destino

            while (pos_atual_vet < pointer.length - 1) {
                // se o próximo número no vetor de origem for igual ao número atual, então
                // incrementar ponteiro
                if (val == arestas_vet[pos_vetor].origem) {
                    pos_ponteiro++; // atualizar o ponteiro para a proxima posição no vetor de destino
                    pos_vetor++; // percorrer para a próxima posição do vetor de origem
                } else {
                    val = arestas_vet[pos_vetor].origem; // atualizar para comparar o novo valor do vértice seguinte
                    pointer[pos_atual_vet] = pos_ponteiro; // posicionar ponteiro logo após o ultimo ponteiro inserido
                    pos_atual_vet++; // atualizar a posição do vetor de origem para inserir um novo endereço
                }
            }
            pointer[pos_atual_vet] = arestas + 1; // ultimo número = total de arestas + 1;

            // ponteiros do foward star processados com sucesso

            System.out.print("Digite o número de um vértice: ");

            int num = entrada.nextInt();
            System.out.println("Carregando informações do vértice [" + num + "]");

            if (num > 0 && num < pointer.length - 1) {
                int next = pointer[num]; //obter o endereço na posição do vértice
                int max = pointer[num + 1] - next; // número de elementos entre o ponteiro atual e o próximo = grau de saída do vértice
                System.out.println("[INFO] -> Grau de Saída = " + max);

                System.out.println("[INFO] -> Sucessores: ");
                while (max > 0) {
                    System.out.println("\t[" + num + "] -> " + arestas_vet[next].destino);
                    next++;
                    max--;
                }

                // ordenar as arestas pelo destino 
                Arrays.sort(arestas_vet, Comparator.comparingInt(aresta -> aresta.destino));

                // aplicar a representação reverse star, aproveitando o espaço alocado pelo
                // arrays de arrestas e de ponteiros na foward star

                // atualizar os ponteiros no vetor pointer
                pos_atual_vet = 2; // posição atual que será inserido um valor no vetor destino (posição 1 sempre terá o valor 1)
                val = 1; // começar pelo vértice 1 (existindo ou não)
                pos_vetor = 1; // usada para percorrer o vetor de destino

                // vértices agpra estão ordenados pela destino
                pos_ponteiro = 1; //posição do ponteiro no vetor de origem
                pointer[1] = pos_ponteiro; // atualizar o ponteiro para a primeira posição do vetor de destino (sempre começa em 1)

                while (pos_atual_vet < pointer.length - 1) {
                    // se o próximo número no vetor de destino for igual ao número atual, então
                    // incrementar ponteiro
                    if (val == arestas_vet[pos_vetor].destino) {
                        pos_ponteiro++; // atualizar o ponteiro para a proxima posição no vetor de origem
                        pos_vetor++; // percorrer para a próxima posição do vetor de destino
                    } else {
                        val = arestas_vet[pos_vetor].destino; // atualizar para comparar o novo valor do vértice seguinte
                        pointer[pos_atual_vet] = pos_ponteiro; // posicionar ponteiro logo após o ultimo ponteiro inserido
                        pos_atual_vet++; // atualizar a posição do vetor de origem para inserir um novo endereço
                    }
                }
                pointer[pos_atual_vet] = vertices + 1; // ultimo número = total de vertices + 1;

                // ponteiros processados com sucesso

                // obter informações do vértice digitado (num)
                next = pointer[num]; // obter o endereço na posição do vértice
                max = pointer[num + 1] - next; // número de elementos entre o ponteiro atual e o próximo = grau de saída do vértice
                System.out.println("\n[INFO] -> Grau de Entrada = " + max);

                System.out.println("[INFO] -> Predecessores: ");
                while (max > 0) {
                    System.out.println("\t" + arestas_vet[next].origem + " -> " + "[" + num + "]");
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