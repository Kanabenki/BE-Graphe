package org.insa.algo.utils;

import org.insa.graph.Node;

public class Label implements Comparable<Label> {
    private double length;
    private Node node;
    private Node predecessor;
    private boolean visited = false;
    
    public Label(double length, Node node, Node predecessor) {
        this.length = length;
        this.node = node;
        this.predecessor = predecessor;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Node getNode() {
        return node;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    private double getCost() {
       return this.getLength();
    }
    
	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

    @Override
    public int compareTo(Label l) {
        return (int)Math.signum(this.getCost() - l.getCost());
    }
}