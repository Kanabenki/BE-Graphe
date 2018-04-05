package org.insa.algo.utils;

import org.insa.graph.Node;

public class Label implements Comparable<Label> {
    private int cost;
    private Node node;
    private Node predecessor;

    Label(int cost, Node node, Node predecessor) {
        this.cost = cost;
        this.node = node;
        this.predecessor = predecessor;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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

    @Override
    public int compareTo(Label l) {
        return this.getCost() - l.getCost();
    }

}