package game;

import game.entities.MovingEntity;
import game.entities.drops.DropFlyweightFactory;
import game.entities.enemy.mobs.BumboMob;
import game.entities.enemy.mobs.ChortleMob;
import game.entities.enemy.mobs.MobFlyweightFactory;
import game.entities.player.PlayerBuilder;
import game.entities.player.PlayerDirector;
import game.entities.EntityObject;
import game.entities.player.Player;
import game.entities.projectile.ProjectileFlyweightFactory;
import game.equips.weapons.Bow;
import game.equips.weapons.Staff;
import game.equips.weapons.Weapon;
import game.util.GameState;
import game.util.Stopwatch;
import game.util.controllers.ControllerComponents;
import game.util.controllers.KeyboardController;
import game.util.controllers.MouseController;
import game.util.managers.GameManagerComponents;
import game.util.managers.TileManager;
import services.LoggerHelper;
import services.models.User;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class Game {

    private Properties gameProperties;

    private User user;

    private static Game gameInstance;

    private GameState gameState;

    private ControllerComponents controllerComponents;

    private GameManagerComponents gameManagerComponents;

    private Player<Weapon> player;

    private ArrayList<MovingEntity> entities;

    private ArrayList<MovingEntity> drops;

    private static boolean spawned;

    private Stopwatch stopwatch;

    private Game() {}
    //TODO: implement timer

    public static Game getInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }

        return gameInstance;
    }

    public void setupGame() {
        this.gameState = GameState.TITLE_SCREEN;
        setupProperties();
        setupFlyweightFactories();
        this.entities = new ArrayList<>();
        this.drops = new ArrayList<>();
        this.controllerComponents = new ControllerComponents(new KeyboardController(), new MouseController());
        setupPlayer();
        this.gameManagerComponents = new GameManagerComponents(new TileManager());
        this.stopwatch = new Stopwatch();
    }

    private void setupProperties() {
        this.gameProperties = new Properties();
        loadPropertiesFile();
    }

    private void setupFlyweightFactories() {
        ProjectileFlyweightFactory.initializeFlyweightProjectiles();
        MobFlyweightFactory.initializeFlyweightEnemies();
        DropFlyweightFactory.initializeFlyweightDrops();
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
        this.player.spawn(tileSize * 23, tileSize * 24);
    }

    private void loadPropertiesFile() {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String gameConfigPath = rootPath + "conf/game.properties";

        try (FileInputStream inputStream = new FileInputStream(gameConfigPath)) {
            gameProperties.load(inputStream);
        } catch (IOException e) {
            LoggerHelper.logError("Error loading properties file: ", e);
        }
    }

    public void renderEntities(Graphics2D g2){
        getGameManagerComponents().getTileManager().render(g2);
        renderEntityList(g2);
        renderDropsList(g2);
        stopwatch.draw(g2);
        player.render(g2);
    }

    public void updateEntities() {
        if(!stopwatch.isActive()) {
            stopwatch.start();
        }

        player.update();
        updateEntityList();
        updateDropsList();
        spawnEnemy();
    }

    private void renderDropsList(Graphics2D g2) {
        if(drops.isEmpty()) {
            return;
        }

        Iterator<MovingEntity> dropsIterator = drops.iterator();
        while(dropsIterator.hasNext()) {
            EntityObject drop = dropsIterator.next();
            if(drop != null && drop.getRenderComponent().isAlive()) {
                drop.render(g2);
            } else {
                dropsIterator.remove();
            }
        }
    }

    private void renderEntityList(Graphics2D g2) {
        if(entities.isEmpty()) {
            return;
        }
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

    private void updateEntityList() {
        if(entities.isEmpty()) {
            return;
        }

        Iterator<MovingEntity> entityObjectIterator = entities.iterator();
        while(entityObjectIterator.hasNext()) {
            EntityObject entityObject = entityObjectIterator.next();
            if(entityObject != null && entityObject.getRenderComponent().isAlive()) {
                entityObject.update();
            } else {
                entityObjectIterator.remove();
            }
        }
    }

    private void updateDropsList() {
        if(drops.isEmpty()) {
            return;
        }

        Iterator<MovingEntity> dropsIterator = drops.iterator();
        while(dropsIterator.hasNext()) {
            EntityObject drop = dropsIterator.next();
            if(drop != null && drop.getRenderComponent().isAlive()) {
                drop.update();
            } else {
                dropsIterator.remove();
            }
        }
    }

    public void spawnEnemy() {
        Random random = new Random();
        int chance = random.nextInt(100);

        if(chance > 98) {
            int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
            int maxWorldCol = Integer.parseInt(Game.getInstance().getProperty("MAX_WORLD_COL"));
            int maxWorldRow = Integer.parseInt(Game.getInstance().getProperty("MAX_WORLD_ROW"));

            int spawnX = random.nextInt(8, maxWorldCol - 8);
            int spawnY = random.nextInt(7, maxWorldRow - 7);

            BumboMob bumboMob = new BumboMob();
            bumboMob.spawn(spawnX * tileSize, spawnY * tileSize);
            entities.add(bumboMob);
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
        int tileSize = Integer.parseInt(gameInstance.getProperty("TILE_SIZE"));
        int maxScreenColumns = Integer.parseInt(gameInstance.getProperty("MAX_SCREEN_COL"));

        return tileSize * maxScreenColumns;
    }

    public int getScreenHeight() {
        int tileSize = Integer.parseInt(gameInstance.getProperty("TILE_SIZE"));
        int maxScreenRows = Integer.parseInt(gameInstance.getProperty("MAX_SCREEN_ROW"));

        return tileSize * maxScreenRows;
    }

    public void addObject(MovingEntity entity) {
        drops.add(entity);
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

    public void addMovingEntity(MovingEntity entityObject) {
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

    public ArrayList<MovingEntity> getDrops() {
        return drops;
    }

    public void setProperty(String key, String value) {
        gameProperties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return gameProperties.getProperty(key);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
