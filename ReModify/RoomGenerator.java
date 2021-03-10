
import java.util.Random;
import java.util.ArrayList;

public class RoomGenerator {

	final public int FLOORWIDTH = 30; // how many objects are in each row of the floor
	final public int FLOORHEIGHT = 30; // how many objects are in each column of the floor
	public final boolean DEBUGMODE = false;// if the program prints debug info to the console about the room generation
	final public int OBJECTWIDTH = 128;
	final public int OBJECTHEIGHT = 128;
	final public int MINIMUMWALLDISTANCE = 6; // the minimum distance between two walls parallel to each other
	final public double TARGETWALLDENSITY = 0.5;// note: to high of a number and the program will never be able to
												// generate a room(0.7 is the highest recommended)
	final public int graveRadius = 3;
	private RoomDetector rd;

	private GameObject[] objects;

	/**
	 * Initializes the Controller
	 */
	public RoomGenerator() {
		objects = new GameObject[this.FLOORHEIGHT * this.FLOORWIDTH];
		rd = new RoomDetector(this);
	}

	/**
	 * Generates the floor layout based on some premade templates. This includes the
	 * room connections and the objects in the rooms This function is still under
	 * development but is is functional
	 */
	public void generateFloor() {

		placeWalls();
		if (this.DEBUGMODE == true) {
			System.out.println("finished placing walls");
			printRoom();
		}

		rd.createRoomNodes();// generate the room IDs

		if (this.DEBUGMODE == true)
			printIDs();

		rd.createEdges();
		generatePath();// make the path
		placeGraves();
		printRoom();

	}

	/**
	 * makes a open space in the centre where the player spawns to ensure they can
	 * leave the starting area and don't get stuck
	 */
	private void placeWalls() {
		// make the initial border around the room
		this.addRectangle(new int[] { 0, 0 }, new int[] { this.FLOORWIDTH - 1, this.FLOORHEIGHT - 1 });

		// for(int i = 0; i < 5; i++) {
		while ((double) this.getRocks().size()
				/ (double) (this.FLOORHEIGHT * this.FLOORWIDTH) < this.TARGETWALLDENSITY) {
			ArrayList<Rock> points = this.getRocks();// all the rocks that we can use as a starting point
			int[] startPoint = new int[2];
			int[] endPoint = new int[2];

			if (this.DEBUGMODE == true)
				this.printRoom();

			// select a object as the starting point
			do {
				startPoint = getCoordinatesOfIndex(
						points.indexOf(points.get(getRandomNumberInRange(0, points.size() - 1))));
				endPoint = new int[] {
						getRandomNumberInRange(0,
								((int) Math.floor(this.FLOORWIDTH - 1) / (this.MINIMUMWALLDISTANCE + 1)))
								* (this.MINIMUMWALLDISTANCE + 1),
						getRandomNumberInRange(0,
								((int) Math.floor(this.FLOORHEIGHT - 1) / (this.MINIMUMWALLDISTANCE + 1)))
								* (this.MINIMUMWALLDISTANCE + 1) };
			} // while(startPoint[0] % (this.MINIMUMWALLDISTANCE+1) != 0 || startPoint[1] %
				// (this.MINIMUMWALLDISTANCE+1) != 0 || startPoint[0] == endPoint[0] ||
				// startPoint[1] == endPoint[1]);
			while (Math.abs(startPoint[0] - endPoint[0]) < this.MINIMUMWALLDISTANCE
					|| Math.abs(startPoint[1] - endPoint[1]) < this.MINIMUMWALLDISTANCE);

			// System.out.println("SP: (" + startPoint[0] + "," + startPoint[1] + ") EP: ("
			// + endPoint[0] + "," + endPoint[1] + ")");

			// make the startPoint the top left and the endPoint the bottomRight
			int[] topLeft = new int[2];
			int[] bottomRight = new int[2];

			// topLeft x coordinate and bottomRight x coordinate
			if (startPoint[0] < endPoint[0]) {
				topLeft[0] = startPoint[0];
				bottomRight[0] = endPoint[0];
			} else {
				topLeft[0] = endPoint[0];
				bottomRight[0] = startPoint[0];
			}

			// topLeft y coordinate and bottomRight y coordinate
			if (startPoint[1] < endPoint[1]) {
				topLeft[1] = startPoint[1];
				bottomRight[1] = endPoint[1];
			} else {
				topLeft[1] = endPoint[1];
				bottomRight[1] = startPoint[1];
			}

			// make the rectangle
			addRectangle(topLeft, bottomRight);
		}

		// remove any double walls
		for (int y = 1; y < this.FLOORHEIGHT - 1; y++) {
			for (int x = 1; x < this.FLOORWIDTH - 1; x++) {
				int count = 0;
				if (this.getObject(x - 1, y - 1) != null) {
					count++;
				}
				if (this.getObject(x - 1, y) != null) {
					count++;
				}
				if (this.getObject(x - 1, y + 1) != null) {
					count++;
				}
				if (this.getObject(x, y - 1) != null) {
					count++;
				}
				if (this.getObject(x, y + 1) != null) {
					count++;
				}
				if (this.getObject(x + 1, y - 1) != null) {
					count++;
				}
				if (this.getObject(x + 1, y) != null) {
					count++;
				}
				if (this.getObject(x + 1, y + 1) != null) {
					count++;
				}

				if (count >= 5) {
					this.removeObject(x, y);
				}
			}
		}
		if (this.DEBUGMODE == true) {
			System.out.println("removed double walls");
			this.printRoom();
		}

	}

	/**
	 * @param topLeft     the coordinate of the top left of the rectangle [x,y]
	 * @param bottomRight the coordinate of the bottom Right of the rectangle [x,y]
	 */
	private void addRectangle(int[] topLeft, int[] bottomRight) {
		// make the top row and bottom row(topLeft[0],topLeft[1] || bottomRight[1]) ->
		// (bottomRight[0], topLeft[1] || bottomRight[1])
		for (int i = 0; i <= bottomRight[0] - topLeft[0]; i++) {
			this.addObject(topLeft[0] + i, topLeft[1],
					new Rock((topLeft[0] + i) * this.OBJECTWIDTH, topLeft[1] * this.OBJECTHEIGHT));
			this.addObject(topLeft[0] + i, bottomRight[1],
					new Rock((topLeft[0] + i) * this.OBJECTWIDTH, bottomRight[1] * this.OBJECTHEIGHT));
		}
		// make the left and right columns (topLeft[0] || bottomRight[0],topLeft[1]) ->
		// (bottomRight[0] || topLeft[0], bottomRight[1])
		for (int i = 0; i <= bottomRight[1] - topLeft[1]; i++) {
			this.addObject(topLeft[0], topLeft[1] + i,
					new Rock(topLeft[0] * this.OBJECTWIDTH, (topLeft[1] + i) * this.OBJECTHEIGHT));
			this.addObject(bottomRight[0], topLeft[1] + i,
					new Rock(bottomRight[0] * this.OBJECTWIDTH, (topLeft[1] + i) * this.OBJECTHEIGHT));
		}
	}

	/**
	 * takes a index and converts it into a 2d coordinate
	 * 
	 * @param index
	 * @return [x,y]
	 */
	private int[] getCoordinatesOfIndex(int index) {
		int x = index % this.FLOORWIDTH;
		int y = (index - x) / this.FLOORWIDTH;
		return new int[] { x, y };
	}

	/**
	 * connects the rooms based on edges
	 */
	private void generatePath() {
		if (this.DEBUGMODE == true)
			System.out.println("connecting rooms");

		ArrayList<RoomNode> notVisited = new ArrayList<RoomNode>();

		for (int i = 0; i < rd.getNumberOfRooms(); i++) {// Initialize the unvisited array with all the rooms
			notVisited.add(rd.getRoomNode(i + 1));
		}

		while (notVisited.size() > 0) {
			RoomNode r = notVisited.get(0);
			ArrayList<Edge> canidateConnections = new ArrayList<Edge>();
			// connect r to an adjacent visited room and remove r. If no room is visited
			// connect to a random room and remove r
			for (Edge e : rd.edges) {
				if ((e.getConnectedRooms()[0].getID() == r.getID() && !notVisited.contains(e.getConnectedRooms()[1]))
						|| (e.getConnectedRooms()[1].getID() == r.getID()
								&& !notVisited.contains(e.getConnectedRooms()[0]))) {// if there is a connection and the
																						// room is visited
					canidateConnections.add(e);
				}
			}
			if (canidateConnections.size() > 0) {
				Edge e = canidateConnections.get(getRandomNumberInRange(0, canidateConnections.size() - 1));
				e.makeOpening(this);
				if (this.DEBUGMODE == true)
					System.out.println("connecting rooms " + e.getConnectedRooms()[0].getID() + " and "
							+ e.getConnectedRooms()[1].getID());

			} else {
				for (Edge e : rd.edges) {
					if ((e.getConnectedRooms()[0].getID() == r.getID()
							&& !notVisited.contains(e.getConnectedRooms()[1]))
							|| (e.getConnectedRooms()[1].getID() == r.getID()
									&& !notVisited.contains(e.getConnectedRooms()[0]))) {// if there is a connection and
																							// the room is visited
						e.makeOpening(this);
						if (this.DEBUGMODE == true)
							System.out.println("connecting rooms " + e.getConnectedRooms()[0] + " and "
									+ e.getConnectedRooms()[1]);

						break;
					}
				}
			}
			notVisited.remove(r);
		}

	}

	private void placeGraves() {
		// Initialize the grave ticket overlay array
		int[] graveTicketOverlay = new int[this.FLOORHEIGHT * this.FLOORWIDTH];
		ArrayList<int[]> candidateLocations = new ArrayList<int[]>();

		do {
			candidateLocations.clear();
			for (int y = 0; y < this.FLOORWIDTH; y++) {
				for (int x = 0; x < this.FLOORWIDTH; x++) {
					if (this.getObject(x, y) == null && graveTicketOverlay[this.convert2DTo1D(x, y)] == 0) {
						candidateLocations.add(new int[] { x, y });
					}
				}
			}

			if (candidateLocations.size() > 0) {
				// pick a candidate location to add a grave and update the graveTicketOverlay
				int[] location = candidateLocations.get(getRandomNumberInRange(0, candidateLocations.size() - 1));
				this.addObject(location[0], location[1],
						new Grave(location[0] * this.OBJECTWIDTH, location[1] * this.OBJECTHEIGHT));

				// update the graveTicketOverlay
				int squareRadius = graveRadius; // ie: squareRadius:3 = 1,2,3,G,3,2,1
				for (int x = -squareRadius; x <= squareRadius; x++) {
					for (int y = -squareRadius; y <= squareRadius; y++) {
						if (location[0] + x >= 0 && location[0] + x <= this.FLOORWIDTH - 1 && location[1] + y >= 0
								&& location[1] + y <= this.FLOORHEIGHT - 1) {
							graveTicketOverlay[this.convert2DTo1D(location[0] + x, location[1] + y)] = 1;
						}
					}

				}
			}
		} while (candidateLocations.size() > 0);

		if (this.DEBUGMODE == true) {
			System.out.println("printing graveTicketOverlay");
			for (int i = 0; i < this.FLOORHEIGHT; i++) {
				for (int j = 0; j < this.FLOORWIDTH; j++) {
					System.out.print("(" + graveTicketOverlay[(i * FLOORWIDTH) + j] + ")");
				}
				System.out.println();
			}
		}

	}

	private int convert2DTo1D(int x, int y) {
		int index = (y * FLOORWIDTH) + x;
		return index;
	}

	private int[] convert1DTo2D(int index) {
		int x = index % this.FLOORWIDTH;
		int y = (index - x) & this.FLOORWIDTH;
		return new int[] { x, y };
	}

	private void printIDs() {
		for (int i = 0; i < this.FLOORHEIGHT; i++) {
			for (int j = 0; j < this.FLOORWIDTH; j++) {
				System.out.print("(" + rd.getRoomIDOverlay()[(i * FLOORWIDTH) + j][0] + ","
						+ rd.getRoomIDOverlay()[(i * FLOORWIDTH) + j][1] + ")");
			}
			System.out.println();
		}
	}

	private void printRoom() {
		// display the room in the console
		System.out.println("# = wall, G = grave, \" \" = nothing");
		for (int i = 0; i < this.FLOORHEIGHT; i++) {
			for (int j = 0; j < this.FLOORWIDTH; j++) {
				if (this.getObject(j, i) != null) {
					if (this.getObject(j, i).getClass() == (new Rock(0, 0).getClass())) {
						System.out.print("# ");
					} else if (this.getObject(j, i).getClass() == (new Grave(0, 0).getClass())) {
						System.out.print("G ");
					} else {
						System.out.print("  ");
					}
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Returns the object at the x,y position on the floor
	 * 
	 * @param x the x position of the object on the floor grid
	 * @param y the y position of the object on the floor grid
	 * @return the room object at the x,y location on the floor grid
	 */
	public GameObject getObject(int x, int y) {
		int arrayLocation = (y * FLOORWIDTH) + x;
		return objects[arrayLocation];
	}

	/**
	 * 
	 * @param x      the x position to add the object at
	 * @param y      the y position to add the object at
	 * @param object the object to add to the floor/array
	 */
	public void addObject(int x, int y, GameObject object) {
		this.objects[(y * FLOORWIDTH) + x] = object;
	}

	public void removeObject(int x, int y) {
		this.objects[(y * FLOORWIDTH) + x] = null;
	}

	public ArrayList<GameObject> getObjectArray() {
		ArrayList<GameObject> toReturn = new ArrayList<GameObject>();
		for (GameObject i : this.objects) {
			if (i != null)
				toReturn.add(i);
		}
		return toReturn;
	}

	public ArrayList<Grave> getGraves() {
		ArrayList<Grave> toReturn = new ArrayList<Grave>();
		for (GameObject i : this.objects) {
			if (i.getClass() == (new Grave(0, 0)).getClass())
				toReturn.add((Grave) i);
		}
		return toReturn;
	}

	public ArrayList<Rock> getRocks() {
		ArrayList<Rock> toReturn = new ArrayList<Rock>();
		for (GameObject i : this.objects) {
			if (i != null) {
				if (i.getClass() == (new Rock(0, 0)).getClass())
					toReturn.add((Rock) i);
			}
		}
		return toReturn;
	}

	/**
	 * 
	 * @param min the lower bound(inclusive)
	 * @param max the upper bound(inclusive)
	 * @return
	 */
	private static int getRandomNumberInRange(int min, int max) {

		if (min > max) {
			throw new IllegalArgumentException("max must be greater than min, recived " + min + "," + max);
		} else if (min == max) {
			return min;
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

}
