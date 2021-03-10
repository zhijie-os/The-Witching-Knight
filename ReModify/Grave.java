import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * a grave class extends GameObject
 * 
 * @author Zhijie Xia
 * @since 3/18/2020
 */
public class Grave extends GameObject {
    private ImageView image;
    private int coolDown = 0;

    /**
     * takes a x,y coordinate to create a Grave at that location
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Grave(int x, int y) {
        super(x, y, 16, 16, 9999, 0, 0, true);
        this.image = setImage((new ImageView(new Image("file:grave.png"))), 32, 32);
        this.myType = Type.Grave;
    }

    /**
     * override the update method and spunEnemy when certain condition is satified
     * 
     * @param game
     */
    public void update(Game game) {
        if (coolDown == 180 && game.EnemyLimit > game.EnemyNum) {
            SpunEnemy(game);
            coolDown -= 180;
            game.EnemyNum++;
        }
        coolDown++;

    }

    /**
     * a method which randomly generatize enemies
     */
    public void SpunEnemy(Game game) {
        int value = game.getRandomGenerator().nextInt(10 - 0);
        if (value <= 6) {
            // System.out.println("New Enemy1 is created.");
            Enemy1 ForAdd = new Enemy1(this.getObjectarea().getX(), this.getObjectarea().getY());
            game.addObjectForAdd(ForAdd);

        } else {
            // System.out.println("New Enemy2 is created.");
            Enemy2 ForAdd = new Enemy2(this.getObjectarea().getX(), this.getObjectarea().getY());
            game.addObjectForAdd(ForAdd);

        }
    }

    /**
     * return this.image
     * 
     * @return this.image
     */
    public ImageView getLabel() {
        return image;
    }

}