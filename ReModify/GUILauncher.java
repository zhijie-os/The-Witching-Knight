import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Launcher of the game, load objects as image views to window
 *
 * @author Junhao Xu
 * @since 2020/03/01
 */
public class GUILauncher extends Application {
    /**
     * GUI Properties
     */
    public static final int S_WIDTH = 1600;
    public static final int S_HEIGHT = 900;
    /**
     * GUI components
     */
    private static StackPane root = new StackPane();
    private static Scene scene = new Scene(root, S_WIDTH, S_HEIGHT);
    private static Rectangle rectangle;
    private static Rectangle ellipse;
    private static Rectangle background;
    /**
     * Game class related objects
     */
    private static Game game;
    private static GameObject player;
    private static int fullHealth;

    /**
     * Set an transparent ellipse to show shadow effect
     */
    private static void shadowEllipse() {
        ellipse = new Rectangle(0, 0, S_WIDTH, S_HEIGHT);

        ellipse.setId("f");

        Stop[] stops = new Stop[]{new Stop(0.5, Color.TRANSPARENT), new Stop(1, Color.BLACK)};
        RadialGradient lg = new RadialGradient(0, 0, 0.5, 0.5, 0.6, true, CycleMethod.NO_CYCLE, stops);
        ellipse.setFill(lg);

        root.getChildren().add(ellipse);
    }

    /**
     *
     * @param stage is the JavaFX stage to add different elements
     */

    public void start(Stage stage) {
        // Set up basic properties of the window
        setBG();
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:icon.png"));
        stage.setTitle("The Witching Knight");
        stage.show();
        stage.setResizable(false);

        background();

        // Start the Game class, game objects will be initiated there
        game = new Game();
        game.setKeyMonitor(this.getKeyMonitor());
        player = game.getPlayer();
        fullHealth = player.getHealth();
        Thread gameThread = new Thread(game);
        gameThread.start();

        // Launch components of the UI
        shadowEllipse();
        healthBar();

        // Start updating
        (new GUIUpdate()).start();


        // Set window closing action
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
    }

    private static Image bgIMG = new Image("file:bg.png", S_WIDTH, S_HEIGHT, false, true);

    /**
     * Set a background image of the map
     */
    private void background() {
        background = new Rectangle(0, 0, 16000, 16000);
        background.setFill(new ImagePattern(new Image("file:grass.jpg"), 0, 0, 0.01, 0.01, true));
        root.getChildren().add(background);
    }

    /**
     * Initialize a health bar
     */
    private void healthBar() {
        rectangle = new Rectangle(0, 0, S_WIDTH, (double) S_HEIGHT / 30);


        rectangle.setTranslateX(0);
        rectangle.setTranslateY((double) S_HEIGHT / 2 - rectangle.getHeight());

        rectangle.setEffect(new MotionBlur());

        rectangle.setId("f");

        System.out.println("pass");

        root.getChildren().add(rectangle);
    }

    /**
     * Updater that updates the window base on the map objects
     */
    private static class GUIUpdate
            extends AnimationTimer {


        // Get game objects from Game class
        ArrayList<GameObject> gameObjects = game.getMapHandler().getObjectOnScreen();

        // Buffer of images
        ArrayList<ImageView> imageViews = new ArrayList<>();

        // Set window components' ID as f (final) will not be removed
        private GUIUpdate() {
            background.setId("f");
        }

        // JavaFX updater
        @Override
        public void handle(long l) {

            // Reset Buffer
            imageViews.clear();

            // Try & Catch IOException in case that the Arraylist is accessed at the same time
            try {

                // Add each object into the buffer and window
                for (GameObject i : gameObjects) {
                    i.getLabel().setId("d");
                    if (!root.getChildren().contains(i.getLabel())) {
                        root.getChildren().add(i.getLabel());
                    }
                    imageViews.add(i.getLabel());
                }


                // Remove image if it is removed from on screen objects
                root.getChildren().removeIf(i -> !imageViews.contains(i) && (!i.getId().equals("f")));

                // Set on-screen coordinates relative to the palyer so the camera is focused on the player
                for (GameObject i : gameObjects) {
                    i.getLabel().setTranslateX(i.getObjectarea().getX() - player.getObjectarea().getX());
                    i.getLabel().setTranslateY(i.getObjectarea().getY() - player.getObjectarea().getY());
                }

            } catch (Exception ignored) {

            }

            // Set background location as well
            background.setTranslateX(background.getX() - player.getObjectarea().getX());
            background.setTranslateY(background.getY() - player.getObjectarea().getY());

            // Calculate the ratio of health bar
            double hRatio = (double) player.getHealth() / fullHealth;

            // Set color for health bar in different ratios
            if (hRatio > 0.8) {
                rectangle.setFill(Color.rgb(0, 200, 0, 0.5));
            } else if (hRatio > .5) {
                rectangle.setFill(Color.rgb(154, 205, 50, 0.5));
            } else if (hRatio > .25) {
                rectangle.setFill(Color.rgb(255, 165, 0, 0.5));
            } else {
                rectangle.setFill(Color.rgb(255, 0, 0, 0.5));
            }

            // Set health bar width to ratio
            rectangle.setWidth(S_WIDTH / 1.1 * hRatio);

            // Set window components to the front
            player.getLabel().toFront();
            ellipse.toFront();
            rectangle.toFront();

            // One update cycle ends here
        }


    }

    /**
     * Launcher of the game. Everything starts from here
     * @param args arguments
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Getter of key monitor base on Scene
     * @return a key event handler
     */
    public KeyMonitor getKeyMonitor() {
        return new KeyMonitor(scene);
    }

    /**
     * Simple method that sets the background of the window
     */
    private void setBG() {
        BackgroundImage bgimg = new BackgroundImage(bgIMG, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background bg = new Background(bgimg);
        root.setBackground(bg);
    }
}
