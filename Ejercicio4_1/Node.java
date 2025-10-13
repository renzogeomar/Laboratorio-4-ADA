public class Node {
    private String name;
    
    public Node(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override 
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
