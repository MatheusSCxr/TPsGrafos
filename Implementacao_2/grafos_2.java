import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//busca em profundidade recursiva baseada no algoritmo passado nos slides em sala
//por ser recursiva para executar ela no grafo de 50.000 vértices é necessário aumentar o tamanho da pilha (stack) do JAVA
//para isso compile o programa e execute-o com o comando "java -Xss16m grafos_2" -Xss16m irá definir a stack de cada thread para 16Mb (a princípio suficiente para o grafo de 50.000 vértices)
public class grafos_2 {

    // classe auxiliar para definir uma aresta
    public static class Aresta {
        int origem;
        int destino;

        // construtor recebendo sempre uma origem e um destino
        Aresta(int origem, int destino) {
            this.origem = origem;
            this.destino = destino;
        }
    }

    //vetor global para acessar as arestas
    public static Aresta[] arestas_vet;

    //vetor global para acessar os sucessores (representacao foward star)
    public static int[] pointer;

    //vetores para a busca em profundidade
    public static int[] T_descoberta;
    public static int[] T_termino;
    public static int[] pai;

    //tempo da busca
    public static int tempo_atual;

    //vertice da busca
    public static int vertice_atual;

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
            int total_vertices = Integer.parseInt(valores_iniciais[0]);
            int total_arestas = Integer.parseInt(valores_iniciais[1]);

            arestas_vet = new Aresta[total_arestas + 1]; // ignorar o índice 0

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

            //iniciar o foward star
            
            //inicializar o vetor de ponteiros
            pointer = new int[total_vertices + 2]; // +2 -> ignorar indice 0, e adicionar "total + 1" no final

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
            pointer[pos_atual_vet] = total_arestas + 1; // ultimo número = total de arestas + 1;


            //preparar a busca em profundidade

            //inicializar os vetores da busca em profundidade (ignorando posição 0)
            T_descoberta = new int[total_vertices + 1];
            T_termino = new int[total_vertices + 1];
            pai = new int[total_vertices + 1];

            tempo_atual = 0; //inicializar o tempo da busca

            //inicializar todo TD com "0"
            for (int i = 1; i <= total_vertices; i++)
                T_descoberta[i] = 0;

            System.out.print("Digite o número de um vértice: ");

            int procurado = entrada.nextInt();

            if (procurado > 0 && procurado <= total_vertices){
                //iniciar a busca em profundidade
                for (int v = 1; v <= total_vertices; v++) {
                    if (T_descoberta[v] == 0) {
                        BuscaProfundidadeR(v, procurado);
                    }
                }
            }

            System.out.println("Busca finalizada.");
  
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }

        entrada.close();
    }

    public static List<Integer> getSucessores(int v){
        List<Integer> sucessores = new ArrayList<>();
        if (v > 0 && v < pointer.length - 1) {
            int next = pointer[v]; //obter o endereço na posição do vértice
            int max = pointer[v + 1] - next; // número de elementos entre o ponteiro atual e o próximo = grau de saída do vértice
            while (max > 0) {
                sucessores.add(arestas_vet[next].destino);
                next++;
                max--;
            }
        }
        return sucessores;
    }

    // busca em profundidade recursiva
    public static void BuscaProfundidadeR(int v, int procurado){
        tempo_atual++; //incrementar o tempo atual
        T_descoberta[v] = tempo_atual; //atualizar o tempo de descoberta do vértice atual

        //obter todos os vértices que saem de v
        List<Integer> sucessores = getSucessores(v);

        sucessores.sort(Integer::compareTo); //ordenar a lista

        int max = sucessores.size(); //total de sucessores
        int w_index = 0; //indice inicial do arraylist de sucessores

        //para todo vértice w sucessor de v
        while (w_index < max){
            int w_atual = sucessores.get(w_index); //sucessor atual

            if (T_descoberta[w_atual] == 0){
                //imprimir a aresta árvore encontrada
                System.out.println("Aresta de Árvore -> (" + v + ", " + w_atual + ")");

                //atualizar o pai de w
                pai[w_atual] = v;

                //nova busca recursiva em profundidade
                BuscaProfundidadeR(w_atual, procurado);

                if (w_atual == procurado) {
                    //interromper a busca depois de ter encontrado o vértice procurado
                    return;
                }
            }
            else if (v == procurado){
                //classificar todas as arestas que saem do vértice procurado (\t para destacar no terminal)
                if (T_termino[w_atual] == 0) {
                    System.out.println("\tAresta de Retorno -> (" + v + ", " + w_atual + ")");
                }
                else if (T_descoberta[v] < T_descoberta[w_atual]) {
                    System.out.println("\tAresta de Avanço -> (" + v + ", " + w_atual + ")");
                }
                else {
                    System.out.println("\tAresta de Cruzamento -> (" + v + ", " + w_atual + ")");
                }
            }

            w_index++; //ir para o próximo sucessor
        }

        //terminou a busca no vértice atual
        tempo_atual++;
        T_termino[v] = tempo_atual; //atualizar o tempo de término do vértice atual
    }

}