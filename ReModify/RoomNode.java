/**
 * a room node is the abstraction of a region of the floor surrounded by walls.
 * 
 * @author Sam
 *
 */
public class RoomNode {
	private int roomSize;// how big the room is(excluding walls)
	private int ID;// the unique ID of the room

	public RoomNode(int roomSize, int ID) {
		this.roomSize = roomSize;
		this.ID = ID;
	}

	public int getRoomSize() {
		return this.roomSize;
	}

	public int getID() {
		return this.ID;
	}
}
