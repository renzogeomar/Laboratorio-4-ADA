package Ejercicio5;
/*
 * -----------------------Probablema-----------------------
 * MINIMIZAR EL CABLEADO DE UN CHIP
 * 
 * Dentro de un microprocesador, hay millones de componentes cuyos puntos de conexión (llamados "pines" o "terminales") 
 * deben estar conectados por cables de metal microscópicos. Un grupo de pines que necesita 
 * compartir la misma señal eléctrica (por ejemplo, la señal del reloj del sistema) debe estar 
 * interconectado.
 * 
 */
public class ejercicio5 {
    public static void main(String[] args) {
        Graph pinGraph = new Graph();
        //Añadiendo de ejemplo 7 pines
        pinGraph.addNode("P0");
        pinGraph.addNode("P1");
        pinGraph.addNode("P2");
        pinGraph.addNode("P3");
        pinGraph.addNode("P4");
        pinGraph.addNode("P5");
        pinGraph.addNode("P6");

        //Añadiendo conexiones entre pines con sus respectivas longitudes (pesos)
        pinGraph.addEdge("P0", "P1", 4);
        pinGraph.addEdge("P0", "P2", 3);
        pinGraph.addEdge("P1", "P2", 1);
        pinGraph.addEdge("P1", "P3", 2);
        pinGraph.addEdge("P2", "P3", 4);
        pinGraph.addEdge("P3", "P4", 2);
        pinGraph.addEdge("P4", "P5", 6);
        pinGraph.addEdge("P5", "P6", 1);
        pinGraph.addEdge("P4", "P6", 3);
        pinGraph.addEdge("P2", "P5", 5);
        pinGraph.addEdge("P1", "P5", 7);
        pinGraph.addEdge("P3", "P6", 8);

        pinGraph.showGraph();
        
        // Ejecutando ambos algoritmos y midiendo tiempos
        System.out.println("======================================================");
        System.out.println("Comparación de algoritmos para minimizar cableado de pines");
        //Ejecutando Prim desde el pin P0
        long startTimePrim = System.nanoTime();
        pinGraph.prim("P0");
        long endTimePrim = System.nanoTime();
        long durationPrim = (endTimePrim - startTimePrim) / 1_000_000; // a milisegundos
        System.out.println("Tiempo de ejecución de Prim:    " + durationPrim + " ms");
        //Ejecutando Kruskal
        long startTimeKruskal = System.nanoTime();
        pinGraph.kruskal();
        long endTimeKruskal = System.nanoTime();
        long durationKruskal = (endTimeKruskal - startTimeKruskal) / 1_000_000; // a milisegundos
        System.out.println("Tiempo de ejecución de Kruskal: " + durationKruskal + " ms");


    }    

}

