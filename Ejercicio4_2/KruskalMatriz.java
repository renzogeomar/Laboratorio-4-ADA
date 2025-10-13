import java.util.*;

public class KruskalMatriz {
    int[][] matrizAdyacencia;
    List<Arista> aristas;
    Map<Node, Node> padre;

    public KruskalMatriz(int numVertices) {
        matrizAdyacencia = new int[numVertices][numVertices];
        aristas = new ArrayList<>();
        padre = new HashMap<>();
    }

    public void agregarArista(Node origen, Node destino, int peso) {
        int o = origen.id;
        int d = destino.id;
        matrizAdyacencia[o][d] = peso;
        matrizAdyacencia[d][o] = peso;
    }

    public void generarListaAristas(List<Node> nodos) {
        aristas.clear();
        int n = nodos.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int peso = matrizAdyacencia[i][j];
                if (peso > 0) {
                    aristas.add(new Arista(nodos.get(i), nodos.get(j), peso));
                }
            }
        }
    }

    private Node encontrar(Node n) {
        if (padre.get(n) != n)
            padre.put(n, encontrar(padre.get(n)));
        return padre.get(n);
    }

    private void unir(Node a, Node b) {
        Node raizA = encontrar(a);
        Node raizB = encontrar(b);
        if (raizA != raizB)
            padre.put(raizA, raizB);
    }

    public List<Arista> generarMST(List<Node> nodos) {
        List<Arista> resultado = new ArrayList<>();
        for (Node n : nodos)
            padre.put(n, n);

        generarListaAristas(nodos);
        Collections.sort(aristas);

        for (Arista a : aristas) {
            if (encontrar(a.origen) != encontrar(a.destino)) {
                resultado.add(a);
                unir(a.origen, a.destino);
            }
            if (resultado.size() == nodos.size() - 1)
                break;
        }

        return resultado;
    }

    public void mostrarMatrizAdyacencia() {
        System.out.println("\nMatriz de Adyacencia:");
        for (int i = 0; i < matrizAdyacencia.length; i++) {
            for (int j = 0; j < matrizAdyacencia[i].length; j++) {
                System.out.print(matrizAdyacencia[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
