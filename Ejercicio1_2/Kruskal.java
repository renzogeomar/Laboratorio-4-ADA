import java.util.*;

public class Kruskal {
    List<Arista> aristas;
    Map<Node, Node> padre;

    public Kruskal() {
        aristas = new ArrayList<>();
        padre = new HashMap<>();
    }

    public void agregarArista(Arista arista) {
        aristas.add(arista);
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
}
