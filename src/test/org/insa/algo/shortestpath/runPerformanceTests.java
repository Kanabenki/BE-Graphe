package org.insa.algo.shortestpath;

import java.io.IOException;

public class runPerformanceTests {
   static int taille = 15;
   public static void main(String[] args) {
      try {
         System.out.println("**** Running test for size = " + taille + " ****");
         PerformanceAlgorithmTest perfTest = new PerformanceAlgorithmTest(taille);
         perfTest.runTest();
         System.out.println("**** End of test ****");
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }      
   }
}