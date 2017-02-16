import java.util.LinkedList;

public class MergeSort {
    private static Node getMin(LinkedList<Node> q1, LinkedList<Node> q2) {
        if (q1.isEmpty()) {
            return q2.removeFirst();
        } else if (q2.isEmpty()) {
            return q1.removeFirst();
        } else {
            double q1MinLon = q1.peekFirst().getTopX();
            double q1MinLat = q1.peekFirst().getTopY();
            double q2MinLon = q2.peekFirst().getTopX();
            double q2MinLat = q2.peekFirst().getTopY();

            if (q1MinLon > q2MinLon && q1MinLat > q2MinLat) {
                return q1.removeFirst();
            } else {
                return q2.removeFirst();
            }
        }
    }

    private static LinkedList<LinkedList<Node>> makeSingleItemQueues(LinkedList<Node> items) {
        LinkedList<LinkedList<Node>> queue = new LinkedList<LinkedList<Node>>();
        for (Node n : items) {
            LinkedList<Node> temp = new LinkedList<Node>();
            temp.add(n);
            queue.add(temp);
        }
        return queue;
    }

    private static LinkedList<Node> mergeSortedQueues(
            LinkedList<Node> q1, LinkedList<Node> q2) {
        LinkedList<Node> sortedQueue = new LinkedList<Node>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            sortedQueue.add(getMin(q1, q2));
        }
        return sortedQueue;
    }

    public static LinkedList<Node> mergeSort(
            LinkedList<Node> items) {
        if (items.size() <= 1) {
            return items;
        }
        LinkedList<LinkedList<Node>> singleItems = makeSingleItemQueues(items);
        LinkedList<Node> q1 = new LinkedList<Node>();
        LinkedList<Node> q2 = new LinkedList<Node>();
        for (int i = 0; i < items.size() / 2; i++) {
            q1.add(singleItems.removeFirst().peek());
        }
        for (int i = items.size() / 2; i < items.size(); i++) {
            q2.add(singleItems.removeFirst().peek());
        }
        q1 = mergeSort(q1);
        q2 = mergeSort(q2);
        items = mergeSortedQueues(q1, q2);
        return items;
    }
}
