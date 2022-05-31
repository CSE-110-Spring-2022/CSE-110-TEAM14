package com.example.cse_110_team14;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Map;

public class DetailedDirections implements DirectionsInterface{
    public String directions(Graph<String, IdentifiedWeightedEdge> g,
                             GraphPath<String, IdentifiedWeightedEdge> path,
                             String start, String goal, Map<String, String> animalIdToName,
                             Map<String, ZooData.VertexInfo> vInfo,
                             Map<String, ZooData.EdgeInfo> eInfo) {

        // List of directions to get to the vertex
        StringBuilder sb = new StringBuilder();
        int i = 1;
        String currentSt = null;
        String curr = animalIdToName.get(start);
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            sb.append(" " + i + ". ");
            Log.d("Directions1", "Edge: " + e);
            Log.d("Directions2", "Edge: " + e.getId());
            Log.d("Directions3", "Edge: " + eInfo.get(e.getId()));
            Log.d("Directions4", "Edge: " + eInfo.get(e.getId()).street);

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
        Log.d("Directions", " i forgor: " + vInfo);
        if (vInfo.get(goal).group_id != null) {
            Log.d("Directions", " i forgor: " + vInfo.get(goal).group_id);
            Log.d("Directions", " i forgor: " + vInfo.get(goal));

            sb.append(" " + i + ". Find " + animalIdToName.get(goal)
                    + " in " + animalIdToName.get(vInfo.get(goal).group_id) + ".\n");
        }
        return sb.toString();
    }
}
