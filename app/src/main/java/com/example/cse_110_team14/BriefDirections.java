package com.example.cse_110_team14;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Map;

public class BriefDirections implements DirectionsInterface{

    @Override
    public String directions(Graph<String, IdentifiedWeightedEdge> g, GraphPath<String, IdentifiedWeightedEdge> path, String start, String goal, Map<String, String> animalIdToName, Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        // eInfo.get(e.getId()).street
        if(path.getEdgeList().size() == 0) {
            if (vInfo.get(goal).group_id != null) {
                sb.append(" " + i + ". Find " + animalIdToName.get(goal)
                        + " in " + animalIdToName.get(vInfo.get(goal).group_id) + ".\n");
            }
            return  sb.toString();
        }
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
        if (vInfo.get(goal).group_id != null) {
            sb.append(" " + i + ". Find " + animalIdToName.get(goal)
                    + " in " + animalIdToName.get(vInfo.get(goal).group_id) + ".\n");
        }
        return sb.toString();
    }
}
