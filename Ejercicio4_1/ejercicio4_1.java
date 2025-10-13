public class ejercicio4_1 {
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.generarGrafoAleatorio(6, 10, true);
        graph.mostrarMatrizAdyacencia();

        long inicioMatriz = System.nanoTime();
        graph.primMatriz("N0");
        long finMatriz = System.nanoTime();
        double tiempoMatriz = (finMatriz - inicioMatriz) / 1_000_000.0;
        System.out.printf("Tiempo de ejecución (Prim matriz): %.4f ms%n", tiempoMatriz);

        long inicioLista = System.nanoTime();
        graph.primLista("N0");
        long finLista = System.nanoTime();
        double tiempoLista = (finLista - inicioLista) / 1_000_000.0;
        System.out.printf("Tiempo de ejecución (Prim lista): %.4f ms%n", tiempoLista);

    }
}
