package Ejercicio3;

public class ejercicio3 {
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.generarGrafoAleatorio(6, 10, true);
        graph.showGraph();
        graph.prim("N0");
        graph.kruskal();
    }



}

