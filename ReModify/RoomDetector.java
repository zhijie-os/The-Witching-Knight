
import java.util.ArrayList;

/**
 * The RoomDetector class takes a array of objects that are considered solid and
 * using them identifies where unique rooms are and where rooms can become
 * connected
 * 
 * @author Samuel Osweiler
 *
 */
public class RoomDetector {
	private int[][] roomIDOverlay;// if it is [-1,-1] then it has not been checked/(it is a blocked wall), if it
									// is [X,-1] then it is a room, if it is [X,Y] then it is a edge node
	/**
	 * room ID 1 is index 0
	 */
	private ArrayList<RoomNode> roomNodes = new ArrayList<RoomNode>();
	public ArrayList<Edge> edges = new ArrayList<Edge>();
	private RoomGenerator roomGenerator;
	private int numOfRooms = -1;

	/**
	 * 
	 * @param roomGenerator a reference to the roomGenerator that created this
	 *                      object
	 */
	public RoomDetector(RoomGenerator roomGenerator) {
		this.roomGenerator = roomGenerator;
		roomIDOverlay = new int[roomGenerator.FLOORHEIGHT * roomGenerator.FLOORWIDTH][2]; // Initialize the array
		// make every element -1
		for (int i = 0; i < roomGenerator.FLOORHEIGHT * roomGenerator.FLOORWIDTH; i++) {
			roomIDOverlay[i][0] = -1;
			roomIDOverlay[i][1] = -1;
		}

		// make the edges of the array 0 to represent the edge
		// the top row
		for (int i = 0; i < roomGenerator.FLOORWIDTH; i++) {
			this.AddID(i, 0, 0);
		}
		// the bottom row
		for (int i = 0; i < roomGenerator.FLOORWIDTH; i++) {
			this.AddID(i, roomGenerator.FLOORHEIGHT - 1, 0);
		}
		// the right column
		for (int i = 1; i < roomGenerator.FLOORHEIGHT - 1; i++) {
			this.AddID(roomGenerator.FLOORWIDTH - 1, i, 0);
		}
		// the left column
		for (int i = 1; i < roomGenerator.FLOORHEIGHT - 1; i++) {
			this.AddID(0, i, 0);
		}

	}

	/**
	 * detects all rooms and creates the roomNodes associated with them
	 */
	public void createRoomNodes() {
		if (roomGenerator.DEBUGMODE == true)
			System.out.println("performing floods to determine rooms and size");

		int currentID = 0; // a counter of what ID to assign to a newly found room

		// loop through every spot on the floor and if it is empty and not assigned an
		// ID perform a flood algorithm
		for (int y = 0; y < roomGenerator.FLOORHEIGHT; y++) {
			for (int x = 0; x < roomGenerator.FLOORWIDTH; x++) {
				if (roomIDOverlay[(y * roomGenerator.FLOORWIDTH) + x][0] == -1
						&& roomGenerator.getObject(x, y) == null) { // check if we should perform the flood algorithm
					currentID++;

					if (roomGenerator.DEBUGMODE == true)
						System.out.print(
								"Starting flood for room ID " + currentID + " at position " + x + "," + y + "...");

					// set the spot to the current ID
					this.AddID(x, y, currentID);
					int roomSize = 0;// how many open tiles are in the room

					// flood outwards
					ArrayList<int[]> toCheck = new ArrayList<int[]>(); // coordinates[x,y] to check and flood
					ArrayList<int[]> forNextCheckCycle = new ArrayList<int[]>(); // when we are looping through the
																					// array any new coordinates are
																					// added to this

					toCheck.add(new int[] { x, y });// add the starting position

					while (toCheck.size() != 0) {// while there are elements to check
						for (int[] pos : toCheck) { // loop through all the coordinates

							// try and flood outwards(Rules: can only spread if not currently on a wall,
							// can't duplicate an ID)
							if ((roomGenerator.getObject(pos[0], pos[1]) == null) || (roomGenerator
									.getObject(pos[0], pos[1]).getClass() != new Rock(0, 0).getClass())) {// if it is
																											// not on a
																											// rock
								roomSize++;

								// try and spread left
								if (pos[0] != 0 && getID(pos[0] - 1, pos[1])[0] != currentID
										&& getID(pos[0] - 1, pos[1])[1] != currentID) {
									this.AddID(pos[0] - 1, pos[1], currentID);
									forNextCheckCycle.add(new int[] { pos[0] - 1, pos[1] });
								}

								// try and spread right
								if (pos[0] != roomGenerator.FLOORWIDTH - 1 && getID(pos[0] + 1, pos[1])[0] != currentID
										&& getID(pos[0] + 1, pos[1])[1] != currentID) {
									this.AddID(pos[0] + 1, pos[1], currentID);
									forNextCheckCycle.add(new int[] { pos[0] + 1, pos[1] });
								}

								// try and spread up
								if (pos[1] != 0 && getID(pos[0], pos[1] - 1)[0] != currentID
										&& getID(pos[0], pos[1] - 1)[1] != currentID) {
									this.AddID(pos[0], pos[1] - 1, currentID);
									forNextCheckCycle.add(new int[] { pos[0], pos[1] - 1 });

								}

								// try and spread down
								if (pos[1] != roomGenerator.FLOORHEIGHT - 1 && getID(pos[0], pos[1] + 1)[0] != currentID
										&& getID(pos[0], pos[1] + 1)[1] != currentID) {
									this.AddID(pos[0], pos[1] + 1, currentID);
									forNextCheckCycle.add(new int[] { pos[0], pos[1] + 1 });
								}

							}

						} // end of for loop
						toCheck.clear();
						// add the cued elements to the toCheck arrayList
						for (int[] i : forNextCheckCycle) {
							toCheck.add(i);
						}
						forNextCheckCycle.clear();
					} // end of while loop for the current iteration of room ID
					if (roomGenerator.DEBUGMODE == true)
						System.out.println(" success, room size is " + roomSize);

					this.roomNodes.add(new RoomNode(roomSize, currentID));
					roomSize = 0;

				} // end of if statement
			}
		}
		this.numOfRooms = currentID;
	}

	public void createEdges() {
		if (roomGenerator.DEBUGMODE == true)
			System.out.println("Detecting valid edges and adding EdgeNodes...");

		// loop through every spot on the floor and if it has 2 ID's(excluding [0,X])
		// make a EdgeNode and add it to the Edge
		for (int y = 0; y < roomGenerator.FLOORHEIGHT; y++) {
			for (int x = 0; x < roomGenerator.FLOORWIDTH; x++) {
				if (this.getID(x, y)[0] != 0 && this.getID(x, y)[0] != -1 && this.getID(x, y)[1] != -1) {// if it is not
																											// on the
																											// outside
																											// edge and
																											// there are
																											// two
																											// overlapping
																											// rooms
					if (roomGenerator.DEBUGMODE == true)
						System.out.println(
								"EdgeNode point detected at (" + x + "," + y + ") between rooms " + this.getID(x, y)[0]
										+ " and " + this.getID(x, y)[1] + " , now adding EdgeNode to edge");

					EdgeNode temp = new EdgeNode(x, y, this.roomNodes.get(this.getID(x, y)[0] - 1),
							this.roomNodes.get(this.getID(x, y)[1] - 1));
					this.addToEdge(temp);
				}
			}
		}

		if (roomGenerator.DEBUGMODE == true) {
			System.out.println("printing all edges and how many edge nodes they contain...");
			for (Edge e : this.edges) {
				System.out.println("(" + e.getConnectedRooms()[0].getID() + "," + e.getConnectedRooms()[1].getID() + ")"
						+ "length: " + e.numOfEdges());
			}
		}
	}

	/**
	 * 
	 * @param room1ID
	 * @param room2ID
	 * @return adds an EdgeNode to the Edge between room1 and room2. If it does not
	 *         exist it is created
	 */
	private void addToEdge(EdgeNode edgeNode) {

		for (Edge e : this.edges) { // loop through each edge to check if we already have an edge between the two
									// rooms
			if (e.getConnectedRooms()[0] == edgeNode.getConnectedRooms()[0]
					&& e.getConnectedRooms()[1] == edgeNode.getConnectedRooms()[1]) { // if it already exists
				if (roomGenerator.DEBUGMODE == true)
					System.out.print("  edge already exists, adding EdgeNode to the Edge...");

				e.addEdgeNode(edgeNode);

				if (roomGenerator.DEBUGMODE == true)
					System.out.println("success\n");

				return;
			}
		}
		// if it doesn't exist create it and add the edge to it
		if (roomGenerator.DEBUGMODE == true)
			System.out.print("  edge doesn't exist, creating new Edge...");

		Edge temp = new Edge(edgeNode.getConnectedRooms()[0], edgeNode.getConnectedRooms()[1]);
		temp.addEdgeNode(edgeNode);
		this.edges.add(temp);

		if (roomGenerator.DEBUGMODE == true)
			System.out.println("success\n");
	}

	public int[][] getRoomIDOverlay() {
		return this.roomIDOverlay;
	}

	/**
	 * 
	 * @param ID the ID of the RoomNode to retrieve
	 * @return the RoomNode of ID ID
	 */
	public RoomNode getRoomNode(int ID) {
		if (ID > 0) {
			return this.roomNodes.get(ID - 1);
		} else {
			System.exit(1);
			return null;
		}
	}

	/**
	 * 
	 * @param x      the x position to add the object at
	 * @param y      the y position to add the object at
	 * @param object the object to add to the floor/array
	 */
	public void AddID(int x, int y, int ID) {
		if (roomIDOverlay[(y * roomGenerator.FLOORWIDTH) + x][0] == -1) {
			this.roomIDOverlay[(y * roomGenerator.FLOORWIDTH) + x][0] = ID;
		} else {
			this.roomIDOverlay[(y * roomGenerator.FLOORWIDTH) + x][1] = ID;
		}
	}

	/**
	 * 
	 * @param x the x position to add the object at
	 * @param y the y position to add the object at
	 */
	public int[] getID(int x, int y) {
		return this.roomIDOverlay[(y * roomGenerator.FLOORWIDTH) + x];
	}

	public int getNumberOfRooms() {
		return this.numOfRooms;
	}

}
