// imports
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

/*
 * Name: Fred Wang
 * Student #: 251103126
 * Graph class that represents an undirected graph
 * 
 */
public class Graph implements GraphADT {
	// instance variables
	private ArrayList<Node> vertex_list;
	private ArrayList<LinkedList<Edge>> edge_list;
	
	// constructor
	public Graph(int n) {
		// create vertex list
		vertex_list = new ArrayList<Node>();
		for (int i = 0; i < n; i++) {
			vertex_list.add(new Node(i));
		}
		
		// create edge list
		edge_list = new ArrayList<LinkedList<Edge>>();
		for (int i = 0; i < n; i++) {
			edge_list.add(new LinkedList<Edge>());
		}
	}
	
	/**
	 * insertEdge method that adds an edge to the graph connecting nodes u and v; throws GraphException if any of the two nodes do not exist, or if there is already an edge connecting those two nodes
	 */
	@Override
	public void insertEdge(Node nodeu, Node nodev, int type, String label) throws GraphException {
		// seeing if the two given nodes exist
		try {
			Node first_node = this.getNode(nodeu.getName());
			Node second_node = this.getNode(nodev.getName());
		}
		catch (GraphException e) {
			throw new GraphException("No such node exists.");
		}
		
		// seeing if there is already an edge connecting the two given nodes
		try {
			this.getEdge(nodeu, nodev);
			throw new GraphException("There is already an edge with the given nodes.");
		}
		catch (GraphException e) {
		}
		
		Edge new_edge = new Edge(nodeu, nodev, type, label);
		int u_index = -1;
		int v_index = -1;
		for (int i = 0; i < vertex_list.size(); i++) {
			if (vertex_list.get(i).getName() == nodeu.getName()) u_index = i;
			if (vertex_list.get(i).getName() == nodev.getName()) v_index = i;
		}
		edge_list.get(u_index).addLast(new_edge);
		edge_list.get(v_index).addLast(new_edge);
	}
	
	/**
	 * insertEdge method similar to before, but it does not specify a label value; will still throw GraphException if any of the two nodes do not exist, or if there is already an edge connecting those two nodes
	 */
	@Override
	public void insertEdge(Node nodeu, Node nodev, int type) throws GraphException {
		// seeing if the two given nodes exist
		try {
			Node first_node = this.getNode(nodeu.getName());
			Node second_node = this.getNode(nodev.getName());
		}
		catch (GraphException e) {
			throw new GraphException("No such node exists.");
		}
		
		// seeing if there is already an edge connecting the two given nodes
		try {
			this.getEdge(nodeu, nodev);
			throw new GraphException("There is already an edge with the given nodes.");
		}
		catch (GraphException e) {
		}
		
		Edge new_edge = new Edge(nodeu, nodev, type);
		int u_index = -1;
		int v_index = -1;
		for (int i = 0; i < vertex_list.size(); i++) {
			if (vertex_list.get(i).getName() == nodeu.getName()) u_index = i;
			if (vertex_list.get(i).getName() == nodev.getName()) v_index = i;
		}
		edge_list.get(u_index).addLast(new_edge);
		edge_list.get(v_index).addLast(new_edge);
	}
	
	/**
	 * Getter method that returns the node of the given name; throws GraphException is node does not exist
	 */
	@Override
	public Node getNode(int u) throws GraphException {
		// look through the vertex list to see if any of their names are equal to the given name
		for (int i = 0; i < vertex_list.size(); i++) {
			if (vertex_list.get(i).getName() == u) return vertex_list.get(i);
		}
		throw new GraphException("No such node exists.");
	}
	
	/**
	 * incidentEdges method returns an iterator that stores all edges incident to the given node; returns null if no incident edges; throws GraphException if the given node is not in the graph
	 */
	@Override
	public Iterator<Edge> incidentEdges(Node u) throws GraphException {
		int index = -1;
		
		// look through vertex list to find index of desired node
		for (int i = 0; i < vertex_list.size(); i++) {
			if (u.getName() == vertex_list.get(i).getName()) index = i;
		}
		
		// if index remains unchanged, that means that the given node is not in the graph
		if (index == -1) throw new GraphException("Node does not exist in the graph.");
		
		// check if size of linked list is 0, if so, that means there are no incident edge and the method must return 0
		if (edge_list.get(index).size() == 0) return null;
		
		// create then return iterator
		Iterator<Edge> incident_itr = edge_list.get(index).iterator();
		
		return incident_itr;
	}
	
	/**
	 * Getter method that returns the edge that connects nodes u and v; throws GraphException if there is no edge between u and v, or if u or v are not nodes of the graph
	 */
	@Override
	public Edge getEdge(Node u, Node v) throws GraphException {
		// look through linked list located at the found index
		try {
			Iterator<Edge> u_incidents = this.incidentEdges(u);
			if (u_incidents == null) throw new GraphException("There is no edge between the two given nodes.");
			while (u_incidents.hasNext()) {
				Edge current = u_incidents.next();
				// check if the first or second input's name value is equal to v, if so, return current
				if (current.firstEndpoint().getName() == v.getName() || current.secondEndpoint().getName() == v.getName()) return current;
			}
			
			
			// if the while loop ends, that means there is no edge between u and v
			throw new GraphException("There is no edge between the two given nodes.");
		}
		catch (GraphException e) {
			throw new GraphException("Node does not exist in the graph.");
		}
	}
	
	/**
	 * areAdjacent method that returns true if the two given nodes are adjacent, false otherwise; throws GraphException if either given node is not in the graph
	 */
	@Override
	public boolean areAdjacent(Node u, Node v) throws GraphException {
		int u_index = -1;
		int v_index = -1;
		
		for (int i = 0; i < vertex_list.size(); i++) {
			if (vertex_list.get(i).getName() == u.getName()) u_index = i;
			if (vertex_list.get(i).getName() == v.getName()) v_index = i;
		}
		
		if (u_index == -1 || v_index == -1) throw new GraphException("Node does not exist in the graph.");
		
		Iterator<Edge> incidents;
		
		// iterate through the smaller of the two lists
		if (edge_list.get(u_index).size() < edge_list.get(v_index).size()) {
			incidents = edge_list.get(u_index).iterator();
			while (incidents.hasNext()) {
				Edge current = incidents.next();
				if (current.firstEndpoint().getName() == v.getName() || current.secondEndpoint().getName() == v.getName()) return true;
			}
		}
		else {
			incidents = edge_list.get(v_index).iterator();
			while (incidents.hasNext()) {
				Edge current = incidents.next();
				if (current.firstEndpoint().getName() == u.getName() || current.secondEndpoint().getName() == u.getName()) return true;
			}
		}
		
		return false;
	}
	
}
