package Ejercicio5;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnionFind {
    private Map<Node, Node> parent;

    // Constructor: Inicializa cada nodo como su propio padre.
    public UnionFind(List<Node> nodes) {
        parent = new HashMap<>();
        for (Node node : nodes) {
            parent.put(node, node);
        }
    }

    // Encuentra el representante (raíz) del conjunto al que pertenece el nodo.
    // Aplica compresión de caminos para optimizar futuras búsquedas.
    public Node find(Node node) {
        if (parent.get(node).equals(node)) {
            return node;
        }
        // Compresión de caminos: hacer que el nodo apunte directamente a la raíz.
        Node root = find(parent.get(node));
        parent.put(node, root);
        return root;
    }

    // Une los conjuntos de dos nodos.
    public void union(Node node1, Node node2) {
        Node root1 = find(node1);
        Node root2 = find(node2);

        // Si no están ya en el mismo conjunto, los une.
        if (!root1.equals(root2)) {
            parent.put(root2, root1);
        }
    }
}