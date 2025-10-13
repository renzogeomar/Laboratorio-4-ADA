public class Edge implements Comparable<Edge>{
    private Node from;
    private Node to;
    private int weight;

    public Edge(Node from, Node to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge otra) {
        if (this.weight < otra.weight) return -1;
        else if (this.weight > otra.weight) return 1;
        else return 0;
    }

    @Override
    public String toString() {
        return from + " --(" + weight + ")--> " + to;
    }
}
