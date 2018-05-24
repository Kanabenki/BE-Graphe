package org.insa.algo.shortestpath;

import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Node;
import org.insa.graph.io.GraphReader;
import org.insa.graph.Graph;
import org.insa.graph.io.BinaryGraphReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.BufferedWriter;
import java.util.concurrent.ThreadLocalRandom;
import junit.framework.Test;

public class PerformanceAlgorithmTest {
    class NodePair {
        NodePair(Node start, Node end) {
            this.start = start;
            this.end = end;
        }

        public Node start;
        public Node end;
    }
    boolean debug = false;
    boolean runBellmanFord = false;
    NodePair testSet[];
    Graph testGraph;
    double results[];

    PerformanceAlgorithmTest(int testSize) throws FileNotFoundException, IOException {
        String mapName = "C://Users/Julien/Desktop/eclipse-workspace/maps/midi-pyrenees.mapgr";

        GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        testGraph = reader.read();
        generateTestSet(testSize);
    }

    void generateTestSet(int size) {
        testSet = new NodePair[size];
        System.out.println("Generating inputs...");
        for (int i = 0; i < size; ++i) {
            Node start, end;
            AStarAlgorithm testAStar;
            do {
                start = testGraph.get(ThreadLocalRandom.current().nextInt(0, testGraph.size()));
                end = testGraph.get(ThreadLocalRandom.current().nextInt(0, testGraph.size()));
                testAStar = new AStarAlgorithm(new ShortestPathData(testGraph, start, end, ArcInspectorFactory.getAllFilters().get(0)));
            } while (!testAStar.doRun().isFeasible());
            testSet[i] = new NodePair(start, end);
            if(debug) {
               System.out.println("Pair is nodes : " + testSet[i].start + " and " + testSet[i].end);
            }
        }
           System.out.println("Inputs Generated !");
    }
 
    void runTest() throws IOException {
        System.out.println("Running tests...");
        FileWriter fw = new FileWriter("result.csv", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        //out.println("AStar;Dijkstra;Bellman");
        if(runBellmanFord) {
           out.println("AStar;Dijkstra;Bellman-Ford;ShortestPath");
        }
        else {
           out.println("AStar;Dijkstra;ShortestPath");
        }
        //long timebellman = 0;
        long timeAStar = 0;
        long timeDijk = 0;
        long timeBellman = 0;
        for (int i = 0; i < testSet.length; ++i) {     
            int percent = i*100 / testSet.length;
            System.out.println(percent + " % done");
            if(debug) {
               System.out.println("test " + i);
               System.out.println("Test on nodes : " + testSet[i].start + " and " + testSet[i].end);
            }
            AStarAlgorithm aStar = new AStarAlgorithm(new ShortestPathData(testGraph, testSet[i].start, testSet[i].end, ArcInspectorFactory.getAllFilters().get(0)));
            DijkstraAlgorithm dijk = new DijkstraAlgorithm(new ShortestPathData(testGraph, testSet[i].start, testSet[i].end, ArcInspectorFactory.getAllFilters().get(0)));
            if(debug) { 
               System.out.println("Running Djikstra AStar...");
            }
            long startTime = System.nanoTime();
            //long startTime =  System.currentTimeMillis();
            ShortestPathSolution Sol0 =  aStar.doRun();
            timeAStar = System.nanoTime() - startTime;
            //timeAStar = System.currentTimeMillis() - startTime;
            if(debug) {
               System.out.println("Running Djikstra...");
            }
            startTime = System.nanoTime();
            //startTime =  System.currentTimeMillis();
            ShortestPathSolution Sol = dijk.doRun();
            timeDijk = System.nanoTime() - startTime;
            if(runBellmanFord) {
               BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(new ShortestPathData(testGraph, testSet[i].start, testSet[i].end, ArcInspectorFactory.getAllFilters().get(0)));
               if(debug) {
                  System.out.println("Running Bellman...");
               }
               startTime = System.nanoTime();
               bellmanFord.doRun();
               timeBellman = System.nanoTime() - startTime;
            }
          //  timeDijk = System.currentTimeMillis() - startTime;
            if(runBellmanFord) {
               out.println(timeAStar + ";" + timeDijk + ";" + timeBellman + ";" + Sol.getPath().getLength());
            }
            else {
               out.println(timeAStar + ";" + timeDijk + ";" + Sol.getPath().getLength());
            }
        }
        out.close();
        fw.close();
    }
}