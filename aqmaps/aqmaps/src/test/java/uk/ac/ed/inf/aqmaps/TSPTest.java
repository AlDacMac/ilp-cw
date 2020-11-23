package uk.ac.ed.inf.aqmaps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class TSPTest{
    private double[][] testCoords = new double[][]{new double[]{1, 2}, new double[]{5, 6}, new double[]{-1, -3}, new double[]{10, 10 }};

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testEdgeGraph(){
        Double[][] graph = TSP.edgeGraph(testCoords);
        double temp = Math.round(graph[0][1]);
        assertTrue(temp == 6);
    }

    @Test
    public void testPrims(){
        Double[][] graph = (Double[][])TSP.edgeGraph(testCoords);
        var output = TSP.prims(graph);
        var ground = new HashMap<Integer, ArrayList<Integer>>();
        ground.put(0, new ArrayList<Integer>(Arrays.asList(2, 1)));
        ground.put(1, new ArrayList<Integer>(Arrays.asList(3)));
        ground.put(2, new ArrayList<Integer>());
        ground.put(3, new ArrayList<Integer>());
        assertTrue(output.equals(ground));
    }

    @Test
    public void testPreorderWalk(){
        Double[][] graph = (Double[][])TSP.edgeGraph(testCoords);
        var mst = TSP.prims(graph);
        var walk = TSP.preorderWalk(0, mst).toArray();
        assertTrue(Arrays.equals(walk, new Integer[]{0, 2, 1, 3}));
    }
}