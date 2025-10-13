package Ejercicio5;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }


    public void addNode(String name) {
        Node node = new Node(name);
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void addEdge(String from, String to, int weight) {
        Node n1 = getNode(from);
        Node n2 = getNode(to);
        if (n1 == null || n2 == null) {
            System.out.println("Uno de los nodos no existe.");
            return;
        }

        // Evitar duplicados
        for (Edge e : edges) {
            if (e.getFrom().equals(n1) && e.getTo().equals(n2)) {
                return;
            }
        }

        edges.add(new Edge(n1, n2, weight));


    }

    public Node getNode(String name) {
        for (Node n : nodes) {
            if (n.getName().equals(name)) return n;
        }
        return null;
    }

    public void showGraph() {
        System.out.println("Nodos:");
        for (Node n : nodes) {
            System.out.println(" - " + n);
        }
        System.out.println("Aristas:");
        for (Edge e : edges) {
            System.out.println(" - " + e);
        }
    }

    public void generarGrafoAleatorio(int cantidadNodos, int cantidadAristas, boolean ponderado) {
        Random rand = new Random();

        // Crear los nodos
        for (int i = 0; i < cantidadNodos; i++) {
            addNode("N" + i); // N0, N1, N2...
        }

        // Crear las aristas aleatorias
        int totalIntentos = 0;
        while (edges.size() < cantidadAristas && totalIntentos < cantidadAristas * 5) {
            int i = rand.nextInt(cantidadNodos);
            int j = rand.nextInt(cantidadNodos);

            if (i == j) { // evitar lazos a sí mismo
                totalIntentos++;
                continue;
            }

            Node from = getNode("N" + i);
            Node to = getNode("N" + j);

            // (Si quieres permitir múltiples aristas, no verifiques duplicados)
            int peso = ponderado ? (rand.nextInt(9) + 1) : 0; // peso aleatorio 1–9
            addEdge(from.getName(), to.getName(), peso);

            totalIntentos++;
        }
    }

    private void printMSTResult(String algorithmName, List<Edge> mst) { // Nuevo método para imprimir resultados
        System.out.println("\nÁrbol de expansión mínima (" + algorithmName + "):");
        int totalWeight = 0;

        if (mst.size() < nodes.size() - 1 && !nodes.isEmpty()) {
            System.out.println("El grafo no es conexo. No se pudo generar un MST completo.");
        }

        for (Edge e : mst) {
            // System.out.println(" - " + e); // Opcional: imprimir cada arista
            totalWeight += e.getWeight();
        }
        System.out.println("Peso total del árbol: " + totalWeight);
    }

    public List<Edge> prim(String startName) {
        Node start = getNode(startName);
        if (start == null) {
            System.out.println("El nodo inicial no existe.");
            return new ArrayList<>(); // Devuelve lista vacía si hay error
        }

        List<Node> visited = new ArrayList<>(); // nodos visitados
        List<Edge> mst = new ArrayList<>(); // aristas del árbol de expansión mínima

        visited.add(start);

        while (visited.size() < nodes.size()) { // mientras no se hayan visitado todos los nodos
            Edge minEdge = null;
            for (Edge e : edges) {
                boolean fromVisited = visited.contains(e.getFrom()); // si el nodo de origen ya fue visitado
                boolean toVisited = visited.contains(e.getTo()); // si el nodo de destino ya fue visitado

                if (fromVisited && !toVisited || !fromVisited && toVisited) { // si uno de los nodos fue visitado y el otro no
                    if (minEdge == null || e.getWeight() < minEdge.getWeight()) { // si es la arista de menor peso encontrada
                        minEdge = e;
                    }
                }
            }

            if (minEdge == null) {
                break; // Grafo no conexo
            }

            mst.add(minEdge);

            if (!visited.contains(minEdge.getTo())) {
                visited.add(minEdge.getTo());
            } 
            else {
                visited.add(minEdge.getFrom());
            }
        }

        printMSTResult("Prim", mst);
        return mst;


    }

    public List<Edge> kruskal() {
        // 1. Crear una lista de aristas y ordenarla por peso
        List<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingInt(Edge::getWeight));

        // Lista para almacenar el Árbol de Expansión Mínima (MST)
        List<Edge> mst = new ArrayList<>();
        
        // 2. Inicializar la estructura Union-Find con todos los nodos del grafo
        UnionFind uf = new UnionFind(nodes);

        // 3. Recorrer las aristas ordenadas
        for (Edge edge : sortedEdges) {
            Node from = edge.getFrom();
            Node to = edge.getTo();

            // 4. Comprobar si añadir la arista forma un ciclo
            // Esto se hace verificando si los nodos 'from' y 'to' ya están en el mismo conjunto.
            if (uf.find(from) != uf.find(to)) {
                // Si no forman un ciclo, añadir la arista al MST
                mst.add(edge);
                // Unir los conjuntos de los dos nodos
                uf.union(from, to);
            }

            // Opcional: El algoritmo puede parar cuando el MST esté completo
            if (mst.size() == nodes.size() - 1) {
                break;
            }
        }

        printMSTResult("Kruskal", mst); // Usar nuevo método para imprimir resultados
        return mst;


    }
}
