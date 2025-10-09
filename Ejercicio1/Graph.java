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
            addNode("N" + i); // los nombres serán N0, N1, N2...
        }

        // Crear las aristas
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

            // evitar duplicados 
            boolean existe = false;
            for (Edge e : edges) {
                if (e.getFrom().equals(from) && e.getTo().equals(to)) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                int peso = ponderado ? (rand.nextInt(9) + 1) : 0; // peso aleatorio 1–9
                addEdge(from.getName(), to.getName(), peso);
            }
            totalIntentos++;
        }
    }

    public void prim(String startName) {
        Node start = getNode(startName); // buscar el nodo inicial
        if (start == null) {
            System.out.println("El nodo inicial no existe.");
            return;
        }

        List<Node> visited = new ArrayList<>(); // nodos ya incluidos en el MST
        List<Edge> mst = new ArrayList<>(); // aristas del MST

        visited.add(start);

        while (visited.size() < nodes.size()) { // mientras no se hayan visitado todos los nodos
            Edge minEdge = null; // arista mínima encontrada

            // Buscar la arista más pequeña que conecte un nodo visitado con uno no visitado
            for (Edge e : edges) { // recorrer todas las aristas
                if (visited.contains(e.getFrom()) && !visited.contains(e.getTo())) { // conecta visitado con no visitado
                    if (minEdge == null || e.getWeight() < minEdge.getWeight()) { // es la más pequeña hasta ahora
                        minEdge = e; // actualizar la mínima
                    }
                }
            }

            if (minEdge == null) { // no se encontró ninguna arista válida
                System.out.println("El grafo no es conexo. No se puede generar MST completo.");
                break;
            }

            mst.add(minEdge); // agregar la arista al MST
            visited.add(minEdge.getTo()); // marcar el nodo destino como visitado
        }

        // Mostrar el MST
        System.out.println("\nÁrbol de expansión mínima (Prim):");
        int pesoTotal = 0;
        for (Edge e : mst) {
            System.out.println(" - " + e);
            pesoTotal += e.getWeight();
        }
        System.out.println("Peso total del árbol: " + pesoTotal);
    }
}
