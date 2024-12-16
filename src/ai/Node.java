package ai;

public class Node implements Comparable<Node> {
    public int row, col;
    public boolean solid = false;
    public boolean checked = false;
    public boolean open = false;
    public int gCost, hCost, fCost;
    public Node parent;

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void calculateFCost() {
        this.fCost = this.gCost + this.hCost;
    }

    @Override
    public int compareTo(Node other) {
        if (this.fCost == other.fCost) {
            return Integer.compare(this.gCost, other.gCost);
        }
        return Integer.compare(this.fCost, other.fCost);
    }
}