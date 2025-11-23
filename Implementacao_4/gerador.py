import random
import networkx as nx

def salvar_grafo(G, nome_arquivo="grafo.txt"):
    """
    Salva o grafo em arquivo no formato:
    primeira linha -> total de vértices e total de arestas
    demais linhas -> origem destino capacidade
    """
    num_vertices = G.number_of_nodes()
    num_arestas = G.number_of_edges()
    with open(nome_arquivo, "w") as f:
        f.write(f"{num_vertices} {num_arestas}\n")
        for u, v, data in G.edges(data=True):
            f.write(f"{u} {v} {data['capacity']}\n")


def gerar_grafo_grade(n, capacidade_max=100):
    """
    Gera um grafo em grade n x n direcionado.
    Vértices numerados de 1 até n*n.
    Arestas ligam vizinhos (direita e baixo).
    Como grade naturalmente possui múltiplos caminhos,
    já garante pelo menos 2 caminhos de 1 até n*n.
    """
    G = nx.DiGraph()
    for i in range(n):
        for j in range(n):
            u = i * n + j + 1  # 1-based
            if j + 1 < n:  # direita
                v = i * n + (j+1) + 1
                G.add_edge(u, v, capacity=random.randint(1, capacidade_max))
            if i + 1 < n:  # baixo
                v = (i+1) * n + j + 1
                G.add_edge(u, v, capacity=random.randint(1, capacidade_max))
    return G


def gerar_grafo_aleatorio(num_vertices, num_arestas, capacidade_max=100):
    """
    Gera um grafo direcionado aleatório garantindo
    pelo menos dois caminhos da fonte (1) até o sumidouro (num_vertices).
    """
    G = nx.DiGraph()

    # Primeiro cria um caminho simples 1 -> 2 -> ... -> V
    for i in range(1, num_vertices):
        G.add_edge(i, i+1, capacity=random.randint(1, capacidade_max))

    # Depois cria um segundo caminho alternativo: 1 -> k -> V
    if num_vertices > 3:
        k = random.randint(2, num_vertices-1)
        G.add_edge(1, k, capacity=random.randint(1, capacidade_max))
        G.add_edge(k, num_vertices, capacity=random.randint(1, capacidade_max))

    # Adiciona arestas extras aleatórias
    while G.number_of_edges() < num_arestas:
        u = random.randint(1, num_vertices)
        v = random.randint(1, num_vertices)
        if u != v and not G.has_edge(u, v):
            G.add_edge(u, v, capacity=random.randint(1, capacidade_max))

    return G


# Geração automática de instâncias
if __name__ == "__main__":
    # Tipos de grafos e tamanhos
    tamanhos_grade = [10, 30, 45, 65, 90, 100]  # 100, 900, 2025, 4225, 8100, 10000 vértices
    tamanhos_aleatorio = [(100, 300), (1000, 5000), (2000, 10000), (4000, 20000), (8000, 40000),  (10000, 80000)] # (vértices, arestas)


    # Gerar grafos em gradew
    for i, n in enumerate(tamanhos_grade, 1):
        G = gerar_grafo_grade(n)
        salvar_grafo(G, f"./grafos_txt/grafo_grade_{i}.txt")

    # Gerar grafos aleatórios
    for i, (nv, na) in enumerate(tamanhos_aleatorio, 1):
        G = gerar_grafo_aleatorio(nv, na)
        salvar_grafo(G, f"./grafos_txt/grafo_aleatorio_{i}.txt")

    print("Todos os grafos foram gerados com sucesso!")