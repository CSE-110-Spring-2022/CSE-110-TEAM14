package com.example.cse_110_team14;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.nio.json.JSONImporter;

public class ZooData {
    public static class VertexInfo {
        public static enum Kind {
            // The SerializedName annotation tells GSON how to convert
            // from the strings in our JSON to this Enum.
            @SerializedName("gate") GATE,
            @SerializedName("exhibit") EXHIBIT,
            @SerializedName("intersection") INTERSECTION,
            @SerializedName("exhibit_group") EXHIBIT_GROUP
        }


        @Override
        public String toString() {
            return "VertexInfo{" +
                    "id='" + id + '\'' +
                    ", parent_id='" + group_id + '\'' +
                    ", kind=" + kind +
                    ", name='" + name + '\'' +
                    ", tags=" + tags +
                    ", latitude=" + lat +
                    ", longitude=" + lng +
                    ", checked=" + checked +
                    '}';
        }

        public String id;
        public String group_id;
        public Kind kind;
        public String name;
        public List<String> tags;
        public double lat;
        public double lng;
        public boolean checked = false;
    }

    public static class EdgeInfo {
        public String id;
        public String street;
    }




    public static Map<String, ZooData.VertexInfo> loadVertexInfoJSON(Context context, String path) {
        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<ZooData.VertexInfo>>() {
            }.getType();
            List<ZooData.VertexInfo> zooData = gson.fromJson(reader, type);

            Map<String, ZooData.VertexInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            // for all nodes with a parent, their lats and lngs should be their respective
            // parent's lat and lng
            for (Map.Entry<String,ZooData.VertexInfo> entry: indexedZooData.entrySet()) {
                if (entry.getValue().group_id != null) {
                    entry.getValue().lat = indexedZooData.get(entry.getValue().group_id).lat;
                    entry.getValue().lng = indexedZooData.get(entry.getValue().group_id).lng;
                }
            }

            for (Map.Entry<String,ZooData.VertexInfo> entry: indexedZooData.entrySet()) {
                Log.d("parseCheck", "" + entry.getKey() + entry.getValue());
            }

            return indexedZooData;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, ZooData.EdgeInfo> loadEdgeInfoJSON(Context context, String path) {
        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<ZooData.EdgeInfo>>() {
            }.getType();
            List<ZooData.EdgeInfo> zooData = gson.fromJson(reader, type);

            Map<String, ZooData.EdgeInfo> indexedZooData = zooData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            return indexedZooData;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Graph<String, IdentifiedWeightedEdge> loadZooGraphJSON(Context context,
                                                                         String graphPath,
                                                                         String vertexPath,
                                                                         String edgePath) {
        // Create an empty graph to populate.
        Graph<String, IdentifiedWeightedEdge> g =
                new DefaultUndirectedWeightedGraph<>(IdentifiedWeightedEdge.class);

        // Create an importer that can be used to populate our empty graph.
        JSONImporter<String, IdentifiedWeightedEdge> importer = new JSONImporter<>();

        // We don't need to convert the vertices in the graph, so we return them as is.
        importer.setVertexFactory(v -> v);

        // We need to make sure we set the IDs on our edges from the 'id' attribute.
        // While this is automatic for vertices, it isn't for edges. We keep the
        // definition of this in the IdentifiedWeightedEdge class for convenience.
        importer.addEdgeAttributeConsumer(IdentifiedWeightedEdge::attributeConsumer);

        // On Android, you would use context.getAssets().open(path) here like in Lab 5.
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(graphPath);
            Reader reader = new InputStreamReader(inputStream);

            Map<String, EdgeInfo> edgeInfo = loadEdgeInfoJSON(context,
                    edgePath);
            Map<String, VertexInfo> vertexInfo = loadVertexInfoJSON(context,
                    vertexPath);
            List<VertexInfo> hasParentId = vertexInfo.values().stream()
                    .filter(v -> v.group_id != null)
                    .collect(Collectors.toList());
            List<VertexInfo> exhibits = vertexInfo.values().stream()
                    .filter(v -> v.kind == VertexInfo.Kind.EXHIBIT_GROUP)
                    .collect(Collectors.toList());

            // And now we just import it!
            importer.importGraph(g, reader);

//            Log.d("exhibits", exhibits.toString());
//            Log.d("exhibits", hasParentId.toString());
//            for (VertexInfo v : hasParentId) {
//                for (VertexInfo parent : exhibits) {
//                    if (v.group_id.equals(parent.id)) {
//
//                        g.addVertex(v.id);
//                        System.out.println(g.containsVertex(parent.id));
//                        System.out.println(g.vertexSet());
//                        for(IdentifiedWeightedEdge e  : g.outgoingEdgesOf(parent.id)) {
//                            if(e.source().equals(parent.id)) {
//                                g.addEdge(v.id, e.target());
//                                IdentifiedWeightedEdge tempE = g.getEdge(v.id,e.target());
//                                tempE.setId(e.getId());
//                                g.setEdgeWeight(tempE, e.weight());
//                            }
//                            else {
//                                g.addEdge(e.source(), v.id);
//                                g.getEdge(v.id,e.source()).setId(e.getId());
//                                IdentifiedWeightedEdge tempE = g.getEdge(v.id,e.source());
//                                tempE.setId(e.getId());
//                                g.setEdgeWeight(tempE, e.weight());
//                            }
//                        }
//
//                        break;
//                    }
//                }
//            }




            return g;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}