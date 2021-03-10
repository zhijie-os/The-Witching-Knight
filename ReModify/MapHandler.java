import java.util.ArrayList;

/**
 * a class which store maps for Game game.
 * 
 * @author Zhijie Xia
 * @since 3/18/2020
 */
public class MapHandler {

    public ArrayList<GameObject> objectOnScreen = new ArrayList<GameObject>();
    public ArrayList<GameObject> objectOnMap = new ArrayList<GameObject>();
    public ArrayList<GameObject> objectForAdd = new ArrayList<GameObject>();
    public ArrayList<GameObject> NoSide = new ArrayList<GameObject>();
    public ArrayList<GameObject> EnemySide = new ArrayList<GameObject>();

    private Player player;

    private Game game;

    /**
     * contructor take a Game instance to make associate with game.
     * 
     * @param game
     */
    public MapHandler(Game game) {
        this.player = game.getPlayer();
        this.game = game;
    }

    /**
     * a method which check each GameObject inside this.objectOnScreen if a
     * GameObject is either dead or not inside camera area, then remove this
     * GameObject
     */
    public void removeObjects() {
        ObjectArea testArea = new ObjectArea(this.player.getObjectarea().getX(), this.player.getObjectarea().getY(),
                1600, 800);
        ArrayList<GameObject> forDelete = new ArrayList<GameObject>();

        // simply add GameObject which is not not alive to the forDelete array;
        for (GameObject e : this.objectOnScreen) {
            if (!e.getAlive()) {
                if (e.myType == Type.Enemy1 || e.myType == Type.Enemy2) {
                    game.EnemyNum--;
                }
                forDelete.add(e);
            }
        }

        // simply add GameObject which is not on the screen to forDelete ArrayList
        for (GameObject e : this.objectOnScreen) {
            if (!e.getObjectarea().OverlapCheck(testArea)) {
                if (e.myType == Type.Enemy1 || e.myType == Type.Enemy2) {
                    game.EnemyNum--;
                }
                forDelete.add(e);
            }
        }

        for (GameObject e : forDelete) {
            this.objectOnScreen.remove(e);
        }

        // two ArrayList holds GameObjects which need to be deleted
        // the reason why I add this two ArrayList is to avoid thread error(U can't
        // delete something inside an ArrayList when you are looping through it)
        ArrayList<GameObject> DeleteEnemy = new ArrayList<GameObject>();
        ArrayList<GameObject> DeleteRock = new ArrayList<GameObject>();
        for (GameObject e : forDelete) {
            if (e.myType == Type.Rock) {
                DeleteRock.add(e);
            }

            else if (e.myType == Type.Grave) {
                DeleteEnemy.add(e);
            }

            else if (e.myType == Type.Enemy1 || e.myType == Type.Enemy2) {
                DeleteEnemy.add(e);
            }

        }

        for (GameObject e : DeleteEnemy) {
            this.EnemySide.remove(e);
        }

        for (GameObject e : DeleteRock) {
            this.NoSide.remove(e);
        }

    }

    /**
     * a method that simply empty this.objectForAdd
     */
    public void resetObjectForAdd() {
        this.objectForAdd = new ArrayList<GameObject>();
    }

    /**
     * a method which calls removeObject() and renewObjectOnScreen(),
     * inScreen(GameObject obj), also it loops through every GameObject inside
     * objectOnMap to check whether the GameOBject is inside camera area. By
     * implement all above, we set up the camera successfully
     */
    public void MapToScreen() {
        removeObjects();

        for (GameObject e : this.objectOnMap) {
            if (inScreen(e) && (!this.objectOnScreen.contains(e)))
                this.objectForAdd.add(e);
        }

        renewObjectOnScreen();
    }

    /**
     * a method that checks the given GamObject is inside the camera area or not
     * 
     * @param obj
     * @return a boolean which indicates whether obj is inside the camera area.
     */
    public boolean inScreen(GameObject obj) {
        ObjectArea testArea = new ObjectArea(this.player.getObjectarea().getX(), this.player.getObjectarea().getY(),
                1600, 800);
        if (obj.getObjectarea().OverlapCheck(testArea)) {
            return true;
        }
        return false;
    }

    /**
     * a method which add GameObjects from objectForAdd to EnemySide and NoSide.
     */
    public void renewObjectOnScreen() {

        for (GameObject e : this.objectForAdd) {
            this.objectOnScreen.add(e);

            if (e.myType == Type.Rock) {
                this.NoSide.add(e);
            }

            else if (e.myType == Type.Grave) {
                this.EnemySide.add(e);
            }

            else if (e.myType == Type.Enemy1 || e.myType == Type.Enemy2) {
                this.EnemySide.add(e);
            }

        }
        resetObjectForAdd();
    }

    // getters and setters below

    public ArrayList<GameObject> getObjectOnScreen() {
        return this.objectOnScreen;
    }

    public ArrayList<GameObject> getObjectOnMap() {
        return this.objectOnMap;
    }

    public ArrayList<GameObject> getObjectForAdd() {
        return this.objectForAdd;
    }

    public ArrayList<GameObject> getNoside() {
        return this.NoSide;
    }

    public ArrayList<GameObject> getEnemySide() {
        return this.EnemySide;
    }

    public void setObjectOnScreen(ArrayList<GameObject> alist) {
        this.objectOnScreen = alist;
    }

    public void setObjectOnMap(ArrayList<GameObject> alist) {
        this.objectOnMap = alist;
    }

    public void AddObjectOnScreen(GameObject obj) {
        this.objectOnScreen.add(obj);
    }

    public void AddObjectForAdd(GameObject obj) {
        this.objectForAdd.add(obj);
    }
}