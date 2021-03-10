import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.util.Random;
import javafx.application.Application;

/**
 * Game class which makes everthing updates and the game runnings in the
 * backend.
 * 
 * @author Zhijie Xia
 * @since 3/18/2020
 */
public class Game implements Runnable {

    private KeyMonitor keyMonitor;
    private MapHandler mapHandler;
    private Random randomGenerater = new Random();

    private Player player;
    public final int EnemyLimit = 10;
    public int EnemyNum = 0;

    /**
     * contructor takes no argument. inside the constructor, create RoomGenrator to
     * randomize a maze which is the map and return it as a ArrayList then store it
     * in the mapHandler create a player instance and addd it to
     * mapHandler.objectOnScreen
     */
    public Game() {
        RoomGenerator roomGenerator = new RoomGenerator();
        roomGenerator.generateFloor();

        player = new Player(1640, 1640);
        mapHandler = new MapHandler(this);

        // check whether there is a rock overlapping with player, if there is one, then
        // delete it.
        addObjectOnScreen(player);
        mapHandler.setObjectOnMap(roomGenerator.getObjectArray());
        GameObject ForD = null;
        for (GameObject e : mapHandler.objectOnMap) {
            if (e.getObjectarea().OverlapCheck(player.getObjectarea())) {
                ForD = e;
            }
        }
        if (ForD != null)
            mapHandler.objectOnMap.remove(ForD);

    }

    /**
     * update() for game, it will loop through the objectOnScreen and call update
     * for each GameObject also it will renew the view inside the camera
     */
    public void update() {

        for (GameObject e : mapHandler.getObjectOnScreen())
            e.update(this);
        mapHandler.MapToScreen();
    }

    /**
     * a method which will be automatically be called when the gameThread is started
     * inside GUIlauncher it also limits the FPS to 60 it will called game.update()
     * 60 times a second the time is implemented by using System.nanoTime();
     */
    public void run() {
        long lastTime = System.nanoTime(); // long 2^63
        double nanoSecondConversion = 1000000000.0 / 60; // 60 frames per second
        double changeInSeconds = 0;

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1) {
                update();
                if (!EndOfGame()) {
                    changeInSeconds--;
                }
                else{
                    System.exit(0);
                }
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {

                }
            }
            lastTime = now;
        }

    }

    /**
     * add an object into mapHandler.objectOnScreen
     * 
     * @param obj
     */
    public void addObjectOnScreen(GameObject obj) {
        this.mapHandler.AddObjectOnScreen(obj);
    }

    /**
     * add an object into mapHandler.objectForAdd
     * 
     * @param obj
     */
    public void addObjectForAdd(GameObject obj) {
        this.mapHandler.AddObjectForAdd(obj);
    }

    /**
     * a method check whether the game should stop.
     * 
     * @return a boolean , if it is true , then game should be stop
     */
    public boolean EndOfGame() {
        if (this.player.getAlive() != true) {
            return true;
        }
        return false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public KeyMonitor getKeyMonitor() {
        return this.keyMonitor;
    }

    public void setKeyMonitor(KeyMonitor keyMonitor) {
        this.keyMonitor = keyMonitor;
    }

    public Random getRandomGenerator() {
        return this.randomGenerater;
    }

    public MapHandler getMapHandler() {
        return this.mapHandler;
    }
}