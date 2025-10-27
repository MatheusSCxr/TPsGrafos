import random
import os
import networkx as nx

# -------------------------------
# Geradores de grafos
# -------------------------------

#Grafo euleriano
def generate_eulerian_digraph(n: int, seed: int | None = 42) -> nx.DiGraph:
    """Gera um grafo direcionado euleriano (fortemente conexo, in-degree = out-degree)."""
    random.seed(seed)
    G = nx.DiGraph()
    G.add_nodes_from(range(1, n+1))

    # Ciclo base
    for i in range(1, n):
        G.add_edge(i, i+1, weight=random.randint(1, 100))
    G.add_edge(n, 1, weight=random.randint(1, 100))

    # Pares extras para manter equilíbrio
    for _ in range(n):
        u, v = random.sample(range(1, n+1), 2)
        if not G.has_edge(u, v):
            G.add_edge(u, v, weight=random.randint(1, 100))
            G.add_edge(v, u, weight=random.randint(1, 100))

    return G

#Grafo árvore
def generate_tree_digraph(n: int, seed: int | None = 42, orientation: str = "out") -> nx.DiGraph:
    """
    Gera uma árvore direcionada (arborescência) com n vértices e pesos aleatórios.
    - orientation="out": todas as arestas saem do raiz (caminho do raiz para qualquer nó).
    - orientation="in": todas as arestas entram no raiz (caminho de qualquer nó para o raiz).
    """
    if orientation not in {"out", "in"}:
        raise ValueError('orientation deve ser "out" ou "in"')

    random.seed(seed)
    # Gera árvore não-direcionada
    T = nx.random_labeled_tree(n=n, seed=seed)  # substitui random_tree

    root = 0
    BFS_tree = nx.bfs_tree(T, source=root)  # orientação natural: pai -> filho

    mapping = {i: i+1 for i in range(n)}
    G = nx.DiGraph()
    G.add_nodes_from(mapping.values())

    for parent, child in BFS_tree.edges():
        a = mapping[parent]
        b = mapping[child]
        if orientation == "out":
            src, dst = a, b
        else:  # "in"
            src, dst = b, a
        G.add_edge(src, dst, weight=random.randint(1, 100))

    assert G.number_of_edges() == n - 1
    return G



# -------------------------------
# Gravação no arquivo
# -------------------------------

def write_edge_list_txt(G: nx.DiGraph, filepath: str) -> None:
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(f"{G.number_of_nodes()} {G.number_of_edges()}\n")
        for u, v, data in G.edges(data=True):
            f.write(f"{u} {v} {data['weight']}\n")


# -------------------------------
# Main
# -------------------------------

def main() -> None:
    output_dir = "grafos_txt"
    seed = random.seed()  #obter grafos diferentes

    sizes = [1000, 10000, 25000, 50000, 100000]

    # Euleriano
    for n in sizes:
        print(f"Gerando Euleriano n={n}")
        G = generate_eulerian_digraph(n=n, seed=seed)
        filepath = os.path.join(output_dir, f"grafo_euleriano_{n}.txt")
        write_edge_list_txt(G, filepath)

    # Árvore (orientação "out")
    for n in sizes:
        print(f"Gerando Árvore n={n}")
        G = generate_tree_digraph(n=n, seed=seed, orientation="out")
        filepath = os.path.join(output_dir, f"grafo_arvore_{n}.txt")
        write_edge_list_txt(G, filepath)

if __name__ == "__main__":
    main()