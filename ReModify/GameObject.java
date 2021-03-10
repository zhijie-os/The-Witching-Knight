

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * a general GameObject type - abstract
 * @author Zhijie Xia
 * @since 3-18-2020
 */
public abstract class GameObject
{   
    
    private ObjectArea objectarea;
    private int health,speed,damage;
    private boolean alive;
    private int countinousMoving=0;
    //let type be access by any class
    Type myType;


    /**
     * GameObject constructor
     * @param x
     * @param y
     * @param width
     * @param height
     * @param health
     * @param speed
     * @param damage
     * @param alive
     */
    public GameObject(int x,int y , int width, int height,int health,int speed, int damage,boolean alive)
    {

        this.objectarea=new ObjectArea(x,y,width,height);

        this.health=health;

        this.speed=speed;

        this.damage=damage;

        this.alive=alive;

    }


    /**
     * copy contructor
     * @param other other GameObject
     */
    public GameObject(GameObject other)
    {
        this.objectarea=other.getObjectarea();

        this.health=other.getHealth();

        this.speed=other.getSpeed();

        this.damage=other.getDamage();

        this.alive=other.getAlive();
    }


    //check the health to and change the alive 
    public void checkAliveByHealth()
    {
        
        if (this.getHealth()==0||this.getHealth()<0)
            
        this.setAlive(false);
    }




    /**
     * abstract update
     * @param game
     */
    public abstract void update(Game game);
    
    //add health
    public void healthUp(int amount)
    {
        this.health+=amount;
    }

    //decrease healt
    public void healthDown(int amount)
    {
        this.health-=amount;
    }
    //increase speed
    public void speedUp(int amount){
        this.speed+=amount;
    }
    //decrease speed
    public void speedDown(int amount){
        this.speed-=amount;
    }
    //increase damage
    public void damageUp(int amount){
        this.damage+=amount;
    }
    //decrease damage
    public void damageDown(int amount){
        this.damage-=amount;
    }


    //private ObjectArea objectarea;
    //private int heath,speed,range,damage;
    //Getter and Setter methods down here

    public ObjectArea getObjectarea()
    {
        return this.objectarea;
    }

    public int getHealth()
    {
        return this.health;
    }

    public int getSpeed()
    {
        return this.speed;
    }

    public int getDamage()
    {
        return this.damage;
    }

    public boolean getAlive()
    {
        return this.alive;
    }

    public void setObjectarea(ObjectArea otherObject)
    {
        this.objectarea=new ObjectArea(otherObject);
    }

    public void setHealth(int health)
    {
        this.health=health;
    }

    public void setSpeed(int speed)
    {
        this.speed=speed;
    }

    public void setDamage(int damage)
    {
        this.damage=damage;
    }

    public void setAlive(boolean alive)
    {
        this.alive=alive;
    }

    //object movement
    public void moveUp()
    {
        objectarea.YDown(this.speed);
    }

    public void moveDown()
    {
        objectarea.YUp(this.speed);
    }

    public void moveLeft()
    {
        objectarea.XDown(this.speed);
    }

    public void moveRight()
    {
        objectarea.XUp(this.speed);
    }

    /**
     * resize image
     * @param aImage
     * @param width width after resized
     * @param height width after resized
     * @return resized ImageView
     */
    public ImageView setImage(ImageView aImage, int width, int height)
    {
        aImage.setFitHeight(height);

        aImage.setFitWidth(width);

        aImage.setPreserveRatio(false);

        return aImage;

    }
    

    public abstract ImageView getLabel();
}
