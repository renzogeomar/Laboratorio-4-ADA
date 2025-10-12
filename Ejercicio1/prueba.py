import matplotlib.pyplot as plt
import networkx as nx
import time

# Crear un grafo de ejemplo
G = nx.Graph()
G.add_weighted_edges_from([
    (0, 1, 4), (0, 2, 3), (1, 2, 1), (1, 3, 2), (2, 3, 4), (3, 4, 2)
])

# Algoritmo de Prim
def prim_animation(graph):
    mst = nx.Graph()  # Árbol de expansión mínima
    visited = set()
    edges = []

    # Seleccionar nodo inicial arbitrario
    start_node = list(graph.nodes)[0]
    visited.add(start_node)

    while len(visited) < len(graph.nodes):
        # Encontrar la arista de menor peso que conecta un nodo visitado con uno no visitado
        min_edge = None
        for u in visited:
            for v, data in graph[u].items():
                if v not in visited:
                    if min_edge is None or data['weight'] < min_edge[2]:
                        min_edge = (u, v, data['weight'])

        if min_edge:
            u, v, weight = min_edge
            mst.add_edge(u, v, weight=weight)
            visited.add(v)
            edges.append((u, v))

            # Dibujar el grafo en cada paso
            plt.clf()
            pos = nx.spring_layout(graph)
            nx.draw(graph, pos, with_labels=True, node_color='lightblue', edge_color='gray')
            nx.draw(mst, pos, with_labels=True, node_color='lightgreen', edge_color='red', width=2)
            plt.title(f"Paso: {len(edges)} - Añadiendo arista ({u}, {v})")
            plt.pause(1)  # Pausa para animación

    plt.show()

prim_animation(G)
