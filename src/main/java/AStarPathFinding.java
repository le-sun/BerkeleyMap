import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AStarPathFinding {
    PriorityQueue<GraphNode> minPQ;
    double distance;
    double hDistance;
    double gDistance;
    double total;
    double startLon;
    double startLat;
    double endLon;
    double endLat;
    GraphNode startNode;
    GraphNode endNode;
    HashMap graphNodes;
    HashSet<GraphNode> visited;
    HashMap<GraphNode, Double> dist;
    HashMap<GraphNode, GraphNode> prev;

    public AStarPathFinding(double sLon, double sLat, double eLon, double eLat, HashMap gNodes) {
        this.graphNodes = gNodes;
        this.startLon = sLon;
        this.startLat = sLat;
        this.endLon = eLon;
        this.endLat = eLat;
        this.distance = 0;
        this.minPQ = new PriorityQueue<GraphNode>();
        this.startNode = getNearestNode(startLon, startLat);
        this.endNode = getNearestNode(endLon, endLat);
        this.visited = new HashSet<GraphNode>();
        this.dist = new HashMap<GraphNode, Double>();
        this.prev = new HashMap<GraphNode, GraphNode>();
    }

    public LinkedList<Long> solve() {

        minPQ.add(startNode);
        dist.put(startNode, 0.0);
        startNode.setPriority(distance + distanceFormula(startNode, endNode));
        GraphNode v = null;
        prev.put(startNode, startNode);

        while (!minPQ.isEmpty()) {
            v = minPQ.remove();
            if (visited.contains(v)) {
                continue;
            }
            visited.add(v);
            if (v.equals(endNode)) {
                break;
            }
            for (GraphNode c : getNeighbors(v)) {
                if (!dist.containsKey(c) || (dist.get(c) > (dist.get(v) + edge(v, c)))) {
                    //double distance = dist.get(v);
                    double gn = distanceFormula(c, v) + dist.get(v);
                    double hn = edge(c, endNode);
                    double fn = gn + hn;
                    c.setPriority(fn);
                    dist.put(c, dist.get(v) + distanceFormula(v, c));
                    minPQ.remove(c);
                    minPQ.add(c);
                    prev.put(c, v);
                }
            }
        }

        LinkedList<Long> route = new LinkedList<Long>();
        route.addLast(Long.parseLong(v.getId()));
        while (!v.equals(startNode)) {
            GraphNode value = prev.get(v);
            route.addFirst(Long.parseLong(value.getId()));
            v = value;
        }
        return route;
    }

    private GraphNode getNearestNode(double sLon, double sLat) {
        double minDistance = 1000000;
        GraphNode closestNode = null;
        for (Object o : graphNodes.values()) {
            GraphNode n = (GraphNode) o;
            if (n.getLon() == sLon && n.getLat() == sLat) {
                return n;
            }
            double lonDiff = Math.abs(n.getLon()) - Math.abs(sLon);
            double latDiff = n.getLat() - sLat;
            double tempDistance = Math.sqrt(lonDiff * lonDiff + latDiff * latDiff);

            if (tempDistance < minDistance) {
                minDistance = tempDistance;
                closestNode = n;
            }
        }
        return closestNode;
    }

    private Iterable<GraphNode> getNeighbors(GraphNode n) {
        LinkedList<GraphNode> neighbors = new LinkedList<GraphNode>();
        GraphNode fakeNullNode = new GraphNode("null", null, null, "null", 0, 0);

        for (Connection c : n.getConnections()) {
            GraphNode previous = c.getPreviousConnection();
            GraphNode next = c.getNextConnection();

            if (!previous.equals(fakeNullNode)) {
                neighbors.add(previous);
            }
            if (!next.equals(fakeNullNode)) {
                neighbors.add(next);
            }
        }
        return neighbors;
    }

    private double edge(GraphNode n1, GraphNode n2) {
        double d;
        double n1Lon = n1.getLon();
        double n1Lat = n1.getLat();
        double n2Lon = n2.getLon();
        double n2Lat = n2.getLat();
        double difference1 = n2Lon - n1Lon;
        double difference2 = n2Lat - n1Lat;

        d = Math.sqrt((difference1 * difference1) + (difference2 * difference2));
        return d;
    }

    private double distanceFormula(GraphNode n1, GraphNode n2) {
        double n1Lon = n1.getLon();
        double n1Lat = n1.getLat();
        double n2Lon = n2.getLon();
        double n2Lat = n2.getLat();
        double result = Math.sqrt(Math.pow(n1Lon - n2Lon, 2) + Math.pow(n1Lat - n2Lat, 2));
        return result;
    }
}
