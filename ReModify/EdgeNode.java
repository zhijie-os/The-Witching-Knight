/**
 * A EdgeNode is a point on the RoomIDOverlay where 2 rooms overlap
 * @author Samuel Osweiler
 *
 */

public class EdgeNode {
	private RoomNode[] connectedRooms = new RoomNode[2];
	public int x;
	public int y;
	

	/**
	 * 
	 * @param x the x position on the RoomOverlay
	 * @param y the y position on the RoomOverlay
	 * @param room1 a RoomNode object
	 * @param room2 a RoomNode object
	 */
	public EdgeNode(int x, int y, RoomNode room1, RoomNode room2) {
		this.x = x;
		this.y = y;
		
		//make sure they are in proper order
		if (room1.getID() < room2.getID()) {
			this.connectedRooms[0] = room1;
			this.connectedRooms[1] = room2;
		} else {
			this.connectedRooms[0] = room2;
			this.connectedRooms[1] = room1;
		}
	}
	
	/**
	 * returns the roomNodes that the EdgeNode is connected to
	 * @return
	 */
	public RoomNode[] getConnectedRooms() {
		return this.connectedRooms;
	}
}
