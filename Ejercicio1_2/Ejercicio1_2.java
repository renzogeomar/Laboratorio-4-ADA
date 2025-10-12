import java.util.*;

public class Ejercicio1_2 {
    public static void main(String[] args) {
        int numNodos = 6;
        int numAristas = 10;
        Random rand = new Random();

        List<Node> nodos = new ArrayList<>(); // se crea nodods
        for (int i = 0; i < numNodos; i++) {
            nodos.add(new Node(i));
        }

        Set<String> usadas = new HashSet<>(); // aristas aleatorias
        Kruskal kruskal = new Kruskal();

        while (kruskal.aristas.size() < numAristas) {
            Node a = nodos.get(rand.nextInt(numNodos));
            Node b = nodos.get(rand.nextInt(numNodos));
            int peso = rand.nextInt(10) + 1;

            if (a != b) {
                String key1 = a.id + "-" + b.id;
                String key2 = b.id + "-" + a.id;

                if (!usadas.contains(key1) && !usadas.contains(key2)) {
                    usadas.add(key1);
                    kruskal.agregarArista(new Arista(a, b, peso));
                }
            }
        }

        System.out.println("Aristas generadas:");
        for (Arista a : kruskal.aristas)
            System.out.println(a);

        List<Arista> mst = kruskal.generarMST(nodos);

        System.out.println("\nÁrbol de Expansión Mínima (Kruskal):");
        for (Arista a : mst)
            System.out.println(a);

        System.out.println("Número de aristas procesadas: " + mst.size());
    }
}
