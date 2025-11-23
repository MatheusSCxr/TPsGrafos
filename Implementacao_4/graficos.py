import pandas as pd
import matplotlib.pyplot as plt

# -----------------------------
# 1. Inserir todos os dados (runs 1 a 10)
# -----------------------------
dados = [
# run1
(1,"Euleriano",1000,1.573,11),(1,"Euleriano",10000,5.646,16),(1,"Euleriano",25000,5.978,7),(1,"Euleriano",50000,24.436,16),(1,"Euleriano",100000,47.303,13),
(1,"Árvore",1000,0.065,44),(1,"Árvore",10000,1.403,112),(1,"Árvore",25000,2.491,147),(1,"Árvore",50000,6.337,300),(1,"Árvore",100000,17.530,849),
# run2
(2,"Euleriano",1000,1.315,10),(2,"Euleriano",10000,6.511,16),(2,"Euleriano",25000,11.853,17),(2,"Euleriano",50000,6.400,13),(2,"Euleriano",100000,58.010,20),
(2,"Árvore",1000,0.119,84),(2,"Árvore",10000,1.052,223),(2,"Árvore",25000,2.846,156),(2,"Árvore",50000,4.031,164),(2,"Árvore",100000,10.265,274),
# run3
(3,"Euleriano",1000,1.460,10),(3,"Euleriano",10000,5.319,12),(3,"Euleriano",25000,5.225,8),(3,"Euleriano",50000,9.453,14),(3,"Euleriano",100000,43.764,16),
(3,"Árvore",1000,0.084,49),(3,"Árvore",10000,1.483,256),(3,"Árvore",25000,4.921,280),(3,"Árvore",50000,6.323,186),(3,"Árvore",100000,18.063,427),
# run4
(4,"Euleriano",1000,0.760,8),(4,"Euleriano",10000,3.394,8),(4,"Euleriano",25000,12.404,14),(4,"Euleriano",50000,18.231,15),(4,"Euleriano",100000,50.857,13),
(4,"Árvore",1000,0.048,23),(4,"Árvore",10000,0.935,137),(4,"Árvore",25000,3.419,298),(4,"Árvore",50000,7.280,375),(4,"Árvore",100000,9.512,215),
# run5
(5,"Euleriano",1000,1.208,11),(5,"Euleriano",10000,5.285,13),(5,"Euleriano",25000,10.767,10),(5,"Euleriano",50000,28.082,17),(5,"Euleriano",100000,32.029,16),
(5,"Árvore",1000,0.126,93),(5,"Árvore",10000,1.091,201),(5,"Árvore",25000,1.339,66),(5,"Árvore",50000,4.820,195),(5,"Árvore",100000,6.949,235),
# run6
(6,"Euleriano",1000,0.730,6),(6,"Euleriano",10000,5.293,9),(6,"Euleriano",25000,7.708,13),(6,"Euleriano",50000,28.420,18),(6,"Euleriano",100000,36.265,14),
(6,"Árvore",1000,0.134,62),(6,"Árvore",10000,0.588,56),(6,"Árvore",25000,1.902,211),(6,"Árvore",50000,4.198,301),(6,"Árvore",100000,14.200,432),
# run7
(7,"Euleriano",1000,1.246,6),(7,"Euleriano",10000,6.150,7),(7,"Euleriano",25000,9.129,12),(7,"Euleriano",50000,24.241,15),(7,"Euleriano",100000,30.437,14),
(7,"Árvore",1000,0.078,67),(7,"Árvore",10000,0.912,61),(7,"Árvore",25000,2.934,169),(7,"Árvore",50000,3.558,177),(7,"Árvore",100000,19.557,432),
# run8
(8,"Euleriano",1000,1.607,11),(8,"Euleriano",10000,5.570,9),(8,"Euleriano",25000,16.457,14),(8,"Euleriano",50000,7.920,9),(8,"Euleriano",100000,50.946,15),
(8,"Árvore",1000,0.095,51),(8,"Árvore",10000,0.704,77),(8,"Árvore",25000,4.678,366),(8,"Árvore",50000,11.139,342),(8,"Árvore",100000,18.831,444),
# run9
(9,"Euleriano",1000,1.344,12),(9,"Euleriano",10000,6.417,18),(9,"Euleriano",25000,11.306,12),(9,"Euleriano",50000,7.201,10),(9,"Euleriano",100000,53.262,12),
(9,"Árvore",1000,0.099,59),(9,"Árvore",10000,1.433,179),(9,"Árvore",25000,4.056,397),(9,"Árvore",50000,6.651,202),(9,"Árvore",100000,4.323,216),
# run10
(10,"Euleriano",1000,1.177,5),(10,"Euleriano",10000,3.211,8),(10,"Euleriano",25000,16.842,15),(10,"Euleriano",50000,17.870,12),(10,"Euleriano",100000,62.222,14),
(10,"Árvore",1000,0.044,38),(10,"Árvore",10000,0.750,139),(10,"Árvore",25000,2.264,233),(10,"Árvore",50000,3.264,151),(10,"Árvore",100000,12.811,288),
]

# -----------------------------
# 2. Criar DataFrame
# -----------------------------
df = pd.DataFrame(dados, columns=["Run","Tipo","Vertices","Tempo","Caminho"])

# -----------------------------
# 3. Estatísticas agregadas
# -----------------------------
resumo = df.groupby(["Tipo","Vertices"]).agg(
    TempoMedio=("Tempo","mean"),
    CaminhoMedio=("Caminho","mean"),
    CaminhoMin=("Caminho","min"),
    CaminhoMax=("Caminho","max")
).reset_index()

print(resumo)

# -----------------------------
# 4. Gráficos
# -----------------------------

# Tempo médio
plt.figure(figsize=(8,5))
for tipo in resumo["Tipo"].unique():
    subset = resumo[resumo["Tipo"]==tipo]
    plt.plot(subset["Vertices"], subset["TempoMedio"], marker="o", label=tipo)
plt.xscale("log")
plt.xlabel("Número de vértices (log)")
plt.ylabel("Tempo médio (ms)")
plt.title("Tempo médio do Dijkstra por tipo de grafo")
plt.legend()
plt.grid(True)
plt.show()

# Caminho médio
plt.figure(figsize=(8,5))
for tipo in resumo["Tipo"].unique():
    subset = resumo[resumo["Tipo"]==tipo]
    plt.plot(subset["Vertices"], subset["CaminhoMedio"], marker="o", label=tipo)
plt.xscale("log")
plt.xlabel("Número de vértices (log)")
plt.ylabel("Tamanho médio do caminho")
plt.title("Tamanho médio do caminho por tipo de grafo")
plt.legend()
plt.grid(True)
plt.show()

# Caminho min e max
# Caminho min e max
plt.figure(figsize=(8,5))
for tipo in resumo["Tipo"].unique():
    subset = resumo[resumo["Tipo"]==tipo]
    # faixa entre mínimo e máximo
    plt.fill_between(subset["Vertices"], subset["CaminhoMin"], subset["CaminhoMax"], alpha=0.3, label=f"{tipo} (min–max)")
    # linha do valor médio
    plt.plot(subset["Vertices"], subset["CaminhoMedio"], marker="o", label=f"{tipo} (médio)")

plt.xscale("log")
plt.xlabel("Número de vértices (log)")
plt.ylabel("Tamanho do caminho")
plt.title("Faixa de variação do tamanho do caminho (mín–máx)")
plt.legend()
plt.grid(True)
plt.show()

# -----------------------------
# Gráficos separados por tipo
# -----------------------------

tipos = resumo["Tipo"].unique()

for tipo in tipos:
    subset = resumo[resumo["Tipo"] == tipo]

    # Tempo médio
    plt.figure(figsize=(8,5))
    plt.plot(subset["Vertices"], subset["TempoMedio"], marker="o", color="blue")
    plt.xscale("log")
    plt.xlabel("Número de vértices (log)")
    plt.ylabel("Tempo médio (ms)")
    plt.title(f"Tempo médio do Dijkstra – {tipo}")
    plt.grid(True)
    plt.show()

    # Caminho médio
    plt.figure(figsize=(8,5))
    plt.plot(subset["Vertices"], subset["CaminhoMedio"], marker="o", color="green")
    plt.xscale("log")
    plt.xlabel("Número de vértices (log)")
    plt.ylabel("Tamanho médio do caminho")
    plt.title(f"Tamanho médio do caminho – {tipo}")
    plt.grid(True)
    plt.show()

    # Caminho min e max
    plt.figure(figsize=(8,5))
    plt.fill_between(subset["Vertices"], subset["CaminhoMin"], subset["CaminhoMax"], alpha=0.3, color="orange", label="Faixa min–máx")
    plt.plot(subset["Vertices"], subset["CaminhoMedio"], marker="o", color="red", label="Médio")
    plt.xscale("log")
    plt.xlabel("Número de vértices (log)")
    plt.ylabel("Tamanho do caminho")
    plt.title(f"Variação do tamanho do caminho – {tipo}")
    plt.legend()
    plt.grid(True)
    plt.show()
