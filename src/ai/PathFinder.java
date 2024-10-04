package ai;

import entity.Entity;
import main.GamePanel;

import java.util.ArrayList;
import java.util.Set;

//A* ALGORITHM
public class PathFinder {
    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();

    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    //TEMP VARIABLES
    int row, col;
    int bestNodeIndex, bestNodefCost;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxWorldRow][gp.maxWorldCol];

        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[row][col] = new Node(row, col);

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {
        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            node[row][col].open = false;
            node[row][col].checked = false;
            node[row][col].solid = false;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNode(int startRow, int startCol, int goalRow, int goalCol) {
        resetNodes();

        startNode = node[startRow][startCol];
        currentNode = startNode;
        goalNode = node[goalRow][goalCol];
        openList.add(currentNode);

        col = 0;
        row = 0;
        int tempRow, tempCol;
        Set<String> keySet;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            //SET SOLID NODE
            //CHECK TILES
            int tileNum = gp.tileMgr.mapTileNum[gp.currentMap][row][col];
            if (gp.tileMgr.tile[tileNum].collision) {
                node[row][col].solid = true;
            }

            keySet = gp.aSetter.getObjectMap(gp.currentMap).keySet();
            for (String key : keySet) {
                // Split the key into row and col based on the comma
                String[] parts = key.split(",");

                // Parse row and col as integers
                tempRow = Integer.parseInt(parts[0]);
                tempCol = Integer.parseInt(parts[1]);

                node[tempRow][tempCol].solid = true;
            }

            //SET COST
            getCost(node[row][col]);
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
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
        while (!goalReached & step < 500) {
            col = currentNode.col;
            row = currentNode.row;

            //CHECK THE CURRENT NODE
            currentNode.checked = true;
            openList.remove(currentNode);

            //OPEN THE UP NODE
            if (row - 1 >= 0) {
                openNode(node[row - 1][col]);
            }
            //OPEN THE DOWN NODE
            if (row + 1 < gp.maxWorldRow) {
                openNode(node[row + 1][col]);
            }
            //OPEN THE LEFT NODE
            if (col - 1 >= 0) {
                openNode(node[row][col - 1]);
            }
            //OPEN THE RIGHT NODE
            if (col + 1 < gp.maxWorldCol) {
                openNode(node[row][col + 1]);
            }

            //FIND THE BEST NODE
            bestNodeIndex = 0;
            bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            //IF THERE IS NO NODE IN THE OPEN LIST, END THE LOOP
            if (openList.isEmpty()) {
                break;
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }

        return goalReached;
    }

    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
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
