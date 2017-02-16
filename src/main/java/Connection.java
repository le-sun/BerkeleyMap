
public class Connection {
    GraphNode n1;
    GraphNode n2;

    public Connection(GraphNode n1, GraphNode n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public GraphNode getNextConnection() {
        return n2;
    }

    public GraphNode getPreviousConnection() {
        return n1;
    }
}
