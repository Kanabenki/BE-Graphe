package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.AStarLabel;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.Arc;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    private BinaryHeap<Label> heapInit() {
      return new BinaryHeap<Label>();       
    }
    
    private Label createLabel(Node node) {
       return new Label(Double.POSITIVE_INFINITY, node, null);
    }
    
    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        BinaryHeap<Label> heap = heapInit();
        Map<Node, Label> nodesMap = new HashMap<>();

        Node origin = data.getOrigin();
        notifyOriginProcessed(origin);
   

        Label originLabel = createLabel(origin);
        nodesMap.put(origin, originLabel);
        nodesMap.put(data.getDestination(), createLabel(data.getDestination()));
        originLabel.setLength(0);
        heap.insert(originLabel);
        Boolean continuer = true;
        while (!heap.isEmpty() && continuer) {
            Label minLabel = heap.deleteMin();
            Node minNode = minLabel.getNode();
            minLabel.setVisited(true);
            //System.out.println("Visited : " + minNode.getId());
            if(data.getDestination().getId() == minNode.getId()) {
               continuer = false;
            }
            notifyNodeMarked(minNode);
            for (Arc arc : minNode) {
               Label neighbLabel;
               if(nodesMap.containsKey(arc.getDestination())) {
                  neighbLabel = nodesMap.get(arc.getDestination());
               }
               else {
                  neighbLabel = createLabel(arc.getDestination());
                  nodesMap.put(arc.getDestination(), neighbLabel);
               }
                if (neighbLabel.isVisited() || !data.isAllowed(arc)) {
                    continue;
                }
                double dist = minLabel.getLength() + data.getCost(arc);
                if (dist < neighbLabel.getLength()) {
                    neighbLabel.setLength(dist);
                    neighbLabel.setPredecessor(minNode);
                    if(heap.contains(neighbLabel)) {
                       heap.remove(neighbLabel); //to update the heap
                    } else {
                        notifyNodeReached(arc.getDestination());
                    }
                    heap.insert(neighbLabel);
                }
            }
        }
        notifyDestinationReached(data.getDestination());
        if(nodesMap.get(data.getDestination()).getLength() == Double.POSITIVE_INFINITY) {
           return new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        List<Node> listNodes = new ArrayList<Node>();
        Label currentNodeL = nodesMap.get(data.getDestination());

        while(currentNodeL.getPredecessor() != null) {
            listNodes.add(0, currentNodeL.getNode());
            currentNodeL = nodesMap.get(currentNodeL.getPredecessor());
        }
        listNodes.add(0, data.getOrigin());
        //System.out.println("solution is made of nb nodes = " + listNodes.size())
        if(data.getMode() == Mode.LENGTH) {
           //System.out.println("length mode");
           return new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(data.getGraph(), listNodes));
        }
        else {
           //System.out.println("time mode");
           return new ShortestPathSolution(data, Status.OPTIMAL, Path.createFastestPathFromNodes(data.getGraph(), listNodes));
        }
    }
}
