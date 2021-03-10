
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
/**
 *@author Dongjie Liu
 *@since 2020/3/3
 */
public class KeyMonitor {

    public static boolean U;
    public static boolean Do;
    public static boolean R;
    public static boolean L;

    public static boolean W;
    public static boolean A;
    public static boolean S;
    public static boolean D;

    public static boolean Esc;

    /**
     * takes a scene and add keyboardlistener to it
     *@param scene
     */
    public KeyMonitor(Scene scene) {
        setKeyListener(scene);
    }

    /**
     * 
     * @return
     */
    public boolean getESC(){
        return this.Esc;
    }


    /**
     *@return W
     */
    public boolean up() {
        return W;
    }


    /**
     *@return S
     */
    public boolean down() {
        return S;
    }


    /**
     *@return A
     */
    public boolean left() {
        return A;
    }


    /**
     *@return D
     */
    public boolean right() {
        return D;
    }


    /**
     *@return Up
     */
    public boolean upArrow() {
        return U;
    }

    

    /**
     *@return Down
     */
    public boolean downArrow() {
        return Do;
    }


    /**
     *@return left
     */
    public boolean leftArrow() {
        return L;
    }



    /**
     *@return right
     */
    public boolean rightArrow() {
        return R;
    }


    /**
     *handle key event : pressed
     */
    public static void setKeyListener(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP:
                    U = true;
                    break;
                case DOWN:
                    Do = true;
                    break;
                case LEFT:
                    L = true;
                    break;
                case RIGHT:
                    R = true;
                    break;
                case W:
                    W = true;
                    break;
                case A:
                    A = true;
                    break;
                case S:
                    S = true;
                    break;
                case D:
                    D = true;
                    break;
                case ESCAPE:
                    Esc =true;
                    break;
            }
        });


        /**
         * handle key event : released
         */
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP:
                        U = false;
                        break;
                    case DOWN:
                        Do = false;
                        break;
                    case LEFT:
                        L = false;
                        break;
                    case RIGHT:
                        R = false;
                        break;
                    case W:
                        W = false;
                        break;
                    case A:
                        A = false;
                        break;
                    case S:
                        S = false;
                        break;
                    case D:
                        D = false;
                        break;
                    case ESCAPE:
                        Esc =false;
                        break;
                }
            }
        });
    }

}
