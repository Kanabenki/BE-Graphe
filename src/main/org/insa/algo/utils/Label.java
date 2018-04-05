package org.insa.algo.utils;

import org.insa.graph.Node;

public class Label implements Comparable<Label> {
    private float length;
    private Node node;
    private Node predecessor;
    private boolean visited = false;

    public Label(float length, Node node, Node predecessor) {
        this.length = length;
        this.node = node;
        this.predecessor = predecessor;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
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

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

    @Override
    public int compareTo(Label l) {
        return (int)Math.signum(this.getLength() - l.getLength());
    }
}