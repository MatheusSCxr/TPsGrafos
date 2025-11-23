import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * Este código foi desenvolvido tomando como BASE o algoritmo de Dinic
 * apresentado no GeeksForGeeks:
 * https://www.geeksforgeeks.org/dsa/dinics-algorithm-maximum-flow/
 *
 */

// Classe auxiliar
class Aresta {
    public int v; // Vértice destino da aresta direcionada
                  // O vértice origem u pode ser obtido pelo índice na lista de adjacência.
    public int fluxo;   // fluxo atual
    public int c;       // capacidade da aresta
    public int ant;     // To store index of reverse
                        // Índice da aresta reversa na lista de adjacência

    public Aresta(int v, int fluxo, int c, int ant) {
        this.v = v;
        this.fluxo = fluxo;
        this.c = c;
        this.ant = ant;
    }
}

// Grafo residual
class Grafo {
    private int V;                // Total de vértices
    public int[] nivel;           // Nível de cada vértice (na busca em largura)
    private List<Aresta>[] adj;   // Lista de adjacência

    public Grafo(int V) {
        adj = new ArrayList[V + 1];
        for (int i = 1; i <= V; i++) {
            adj[i] = new ArrayList<Aresta>();
        }
        this.V = V;
        nivel = new int[V + 1];
    }

    // Adicionar aresta no grafo residual
    public void addAresta(int u, int v, int C) {
      
        //Aresta de avanço -> fluxo 0; capacidade c
        Aresta a = new Aresta(v, 0, C, adj[v].size());
      
        //Aresta de retorno -> fluxo 0; capacidade 0
        Aresta b = new Aresta(u, 0, 0, adj[u].size());

        adj[u].add(a);
        adj[v].add(b);
    }

    // Busca em largura -> procura a possibilidade de enviar mais fluxo de s para t e atribui os níveis
    public boolean BFS(int s, int t) {
        for (int i = 1; i <= V; i++) {
            nivel[i] = -1;
        }

        nivel[s] = 0;        // nivel do vértice fonte

        // Fila para percorrer os vértices
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(s);

        ListIterator<Aresta> i;
        while (q.size() != 0) {
            int u = q.poll();

            for (i = adj[u].listIterator(); i.hasNext();) {
                Aresta e = i.next();
                if (nivel[e.v] < 0 && e.fluxo < e.c) {
                    // Define nível do vértice vizinho como nível do pai + 1
                    nivel[e.v] = nivel[u] + 1;
                    q.add(e.v);
                }
            }
        }

        return nivel[t] < 0 ? false : true;
    }


    // Função baseada em DFS para enviar fluxo após a BFS
    // Ela tenta encontrar caminhos aumentantes de s até t
    // e envia o fluxo máximo possível por esses caminhos.
    public int enviarFluxo(int u, int fluxo, int t, int start[]) {
      
        // Encontrou o sumidouro (caso base)
        if (u == t) {
            return fluxo;
        }

        // Só segue arestas válidas (nível correto e capacidade disponível)
        for (; start[u] < adj[u].size(); start[u]++) {
            Aresta e = adj[u].get(start[u]);

            if (nivel[e.v] == nivel[u] + 1 && e.fluxo < e.c) {
                // Calcula fluxo mínimo possível até o destino
                int fluxo_atual = Math.min(fluxo, e.c - e.fluxo);

                // Chamada recursiva para continuar o caminho
                int fluxo_temp = enviarFluxo(e.v, fluxo_atual, t, start);
                
                // Se conseguiu enviar fluxo > 0
                if (fluxo_temp > 0) {
                    // Atualiza fluxo da aresta
                    e.fluxo += fluxo_temp;

                    // Atualiza fluxo da aresta de retorno
                    adj[e.v].get(e.ant).fluxo -= fluxo_temp;
                    return fluxo_temp;
                }
            }
        }

        return 0;
    }

    // Calcula o fluxo máximo no grafo usando o algoritmo de Dinic
    public int DinicMaxflow(int s, int t) {
        if (s == t) {
            return 0;
        }

        int total = 0;

        // Enquanto existir caminho aumentante de s até t
        while (BFS(s, t) == true) {
          
            // Vetor para controlar quais arestas já foram exploradas
            int[] start = new int[V + 1];

            // Enquanto ainda houver fluxo possível
            while (true) {
                int fluxo = enviarFluxo(s, Integer.MAX_VALUE, t, start);
                if (fluxo == 0) {
                    break; //se não houver fluxo break
                }
              
                // Soma fluxo encontrado ao total
                total += fluxo;
            }
        }

        // Retorna o fluxo máximo
        return total;
    }

    // Retorna todos os caminhos disjuntos encontrados após calcular o fluxo máximo
    // Cada caminho é armazenado junto com a quantidade de vezes que foi encontrado
    public Map<List<Integer>, Integer> listarCaminhosComFluxo(int s, int t) {
        Map<List<Integer>, Integer> caminhos = new HashMap<>();

        boolean encontrou = true;
        while (encontrou) {
            List<Integer> caminho = new ArrayList<>();
            caminho.add(s);
            encontrou = dfsCaminho(s, t, caminho);
            if (encontrou) {
                // Reduz fluxo em 1 unidade ao longo do caminho encontrado
                reduzirFluxo(caminho);

                // Agrupa caminhos iguais e conta quantas vezes aparecem
                caminhos.merge(new ArrayList<>(caminho), 1, Integer::sum);
            }
        }
        return caminhos;
    }

    // DFS que encontra um caminho de s até t seguindo apenas arestas com fluxo > 0
    private boolean dfsCaminho(int u, int t, List<Integer> caminho) {
        if (u == t) return true;
        for (Aresta e : adj[u]) {
            if (e.fluxo > 0) {
                caminho.add(e.v);
                boolean achou = dfsCaminho(e.v, t, caminho);
                if (achou) return true;
                caminho.remove(caminho.size()-1);
            }
        }
        return false;
    }

    // Reduz fluxo em 1 unidade ao longo do caminho encontrado
    // Isso permite contabilizar múltiplos caminhos disjuntos
    private void reduzirFluxo(List<Integer> caminho) {
        for (int i = 0; i < caminho.size()-1; i++) {
            int u = caminho.get(i);
            int v = caminho.get(i+1);
            for (Aresta e : adj[u]) {
                if (e.v == v && e.fluxo > 0) {
                    e.fluxo -= 1;
                    adj[v].get(e.ant).fluxo += 1; // ajusta aresta de retorno
                    break;
                }
            }
        }
    }
}


public class grafos_4 {

    // Leitura do arquivo de grafo
    // Lê um arquivo de texto contendo a definição do grafo
    // Primeira linha: número de vértices e número de arestas
    // Linhas seguintes: origem destino capacidade
    public static Grafo carregarGrafo(String filepath, int[] totalVertices) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            
            // obter numero de vértices e numero de arestas do grafo
            String linha = br.readLine();
            String[] valores_iniciais = linha.trim().split("\\s+");
            int V = Integer.parseInt(valores_iniciais[0]);
            int M = Integer.parseInt(valores_iniciais[1]);
            totalVertices[0] = V; // obter esse valor fora da função ao invés de ler 2 vezes

            Grafo G = new Grafo(V);

            // ler e montar o grafo
            while ((linha = br.readLine()) != null) {
                String[] valores = linha.trim().split("\\s+");
                if (valores.length == 3) {
                    int origem = Integer.parseInt(valores[0]);
                    int destino = Integer.parseInt(valores[1]);
                    int capacidade = Integer.parseInt(valores[2]);
                    G.addAresta(origem, destino, capacidade);
                }
            }
            return G;
        }
    }

    public static void main(String[] args) {
        //grafos que serão testados
        String[] arquivos = {
                "grafo_test.txt",  
        };

        int repeticoes = 1; // execuções por grafo


        for (String arquivo : arquivos) {
            try {
                //DEBUG
                //System.out.println("Grafo carregado com sucesso!");

                int src = 1; //fonte
                int V = 0; //será carregado posteriormente

                long somaTempos = 0;

                int fluxo_max = 0;

                for (int i = 0; i < repeticoes; i++) {
                    // refazer o grafo a cada repetição
                    int[] totalVertices = new int[1]; // vetor para retornar V ao invés de abrir o arquivo para leitura 2 vezes
                    Grafo G = carregarGrafo("./grafos_txt/" + arquivo, totalVertices);
                    V = totalVertices[0]; // obter o V do vetor após ter feito a leitura
                    
                    int dest = V; //sumidouro

                    long inicio = System.nanoTime();
                    
                    fluxo_max = G.DinicMaxflow(src, dest);

                    long fim = System.nanoTime();

                    somaTempos += (fim - inicio);


                    //IMPRIMIR CAMINHOS
                    if (i == 0){
                        // listar caminhos e o fluxo associado
                        System.out.println("Caminhos disjuntos encontrados:");

                        Map<List<Integer>, Integer> caminhos = G.listarCaminhosComFluxo(src, dest);
                        for (Map.Entry<List<Integer>, Integer> entry : caminhos.entrySet()) {
                            System.out.println(entry.getKey() + " | fluxo = " + entry.getValue());
                        }
                    }
                }
                //imprimir os resultados formatados (espaços iguais)
                System.out.printf("%-25s %10s %15s %15s\n", "Arquivo", "Vertices", "TempoMedio(ms)", "FluxoMax");

                //obter o tempo médio (em ms) de execução
                double tempoMedioMs = (somaTempos / repeticoes) / 1_000_000.0;

                //imprimir formatado com espaçamento igual por coluna
                System.out.printf("%-25s %10d %15.3f %15d\n", arquivo, V, tempoMedioMs, fluxo_max);

            } catch (IOException e) {
                System.err.println("Erro ao ler " + arquivo + ": " + e.getMessage());
            }
        }
    }
}