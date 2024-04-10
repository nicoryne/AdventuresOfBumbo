package classes;

import classes.entities.MovingEntity;
import classes.entities.enemies.BumboEnemy;
import classes.entities.enemies.EnemyFlyweightFactory;
import classes.entities.player.PlayerBuilder;
import classes.entities.player.PlayerDirector;
import classes.entities.EntityObject;
import classes.entities.player.Player;
import classes.entities.projectile.ProjectileFlyweightFactory;
import classes.equips.weapons.Bow;
import classes.equips.weapons.Staff;
import classes.equips.weapons.Weapon;
import classes.exceptions.GameInitializationException;
import classes.util.controllers.ControllerComponents;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.GameManagerComponents;
import classes.util.managers.TileManager;
import classes.util.pathfinding.PathFinder;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class Game {

    private Properties gameProperties;

    private static Game game;

    private GameState gameState;

    private ControllerComponents controllerComponents;

    private GameManagerComponents gameManagerComponents;

    private Player<Weapon> player;

    private ArrayList<MovingEntity> entities;

    private PathFinder pathFinder;

    private static boolean spawned;


    private Game() {}

    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }

        return game;
    }

    public void setupGame() throws GameInitializationException {
        this.gameProperties = new Properties();

        try {
            loadPropertiesFile();
        } catch (IOException e) {
            throw new GameInitializationException("Error loading properties file: ", e);
        }

        ProjectileFlyweightFactory.initializeFlyweightProjectiles();
        EnemyFlyweightFactory.initializeFlyweightEnemies();
        this.entities = new ArrayList<>();
        this.controllerComponents = new ControllerComponents(new KeyboardController(), new MouseController());
        setupPlayer();
        this.gameManagerComponents = new GameManagerComponents(new TileManager());
        this.gameState = GameState.PLAYING;
        this.pathFinder = new PathFinder();
    }

    private void setupPlayer() {

        PlayerBuilder<Weapon> playerBuilder = new PlayerBuilder<>();
        PlayerDirector playerDirector = new PlayerDirector();

        KeyboardController keyboardController = controllerComponents.getKeyboardController();
        MouseController mouseController = getControllerComponents().getMouseController();
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        Bow bow = new Bow();
        Staff staff = new Staff();

        playerDirector.constructPlayer(playerBuilder, keyboardController, mouseController, staff);

        this.player = playerBuilder.build();
        this.player.getAttributeComponents().setAgility(20);
        this.player.getAttributeComponents().setIntelligence(10);
        this.player.getAttributeComponents().setStrength(10);
        this.player.spawn(tileSize * 23, tileSize * 24);
    }

    private void loadPropertiesFile() throws IOException {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String gameConfigPath = rootPath + "game.properties";

        try (FileInputStream inputStream = new FileInputStream(gameConfigPath)) {
            gameProperties.load(inputStream);
        }
    }

    public void renderEntities(Graphics2D g2){
        getGameManagerComponents().getTileManager().render(g2);

        player.render(g2);

        Iterator<MovingEntity> entityObjectIterator = entities.iterator();
        while(entityObjectIterator.hasNext()) {
            EntityObject entityObject = entityObjectIterator.next();
            if(entityObject != null && entityObject.getRenderComponent().isAlive()) {
                entityObject.render(g2);
            } else {
                entityObjectIterator.remove();
            }
        }

    }

    public void updateEntities() {
        player.update();

        Iterator<MovingEntity> entityObjectIterator = entities.iterator();
        while(entityObjectIterator.hasNext()) {
            EntityObject entityObject = entityObjectIterator.next();
            if(entityObject != null && entityObject.getRenderComponent().isAlive()) {
                entityObject.update();
            } else {
                entityObjectIterator.remove();
            }
        }

        spawnEnemy();
    }

    public void spawnEnemy() {
        Random random = new Random();
        int chance = random.nextInt(100);

        if(chance > 97) {
            int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
            int maxWorldCol = Integer.parseInt(Game.getInstance().getProperty("MAX_WORLD_COL"));
            int maxWorldRow = Integer.parseInt(Game.getInstance().getProperty("MAX_WORLD_ROW"));

            int spawnX = random.nextInt(8, maxWorldCol - 8);
            int spawnY = random.nextInt(7, maxWorldRow - 7);

            BumboEnemy bumboEnemy = new BumboEnemy();
            bumboEnemy.spawn(spawnX * tileSize, spawnY * tileSize);
            entities.add(bumboEnemy);
        }
    }

    public double getPlayerMaxViewableX() {
        return player.getPositionComponent().getWorldPositionX().doubleValue() +
                player.getPositionComponent().getScreenPositionX().doubleValue();
    }

    public double getPlayerMinViewableX() {
        return player.getPositionComponent().getWorldPositionX().doubleValue() -
                player.getPositionComponent().getScreenPositionX().doubleValue();
    }

    public double getPlayerMaxViewableY() {
        return player.getPositionComponent().getWorldPositionY().doubleValue() +
                player.getPositionComponent().getScreenPositionY().doubleValue();
    }

    public double getPlayerMinViewableY() {
        return player.getPositionComponent().getWorldPositionY().doubleValue() -
                player.getPositionComponent().getScreenPositionY().doubleValue();
    }

    public int getScreenWidth() {
        int tileSize = Integer.parseInt(game.getProperty("TILE_SIZE"));
        int maxScreenColumns = Integer.parseInt(game.getProperty("MAX_SCREEN_COL"));

        return tileSize * maxScreenColumns; // Returns screen width based on tile size and number of columns
    }

    public int getScreenHeight() {
        int tileSize = Integer.parseInt(game.getProperty("TILE_SIZE"));
        int maxScreenRows = Integer.parseInt(game.getProperty("MAX_SCREEN_ROW"));

        return tileSize * maxScreenRows; // Returns screen height based on tile size and number of rows
    }

    public ControllerComponents getControllerComponents() {
        return controllerComponents;
    }

    public GameManagerComponents getGameManagerComponents() {
        return gameManagerComponents;
    }

    public Player<Weapon> getPlayer() {
        return player;
    }

    public void addEntity(MovingEntity entityObject) {
        entities.add(entityObject);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public ArrayList<MovingEntity> getEntities() {
        return entities;
    }

    public void setProperty(String key, String value) {
        gameProperties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return gameProperties.getProperty(key);
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }
}
