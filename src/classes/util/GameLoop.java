package classes.util;

import classes.ui.components.GamePanel;

public class GameLoop {

    private Thread frameThread;
    private double FPS;
    private GamePanel gamePanel;
    private static GameLoop gameLoopInstance;

    private GameLoop() {}

    public static GameLoop getInstance() {
        if(gameLoopInstance == null) {
            gameLoopInstance = new GameLoop();
        }

        return gameLoopInstance;
    }

    public void setupGameLoop(double fps, Thread frameThread, GamePanel gamePanel) {
        this.FPS = fps;
        this.frameThread = frameThread;
        this.gamePanel = gamePanel;
    }

    public void startGameLoop() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0.0;
        long prevTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(frameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - prevTime) / drawInterval;
            timer += (currentTime - prevTime);
            prevTime = currentTime;

            if(delta >= 1) {
                gamePanel.update();
                gamePanel.repaint();
                delta--;
                drawCount++;
            }

            // display FPS
            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public Thread getGameThread() {
        return frameThread;
    }

    public void setGameThread(Thread frameThread) {
        this.frameThread = frameThread;
    }

    public double getFps() {
        return FPS;
    }

    public void setFps(double fps) {
        this.FPS = fps;
    }
}
