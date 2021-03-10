/**
 * a class which represent the area of a GameObject
 */
public class ObjectArea {
    private int x, y, width, height;

   /**
    * ObjectArea constructor
    * @param x
    * @param y
    * @param width
    * @param height
    */
    public ObjectArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }
    /**
     * default ObjectArea constructor
     */
    public ObjectArea() {
        this(0, 0, 0, 0);
    }

    /**
     * ObjectArea copy constructor
     * @param otherObject  otherObject to be copid
     */
    public ObjectArea(ObjectArea otherObject) {
        this.x = otherObject.getX();
        this.y = otherObject.getY();
        this.width = otherObject.getWidth();
        this.height = otherObject.getHeight();

    }

    // only getter and setter method down
    // get for X,Y limit

    public void XDown(int amount) {
        this.x -= amount;
    }

    public void XUp(int amount) {
        this.x += amount;
    }

    public void YDown(int amount) {
        this.y -= amount;
    }

    public void YUp(int amount) {
        this.y += amount;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getUpMostY() {
        return this.getY() + this.getHeight() / 2;
    }

    public int getDownMostY() {
        return this.getY() - this.getHeight() / 2;
    }

    public int getLeftMostX() {
        return this.getX() - this.getWidth() / 2;
    }

    public int getRightMostX() {
        return this.getX() + this.getWidth() / 2;
    }

    public int getUpMostY(int y, int height) {
        return y + height / 2;
    }

    public int getDownMostY(int y, int height) {
        return y - height / 2;
    }

    public int getLeftMostX(int x, int width) {
        return x - width / 2;
    }

    public int getRightMostX(int x, int width) {
        return x + width / 2;
    }

    /**
     * overlapping check
     * @param otherObject
     * @return if this objectarea and other objectarea overlaps return true , else return false
     */
    public boolean OverlapCheck(ObjectArea otherObject) {
        int otherXRight = (otherObject.getX() + otherObject.getWidth() / 2);
        int otherXLeft = (otherObject.getX() - otherObject.getWidth() / 2);
        int otherYDown = (otherObject.getY() - otherObject.getHeight() / 2);
        int otherYUp = (otherObject.getY() + otherObject.getHeight() / 2);
        int thisXRight = (this.getX() + this.getWidth() / 2);
        int thisXLeft = (this.getX() - this.getWidth() / 2);
        int thisYDown = (this.getY() - this.getHeight() / 2);
        int thisYUp = (this.getY() + this.getHeight() / 2);

        if (thisXRight < otherXLeft || thisXLeft > otherXRight || thisYDown > otherYUp || thisYUp < otherYDown) {
            return false;
        } else {
            return true;
        }
    }
}