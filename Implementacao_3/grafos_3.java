import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * Este código foi desenvolvido tomando como BASE o algoritmo de Dijkstra
 * apresentado no GeeksForGeeks:
 * https://www.geeksforgeeks.org/dsa/dijkstras-shortest-path-algorithm-greedy-algo-7/ (atualmente acessível apenas por VPN)
 *
 * Importante: o código original foi bastante modificado e adaptado para
 * atender às necessidades específicas deste projeto (obter caminho mínimo, com o menor número de arestas).
 */
public class grafos_3 {

    //lista de adjacencia no formato de ArrayList<ArrayList<ArrayList<Integer>>>
    public static ArrayList<ArrayList<ArrayList<Integer>>> constructAdj(int[][] edges, int V) {
        ArrayList<ArrayList<ArrayList<Integer>>> adj = new ArrayList<>();

        for (int i = 0; i <= V; i++) {
            adj.add(new ArrayList<>());
        }

        //preenche a lista de adjacencia
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];

            //adicionar aresta de u para v
            ArrayList<Integer> e1 = new ArrayList<>();
            e1.add(v);
            e1.add(wt);
            adj.get(u).add(e1);
        }

        return adj;
    }

    //aplica o Dijkstra e retorna o caminho mínimo com menor número de arestas da origem ao destino
    public static List<Integer> dijkstra_minArestas(int V, int[][] edges, int src, int dest) {

        //criar lista de adjacencia
        ArrayList<ArrayList<ArrayList<Integer>>> adj = constructAdj(edges, V);

        //cada elemento da lista de prioridade será [distância, vertice, arestas],
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> {
                    // critérios de ordenação
                    if (a[0] != b[0])
                        return Integer.compare(a[0], b[0]); // menor distância primeiro
                    return Integer.compare(a[2], b[2]); // em empate menos arestas primeiro
                });

        //array de distancia
        int[] dist = new int[V + 1];

        //array de quantidade de arestas que o caminho mínimo teve até cada vértice
        int[] arest = new int[V + 1];

        int[] predecessor = new int[V + 1];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(arest, 0); // inicializar vetor de quantidades de arestas no caminho como 0
        Arrays.fill(predecessor, -1); // inicializar vetor de predecessores

        //origem sempre tem distancia para si mesma como 0
        dist[src] = 0;

        int[] start = new int[3];

        start[0] = dist[src]; // distancia da origem até ela mesma
        start[1] = src; // origem
        start[2] = 0; // número de arestas do caminho mínimo até a origem
        pq.offer(start);

        //continuar até a fila de prioridade estiver vazia
        while (!pq.isEmpty()) {

            //obter vértice com distancia minima
            int[] curr = pq.poll();

            int d = curr[0];
            int u = curr[1];

            // Se a distância retirada da fila for maior que a melhor distância já conhecida
            // para 'u',
            // significa que esta entrada está desatualizada (um caminho melhor já foi
            // encontrado).
            // Nesse caso, ignoramos e seguimos para o próximo elemento da fila.
            if (d > dist[u])
                continue;

            // Se encontramos o vértice de destino, então interromper o laço.
            // o break nesse caso é válido pois na fila de prioridade estamos ordenando
            // também pelo menor número de arestas.
            // Portanto se 'u' saiu da fila, significa que não tem um caminho para 'u' com
            // menos arestas.
            if (u == dest) {
                break;
            }

            // Traverse all adjacent vertices of the current node
            for (ArrayList<Integer> neighbor : adj.get(u)) {
                int v = neighbor.get(0);
                int weight = neighbor.get(1);
                int arestas = arest[u] + 1;

                if (dist[v] > dist[u] + weight || (dist[v] == dist[u] + weight && arestas < arest[v])) {

                    // atualizar distancia de v
                    dist[v] = dist[u] + weight;
                    // atualizar o número de arestas do caminho minimo atual
                    arest[v] = arestas;
                    // atualizar o predecessor
                    predecessor[v] = u;

                    pq.offer(new int[] { dist[v], v, arestas });
                }
            }

        }

        // DEBUG

        // // Prin menores distancias
        // for (int d : dist)
        // System.out.print(d + " ");

        // System.out.println();

        // // Print numero de arestas para atingir até o vértice
        // for (int e : arest)
        // System.out.print(e + " ");

        // System.out.println();
        // // Predecessores
        // for (int p : predecessor)
        // System.out.print(p + " ");

        // System.out.println();

        // obter o caminho fazendo backtracking da lista de predecessores
        List<Integer> path = new ArrayList<>();
        for (int atual = dest; atual != -1; atual = predecessor[atual]) {
            path.add(atual);
        }
        Collections.reverse(path);

        // retorna caminho mínimo em forma de array
        return path;
    }

    // Leitura do arquivo de grafo
    public static int[][] carregarGrafo(String filepath, int[] totalVertices) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

            // obter numero de vértices e numero de arestas do grafo
            String linha = br.readLine();
            String[] valores_iniciais = linha.trim().split("\\s+");
            int V = Integer.parseInt(valores_iniciais[0]);
            int M = Integer.parseInt(valores_iniciais[1]);
            totalVertices[0] = V; // obter esse valor fora da função ao invés de ler 2 vezes

            int[][] edges = new int[M][3];
            int pos = 0;

            // ler e montar a matriz de adjacencia do grafo
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.trim().split("\\s+");
                if (valores.length == 3) {
                    int origem = Integer.parseInt(valores[0]);
                    int destino = Integer.parseInt(valores[1]);
                    int peso = Integer.parseInt(valores[2]);
                    edges[pos][0] = origem;
                    edges[pos][1] = destino;
                    edges[pos][2] = peso;
                    pos++;
                }
            }
            return edges;
        }
    }

    public static void main(String[] args) {
        //grafos que serão testados
        String[] arquivos = {
                "grafo_euleriano_1000.txt",
                "grafo_euleriano_10000.txt",
                "grafo_euleriano_25000.txt",
                "grafo_euleriano_50000.txt",
                "grafo_euleriano_100000.txt",
                "grafo_arvore_1000.txt",
                "grafo_arvore_10000.txt",
                "grafo_arvore_25000.txt",
                "grafo_arvore_50000.txt",
                "grafo_arvore_100000.txt",
        };

        int repeticoes = 5; // execuções por grafo

        //imprimir os resultados formatados (espaços iguais)
        System.out.printf("%-25s %10s %15s %15s\n", "Arquivo", "Vertices", "TempoMedio(ms)", "TamCaminho");

        for (String arquivo : arquivos) {
            try {
                // carregar o grafo
                int[] totalVertices = new int[1]; // vetor para retornar V ao invés de abrir o arquivo para leitura 2
                                                  // vezes
                int[][] edges = carregarGrafo("./grafos_txt/" + arquivo, totalVertices);
                int V = totalVertices[0]; // obter o V após ter feito a leitura
                int src = 1;
                int dest = V;

                long somaTempos = 0;
                int tamCaminho = 0;

                for (int i = 0; i < repeticoes; i++) {
                    long inicio = System.nanoTime();
                    List<Integer> path = dijkstra_minArestas(V, edges, src, dest);
                    // Imprimir o caminho mínimo
                    //System.out.println("Caminho Mínimo com menor número de arestas: " + path);
                    long fim = System.nanoTime();
                    somaTempos += (fim - inicio);
                    tamCaminho = path.size();
                }

                //obter o tempo médio (em ms) de execução
                double tempoMedioMs = (somaTempos / repeticoes) / 1_000_000.0;

                //imprimir formatado com espaçamento igual por coluna
                System.out.printf("%-25s %10d %15.3f %15d\n", arquivo, V, tempoMedioMs, tamCaminho);


            } catch (IOException e) {
                System.err.println("Erro ao ler " + arquivo + ": " + e.getMessage());
            }
        }
    }
}