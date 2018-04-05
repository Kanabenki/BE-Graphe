package org.insa.algo.shortestpath;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Node;
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
        Map<Label, Boolean> insertedMap = new HashMap<>(); 

        Node origin = data.getOrigin();
        for (Node node: data.getGraph()) {
            Label newLabel = new Label(Integer.MAX_VALUE, node, null);
            nodesMap.put(node, newLabel);
            insertedMap.put(newLabel, false);
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
                if (neighbLabel.isVisited()) {
                    continue;
                }
                if (!insertedMap.get(neighbLabel)) {
                    insertedMap.put(neighbLabel, true);
                    heap.insert(neighbLabel);
                }
                float dist = minLabel.getLength() + arc.getLength();
                if (dist < neighbLabel.getLength()) {
                    neighbLabel.setLength(dist);
                    neighbLabel.setPredecessor(minNode);
                    neighbLabel.setVisited(true);
                }
            }
        }

        return new ShortestPathSolution(data, null, null);
    }

}
