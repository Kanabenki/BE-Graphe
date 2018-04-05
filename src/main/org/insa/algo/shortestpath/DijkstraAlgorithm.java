package org.insa.algo.shortestpath;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Node;

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
        Iterator<Node> it = data.getGraph().iterator();
        while (it.hasNext()) {
            Node node = it.next();
            nodesMap.put(node, new Label(Integer.MAX_VALUE, node, null));
        }

        Label originLabel = nodesMap.get(origin);
        originLabel.setCost(0);
        heap.insert(originLabel);

        return new ShortestPathSolution(data, null, null);
    }

}
