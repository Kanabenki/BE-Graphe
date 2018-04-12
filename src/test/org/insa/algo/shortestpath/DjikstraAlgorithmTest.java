package org.insa.algo.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;


public class DjikstraAlgorithmTest {


   // Small graph use for tests
   private static Graph graph;

   // List of nodes
   private static List<Node> nodes;
   
   @BeforeClass
   public static void initAll() throws IOException {

       // 10 and 20 meters per seconds
       RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
       speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, ""); //arbitrary, we don't need these informations for this test

       // Create nodes
       nodes = new ArrayList<Node>();
       for(int i = 0 ; i <= 5 ; i++) { //we zero-index the nodes
          nodes.add(new Node(i, null));
       }
       
       // Add arcs...
       Arc a1To2 = Node.linkNodes(nodes.get(0), nodes.get(1), 7, speed10, null);
       Arc a1To3 = Node.linkNodes(nodes.get(0), nodes.get(2), 8, speed10, null);
       
       Arc a2To4 = Node.linkNodes(nodes.get(1), nodes.get(3), 4, speed10, null);
       Arc a2To5 = Node.linkNodes(nodes.get(1), nodes.get(4), 1, speed10, null);
       
       Arc a3To1 = Node.linkNodes(nodes.get(2), nodes.get(0), 7, speed10, null);
       Arc a3To2 = Node.linkNodes(nodes.get(2), nodes.get(1), 2, speed10, null);
       Arc a3To6 = Node.linkNodes(nodes.get(2), nodes.get(5), 2, speed10, null);
       
       Arc a5To3 = Node.linkNodes(nodes.get(4), nodes.get(2), 2, speed10, null);
       Arc a5To4 = Node.linkNodes(nodes.get(4), nodes.get(3), 2, speed10, null);
       Arc a5To6 = Node.linkNodes(nodes.get(4), nodes.get(5), 3, speed10, null);
       
       Arc a6To5 = Node.linkNodes(nodes.get(5), nodes.get(4), 3, speed10, null);


       // Create graph from nodes
       graph = new Graph(null, null, nodes, null);

   }

   @SuppressWarnings("deprecation")
   @Test
   public void testDjikstraGraph1() {
       DijkstraAlgorithm djikstra1 = new DijkstraAlgorithm(new ShortestPathData(graph, nodes.get(0), nodes.get(1), ArcInspectorFactory.getAllFilters().get(0)));
       BellmanFordAlgorithm bellmanford1 = new BellmanFordAlgorithm(new ShortestPathData(graph, nodes.get(0), nodes.get(1), ArcInspectorFactory.getAllFilters().get(0)));
       Path djikstraSolution = djikstra1.doRun().getPath();
       Path bellmanFordSolution =  bellmanford1.doRun().getPath();
       assertTrue(djikstraSolution.isValid());
       assertEquals(djikstraSolution.getMinimumTravelTime(), bellmanFordSolution.getMinimumTravelTime(), 1e-6); //check our solution's cost
       assertEquals(djikstraSolution.getLength(), bellmanFordSolution.getLength(), 1e-6); 
   }

   
}
