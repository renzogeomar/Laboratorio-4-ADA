package Ejercicio1;

public class ejercicio1 {
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.generarGrafoAleatorio(6, 10, true);
        graph.showGraph();
    }

    public static Graph prim(Graph graph, String startName) {
        Graph mst = new Graph();
        // 1️⃣ Agregar el nodo inicial al MST
        // 2️⃣ Mientras el MST no tenga todos los nodos del grafo original:
        //    a) Encontrar la arista de menor peso que conecte un nodo en el MST con un nodo fuera del MST
        //    b) Agregar esa arista y el nodo conectado al MST
        return mst;
    }

}

