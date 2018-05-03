package org.insa.algo.utils;

import org.insa.graph.Node;

public class AStarLabel extends Label {
    AStarLabel(float length, float destLength, Node node, Node predecessor) {
        super(length, node, predecessor);
        this.destLength = destLength;
    }

    private float destLength;

    public float getLength() {
        return this.destLength;
    }

    public void setDestLength(float destLength) {
        this.destLength = destLength;
    }
}