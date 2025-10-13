package Ejercicio3;

import java.util.List;

public class ejercicio3 {
    public static void main(String[] args) {
        comparacion(10);
        comparacion(50);
        comparacion(100);
    }    

    public static void comparacion(int numVertices) {
        System.out.println("======================================================");
        System.out.println("Prueba para " + numVertices + " vertices");
        System.out.println("======================================================");

        // Definir la densidad del grafo. Un grafo más denso tiene más aristas.
        // ejemplo, V * (V-1) / 4 es un grafo con un 25% de las aristas posibles.
        int numAristas = (numVertices * (numVertices - 1)) / 4;
        
        // 1. Generar el grafo aleatorio
        Graph graph = new Graph();
        graph.generarGrafoAleatorio(numVertices, numAristas, true);
        System.out.println("Grafo generado con " + numVertices + " nodos y " + graph.getEdges().size() + " aristas.");

        // --- Medición de Prim ---
        long startTimePrim = System.nanoTime();
        List<Edge> mstPrim = graph.prim("N0"); // Empezamos desde el nodo "N0"
        long endTimePrim = System.nanoTime();
        long durationPrim = (endTimePrim - startTimePrim) / 1_000_000; // a milisegundos

        // --- Medición de Kruskal ---
        long startTimeKruskal = System.nanoTime();
        List<Edge> mstKruskal = graph.kruskal();
        long endTimeKruskal = System.nanoTime();
        long durationKruskal = (endTimeKruskal - startTimeKruskal) / 1_000_000; // a milisegundos

        // 3. Comparar resultados y mostrar tiempos
        System.out.println("\n--- RESULTADOS ---");
        System.out.println("Tiempo de ejecución de Prim:    " + durationPrim + " ms");
        System.out.println("Tiempo de ejecución de Kruskal: " + durationKruskal + " ms");

        // Verificación de consistencia: El peso total del MST debe ser el mismo
        int pesoPrim = mstPrim.stream().mapToInt(Edge::getWeight).sum();
        int pesoKruskal = mstKruskal.stream().mapToInt(Edge::getWeight).sum();
        
        System.out.println("Peso total del MST (Prim):    " + pesoPrim);
        System.out.println("Peso total del MST (Kruskal): " + pesoKruskal);

        if (pesoPrim == pesoKruskal) {
            System.out.println("-> CONSISTENCIA: Ambos algoritmos produjeron un MST con el mismo peso total. ");
        } else {
            System.out.println("-> INCONSISTENCIA: Los algoritmos produjeron MST con pesos diferentes. ");
        }
        System.out.println();
    }

}

