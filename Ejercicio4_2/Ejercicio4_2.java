import java.util.*;

public class Ejercicio4_2 {
    public static void main(String[] args) {
        int numVertices = 6;
        int numAristas = 10;

        KruskalList kruskal = new KruskalList();

        List<Node> nodos = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            nodos.add(new Node(i));
        }

        Random rand = new Random();
        Set<String> usadas = new HashSet<>();

        // Generar aristas aleatorias
        while (kruskal.aristas.size() < numAristas) {
            int o = rand.nextInt(numVertices);
            int d = rand.nextInt(numVertices);

            if (o != d) {
                String key = (Math.min(o, d)) + "-" + (Math.max(o, d));
                if (!usadas.contains(key)) {
                    usadas.add(key);
                    int peso = rand.nextInt(20) + 1;
                    Arista arista = new Arista(nodos.get(o), nodos.get(d), peso);
                    kruskal.agregarArista(arista);
                }
            }
        }

        System.out.println("========== KRUSKAL CON LISTA DE ADYACENCIA ==========");
        kruskal.mostrarListaAdyacencia();

        long inicioLista = System.nanoTime();
        List<Arista> mstLista = kruskal.generarMST(nodos);
        long finLista = System.nanoTime();

        System.out.println("\nÁrbol de expansión mínima con Kruskal (lista):");
        for (Arista a : mstLista) {
            System.out.println(a);
        }

        System.out.println("\nNúmero de aristas en el MST (lista): " + mstLista.size());
        System.out.println("Tiempo de ejecución (lista): " + (finLista - inicioLista) / 1_000_000.0 + " ms");

        // ---------------------- KRUSKAL CON MATRIZ ----------------------
        System.out.println("\n========== KRUSKAL CON MATRIZ DE ADYACENCIA ==========");
        KruskalMatriz kruskalMatriz = new KruskalMatriz(numVertices);

        // Reutilizamos las mismas aristas
        for (Arista a : kruskal.aristas) {
            kruskalMatriz.agregarArista(a.origen, a.destino, a.peso);
        }

        kruskalMatriz.mostrarMatrizAdyacencia();

        long inicioMatriz = System.nanoTime();
        List<Arista> mstMatriz = kruskalMatriz.generarMST(nodos);
        long finMatriz = System.nanoTime();

        System.out.println("\nÁrbol de expansión mínima con Kruskal (matriz):");
        for (Arista a : mstMatriz) {
            System.out.println(a);
        }

        System.out.println("\nNúmero de aristas en el MST (matriz): " + mstMatriz.size());
        System.out.println("Tiempo de ejecución (matriz): " + (finMatriz - inicioMatriz) / 1_000_000.0 + " ms");

    }
}
