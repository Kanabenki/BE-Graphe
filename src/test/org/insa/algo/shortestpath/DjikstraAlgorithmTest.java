package org.insa.algo.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;


public class DjikstraAlgorithmTest {


   // Small graph use for tests
   private static Graph graph;

   private boolean debug = false;
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
      if(debug) {
         System.out.println("**** TESTS SUR UN GRAPHE SIMPLE ***");
      }
       /* We test for each pair of nodes */
       DijkstraAlgorithm djikstra1 ;
       BellmanFordAlgorithm bellmanford1 ;
       for(Node n1 : nodes) {
        for(Node n2 : nodes) {
           if(n1.getId() != n2.getId()) {
              djikstra1 = new DijkstraAlgorithm(new ShortestPathData(graph, n1, n2, ArcInspectorFactory.getAllFilters().get(0)));
              bellmanford1 = new BellmanFordAlgorithm(new ShortestPathData(graph, n1, n2, ArcInspectorFactory.getAllFilters().get(0)));
              ShortestPathSolution djikstraSol = djikstra1.doRun();
              ShortestPathSolution bellmanSol = bellmanford1.doRun();
              Path djikstraSolution = djikstraSol.getPath();
              Path bellmanFordSolution =  bellmanSol.getPath();
              if(!bellmanSol.isFeasible()) {
                 assertTrue(!djikstraSol.isFeasible());
                 if(debug) {
                 System.out.println("No shortest path from " + n1.getId() + " to " + n2.getId());
                 }
              }
              else {
                 if(debug) {
                    System.out.println("[Bellman-Ford] Shortest path from " + n1.getId() + " to " + n2.getId() +" is : " + bellmanFordSolution.getLength());
                    System.out.println("[Djikstra] Shortest path from " + n1.getId() + " to " + n2.getId() +" is : " + djikstraSolution.getLength());
                    System.out.println("");
                 }
                 assertTrue(djikstraSol.isFeasible());
                 assertTrue(djikstraSolution.isValid());
                 assertEquals(djikstraSolution.getMinimumTravelTime(), bellmanFordSolution.getMinimumTravelTime(), 1e-6); //check our solution's cost
                 assertEquals(djikstraSolution.getLength(), bellmanFordSolution.getLength(), 1e-6);
              }
           }
        }
       }
   }
   
   @SuppressWarnings("deprecation")
   @Test
   public void testDjikstraGraph2() throws IOException {
      if(debug) {
         System.out.println("**** TESTS SUR TOULOUSE ***");
      }
      // Visit these directory to see the list of available files on Commetud.
      String mapName = "C://Users/Julien/Desktop/eclipse-workspace/maps/toulouse.mapgr";

      // Create a graph reader.
      GraphReader reader = new BinaryGraphReader(
              new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
      
      // Read the graph.
      Graph graphe = reader.read();
      
      //Test 1
      Node n1 =  graphe.get(22596);
      Node n2 = graphe.get(3030);
      runTest(n1, n2, graphe, 0);
      
      //Test 2
      n1 =  graphe.get(35052);
      n2 = graphe.get(16597);
      runTest(n1, n2, graphe, 4);
      
     //Test 3
     n1 =  graphe.get(35052);
     n2 = graphe.get(16597);
     runTest(n1, n2, graphe, 6);
     
     //Test 4 TODO Fix this
   /*  n1 =  graphe.get(35052);
     n2 = graphe.get(16597);
     runTest(n1, n2, graphe, 5);*/
      
   }
   
   @SuppressWarnings("deprecation")
   @Test
   public void testDjikstraGraph3() throws IOException {
      if(debug) {
         System.out.println("**** TESTS SUR LA GUADELOUPE ***");
      }
      // Visit these directory to see the list of available files on Commetud.
      String mapName = "C://Users/Julien/Desktop/eclipse-workspace/maps/guadeloupe.mapgr";

      // Create a graph reader.
      GraphReader reader = new BinaryGraphReader(
              new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
      
      // Read the graph.
      Graph graphe = reader.read();
      
      //Test 1
      Node n1 =  graphe.get(25116);
      Node n2 = graphe.get(15929);
      runTest(n1, n2, graphe, 0);
      
      //Test 2
      n1 =  graphe.get(23716);
      n2 = graphe.get(31627);
      runTest(n1, n2, graphe, 4);
      
     //Test 3
     n1 =  graphe.get(21514);
     n2 = graphe.get(22152);
     runTest(n1, n2, graphe, 6);
     
     //Test 4
     n1 =  graphe.get(11572);
     n2 = graphe.get(30910);
     runTest(n1, n2, graphe, 5);
      
   }
   
   @SuppressWarnings("deprecation")
   @Test
   public void testDjikstraGraph4() throws IOException {
      if(debug) {
         System.out.println("**** TESTS SUR PARIS ***");
      }
      // Visit these directory to see the list of available files on Commetud.
      String mapName = "C://Users/Julien/Desktop/eclipse-workspace/maps/paris.mapgr";

      // Create a graph reader.
      GraphReader reader = new BinaryGraphReader(
              new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
      
      // Read the graph.
      Graph graphe = reader.read();
      
      //Test 1
      Node n1 =  graphe.get(5106);
      Node n2 = graphe.get(9752);
      runTest(n1, n2, graphe, 0);
      
      n1 =  graphe.get(5106);
      n2 = graphe.get(9752);
      runTest(n1, n2, graphe, 1);
      n1 =  graphe.get(5106);
      n2 = graphe.get(9752);
      runTest(n1, n2, graphe, 2);
      n1 =  graphe.get(5106);
      n2 = graphe.get(9752);
      runTest(n1, n2, graphe, 3);
      n1 =  graphe.get(5106);
      n2 = graphe.get(9752);
      runTest(n1, n2, graphe, 4);
      n1 =  graphe.get(5106);
      n2 = graphe.get(9752);
      runTest(n1, n2, graphe, 5);
      
      //Test 2
      n1 =  graphe.get(23716);
      n2 = graphe.get(31627);
      runTest(n1, n2, graphe, 4);
      
     //Test 3
     n1 =  graphe.get(21514);
     n2 = graphe.get(22152);
     runTest(n1, n2, graphe, 6);
     
     //Test 4
     n1 =  graphe.get(11572);
     n2 = graphe.get(30910);
     runTest(n1, n2, graphe, 5);
      
   }
   
   private void runTest(Node n1, Node n2, Graph graph, int mode) {  
      DijkstraAlgorithm djikstra1 = new DijkstraAlgorithm(new ShortestPathData(graph, n1, n2, ArcInspectorFactory.getAllFilters().get(mode)));
      BellmanFordAlgorithm bellmanford1 = new BellmanFordAlgorithm(new ShortestPathData(graph, n1, n2, ArcInspectorFactory.getAllFilters().get(mode)));
      AStarAlgorithm AStar1 = new AStarAlgorithm(new ShortestPathData(graph, n1, n2, ArcInspectorFactory.getAllFilters().get(mode)));
      ShortestPathSolution djikstraSol = djikstra1.doRun();
      ShortestPathSolution bellmanSol = bellmanford1.doRun();
      ShortestPathSolution AStarSol = AStar1.doRun();
      Path djikstraSolution = djikstraSol.getPath();
      Path bellmanFordSolution =  bellmanSol.getPath();
      Path AStarSolution =  AStarSol.getPath();
      if(!bellmanSol.isFeasible()) {
         assertTrue(!djikstraSol.isFeasible());
         assertTrue(!AStarSol.isFeasible());
         if(debug) {
         System.out.println("No shortest path from " + n1.getId() + " to " + n2.getId());
         }
      }
      else {
         if(debug) {
            System.out.println("[Bellman-Ford] Shortest path from " + n1.getId() + " to " + n2.getId() +" is : " + bellmanFordSolution.getLength());
            System.out.println("[Djikstra] Shortest path from " + n1.getId() + " to " + n2.getId() +" is : " + djikstraSolution.getLength());
            System.out.println("[AStar Djikstra] Shortest path from " + n1.getId() + " to " + n2.getId() +" is : " + AStarSolution.getLength());
            System.out.println("");
         }
         assertTrue(djikstraSol.isFeasible());
         assertTrue(djikstraSolution.isValid());
         assertEquals(djikstraSolution.getMinimumTravelTime(), bellmanFordSolution.getMinimumTravelTime(), 1e-6); //check our solution's cost
         assertEquals(djikstraSolution.getLength(), bellmanFordSolution.getLength(), 1e-6);
         assertTrue(AStarSol.isFeasible());
         assertTrue(AStarSolution.isValid());
         assertEquals(AStarSolution.getMinimumTravelTime(), bellmanFordSolution.getMinimumTravelTime(), 1e-6); //check our solution's cost
         assertEquals(AStarSolution.getLength(), bellmanFordSolution.getLength(), 1e-6);
      }
   }
   
   
}
