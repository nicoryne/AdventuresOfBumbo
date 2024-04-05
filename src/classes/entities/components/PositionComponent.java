package classes.entities.components;

public class PositionComponent<T extends Number> {

    private T worldPositionX;

    private T worldPositionY;

    private T screenPositionX;

    private T screenPositionY;

    public T getWorldPositionX() {
        return worldPositionX;
    }

    public void setWorldPositionX(T worldPositionX) {
        this.worldPositionX = worldPositionX;
    }

    public T getWorldPositionY() {
        return worldPositionY;
    }

    public void setWorldPositionY(T worldPositionY) {
        this.worldPositionY = worldPositionY;
    }

    public T getScreenPositionX() {
        return screenPositionX;
    }

    public void setScreenPositionX(T screenPositionX) {
        this.screenPositionX = screenPositionX;
    }

    public T getScreenPositionY() {
        return screenPositionY;
    }

    public void setScreenPositionY(T screenPositionY) {
        this.screenPositionY = screenPositionY;
    }

}
