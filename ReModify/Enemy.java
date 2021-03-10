import java.lang.Math;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public abstract class Enemy extends GameObject {

    private int coolDown = 0;
    private int enemyRange = 48;

    public Enemy(int x, int y, int width, int height, int health, int speed, int damage, boolean alive) {
        super(x, y, width, height, health, speed, damage, alive);
    }

    public abstract void update(Game game);

    // enemy moves to player
    /**
     * enemy automtic movement towards player. Player will be passed by Game game
     * 
     * @param game
     */
    public void autoMove(Game game) {
        int X_difference, Y_difference;
        double distance, sin, cos;
        X_difference = Math.abs(game.getPlayer().getObjectarea().getX() - this.getObjectarea().getX());
        Y_difference = Math.abs(game.getPlayer().getObjectarea().getY() - this.getObjectarea().getY());
        distance = (Math.pow(Math.pow(X_difference, 2) + Math.pow(Y_difference, 2), 0.5));
        sin = Y_difference / distance;
        cos = X_difference / distance;
        // System.out.println(distance);

        if (game.getPlayer().getObjectarea().getX() < this.getObjectarea().getX()) {
            moveLeft((int) (cos * this.getSpeed()), game);
        } else if (game.getPlayer().getObjectarea().getX() > this.getObjectarea().getX()) {
            moveRight((int) (cos * this.getSpeed()), game);
        }

        if (game.getPlayer().getObjectarea().getY() > this.getObjectarea().getY()) {
            moveDown((int) (sin * this.getSpeed()), game);
        } else if (game.getPlayer().getObjectarea().getY() < this.getObjectarea().getY()) {
            moveUp((int) (sin * this.getSpeed()), game);
        }
    }

    /**
     * abstract attack(Game game) method to make sure every class which extends
     * Enemy has this method
     * 
     * @param game
     */
    public abstract void attack(Game game);

    /**
     * overload movements because enemy movements are depended on player's position.
     * 
     * @param distance distance for the movement
     * @param game
     */
    public void moveLeft(int distance, Game game) {

        if (EnemyMoveValidity(this.getObjectarea().getX() - distance, this.getObjectarea().getY(), game)) {
            this.getObjectarea().XDown(distance);
        }

    }

    /**
     * overload movements because enemy movements are depended on player's position.
     * 
     * @param distance distance for the movement
     * @param game
     */
    public void moveRight(int distance, Game game) {

        if (EnemyMoveValidity(this.getObjectarea().getX() + distance, this.getObjectarea().getY(), game)) {
            this.getObjectarea().XUp(distance);
        }

    }

    /**
     * overload movements because enemy movements are depended on player's position.
     * 
     * @param distance distance for the movement
     * @param game
     */
    public void moveUp(int distance, Game game) {

        if (EnemyMoveValidity(this.getObjectarea().getX(), this.getObjectarea().getY() - distance, game)) {
            this.getObjectarea().YDown(distance);
        }

    }

    /**
     * overload movements because enemy movements are depended on player's position.
     * 
     * @param distance distance for the movement
     * @param game
     */
    public void moveDown(int distance, Game game) {

        if (EnemyMoveValidity(this.getObjectarea().getX(), this.getObjectarea().getY() + distance, game)) {
            this.getObjectarea().YUp(distance);
        }

    }

    /**
     * a method which returns a double which indicates the the distance between this
     * and player
     * 
     * @param game
     * @return a double which indicates the the distance between this and player
     */
    public double distanceToPlayer(Game game) {
        int X_difference, Y_difference;
        double distance, sin, cos;
        X_difference = Math.abs(game.getPlayer().getObjectarea().getX() - this.getObjectarea().getX());
        Y_difference = Math.abs(game.getPlayer().getObjectarea().getY() - this.getObjectarea().getY());
        distance = (Math.pow(Math.pow(X_difference, 2) + Math.pow(Y_difference, 2), 0.5));
        return distance;
    }

    /**
     * a method which checks the validity of Enemy movement
     * 
     * @param x    a x-coordinate
     * @param y    a y-coordinate
     * @param game
     * @return a boolean. False indicate the intended movement is invalid
     */
    public boolean EnemyMoveValidity(int x, int y, Game game) {
        if (new ObjectArea(x, y, this.getObjectarea().getWidth(), this.getObjectarea().getHeight())
                .OverlapCheck(game.getPlayer().getObjectarea())) {

            if (game.getPlayer().Inevitable() != true) {
                game.getPlayer().healthDown(this.getDamage());
            }

            return false;
        }

        for (GameObject e : game.getMapHandler().getNoside()) {
            if (new ObjectArea(x, y, this.getObjectarea().getWidth(), this.getObjectarea().getHeight())
                    .OverlapCheck(e.getObjectarea())) {
                return false;
            }
        }

        return true;
    }

    /**
     * abstract getLabel() make classes which extends Enemy must have getLabel()
     * 
     * @return a ImageView
     */
    public abstract ImageView getLabel();
}