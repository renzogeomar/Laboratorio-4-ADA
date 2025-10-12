public class Arista implements Comparable<Arista> {
    Node origen, destino;
    int peso;

    public Arista(Node origen, Node destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public int compareTo(Arista otra) {
        return Integer.compare(this.peso, otra.peso);
    }

    public String toString() {
        return origen + " - " + destino + " (peso: " + peso + ")";
    }
}
