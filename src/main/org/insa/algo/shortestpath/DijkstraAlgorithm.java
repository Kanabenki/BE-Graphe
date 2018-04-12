package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.Arc;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    
    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        Map<Node, Label> nodesMap = new HashMap<>();

        Node origin = data.getOrigin();
        for (Node node: data.getGraph()) {
            Label newLabel = new Label(Integer.MAX_VALUE, node, null);
            nodesMap.put(node, newLabel);
        }

        Label originLabel = nodesMap.get(origin);
        originLabel.setLength(0);
        heap.insert(originLabel);

        while (!heap.isEmpty()) {
            Label minLabel = heap.deleteMin();
            Node minNode = minLabel.getNode();
            minLabel.setVisited(true);

            for (Arc arc : minNode) {
                Label neighbLabel = nodesMap.get(arc.getDestination());
                if (neighbLabel.isVisited() || data.isAllowed(arc)) {
                    continue;
                }
                float dist = minLabel.getLength() + arc.getLength();
                if (dist < neighbLabel.getLength()) {
                    neighbLabel.setLength(dist);
                    neighbLabel.setPredecessor(minNode);
                    if(heap.contains(neighbLabel)) {
                       heap.remove(neighbLabel); //to update the heap
                    }
                    heap.insert(neighbLabel);
                }
            }
        }
        if(nodesMap.get(data.getDestination()).getLength() == Integer.MAX_VALUE) {
           return new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        List<Node> listNodes= new ArrayList<Node>();
        boolean end = false;
        Label currentNodeL = nodesMap.get(data.getDestination());
        while(!end) {
           if(currentNodeL.getPredecessor() == null) {
              end = true;
           }
           else {
             listNodes.add(0, currentNodeL.getNode());
             currentNodeL = nodesMap.get(currentNodeL.getPredecessor());
           }
        }
        System.out.println("list size = " + listNodes.size());
        return new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(data.getGraph(), listNodes));
    }

}
