import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * Enemy2 type Enemy
 * 
 * @author Zhijie Xia
 * @since 3/18/2020
 */
public class Enemy2 extends Enemy {
    private ImageView image;

    /**
     * constructor takes x,y-coordinate to init the Enemy1
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Enemy2(int x, int y) {
        super(x, y, 32, 32, 10, 4, 20, true);
        this.image = setImage((new ImageView(new Image("file:enemy.png"))), 32, 32);
        this.myType = Type.Enemy2;
    }

    /**
     * override update(Game game) calls automove(Game game) and checkAliveByHealth()
     * 
     * @param game
     */
    public void update(Game game) {
        this.autoMove(game);
        this.checkAliveByHealth();
    }

    /**
     * override attack(Game game) but leave it empty
     */
    public void attack(Game game) {
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