// imports
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/*
 * Name: Fred Wang
 * Student #: 251103126
 * Labyrinth class that represents the Labyrinth
 * 
 */
public class Labyrinth {
	// instance variables
	private Graph lab;
	private int lab_width, lab_length;
	private Node start, end;
	private Stack<Node> path = new Stack<Node>();
	private ArrayList<Integer> keys_list;
	
	// constructor
	Labyrinth(String inputFile) {
		try {
			// read the input file
			BufferedReader inFile = new BufferedReader(new FileReader(inputFile));
			inFile.readLine();
			
			// define the width and length of the labyrinth
			lab_width = Integer.parseInt(inFile.readLine());
			lab_length = Integer.parseInt(inFile.readLine());
			
			// store the number of each type of keys provided by the input file
			String[] input_list = inFile.readLine().split(" ");
			keys_list = new ArrayList<Integer>();
			for (int i = 0; i < input_list.length; i++) {
				keys_list.add(Integer.parseInt(input_list[i])); 
			}
			
			// constructing the labyrinth
			lab = new Graph(lab_width*lab_length);
			String line;
			line = inFile.readLine();
			int line_num = 0; // to keep track of the current line number
			
			while (line != null) {
				// turn the line into an array so each character can be analyzed and dealt with accordingly
				String[] line_array = line.split("");
				
				// loop through the array
				for (int i = 0; i < line_array.length; i++) {
					// the start of the labyrinth
					if (line_array[i].equals("s")) start = lab.getNode(i/2 + (line_num/2)*lab_width);
					
					// the exit of the labyrinth
					else if (line_array[i].equals("x")) end = lab.getNode(i/2 + (line_num/2)*lab_width);
					
					// if the character is a room or a wall, nothing needs to be done
					else if (line_array[i].equals("i") || line_array[i].equals("w")) {
						continue;
					}
					
					// represents a corridor
					else if (line_array[i].equals("c")) {
						// check the type of row it's on, RHRH..RH will be an even row and VWVW..VW will be an odd row
						if (line_num % 2 == 0) {
							// get nodes on either side of the corridor
							Node first_node = lab.getNode(i/2 + line_num/2*lab_width);
							Node second_node = lab.getNode((i+1)/2 + (line_num+1)/2*lab_width);
							
							// insert edge object representing the corridor
							lab.insertEdge(first_node, second_node, -1, "corridor"); // what type is corridor?
						}
						else {
							// get nodes above and below corridor
							Node first_node = lab.getNode(i/2 + line_num/2*lab_width);
							Node second_node = lab.getNode(i/2 + (line_num+1)/2*lab_width);
							
							// insert edge object representing the corridor
							lab.insertEdge(first_node, second_node, -1, "corridor"); // what type is corridor?
						}
					}
					
					// represents when a digit is given, which would be a door with the specified type
					else {
						// change from string to integer variable
						int type = Integer.parseInt(line_array[i]);
						
						// check the type of row it's on, RHRH..RH will be an even row and VWVW..VW will be an odd row
						if (line_num % 2 == 0) {
							// get nodes on either side of the corridor
							Node first_node = lab.getNode(i/2 + line_num/2*lab_width);
							Node second_node = lab.getNode((i+1)/2 + (line_num+1)/2*lab_width);
							
							// insert edge object representing the door
							lab.insertEdge(first_node, second_node, type, "door");
						}
						else {
							// get nodes above and below corridor
							Node first_node = lab.getNode(i/2 + line_num/2*lab_width);
							Node second_node = lab.getNode(i/2 + (line_num+1)/2*lab_width);
							
							// insert edge object representing the door
							lab.insertEdge(first_node, second_node, type, "door");
						}
					}
				}
				
				// read next line in the file
				line = inFile.readLine();
				line_num++;
			}
		}
		// catch any exception to due with opening the file
		catch (IOException e ) {
			System.out.println("Error opening file. " + e.getMessage());
			System.exit(0);
		}
		// catch exceptions unrelated to opening the file
		catch (Exception e) {
			System.out.println("Error in input file. " + e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * Getter method that returns the graph object
	 */
	public Graph getGraph() {
		return lab;
	}
	
	/**
	 * solve method that returns an iterator containing the nodes of the path from the entrance to the exit, if the path exists; method returns null if the path does not exist
	 */
	public Iterator<Node> solve() {
		if (this.solveHelper(start, end)) {
			Iterator<Node> path_itr = path.iterator();
			return path_itr;
		}
		else return null;
	}
	
	/**
	 * helper method that returns true if a path is found, false if not
	 */
	private boolean solveHelper(Node start, Node end) {
		start.setMark(true);
		path.push(start);
		
		// if the names are equal, there is a path between the two given nodes
		if (start.getName() == end.getName()) return true;
		else {
			Iterator<Edge> start_incidents;
			try {
				start_incidents = lab.incidentEdges(start);
				
			} catch (GraphException e) {
				System.out.println(e.getMessage());
				return false;
			}
			// loop through the incident edges of start
			while (start_incidents.hasNext()) {
				Edge current_edge = start_incidents.next();
				
				// check if we have the key for the given door; do not need to check if edge is corridor bc nothing needs to be done
				if (current_edge.getLabel().equals("door")) {
					int door_type = current_edge.getType();
					if (keys_list.get(door_type) > 0) {
						// reduce the number of keys for that particular type
						keys_list.set(door_type, keys_list.get(door_type) - 1);
						
						Node new_node = null;
						
						// get the node on the other end of the edge
						if (current_edge.firstEndpoint().getName() == start.getName()) {
							new_node = current_edge.secondEndpoint();
						}
						if (current_edge.secondEndpoint().getName() == start.getName()) {
							new_node = current_edge.firstEndpoint();
						}
						
						// check if the node is marked
						if (new_node.getMark() == false) {
							// look for the path recursively, if a path is found then return true
							if (this.solveHelper(new_node, end)) return true;
							else {
								// if there is no path, then we have to add back the used key
								keys_list.set(door_type, keys_list.get(door_type) + 1);
							}
						}
						// if the node is marked and a key was used, have to get back the key
						else keys_list.set(door_type, keys_list.get(door_type) + 1);
					}
					else {
						// if there is no keys of the given type, the method must return false
						continue;
					}
				}
				else {
					Node new_node = null;
					
					// get the node on the other end of the edge
					if (current_edge.firstEndpoint().getName() == start.getName()) {
						new_node = current_edge.secondEndpoint();
					}
					if (current_edge.secondEndpoint().getName() == start.getName()) {
						new_node = current_edge.firstEndpoint();
					}
					
					// check if the node is marked
					if (new_node.getMark() == false) {
						// look for the path recursively, if a path is found then return true
						if (this.solveHelper(new_node, end)) return true;
					}
				}
			}
			
			// if the method if never returned as true previously, take off the recently added node and return false as there is no path
			path.pop();
			start.setMark(false);
			return false;
		}
	}
}
