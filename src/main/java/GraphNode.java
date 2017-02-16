import java.util.HashSet;
import java.util.LinkedHashSet;

public class GraphNode implements Comparable<GraphNode> {
    GraphNode prev;
    GraphNode next;
    String activeState;
    String ref;
    double lon;
    double lat;
    int size;
    LinkedHashSet<Connection> connections;
    double priority;
    double distanceFromStart;
    double distanceFromEnd;

    public GraphNode(String ref, GraphNode p, GraphNode n, String aS, double lon, double lat) {
        this.ref = ref;
        this.prev = p;
        this.next = n;
        this.activeState = aS;
        this.lon = lon;
        this.lat = lat;
        this.connections = new LinkedHashSet<Connection>();
    }

    public void setPrev(GraphNode prev) {
        this.prev = prev;
    }

    public void setNext(GraphNode next) {
        this.next = next;
    }

    public String getId() {
        return this.ref;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public int getSize() {
        return size;
    }

    public boolean hasConnections() {
        return !connections.isEmpty();
    }

    public void connect(GraphNode n1, GraphNode n2) {
        connections.add(new Connection(n1, n2));
    }

    public HashSet<Connection> getConnections() {
        return connections;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public void setPriority(double p) {
        this.priority = p;
    }

    public double getPriority() {
        return this.priority;
    }

    public String toString() {
        return this.ref;
    }

    @Override
    public int compareTo(GraphNode n) {
        Double thisPriority = this.priority;
        Double nPriority = n.priority;
        int result = thisPriority.compareTo(nPriority);

        if (result < 0) {
            return result;
        } else if (result > 0) {
            return result;
        } else if (result == 0) {
            Double thisDistanceFromEnd = this.distanceFromEnd;
            Double nDistanceFromEnd = n.distanceFromEnd;
            int result2 = thisDistanceFromEnd.compareTo(nDistanceFromEnd);

            if (result2 < 0) {
                return result;
            } else if (result2 > 0) {
                return result;
            } else if (result2 == 0) {
                Double thisDistanceFromStart = this.distanceFromStart;
                Double nDistanceFromStart = n.distanceFromStart;
                int result3 = thisDistanceFromStart.compareTo(nDistanceFromStart);

                if (result3 < 0) {
                    return result;
                } else if (result3 >= 0) {
                    return result;
                }
            }
        }
        System.out.print("this should never appear");
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GraphNode)) {
            return false;
        }

        GraphNode graphNode = (GraphNode) o;

        if (Double.compare(graphNode.getLon(), getLon()) != 0) {
            return false;
        }
        if (Double.compare(graphNode.getLat(), getLat()) != 0) {
            return false;
        }
        if (ref != null ? !ref.equals(graphNode.ref) : graphNode.ref != null) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = ref != null ? ref.hashCode() : 0;
        temp = Double.doubleToLongBits(getLon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
