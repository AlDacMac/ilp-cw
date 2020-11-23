package uk.ac.ed.inf.aqmaps;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Point2D;

public class TSP{

    /**
     * Given a list of 2d co-ordinate pairs, calculates euclidean distance between each of them.
     * @param coords A list of 2d co-ordinate pairs (e.g [[1, 2], [5, 4.5], [-1, 0]])
     * @return An n by n graph (where n is the length of coords) where return[i][j] is the euclidean
     *  distance between coords[i] and coords[j]
     */
    public static Double[][] edgeGraph(double[][] coords){
        var graph = new Double[coords.length][coords.length];
        for(int i = 0; i < coords.length; i++){
            for(int j = 0; j < i; j++){
                //Set graph[i][j] to the euclidean distance between coords[i] and coords[j]
                graph[i][j] = Math.sqrt(Math.pow((coords[i][0] - coords[j][0]), 2) + 
                Math.pow((coords[i][1] - coords[j][1]), 2));
                if(i != j){
                    //We cut down computation by reusing the value, as the distance is the same.
                    graph[j][i] = graph[i][j];
                }
            }
        }
        return graph;
    }

    /**
     * Given a list of Points, calculates euclidean distance between each of them.
     * @param coords A list of elements of type Point
     * @return An n by n graph (where n is the length of coords) where return[i][j] is the euclidean
     *  distance between coords[i] and coords[j]
     */
    public static Double[][] edgeGraph(Point2D.Double[] coords){
        var graph = new Double[coords.length][coords.length];
        for(int i = 0; i < coords.length; i++){
            for(int j = 0; j < i; j++){
                //Set graph[i][j] to the euclidean distance between coords[i] and coords[j]
                graph[i][j] = coords[i].distance(coords[j]);
                if(i != j){
                    //We cut down computation by reusing the value, as the distance is the same.
                    graph[j][i] = graph[i][j];
                }
            }
        }
        return graph;
    }

    /**
     * Uses prim's aogorithm
     * @param edges
     */
    public static HashMap<Integer, ArrayList<Integer>> prims(Double[][] edges){
        int n = edges.length;
        var mst = new HashMap<Integer, ArrayList<Integer>>();
        var shortestENode = new HashMap<Integer, Integer>();
        var shortestEWeight = new HashMap<Integer, Double>();
        for(int j = 0; j < edges.length; j++){
            shortestENode.put(j, null);
            shortestEWeight.put(j, null);
        }
        for(int i = 0; i < n; i++){
            int node = 0;
            for(Integer key: shortestEWeight.keySet()){
                if(!shortestENode.keySet().contains(node) || 
                (shortestEWeight.get(key) != null && shortestEWeight.get(key) < shortestEWeight.get(node))){
                    node = key;
                }
            }
            mst.put(node, new ArrayList<Integer>());
            if(shortestENode.get(node) != null){
                var connectorNode = shortestENode.get(node);
                mst.get(connectorNode).add(node);
            }
            shortestENode.remove(node);
            shortestEWeight.remove(node);
            for(int k: shortestENode.keySet()){
                if(shortestEWeight.get(k) == null || edges[node][k] < shortestEWeight.get(k)){
                    shortestENode.put(k, node);
                    shortestEWeight.put(k, edges[node][k]);
                }
            }
        }
        return mst;
    }

    public static ArrayList<Integer> preorderWalk(int start, HashMap<Integer, ArrayList<Integer>> tree){
        var traversal = new ArrayList<Integer>();
        traversal.add(start);
        if(tree.get(start).size() == 0){
            return traversal;
        }
        else{
            for(int child: tree.get(start)){
                traversal.addAll(preorderWalk(child, tree));
            }
        }
        return traversal;
    }
}