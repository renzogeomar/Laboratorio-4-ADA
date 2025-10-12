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

        while (kruskal.aristas.size() < numAristas) {
            int o = rand.nextInt(numVertices);
            int d = rand.nextInt(numVertices);

            if (o != d) {
                String key;
                if (o < d) {
                    key = o + "-" + d;
                } else {
                    key = d + "-" + o;
                }

                if (!usadas.contains(key)) {
                    usadas.add(key);
                    int peso = rand.nextInt(20) + 1; 
                    Arista arista = new Arista(nodos.get(o), nodos.get(d), peso);
                    kruskal.agregarArista(arista);
                }
            }
        }

        kruskal.mostrarListaAdyacencia();

        List<Arista> mst = kruskal.generarMST(nodos);

        System.out.println("\nÁrbol de expansión mínima  con Kruskal:");
        for (Arista a : mst) {
            System.out.println(a);
        }

        System.out.println("\nNúmero de aristas procesadas para MST: " + mst.size());
    }
}
