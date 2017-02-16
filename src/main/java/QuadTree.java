import java.util.LinkedList;
import java.awt.geom.Rectangle2D;

public class QuadTree {
    private Node root;
    private int height;

    public QuadTree(String path, double tX, double tY, double bX, double bY) {
        this.root = new Node(path, null, tX, tY, bX, bY);
        this.height = 0; //Needed? Might delete later

        Node topLeft = new Node("1", root, tX, tY, ((bX - tX) / 2) + tX, (bY + tY) / 2);
        Node topRight = new Node("2", root, ((bX - tX) / 2) + tX, tY, bX, (bY + tY) / 2);
        Node bottomLeft = new Node("3", root, tX, (tY + bY) / 2, ((bX - tX) / 2) + tX, bY);
        Node bottomRight = new Node("4", root, ((bX - tX) / 2) + tX, (tY + bY) / 2, bX, bY);

        root.setTopLeft(topLeft);
        root.setTopRight(topRight);
        root.setBottomLeft(bottomLeft);
        root.setBottomRight(bottomRight);

        Node topLeftChild = root.getTopLeft();
        Node topRightChild = root.getTopRight();
        Node bottomLeftChild = root.getBottomLeft();
        Node bottomRightChild = root.getBottomRight();
        treeBuilderHelper(topLeftChild, topLeftChild, topLeftChild, topLeftChild);
        treeBuilderHelper(topRightChild, topRightChild, topRightChild, topRightChild);
        treeBuilderHelper(bottomLeftChild, bottomLeftChild, bottomLeftChild, bottomLeftChild);
        treeBuilderHelper(bottomRightChild, bottomRightChild, bottomRightChild, bottomRightChild);

    }

    public Iterable<Node> children() {
        LinkedList<Node> children = new LinkedList<Node>();
        children.add(root.getTopLeft());
        children.add(root.getTopRight());
        children.add(root.getBottomLeft());
        children.add(root.getBottomRight());
        return children;
    }

    public boolean tI(Node n, double tX, double tY,
                      double bX, double bY, double fPP, LinkedList<Node> tiles) {
        double tileX = Math.abs(n.getTopX()) - Math.abs(n.getBottomX());
        double tileY = n.getTopY() - n.getBottomY();
        double tTX = n.getTopX();
        double tBY = n.getBottomY();
        double qBX = Math.abs(tX) - Math.abs(bX);
        Rectangle2D.Double tR = new Rectangle2D.Double(tTX, tBY, tileX, tileY);
        Rectangle2D.Double qR = new Rectangle2D.Double(tX, bY, qBX, tY - bY);
        double nminX = tR.getMaxX();
        double nmaxY = tR.getMaxY();
        double nmaxX = tR.getMinX();
        double nminY = tR.getMinY();
        double qminX = qR.getMaxX();
        double qmaxY = qR.getMaxY();
        double qmaxX = qR.getMinX();
        double qminY = qR.getMinY();

        if (tR.contains(qmaxX, qmaxY) || tR.contains(qminX, qminY)
                || tR.contains(qmaxX, qminY) || tR.contains(qminX, qmaxY)
                || qR.contains(nmaxX, nmaxY) || qR.contains(nminX, nminY)
                || tR.intersects(qR) || qR.intersects(tR)) {
            if (satisfiesDepthOrLeaf(fPP, n)) {
                tiles.add(n);
                return true;
            } else {
                tI(n.getTopLeft(), tX, tY, bX, bY, fPP, tiles);
                tI(n.getTopRight(), tX, tY, bX, bY, fPP, tiles);
                tI(n.getBottomLeft(), tX, tY, bX, bY, fPP, tiles);
                tI(n.getBottomRight(), tX, tY, bX, bY, fPP, tiles);
            }
        }
        return false;
    }

    public LinkedList<Node> orderTiles(LinkedList<Node> tiles) {
        LinkedList<Node> ordered = new LinkedList<>();
        ordered = MergeSort.mergeSort(tiles);
        return ordered;
    }


    public boolean satisfiesDepthOrLeaf(double feetPerPixel, Node n) {
        int depth = n.getHeight();
        if (feetPerPixel >= n.feetPerPixel() || depth == 7) {
            return true;
        }
        return false;
    }

    public double feetPerPixel(Node n) {
        return n.feetPerPixel();
    }

    private void treeBuilderHelper(Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
        if (topLeft.getHeight() == 7 || topRight.getHeight() == 7) {
            return;
        }
        if (bottomLeft.getHeight() == 7 || bottomRight.getHeight() == 7) {
            return;
        }

        double tlTX = topLeft.getTopX();
        double tlTY = topLeft.getTopY();
        double tlBX = topLeft.getBottomX();
        double tlBY = topLeft.getBottomY();
        Node topLeftNode = new Node(topLeft.getName() + "1",
                topLeft, tlTX, tlTY, ((tlBX - tlTX) / 2) + tlTX, (tlBY + tlTY) / 2);
        topLeft.setTopLeft(topLeftNode);
        topLeft = topLeft.getTopLeft();

        double trTX = topRight.getTopX();
        double trTY = topRight.getTopY();
        double trBX = topRight.getBottomX();
        double trBY = topRight.getBottomY();
        Node topRightNode = new Node(topRight.getName() + "2", topRight,
                ((trBX - trTX) / 2) + trTX, trTY, trBX, (trTY + trBY) / 2);
        topRight.setTopRight(topRightNode);
        topRight = topRight.getTopRight();

        double blTX = bottomLeft.getTopX();
        double blTY = bottomLeft.getTopY();
        double blBX = bottomLeft.getBottomX();
        double blBY = bottomLeft.getBottomY();
        Node bottomLeftNode = new Node(bottomLeft.getName() + "3", bottomLeft,
                blTX, (blTY + blBY) / 2, ((blBX - blTX) / 2) + blTX, blBY);
        bottomLeft.setBottomLeft(bottomLeftNode);
        bottomLeft = bottomLeft.getBottomLeft();

        double brTX = bottomRight.getTopX();
        double brTY = bottomRight.getTopY();
        double brBX = bottomRight.getBottomX();
        double brBY = bottomRight.getBottomY();
        Node bottomRightNode = new Node(bottomRight.getName() + "4", bottomRight,
                ((brBX - brTX) / 2) + brTX, (brTY + brBY) / 2, brBX, brBY);
        bottomRight.setBottomRight(bottomRightNode);
        bottomRight = bottomRight.getBottomRight();

        treeBuilderHelper(topLeft, topLeft, topLeft, topLeft);
        treeBuilderHelper(topRight, topRight, topRight, topRight);
        treeBuilderHelper(bottomLeft, bottomLeft, bottomLeft, bottomLeft);
        treeBuilderHelper(bottomRight, bottomRight, bottomRight, bottomRight);
    }

    public Node getRoot() {
        return this.root;
    }
}
