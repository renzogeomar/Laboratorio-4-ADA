import java.util.*;

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

    public void primMatriz(String startName) {
        Node start = getNode(startName);
        if (start == null) {
            System.out.println("El nodo inicial no existe.");
            return;
        }

        int n = nodes.size();
        int[][] matriz = new int[n][n]; // Matriz de adyacencia (0 = sin conexion)

        // llenar la matriz con los pesos
        for (Edge e : edges) {
            int i = nodes.indexOf(e.getFrom());
            int j = nodes.indexOf(e.getTo());
            matriz[i][j] = e.getWeight();
            matriz[j][i] = e.getWeight();
        }

        boolean[] visitado = new boolean[n];
        int[] minPeso = new int[n];
        int[] padre = new int[n];

        for (int i = 0; i < n; i++) {
            minPeso[i] = Integer.MAX_VALUE;
            padre[i] = -1;
        }

        int startIndex = nodes.indexOf(start);
        minPeso[startIndex] = 0;

        for (int count = 0; count < n - 1; count++) {
            // Buscar el nodo no visitado con menor peso
            int u = -1;
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && minPeso[i] < min) {
                    min = minPeso[i];
                    u = i;
                }
            }

            if (u == -1) break;

            visitado[u] = true;

            // Actualizar pesos de vecinos
            for (int v = 0; v < n; v++) {
                if (matriz[u][v] != 0 && !visitado[v] && matriz[u][v] < minPeso[v]) {
                    minPeso[v] = matriz[u][v];
                    padre[v] = u;
                }
            }
        }

        // Mostrar el MST
        System.out.println("\nÁrbol de expansión mínima (Prim con matriz):");
        int pesoTotal = 0;
        for (int i = 0; i < n; i++) {
            if (padre[i] != -1) {
                Node desde = nodes.get(padre[i]);
                Node hasta = nodes.get(i);
                int peso = matriz[padre[i]][i];
                System.out.println(" - " + desde + " --(" + peso + ")--> " + hasta);
                pesoTotal += peso;
            }
        }

        System.out.println("Peso total del árbol: " + pesoTotal);
    }

    public void mostrarMatrizAdyacencia() {
        int n = nodes.size();
        int[][] matriz = new int[n][n];

        // Llenar la matriz con los pesos
        for (Edge e : edges) {
            int i = nodes.indexOf(e.getFrom());
            int j = nodes.indexOf(e.getTo());
            matriz[i][j] = e.getWeight();
            matriz[j][i] = e.getWeight(); // grafo no dirigido
        }

        System.out.println("\nMatriz de adyacencia:");
        
        // Imprimir encabezado
        System.out.print("     ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%4s", nodes.get(i).getName());
        }
        System.out.println();

        // Imprimir filas
        for (int i = 0; i < n; i++) {
            System.out.printf("%4s", nodes.get(i).getName());
            for (int j = 0; j < n; j++) {
                System.out.printf("%4d", matriz[i][j]);
            }
            System.out.println();
        }
    }

    public void primLista(String startName) {
        Node start = getNode(startName);
        if (start == null) {
            System.out.println("El nodo inicial no existe.");
            return;
        }

        Map<Node, List<Edge>> listaAdyacencia = new HashMap<>();

        // Construir lista de adyacencia
        for (Edge e : edges) {
            listaAdyacencia.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(e);
            listaAdyacencia.computeIfAbsent(e.getTo(), k -> new ArrayList<>())
                .add(new Edge(e.getTo(), e.getFrom(), e.getWeight())); // arista opuesta
        }

        Set<Node> visitados = new HashSet<>();
        PriorityQueue<Edge> cola = new PriorityQueue<>();

        visitados.add(start);
        List<Edge> adyacentesStart = listaAdyacencia.get(start);
        if (adyacentesStart != null)
            cola.addAll(adyacentesStart);

        List<Edge> mst = new ArrayList<>();
        int pesoTotal = 0;

        while (!cola.isEmpty() && mst.size() < nodes.size() - 1) {
            Edge menor = cola.poll();
            if (visitados.contains(menor.getTo())) continue;

            visitados.add(menor.getTo());
            mst.add(menor);
            pesoTotal += menor.getWeight();

            List<Edge> adyacentes = listaAdyacencia.get(menor.getTo());
            if (adyacentes != null) {
                for (Edge vecino : adyacentes) {
                    if (!visitados.contains(vecino.getTo())) {
                        cola.add(vecino);
                    }
                }
            }
        }

        System.out.println("\nÁrbol de expansión mínima (Prim con lista):");
        for (Edge e : mst) {
            System.out.println(" - " + e.getFrom() + " --(" + e.getWeight() + ")--> " + e.getTo());
        }
        System.out.println("Peso total del árbol: " + pesoTotal);
    }


}
