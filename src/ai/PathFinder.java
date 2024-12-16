package ai;

import entity.Entity;
import main.GamePanel;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

//A* ALGORITHM
public class PathFinder {
    GamePanel gp;
    Node[][] node;
    private PriorityQueue<Node> openList;
    public ArrayList<Node> pathList = new ArrayList<>();

    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    //TEMP VARIABLES
    int row, col;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        this.openList = new PriorityQueue<>();
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxWorldRow][gp.maxWorldCol];

        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                node[row][col] = new Node(row, col);
            }
        }
    }

    public void resetNodes() {
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                node[row][col].open = false;
                node[row][col].checked = false;
                node[row][col].solid = false;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNode(int startRow, int startCol, int goalRow, int goalCol) {
        startRow = Math.min(gp.maxWorldRow - 1, Math.max(0, startRow));
        startCol = Math.min(gp.maxWorldCol - 1, Math.max(0, startCol));
        goalRow = Math.min(gp.maxWorldRow - 1, Math.max(0, goalRow));
        goalCol = Math.min(gp.maxWorldCol - 1, Math.max(0, goalCol));
        resetNodes();

        if (startCol < gp.maxWorldCol && startRow < gp.maxWorldRow) {
            startNode = node[startRow][startCol];
            currentNode = startNode;
            goalNode = node[goalRow][goalCol];
            openList.add(startNode);

            col = 0;
            row = 0;
            int tempRow, tempCol;
            Set<String> keySet = gp.aSetter.getObjectMap(gp.currentMap).keySet();
            for (String key : keySet) {
                String[] parts = key.split(",");
                tempRow = Integer.parseInt(parts[1]);
                tempCol = Integer.parseInt(parts[0]);

                node[tempRow][tempCol].solid = true;
            }

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                int tileNum = gp.tileMgr.mapTileNum[gp.currentMap][row][col];
                if (gp.tileMgr.tile[tileNum].collision) {
                    node[row][col].solid = true;
                }

                getCost(node[row][col]);

                col++;
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private void getCost(Node node) {
        // G COST
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        //H COST
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        //F COST
        node.fCost = node.gCost + node.hCost;
    }

    public boolean search() {
        while (!goalReached && step < 500) {
            if (openList.isEmpty()) {
                break;
            }

            currentNode = openList.poll();
            currentNode.checked = true;

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                break;
            }

            int row = currentNode.row;
            int col = currentNode.col;

            openAdjacentNodes(row - 1, col); // UP
            openAdjacentNodes(row + 1, col); // DOWN
            openAdjacentNodes(row, col - 1); // LEFT
            openAdjacentNodes(row, col + 1); // RIGHT

            step++;
        }

        return goalReached;
    }

    private void openAdjacentNodes(int row, int col) {
        if (row >= 0 && row < node.length && col >= 0 && col < node[0].length) {
            Node neighbor = node[row][col];
            if (!neighbor.solid && !neighbor.checked) {
                neighbor.gCost = currentNode.gCost + 1;
                neighbor.hCost = Math.abs(neighbor.row - goalNode.row) + Math.abs(neighbor.col - goalNode.col);
                neighbor.calculateFCost();
                neighbor.parent = currentNode;
                openList.add(neighbor);
            }
        }
    }

    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
