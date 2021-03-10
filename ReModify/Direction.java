/**
 * using 4 boolean instance variable to indicate direction
 * 
 * @author Zhijie Xia
 * @since 3/5/2020
 */
public class Direction {
    private boolean headUp, headDown, headRight, headLeft;

    /**
     * @param up
     * @param down
     * @param left
     * @param right
     */
    public Direction(boolean up, boolean down, boolean left, boolean right) {
        this.headUp = up;
        this.headDown = down;
        this.headLeft = left;
        this.headRight = right;
    }

    /**
     * a defalut contructor which set every instance variable false
     */
    public Direction() {
        setStill();
    }

    /**
     * check whether two Direction are equal
     * 
     * @param other another Direction
     * @return boolean, true: if two Direction are equal. false: if two Direction
     *         are differnt
     */
    public boolean equals(Direction other) {
        if (this.headUp == other.getHeadUp() && this.headDown == other.getHeadDown()
                && this.headLeft == other.getHeadLeft() && this.headRight == other.getHeadRight()) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @return return the value of headUp
     */
    public boolean getHeadUp() {
        return this.headUp;
    }

    /**
     * 
     * @return return the value of headDown
     */
    public boolean getHeadDown() {
        return this.headDown;
    }

    /**
     * 
     * @return return the value of headLeft
     */
    public boolean getHeadLeft() {
        return this.headLeft;
    }

    /**
     * 
     * @return return the value of headRight
     */
    public boolean getHeadRight() {
        return this.headRight;
    }

    /**
     * set all direction false
     */
    public void setStill() {
        this.headUp = false;
        this.headDown = false;
        this.headLeft = false;
        this.headRight = false;
    }

    /**
     * set the value of headUp
     * 
     * @param sign : a boolean
     */
    public void setHeadUp(boolean sign) {
        this.headUp = sign;
    }

    /**
     * set the value of headDown
     * 
     * @param sign : a boolean
     */
    public void setHeadDown(boolean sign) {
        this.headDown = sign;
    }

    /**
     * set the value of headLeft
     * 
     * @param sign : a boolean
     */
    public void setHeadLeft(boolean sign) {
        this.headLeft = sign;
    }

    /**
     * set the value of headRight
     * 
     * @param sign : a boolean
     */
    public void setHeadRight(boolean sign) {
        this.headRight = sign;
    }

}