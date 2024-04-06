package classes;

import classes.entities.player.PlayerBuilder;
import classes.entities.player.PlayerDirector;
import classes.entities.EntityObject;
import classes.entities.player.Player;
import classes.entities.projectile.Projectile;
import classes.entities.projectile.ProjectileFlyweightFactory;
import classes.equips.weapons.Bow;
import classes.equips.weapons.Weapon;
import classes.exceptions.GameInitializationException;
import classes.util.controllers.ControllerComponents;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.GameManagerComponents;
import classes.util.managers.TileManager;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;


public class Game {

    private Properties gameProperties;

    private static Game game;

    private ControllerComponents controllerComponents;

    private GameManagerComponents gameManagerComponents;

    private Player<Weapon> player;

    private ArrayList<EntityObject> entities;


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

        this.entities = new ArrayList<>();
        this.controllerComponents = new ControllerComponents(new KeyboardController(), new MouseController());
        setupPlayer();
        this.gameManagerComponents = new GameManagerComponents(new TileManager());
    }



    public void setProperty(String key, String value) {
        gameProperties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return gameProperties.getProperty(key);
    }

    private void setupPlayer() {
        ProjectileFlyweightFactory.initializeFlyweightProjectiles();
        PlayerBuilder<Weapon> playerBuilder = new PlayerBuilder<>();
        PlayerDirector playerDirector = new PlayerDirector();
        KeyboardController keyboardController = controllerComponents.getKeyboardController();
        MouseController mouseController = getControllerComponents().getMouseController();
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        Bow bow = new Bow();

        playerDirector.constructPlayer(playerBuilder, keyboardController, mouseController, bow);

        this.player = playerBuilder.build();
        this.player.spawn(tileSize * 23, tileSize * 24);
    }

    private void loadPropertiesFile() throws IOException {
        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String gameConfigPath = rootPath + "game.properties";

        try (FileInputStream inputStream = new FileInputStream(gameConfigPath)) {
            gameProperties.load(inputStream);
        }
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

    public void addEntity(EntityObject entityObject) {
        entities.add(entityObject);
    }

    public void renderEntities(Graphics2D g2){
        getGameManagerComponents().getTileManager().render(g2);

        player.render(g2);

        Iterator<EntityObject> entityObjectIterator = entities.iterator();
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

        Iterator<EntityObject> entityObjectIterator = entities.iterator();
        while(entityObjectIterator.hasNext()) {
            EntityObject entityObject = entityObjectIterator.next();
            if(entityObject != null && entityObject.getRenderComponent().isAlive()) {
                entityObject.update();
            } else {
                entityObjectIterator.remove();
            }
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


}
