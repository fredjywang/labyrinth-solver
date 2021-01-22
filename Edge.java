/*
 * Name: Fred Wang
 * Student #: 251103126
 * Edge class that represents a edge of the graph
 * 
 */
public class Edge {
	// instance variables
	private Node first_endpoint, second_endpoint;
	private int edge_type;
	private String edge_label;
	
	// constructor without label
	public Edge(Node u, Node v, int type) {
		first_endpoint = u;
		second_endpoint = v;
		edge_type = type;
	}
	
	// constructor with label
	public Edge(Node u, Node v, int type, String label) {
		first_endpoint = u;
		second_endpoint = v;
		edge_type = type;
		edge_label = label;
	}
	
	/**
	 * Getter method for first endpoint of the edge
	 */
	public Node firstEndpoint() {
		return first_endpoint;
	}
	
	/**
	 * Getter method for second endpoint of the edge
	 */
	public Node secondEndpoint() {
		return second_endpoint;
	}
	
	/**
	 * Getter method for type of the edge
	 */
	public int getType() {
		return edge_type;
	}
	
	/**
	 * Getter method for label of the edge
	 */
	public String getLabel() {
		return edge_label;
	}
	
	/**
	 * Setter method for type of edge
	 */
	public void setType(int newType) {
		edge_type = newType;
	}
	
	/**
	 * Setter method for label of edge
	 */
	public void setLabel(String newLabel) {
		edge_label = newLabel;
	}
}
