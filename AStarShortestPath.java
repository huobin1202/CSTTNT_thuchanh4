import java.io.*;
import java.util.*;

class Node implements Comparable<Node> {
    int id;
    int g, h;
    Node parent;
    
    public Node(int id, int g, int h, Node parent) {
        this.id = id;
        this.g = g;
        this.h = h;
        this.parent = parent;
    }
    
    public int f() {
        return g + h;
    }
    
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.f(), other.f());
    }
}

public class AStarShortestPath {
    public static List<Integer> aStar(int n, Map<Integer, List<int[]>> graph, int s, int t, Map<Integer, Integer> heuristic) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Integer, Integer> gScore = new HashMap<>();
        
        for (int i = 1; i <= n; i++) {
            gScore.put(i, Integer.MAX_VALUE);
        }
        gScore.put(s, 0);
        
        openSet.add(new Node(s, 0, heuristic.getOrDefault(s, 0), null));
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.id == t) {
                List<Integer> path = new ArrayList<>();
                while (current != null) {
                    path.add(current.id);
                    current = current.parent;
                }
                Collections.reverse(path);
                return path;
            }
            
            for (int[] neighbor : graph.getOrDefault(current.id, new ArrayList<>())) {
                int neighborId = neighbor[0], weight = neighbor[1];
                int tentativeGScore = current.g + weight;
                
                if (tentativeGScore < gScore.getOrDefault(neighborId, Integer.MAX_VALUE)) {
                    gScore.put(neighborId, tentativeGScore);
                    Node nextNode = new Node(neighborId, tentativeGScore, heuristic.getOrDefault(neighborId, 0), current);
                    openSet.add(nextNode);
                }
            }
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("path5.txt"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int s, t;
        
        if (st.hasMoreTokens()) {
            s = Integer.parseInt(st.nextToken());
            t = Integer.parseInt(st.nextToken());
        } else {
            st = new StringTokenizer(br.readLine());
            s = Integer.parseInt(st.nextToken());
            t = Integer.parseInt(st.nextToken());
        }
        
        Map<Integer, List<int[]>> graph = new HashMap<>();
        Map<Integer, Integer> heuristic = new HashMap<>();
        
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new int[]{v, w});
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(new int[]{u, w}); // If undirected
        }
        
        if (br.ready()) {
            st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= n; i++) {
                if (st.hasMoreTokens()) {
                    heuristic.put(i, Integer.parseInt(st.nextToken()));
                } else {
                    heuristic.put(i, 0); // Default to 0 if heuristic is missing
                }
            }
        }
        
        List<Integer> path = aStar(n, graph, s, t, heuristic);
        System.out.println("Shortest Path: " + path);
    }
}
