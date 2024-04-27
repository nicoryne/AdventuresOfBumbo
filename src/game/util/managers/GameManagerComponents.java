package game.util.managers;

public class GameManagerComponents {

    private final TileManager tileManager;

    private final SoundManager soundManager;

    public GameManagerComponents() {
        this.tileManager = new TileManager();
        this.soundManager = SoundManager.getInstance();
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}
