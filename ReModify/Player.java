import java.util.ArrayList;

import javafx.scene.image.ImageView;

import java.lang.Math;
import javafx.scene.image.Image;

/**
 * player class extends GameObject, this class takes input from keyMonitor and
 * react (move and attack)
 * 
 * @author Zhijie Xia
 * @since 3/18/2020
 */
public class Player extends GameObject {
	private Direction attackDirection = new Direction();

	private int coolDown = 0;

	private int inevitableTime = 0;

	private ImageView image;

	/**
	 * contructor which takes 2 arguments- x and y coordinate
	 * 
	 * @param x indicate the x-coordinate of player
	 * @param y indicate the x-coordinate of player
	 */
	public Player(int x, int y) {
		super(x, y, 40, 40, 100, 3, 2, true);
		this.image = setImage((new ImageView(new Image("file:player.png"))), 40, 40);
		myType = Type.Player;
	}

	/**
	 * handle the keyMonitor to takes input for player's movement and attacking
	 * 
	 * @param game
	 */
	public void update(Game game) {
		KeyMonitor keyListener = game.getKeyMonitor();

		if(keyListener.getESC()==true){
			System.exit(0);
		}
		

		if (keyListener.up()) {
			if (PlayerMoveValidity(this.getObjectarea().getX(), this.getObjectarea().getY() - this.getSpeed(), game))
				this.moveUp();

		}

		// S is pressed, player moving down
		if (keyListener.down()) {
			if (PlayerMoveValidity(this.getObjectarea().getX(), this.getObjectarea().getY() + this.getSpeed(), game))
				this.moveDown();

		}

		// A is pressed, player moving left
	    if (keyListener.left()) {

			if (PlayerMoveValidity(this.getObjectarea().getX() - this.getSpeed(), this.getObjectarea().getY(), game))
				this.moveLeft();

		}

		// D is pressed, player moving right
		if (keyListener.right()) {
			if (PlayerMoveValidity(this.getObjectarea().getX() + this.getSpeed(), this.getObjectarea().getY(), game))
				this.moveRight();

		}

		// player attack trigue
		if (coolDown >= 20) {
			if (keyListener.upArrow() && keyListener.rightArrow()) {// heading up and right
				attackDirection = new Direction(true, false, false, true);
				attack(game);
			}

			else if (keyListener.upArrow() && keyListener.leftArrow()) {// heading up and left
				attackDirection = new Direction(true, false, true, false);
				attack(game);
			}

			else if (keyListener.downArrow() && keyListener.rightArrow()) {// heading down and right
				attackDirection = new Direction(false, true, false, true);
				attack(game);
			}

			else if (keyListener.downArrow() && keyListener.leftArrow()) {// heading down and left
				attackDirection = new Direction(false, true, true, false);
				attack(game);
			}

			else if (keyListener.upArrow()) {// heading up
				attackDirection = new Direction(true, false, false, false);
				attack(game);
			}

			else if (keyListener.downArrow()) {// heading down
				attackDirection = new Direction(false, true, false, false);
				attack(game);
			}

			else if (keyListener.leftArrow()) {// heading left
				attackDirection = new Direction(false, false, true, false);
				attack(game);
			}

			else if (keyListener.rightArrow()) {// heading right
				attackDirection = new Direction(false, false, false, true);
				attack(game);
			}

		}

		this.checkAliveByHealth();
		this.coolDown++;
		if (this.inevitableTime != 0) {
			this.inevitableTime--;
		}

	}

	/**
	 * a method which will check the attackDirection and create an weapon() to
	 * attack
	 * 
	 * @param game
	 */
	public void attack(Game game) {
		// System.out.println("Player attacked!");

		// after attacked, player coolDown renew;
		this.coolDown = 0;

		if ((attackDirection.getHeadDown() && attackDirection.getHeadRight())
				|| (attackDirection.getHeadDown() && attackDirection.getHeadLeft())) {
			Weapon forAdd = new Weapon(getObjectarea().getX(), getObjectarea().getY(), attackDirection, 0);
			game.addObjectForAdd(forAdd);
		}

		else if ((attackDirection.getHeadUp() && attackDirection.getHeadRight())
				|| (attackDirection.getHeadUp() && attackDirection.getHeadLeft())) {
			Weapon forAdd = new Weapon(getObjectarea().getX(), getObjectarea().getY(), attackDirection, 0);
			game.addObjectForAdd(forAdd);
		}

		else if (attackDirection.getHeadDown()) {
			Weapon forAdd = new Weapon(getObjectarea().getX(), getObjectarea().getY(), attackDirection, 0);
			game.addObjectForAdd(forAdd);
		}

		else if (attackDirection.getHeadUp()) {
			Weapon forAdd = new Weapon(getObjectarea().getX(), getObjectarea().getY(), attackDirection, 0);
			game.addObjectForAdd(forAdd);
		}

		else if (attackDirection.getHeadLeft()) {
			Weapon forAdd = new Weapon(getObjectarea().getX(), getObjectarea().getY(), attackDirection, 0);
			game.addObjectForAdd(forAdd);
		}

		else if (attackDirection.getHeadRight()) {
			Weapon forAdd = new Weapon(getObjectarea().getX(), getObjectarea().getY(), attackDirection, 0);
			game.addObjectForAdd(forAdd);
		}
	}

	/**
	 * takes a x,y coordinate and check whether player can move to that location
	 * 
	 * @param x    a x-coordinate
	 * @param y    a y-coordinate
	 * @param game
	 * @return boolean, if the movement is valid, then return true
	 */
	public boolean PlayerMoveValidity(int x, int y, Game game) {

		// when player
		if (this.inevitableTime != 0) {
			// when player is inevitable, only check the collision between player and the
			// rocks
			for (GameObject e : game.getMapHandler().getNoside()) {
				if (new ObjectArea(x, y, this.getObjectarea().getWidth(), this.getObjectarea().getHeight())
						.OverlapCheck(e.getObjectarea())) {
					// System.out.println("invalid move");
					return false;
				}
			}
		}

		// when player is not inevitable, check the collision betwwen player and enemys
		// and rocks.
		else {

			// checking collision with rocks
			for (GameObject e : game.getMapHandler().getNoside()) {
				if (new ObjectArea(x, y, this.getObjectarea().getWidth(), this.getObjectarea().getHeight())
						.OverlapCheck(e.getObjectarea())) {
					// System.out.println("invalid move");
					return false;
				}
			}

			// checking collision witn enemys
			for (GameObject e : game.getMapHandler().getEnemySide()) {
				if (new ObjectArea(x, y, this.getObjectarea().getWidth(), this.getObjectarea().getHeight())
						.OverlapCheck(e.getObjectarea())) {
					// System.out.println("invalid move");
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * override the helthDown(int amount) to give player inevitable time
	 * 
	 * @param damage a damage number that player will take
	 */
	public void healthDown(int damage) {
		super.healthDown(damage);
		// when player taking damage, player become inevitable for 2s;
		inevitableTime = 120;
	}

	/**
	 * a method check whether player is currently in its inevitable time
	 * 
	 * @return a boolean, if player is evitable , then return true.
	 */
	public boolean Inevitable() {

		if (this.inevitableTime != 0) {
			return true;
		}

		return false;
	}

	/**
	 * player moves up and left
	 * 
	 * @param distance a amount of distance player will move diagonally
	 * @param game
	 */
	public void moveUpLeft(int distance, Game game) {
		int Movement = ((int) (distance / (Math.pow(distance, 0.5))));

		if (PlayerMoveValidity(this.getObjectarea().getX() - Movement, this.getObjectarea().getY() - Movement, game)) {
			this.getObjectarea().XDown(Movement + 1);
			this.getObjectarea().YDown(Movement + 1);
		}
	}

	/**
	 * player moves up and right
	 * 
	 * @param distance a amount of distance player will move diagonally
	 * @param game
	 */
	public void moveUpRight(int distance, Game game) {
		int Movement = ((int) (distance / (Math.pow(distance, 0.5))));

		if (PlayerMoveValidity(this.getObjectarea().getX() + Movement, this.getObjectarea().getY() - Movement, game)) {
			this.getObjectarea().XUp(Movement + 1);
			this.getObjectarea().YDown(Movement + 1);
		}
	}

	/**
	 * player moves down and left
	 * 
	 * @param distance a amount of distance player will move diagonally
	 * @param game
	 */
	public void moveDownLeft(int distance, Game game) {
		int Movement = ((int) (distance / (Math.pow(distance, 0.5))));

		if (PlayerMoveValidity(this.getObjectarea().getX() - Movement, this.getObjectarea().getY() + Movement, game)) {
			this.getObjectarea().XDown(Movement + 1);
			this.getObjectarea().YUp(Movement + 1);
		}
	}

	/**
	 * player moves down and right
	 * 
	 * @param distance a amount of distance player will move diagonally
	 * @param game
	 */
	public void moveDownRight(int distance, Game game) {
		int Movement = ((int) (distance / (Math.pow(distance, 0.5))));

		if (PlayerMoveValidity(this.getObjectarea().getX() + Movement, this.getObjectarea().getY() + Movement, game)) {
			this.getObjectarea().XUp(Movement + 1);
			this.getObjectarea().YUp(Movement + 1);
		}
	}

	/**
	 * a method return this.ImageView
	 * 
	 * @return this.ImageView
	 */
	public ImageView getLabel() {
		return image;
	}

}