package Ejercicio1;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

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

    public void prim(String startName) {
        Node start = getNode(startName);
        if (start == null) {
            System.out.println("El nodo inicial no existe.");
            return;
        }

        List<Node> visited = new ArrayList<>();
        List<Edge> mst = new ArrayList<>();
        int aristasProcesadas = 0;

        visited.add(start);

        while (visited.size() < nodes.size()) {
            Edge minEdge = null;

            // Buscar la arista más pequeña que conecte un nodo visitado con uno no visitado
            for (Edge e : edges) {
                Node from = e.getFrom();
                Node to = e.getTo();
                int weight = e.getWeight();

                if ((visited.contains(from) && !visited.contains(to)) ||
                    (visited.contains(to) && !visited.contains(from))) {

                        aristasProcesadas++;

                    if (minEdge == null || weight < minEdge.getWeight()) {
                        minEdge = e;
                    }
                }
            }

            if (minEdge == null) {
                System.out.println("El grafo no es conexo. No se puede generar MST completo.");
                break;
            }

            mst.add(minEdge);

            // Añadir el nodo no visitado
            if (visited.contains(minEdge.getFrom()) && !visited.contains(minEdge.getTo())) {
                visited.add(minEdge.getTo());
            } else {
                visited.add(minEdge.getFrom());
            }
        }

        // Mostrar el MST
        System.out.println("\nÁrbol de expansión mínima (Prim):");
        int pesoTotal = 0;
        for (Edge e : mst) {
            System.out.println(" - " + e);
            pesoTotal += e.getWeight();
        }
        System.out.println("Peso total del árbol: " + pesoTotal);
        System.out.println("Número de aristas procesadas: " + aristasProcesadas);

    }
}
