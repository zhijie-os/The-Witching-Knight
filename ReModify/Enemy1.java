import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * Enemy1 type Enemy
 * 
 * @author Zhijie Xia
 * @since 3/18/2020
 */
public class Enemy1 extends Enemy {

    private int coolDown = 0;
    private int enemyRange = 64;
    private ImageView image;

    /**
     * constructor takes x,y-coordinate to init the Enemy1
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Enemy1(int x, int y) {
        super(x, y, 32, 32, 25, 2, 5, true);
        this.image = setImage((new ImageView(new Image("file:enemy.png"))), 32, 32);
        this.myType = Type.Enemy1;
    }

    /**
     * override the update method calls autoMove(Game game), checkAliveByHealth(),
     * attack(Game game) to update this Enemy1
     * 
     * @param game
     */
    public void update(Game game) {
        this.autoMove(game);

        this.checkAliveByHealth();

        // System.out.println("Enemy health is "+this.getHealth());

        if (coolDown > 120) {
            if (this.distanceToPlayer(game) < this.enemyRange) {
                attack(game);
            }
            coolDown = 0;
        }
        coolDown++;
    }

    /**
     * attack method and create new weapon()
     * 
     * @param game
     */
    public void attack(Game game) {
        // System.out.println("Enemy attacked!");
        // attack up
        game.addObjectForAdd(new Weapon(this.getObjectarea().getX(), this.getObjectarea().getUpMostY(),
                new Direction(true, false, false, false), 1));

        // attack down
        game.addObjectForAdd(new Weapon(this.getObjectarea().getX(), this.getObjectarea().getDownMostY(),
                new Direction(false, true, false, false), 1));

        // attack left
        game.addObjectForAdd(new Weapon(this.getObjectarea().getLeftMostX(), this.getObjectarea().getY(),
                new Direction(false, false, true, false), 1));

        // attack right
        game.addObjectForAdd(new Weapon(this.getObjectarea().getRightMostX(), this.getObjectarea().getY(),
                new Direction(false, false, false, true), 1));
    }

    /**
     * return this.image- a ImageView
     * 
     * @return this.image
     */
    public ImageView getLabel() {
        return image;
    }
}