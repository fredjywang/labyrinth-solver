/*
 * Name: Fred Wang
 * Student #: 251103126
 * Node class that represents a node of the graph
 * 
 */
public class Node {
	// instance variables
	private int node_name;
	private boolean node_mark;
	
	// constructor
	public Node (int name) {
		node_name = name;
		node_mark = false;
	}
	
	/**
	 * Setter method to set mark variable to given value
	 */
	public void setMark(boolean mark) {
		node_mark = mark;
	}
	
	/**
	 * Getter method to retrieve the value of mark
	 */
	public boolean getMark() {
		return node_mark;
	}
	
	/**
	 * Getter method to retrieve the value of name
	 */
	public int getName() {
		return node_name;
	}
}
