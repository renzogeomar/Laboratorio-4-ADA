public class Arista implements Comparable<Arista> {
    Node origen;
    Node destino;
    int peso;

    public Arista(Node origen, Node destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    @Override
    public int compareTo(Arista otra) {
        return Integer.compare(this.peso, otra.peso);
    }

    @Override
    public String toString() {
        return "(" + origen + " - " + destino + ", peso: " + peso + ")";
    }
}
