package classes.util.pathfinding;

import classes.Game;
import classes.util.managers.TileManager;

import java.util.ArrayList;

public class PathFinder {

    private Game game;

    private Node[][] nodeList;

    private ArrayList<Node> openNodes = new ArrayList<>();

    private ArrayList<Node> pathNodes = new ArrayList<>();

    private Node startNode, goalNode, currentNode;

    private boolean goalReached = false;

    private int step = 0;

    private final int maxWorldCol;
    private final int maxWorldRow;

    public PathFinder() {
        this.game = Game.getInstance();
        maxWorldCol = Integer.parseInt(game.getProperty("MAX_WORLD_COL"));
        maxWorldRow = Integer.parseInt(game.getProperty("MAX_WORLD_ROW"));
        instantiateNodes();
    }

    public void instantiateNodes() {
        nodeList = new Node[maxWorldCol][maxWorldRow];

        int col = 0;
        int row = 0;

        while(col < maxWorldCol && row < maxWorldRow) {
            nodeList[col][row] = new Node(col,row);

            col++;
            if(col == maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {
        int col = 0;
        int row = 0;

        while(col < maxWorldCol && row < maxWorldRow) {
            nodeList[col][row].setOpen(false);
            nodeList[col][row].setChecked(false);
            nodeList[col][row].setSolid(false);

            col++;
            if(col == maxWorldCol) {
                col = 0;
                row++;
            }
        }

        openNodes.clear();
        pathNodes.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = nodeList[startCol][startRow];
        currentNode = startNode;
        goalNode = nodeList[goalCol][goalRow];
        openNodes.add(currentNode);

        int col = 0;
        int row = 0;
        while(col < maxWorldCol && row < maxWorldRow) {
            TileManager tileManager = game.getGameManagerComponents().getTileManager();
            int tileNum = tileManager.getMapTile2DArray()[col][row];

            if (tileManager.getTileArrayList().get(tileNum).isCollidable()) {
                nodeList[col][row].setSolid(true);
            }

            getCost(nodeList[col][row]);

            col++;
            if(col == maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {
        int xDistance = Math.abs(node.getCol() - startNode.getCol());
        int yDistance = Math.abs(node.getRow() - startNode.getRow());
        node.setGCost(xDistance + yDistance);

        xDistance = Math.abs(node.getCol() - goalNode.getCol());
        yDistance = Math.abs(node.getRow() - goalNode.getRow());
        node.setHCost(xDistance + yDistance);

        node.setFCost(node.getGCost() + node.getHCost());
    }

    public boolean search() {
        while(!goalReached && step < 500) {
            int col = currentNode.getCol();
            int row = currentNode.getRow();

            currentNode.setChecked(true);
            openNodes.remove(currentNode);

            if(row-1 >= 0) {
                openNode(nodeList[col][row-1]);
            }

            if(col-1 >= 0) {
                openNode(nodeList[col-1][row]);
            }

            if(row+1 < maxWorldRow) {
                openNode(nodeList[col][row+1]);
            }

            if(col+1 < maxWorldCol) {
                openNode(nodeList[col+1][row]);
            }

            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for(int i = 0; i < openNodes.size(); i++) {
                if(openNodes.get(i).getFCost() < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openNodes.get(i).getFCost();
                } else if(openNodes.get(i).getFCost() == bestNodeFCost) {
                    if(openNodes.get(i).getGCost() < openNodes.get(bestNodeIndex).getGCost()) {
                        bestNodeIndex = i;
                    }
                }
            }

            if(openNodes.isEmpty()) {
                break;
            }

            currentNode = openNodes.get(bestNodeIndex);

            if(currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }

            step++;
        }

        return goalReached;
    }

    private void openNode(Node node) {
        if(!node.isOpen() && !node.isChecked() && !node.isSolid()) {
            node.setOpen(true);
            node.setParent(currentNode);
            openNodes.add(node);
        }
    }

    private void trackPath() {
        Node current = goalNode;

        while(current != startNode) {
           pathNodes.add(0, current);
           current = current.getParent();
        }
    }

    public ArrayList<Node> getPathNodes() {
        return pathNodes;
    }
}
