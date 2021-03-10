import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * Rock class extends GameObject
 * 
 * @author Zhijie Xia
 * @since 3/22/2020
 */
public class Rock extends GameObject {
    private ImageView image;

    /**
     * constructor takes x,y coordinate
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Rock(int x, int y) {
        super(x, y, 128, 128, 9999, 0, 0, true);
        this.image = setImage((new ImageView(new Image("file:rock.png"))), 128, 128);
        this.myType = Type.Rock;
    }

    public void update(Game game) {
    };

    public ImageView getLabel() {
        return image;
    }
}
