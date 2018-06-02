package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.Label;
import org.insa.graph.Arc;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class TwoRobotsShortestPath1 extends ShortestPathAlgorithm {

   public TwoRobotsShortestPath1(ShortestPathData data) {
      super(data);
  }

   private Map<Node, double[]> allPointsData;

   private BinaryHeap<Label> heapInit() {
      return new BinaryHeap<Label>();       
    }
    
    private Label createLabel(Node node) {
       return new Label(Double.POSITIVE_INFINITY, node, null);
    }
    
    private void initData(ShortestPathData data) {
       allPointsData = new HashMap<Node, double[]>();
       for (Node node: data.getGraph()) {
          allPointsData.put(node, new double[] {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY});
      }
    }
    
    @Override
    protected ShortestPathSolution doRun() {
       ShortestPathData data = getInputData();
       ShortestPathData dataReverse = new ShortestPathData(data.getGraph().transpose(), null, null, data.getArcInspector());
       initData(data);    
       Node O1 = data.getGraph().get(TwoRobotsPoints.idnodeO1);
       Node D1 = data.getGraph().get(TwoRobotsPoints.idnodeD1);
       Node O2 = data.getGraph().get(TwoRobotsPoints.idnodeO2);
       Node D2 = data.getGraph().get(TwoRobotsPoints.idnodeD2);
       runPath(O1, 0, data); //O1 -> Meeting Point
       runPath(O2, 1, data); //O2 -> Meeting Point
       runPath(D1, 2, dataReverse); //Meeting Point -> D1
       runPath(D2, 3, dataReverse); //Meeting Point -> D2
       computeTotals();
       Node meetingNode = findMin();
       notifyNodeMarked(meetingNode);
       ShortestPathData data1 = new ShortestPathData(data.getGraph(), O1, meetingNode, data.getArcInspector());
       Path path0 = (new AStarAlgorithm(data1)).doRun().getPath();
       //ShortestPathData data2 = new ShortestPathData(data.getGraph(), O2, meetingNode, data.getArcInspector());
       //Path path1 = (new AStarAlgorithm(data2)).doRun().getPath();
       ShortestPathData data3 = new ShortestPathData(data.getGraph(), meetingNode, D1, data.getArcInspector());
       Path path2 = (new AStarAlgorithm(data3)).doRun().getPath();
       //ShortestPathData data4 = new ShortestPathData(data.getGraph(), meetingNode, D2, data.getArcInspector());
       //Path path3 = (new AStarAlgorithm(data4)).doRun().getPath();
       System.out.println("Total travel time : " + allPointsData.get(meetingNode)[4]);
       System.out.println("(" + (allPointsData.get(meetingNode)[0] +  allPointsData.get(meetingNode)[2]) + " for Robot 1)");
       System.out.println("(" + (allPointsData.get(meetingNode)[1] +  allPointsData.get(meetingNode)[3]) + " for Robot 2)");
       return new ShortestPathSolution(data, Status.FEASIBLE, Path.concatenate(path0, path2));
    }
    
    private void computeTotals() {
       for (Node node: allPointsData.keySet()) {
          allPointsData.get(node)[4] =  allPointsData.get(node)[0] +  allPointsData.get(node)[1] + allPointsData.get(node)[2] +  allPointsData.get(node)[3];
      }
    }
    
    private Node findMin() {
       Node currentNodeMin = null;
       double currentMin = Double.POSITIVE_INFINITY;
       for (Node node: allPointsData.keySet()) {
          if(allPointsData.get(node)[4] < currentMin) {
             currentMin = allPointsData.get(node)[4];
             currentNodeMin = node;
          }
      }
       return currentNodeMin;
    }
    
    private void runPath(Node origin, int n, ShortestPathData data) {
       BinaryHeap<Label> heap = heapInit();
       Map<Node, Label> nodesMap = new HashMap<>();
       Label originLabel = createLabel(origin);
       nodesMap.put(origin, originLabel);
       originLabel.setLength(0);
       heap.insert(originLabel);
       while (!heap.isEmpty()) {
           Label minLabel = heap.deleteMin();
           Node minNode = minLabel.getNode();
           minLabel.setVisited(true);
           //System.out.println("Visited : " + minNode.getId());
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
                   }
                   heap.insert(neighbLabel);
               }
           }
       }
       for(Node node : nodesMap.keySet()) {
          allPointsData.get(node)[n] = nodesMap.get(node).getLength();
       }
    }
    
}
