package game.util.pathfinding;

import game.Game;
import game.util.managers.TileManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PathFinder {

    private static final int MAX_STEPS = 500;

    private final Game game;

    private Node[][] nodeList;

    private final ArrayList<Node> pathNodes = new ArrayList<>();

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
        for (int col = 0; col < maxWorldCol; col++) {
            for (int row = 0; row < maxWorldRow; row++) {
                nodeList[col][row] = new Node(col, row);
            }
        }
    }

    public void resetNodes() {
        for (int col = 0; col < maxWorldCol; col++) {
            for (int row = 0; row < maxWorldRow; row++) {
                Node node = nodeList[col][row];
                node.setOpen(false);
                node.setChecked(false);
                node.setSolid(false);
            }
        }

        pathNodes.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        startNode = nodeList[startCol][startRow];
        currentNode = startNode;
        goalNode = nodeList[goalCol][goalRow];


        for(int col = 0; col < maxWorldCol; col++) {
            for(int row = 0; row < maxWorldRow; row++) {
                TileManager tileManager = game.getGameManagerComponents().getTileManager();
                int tileNum = tileManager.getMapTile2DArray()[col][row];

                if (tileManager.getTileArrayList().get(tileNum).isCollidable()) {
                    nodeList[col][row].setSolid(true);
                }
                getCost(nodeList[col][row]);
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
        PriorityQueue<Node> openNodesQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getFCost));

        while (!goalReached && step < MAX_STEPS) {
            int col = currentNode.getCol();
            int row = currentNode.getRow();

            currentNode.setChecked(true);

            if (row - 1 >= 0) {
                openNode(nodeList[col][row - 1], openNodesQueue);
            }

            if (col - 1 >= 0) {
                openNode(nodeList[col - 1][row], openNodesQueue);
            }

            if (row + 1 < maxWorldRow) {
                openNode(nodeList[col][row + 1], openNodesQueue);
            }

            if (col + 1 < maxWorldCol) {
                openNode(nodeList[col + 1][row], openNodesQueue);
            }

            if (openNodesQueue.isEmpty()) {
                break;
            }

            currentNode = openNodesQueue.poll();

            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }

            step++;
        }

        return goalReached;
    }

    private void openNode(Node node, PriorityQueue<Node> openNodesQueue) {
        if (!node.isOpen() && !node.isChecked() && !node.isSolid()) {
            node.setOpen(true);
            node.setParent(currentNode);
            openNodesQueue.add(node);
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
