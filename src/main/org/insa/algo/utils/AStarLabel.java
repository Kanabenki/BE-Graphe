package org.insa.algo.utils;

import org.insa.graph.Node;

public class AStarLabel extends Label {
    
   public AStarLabel(double length, double destLength, Node node, Node predecessor) {
        super(length, node, predecessor);
        this.destLength = destLength;
    }

    private double destLength;

    public double getdestLength() {
        return this.destLength;
    }

    public void setDestLength(float destLength) {
        this.destLength = destLength;
    }
    
    private double getCost() {
       return this.getLength() + this.getdestLength();
    }

    @Override
    public int compareTo(Label la) {
        AStarLabel l = (AStarLabel) la;
        if(0 == (int)Math.signum(this.getCost() - l.getCost())) {
           if(this.destLength >= l.destLength) {
              return 1;
           }
           else {
              return 0;
           }
        }
          else {
           return (int)Math.signum(this.getCost() - l.getCost());
        }
    }
    
}