public class Node {
    int id;

    public Node(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "V" + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        return this.id == ((Node) obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
