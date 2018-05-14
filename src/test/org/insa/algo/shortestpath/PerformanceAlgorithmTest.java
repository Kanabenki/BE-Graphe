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

class PerformanceAlgorithmTest {
    class NodePair {
        NodePair(Node start, Node end) {
            this.start = start;
            this.end = end;
        }

        public Node start;
        public Node end;
    }

    NodePair testSet[];
    Graph testGraph;
    double results[];

    PerformanceAlgorithmTest(int testSize) throws FileNotFoundException, IOException {
        String mapName = "C://Users/Julien/Desktop/eclipse-workspace/maps/insa.mapgr";
        String pathName = "C://Users/Julien/Desktop/eclipse-workspace/maps/Paths/path_fr31insa_rangueil_r2.path";

        GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));

        testGraph = reader.read();
        generateTestSet(testSize);
    }

    void generateTestSet(int size) {
        testSet = new NodePair[size];
        for (int i = 0; i < size; ++i) {
            Node start, end;
            AStarAlgorithm testAStar;
            do {
                start = testGraph.get(ThreadLocalRandom.current().nextInt(0, testGraph.size()));
                end = testGraph.get(ThreadLocalRandom.current().nextInt(0, testGraph.size()));
                testAStar = new AStarAlgorithm(new ShortestPathData(testGraph, start, end, ArcInspectorFactory.getAllFilters().get(0)));
            } while (!testAStar.doRun().isFeasible());
            testSet[i] = new NodePair(null, null);
        }
    }

    void runTest() throws IOException {
        FileWriter fw = new FileWriter("result.csv", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.println("AStar,Dijkstra");

        long timeAStar = 0;
        long timeDijk = 0;
        for (int i = 0; i < testSet.length; ++i) {
            AStarAlgorithm aStar = new AStarAlgorithm(new ShortestPathData(testGraph, testSet[i].start, testSet[i].end, ArcInspectorFactory.getAllFilters().get(0)));
            DijkstraAlgorithm dijk = new DijkstraAlgorithm(new ShortestPathData(testGraph, testSet[i].start, testSet[i].end, ArcInspectorFactory.getAllFilters().get(0)));

            long startTime = System.nanoTime();
            aStar.doRun();
            timeAStar = System.nanoTime() - startTime;

            startTime = System.nanoTime();
            dijk.doRun();
            timeDijk = System.nanoTime() - startTime;

            out.println(timeAStar + "," + timeDijk);
        }
        out.close();
        fw.close();
    }
}