
import java.util.ArrayList;
import java.util.Random;

/**
 * The Edge class represents a connection between 2 unique rooms all the
 * EdgeNodes in which two rooms overlap in a valid way are part of the edge It
 * contains methods to make a opening and MUST BE unique to the two rooms
 * 
 * @author Samuel Osweiler
 *
 */
public class Edge {
	private ArrayList<EdgeNode> edgeNodes = new ArrayList<EdgeNode>();
	private RoomNode[] connectedRooms = new RoomNode[2];

	/**
	 * initalizes the edge with the 2 roomNodes it is connected to
	 * 
	 * @param room1 The first room
	 * @param room2 The second Room(the ID of room2 MUST BE larger than the ID of
	 *              room1)
	 */
	public Edge(RoomNode room1, RoomNode room2) {
		this.connectedRooms[0] = room1;
		this.connectedRooms[1] = room2;
	}

	/**
	 * adds the edgeNode to the edge(pass by reference)
	 * 
	 * @param edge the EdgeNode to add to the Edge
	 */
	public void addEdgeNode(EdgeNode edge) {
		this.edgeNodes.add(edge);
	}

	/**
	 * returns an arrayList containing the roomNodes that connect via this edge
	 * 
	 * @return array of RoomNodes, index 0 = room1, index 1 = room2
	 */
	public RoomNode[] getConnectedRooms() {
		return this.connectedRooms;
	}

	/**
	 * how many EdgeNodes are contained in this edge
	 * 
	 * @return how many EdgeNodes are contained in the edge
	 */
	public int numOfEdges() {
		return this.edgeNodes.size();
	}

	/**
	 * removes a object at the location of an EdgeNode contained in the Edge
	 * 
	 * @param roomGenerator
	 */
	public void makeOpening(RoomGenerator roomGenerator) {// make an opening using the location of an edge node
		EdgeNode en = this.edgeNodes.get(getRandomNumberInRange(0, this.numOfEdges() - 1));
		roomGenerator.removeObject(en.x, en.y);
	}

	/**
	 * 
	 * @param min the lower bound(inclusive)
	 * @param max the upper bound(inclusive)
	 * @return
	 */
	private static int getRandomNumberInRange(int min, int max) {

		if (min > max) {
			throw new IllegalArgumentException("max must be greater than min");
		} else if (min == max) {
			return min;
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
