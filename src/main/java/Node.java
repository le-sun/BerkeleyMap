import java.util.LinkedList;

public class Node {
    String imagePath;
    Node parent;
    Node topLeft;
    Node topRight;
    Node bottomLeft;
    Node bottomRight;
    //Node left; Node right; Node top; Node bottom;
    int height;
    double topX;
    double topY;
    double bottomX;
    double bottomY;

    public Node(String image, Node p, double topX, double topY, double bottomX, double bottomY) {
        this.imagePath = image;
        this.parent = p;
        this.height = image.length();
        this.topX = topX;
        this.topY = topY;
        this.bottomX = bottomX;
        this.bottomY = bottomY;
    }

    public String getName() {
        return imagePath;
    }

    public int getHeight() {
        return height;
    }

    public void setTopLeft(Node n) {
        this.topLeft = n;
    }

    public void setTopRight(Node n) {
        this.topRight = n;
    }

    public void setBottomLeft(Node n) {
        this.bottomLeft = n;
    }

    public void setBottomRight(Node n) {
        this.bottomRight = n;
    }

    public Node getTopLeft() {
        return this.topLeft;
    }

    public Node getTopRight() {
        return this.topRight;
    }

    public Node getBottomLeft() {
        return this.bottomLeft;
    }

    public Node getBottomRight() {
        return this.bottomRight;
    }

    public double getTopX() {
        return this.topX;
    }

    public double getTopY() {
        return this.topY;
    }

    public double getBottomX() {
        return this.bottomX;
    }

    public double getBottomY() {
        return this.bottomY;
    }

    public double feetPerPixel() {
        double longDifference = Math.abs(this.topX) - Math.abs(this.bottomX);
        double pixels = (longDifference * 288200) / 256;
        return pixels;
    }

    public Iterable<Node> children() {
        LinkedList<Node> children = new LinkedList<Node>();
        children.add(topLeft);
        children.add(topRight);
        children.add(bottomLeft);
        children.add(bottomRight);
        return children;
    }

    @Override
    public String toString() {
        return this.imagePath;
    }
}
