import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.lang.Math;
import java.util.ArrayList;

/**
 * Weapon class extends GameObject
 * 
 * @author Zhijie Xia
 * @since 3/22/2020
 */
public class Weapon extends GameObject {

    private double Travelled;// how far the weapon has travelled

    private Direction direction;// direction will be created inside the player attack method

    private int range;// how far can a weapon travel

    private int side;// playerSide:0 EnemySide=1;
    private ImageView image;

    /**
     * Takes spun x,y cooridnate and a direction and side
     * 
     * @param AttackSpunX x-coordinate
     * @param AttackSpunY y-coordinate
     * @param direction   direction which will lead this weapon to move
     * @param side
     */
    public Weapon(int AttackSpunX, int AttackSpunY, Direction direction, int side) {
        super(AttackSpunX, AttackSpunY, 18, 18, 1, 6, 5, true);
        this.range = 400;
        this.direction = direction;
        this.Travelled = 0.0;
        this.side = side;
        this.image = setImage((new ImageView(new Image("file:fireball.png"))), 18, 18);
        this.myType = Type.Weapon;
    }

    /**
     * override update method and weapon will move independently
     * 
     * @param game
     */
    public void update(Game game) {

        int changeInX = 0, changeInY = 0;

        if (this.direction.getHeadUp()) {
            this.moveUp();
            changeInY += this.getSpeed();
        }

        if (this.direction.getHeadDown()) {
            this.moveDown();
            changeInY += this.getSpeed();
        }

        if (this.direction.getHeadLeft()) {
            this.moveLeft();
            changeInX += this.getSpeed();
        }

        if (this.direction.getHeadRight()) {
            this.moveRight();
            changeInX += this.getSpeed();
        }

        this.Travelled += Math.pow(Math.pow(changeInX, 2) + Math.pow(changeInY, 2), 0.5);
        checkAlive(game);

    }

    /**
     * This method checks collusion and if the weapon hit something , weapon will
     * die It also checks the distance that this weapon is travelled, if the
     * travelled distance is larger than its range, the weapon dies
     * 
     * @param game
     */
    public void checkAlive(Game game) {
        // player's weapon
        if (Travelled > range) {
            this.setAlive(false);
        }

        else {
            if (this.side == 0) {
                // check whether hit any rocks
                for (GameObject e : game.getMapHandler().getNoside()) {
                    if (this.getObjectarea().OverlapCheck(e.getObjectarea())) {
                        System.out.println("This weapon hit rock and died");

                        this.setAlive(false);

                        break;
                    }
                }

                if (this.getAlive()) {// only if weapon has not hit rocks, then check whether hit any enemy objects
                    // this part is checking whether weapon hit Enemy objects
                    for (GameObject e : game.getMapHandler().getEnemySide()) {
                        if (this.getObjectarea().OverlapCheck(e.getObjectarea())) {
                            this.setAlive(false);

                            e.healthDown(this.getDamage());
                            if (e.myType == Type.Enemy1 || e.myType == Type.Enemy2) {
                                if (this.direction.getHeadUp()) {
                                    e.moveUp();
                                }
                                if (this.direction.getHeadDown()) {
                                    e.moveDown();
                                }
                                if (this.direction.getHeadLeft()) {
                                    e.moveLeft();
                                }
                                if (this.direction.getHeadRight()) {
                                    e.moveRight();
                                }
                            }

                            System.out.println("This weapon hit enemy and died");
                            break;
                        }
                    }
                }

            }

            else if (this.side == 1) {
                // check whether hit any rocks
                if (this.getObjectarea().OverlapCheck(game.getPlayer().getObjectarea())) {

                    if (game.getPlayer().Inevitable() != true) {
                        game.getPlayer().healthDown(this.getDamage());
                    }
                    this.setAlive(false);
                    System.out.println("This weapon hit player and died");
                }

                // only if the weapon hasn't hit the player, the following will be executed.
                if (this.getAlive()) {
                    for (GameObject e : game.getMapHandler().getNoside()) {
                        if (this.getObjectarea().OverlapCheck(e.getObjectarea())) {

                            this.setAlive(false);

                            System.out.println("This weapon hit rock and died");
                            break;
                        }
                    }
                }

            }
        }
    }

    public ImageView getLabel() {
        return image;
    }
}