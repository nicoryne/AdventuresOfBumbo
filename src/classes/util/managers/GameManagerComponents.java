package classes.util.managers;

public class GameManagerComponents {

    private final TileManager tileManager;

    private final SoundManager soundManager;

    public GameManagerComponents(TileManager tileManager) {
        this.tileManager = tileManager;
        this.soundManager = SoundManager.getInstance();
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }
}
