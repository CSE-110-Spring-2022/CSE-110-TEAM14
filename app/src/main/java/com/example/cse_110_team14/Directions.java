package com.example.cse_110_team14;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Map;

public class Directions {

    /**
     * This method converts the graph path to directions a user can follow.
     * @param g The graph
     * @param path The path
     * @param start The starting vertex
     * @param goal The last vertex
     * @return The directions to follow
     */
    public static String detailedDirections(Graph<String, IdentifiedWeightedEdge> g,
                             GraphPath<String, IdentifiedWeightedEdge> path,
                             String start, String goal, Map<String, String> animalIdToName,
                                            Map<String, ZooData.VertexInfo> vInfo,
                                            Map<String, ZooData.EdgeInfo> eInfo
                                            ) {
        // List of directions to get to the vertex
        StringBuilder sb = new StringBuilder();
        int i = 1;
        String currentSt = null;
        String curr = animalIdToName.get(start);
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            sb.append(" " + i + ". ");
            if(eInfo.get(e.getId()).street.equals(currentSt)) {
                sb.append("Continue on " + eInfo.get(e.getId()).street + " ");
            }
            else {
                sb.append("Proceed on " + eInfo.get(e.getId()).street +" ");
            }
            currentSt = eInfo.get(e.getId()).street;
            String target = vInfo.get(g.getEdgeTarget(e).toString()).name;
            String source = vInfo.get(g.getEdgeSource(e).toString()).name;

            sb.append((int) g.getEdgeWeight(e) + " ft towards" );

            if(curr.equals(target)) {
                sb.append(" " + source + ".\n");
                curr = source;
            }
            else {
                sb.append(" " + target + ".\n");
                curr = target;
            }

            i++;
        }

        return sb.toString();
    }

    public static String briefDirections(Graph<String, IdentifiedWeightedEdge> g,
                                            GraphPath<String, IdentifiedWeightedEdge> path,
                                            String start, String goal, Map<String, String>
                                                 animalIdToName,
                                            Map<String, ZooData.VertexInfo> vInfo,
                                            Map<String, ZooData.EdgeInfo> eInfo){
        if(path.getEdgeList().size() == 0) {
            return "No path found";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        // eInfo.get(e.getId()).street
        String currentSt = eInfo.get(path.getEdgeList().get(0).getId()).street;
        String curr = animalIdToName.get(start);
        int dist = 0;
        ArrayList<Integer> distances = new ArrayList<>();



        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            if((eInfo.get(e.getId()).street.equals(currentSt))) {
                dist += (int) g.getEdgeWeight(e);
            }
            else {
                distances.add(dist);
                dist =(int) g.getEdgeWeight(e);
            }
            currentSt = eInfo.get(e.getId()).street;
        }
        distances.add(dist);

        currentSt = null;
       for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            if(eInfo.get(e.getId()).street.equals(currentSt)) {
                // sb.append("Continue on " + eInfo.get(e.getId()).street + " ");
                dist += (int) g.getEdgeWeight(e);
                continue;
            }
            else {
                sb.append(" " + i + ". ");
                sb.append("Proceed on " + eInfo.get(e.getId()).street +" ");
                sb.append( distances.get(i-1) + " ft towards" );
                dist = 0;
                i++;

            }
            currentSt = eInfo.get(e.getId()).street;
            String target = vInfo.get(g.getEdgeTarget(e).toString()).name;
            String source = vInfo.get(g.getEdgeSource(e).toString()).name;


            if(curr.equals(target)) {
                sb.append(" " + source + ".\n");
                curr = source;
            }
            else {
                sb.append(" " + target + ".\n");
                curr = target;
            }

        }
        Log.d("DirectionsBrief", distances.toString());

        return sb.toString();
    }
}
