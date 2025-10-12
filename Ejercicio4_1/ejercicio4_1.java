public class ejercicio4_1 {
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.generarGrafoAleatorio(6, 10, true);
        graph.mostrarMatrizAdyacencia();
        graph.primMatriz("N0");
       
    }
}