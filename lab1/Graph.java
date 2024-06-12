package lab1;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class Graph {
    private Map<String, Node> nodes;
    // 相当于邻接表
    Map<Node, Map<Node, Integer>> edges;

    SecureRandom random = new SecureRandom();

//    private static  Scanner scanner = new Scanner(System.in);

    public Graph() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }

    // 复制构造函数
    public Graph(Graph other) {
        // 创建nodes的深层复制
        this.nodes = new HashMap<>(other.nodes);

        // 创建edges的深层复制
        this.edges = new HashMap<>();
        for (Map.Entry<Node, Map<Node, Integer>> entry : other.edges.entrySet()) {
            Node key = entry.getKey();
            Map<Node, Integer> value = new HashMap<>(entry.getValue());
            this.edges.put(key, value);
        }

        // SecureRandom是不可变的，所以可以直接复制引用
        this.random = other.random;
    }

    // 功能需求1：通过读入的文本生成有向图
    public Graph(String[] words) {
        this();
        for (int i = 0; i < words.length - 1; i++) {
            String sourceName = words[i];
            String targetName = words[i + 1];

            if (!nodes.containsKey(sourceName)) {
                addNode(sourceName);
            }
            if (!nodes.containsKey(targetName)) {
                addNode(targetName);
            }
            Node sourceNode = getNode(sourceName);
            Node targetNode = getNode(targetName);

            // 获取与sourceNode相关联的边集（Map<Node, Integer>），如果没有则创建新的
            Map<Node, Integer> sourceNodeEdges = edges.get(sourceNode);
            if (sourceNodeEdges == null) {
                sourceNodeEdges = new HashMap<>();
                edges.put(sourceNode, sourceNodeEdges);
            }

            int weight = sourceNodeEdges.getOrDefault(targetNode, 0);
            sourceNodeEdges.put(targetNode, weight + 1);
        }
    }

    public void addNode(String name) {
        nodes.put(name, new Node(name));
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    public void displayEdges() {
        for (Entry<Node, Map<Node, Integer>> sourceEntry : edges.entrySet()) {
            Node source = sourceEntry.getKey();
            Map<Node, Integer> sourceNodeEdges = sourceEntry.getValue();
            for (Map.Entry<Node, Integer> targetEntry : sourceNodeEdges.entrySet()) {
                Node target = targetEntry.getKey();
                int weight = targetEntry.getValue();
                System.out.println(source.getName() + " -> " + target.getName() + " (weight: " + weight + ")");
            }
        }
    }

    // word1 -> word3 -> word2, return word3List
    // 功能需求3：返回所有的桥接词，如果有多个通过空格分割，再拼接
    public String queryBridgeWords(String word1, String word2){
        Node node1 = getNode(word1);
        Node node2 = getNode(word2);
        ArrayList<String> word3List = new ArrayList<>();
        if(node1 == null || node2 == null){
            System.err.println("No \"" + word1 + "\" or \"" + word2 + "\" in the graph!");
            return null;
        }
        Map<Node, Integer> node1Edges = edges.get(node1);
        for (Node node3 : node1Edges.keySet()){
            Map<Node, Integer> node3Edges = edges.get(node3);
            if(node3Edges != null && node3Edges.containsKey(node2)){
                word3List.add(node3.getName());
            }
        }

        if(word3List.size() == 0){
            System.err.println("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"!");
            return null;
        }
        else {
            StringBuffer words = new StringBuffer();
            for (int i = 0; i<word3List.size(); ++i){
                //words = words + word3List.get(i) + " ";
                words.append(word3List.get(i));
                words.append(" ");
            }
            return words.toString();
        }
    }

    // word1 -> word3 -> word2, return 1 word3 only
    // 如果有多个桥接词，只返回第一个，功能需求4中需要调用
    public String queryBridgeWord(String word1, String word2){
        Node node1 = getNode(word1);
        Node node2 = getNode(word2);
        ArrayList<String> word3List = new ArrayList<>();
        if(node1 == null || node2 == null){
            return null;
        }
        Map<Node, Integer> node1Edges = edges.get(node1);
        for (Node node3 : node1Edges.keySet()){
            Map<Node, Integer> node3Edges = edges.get(node3);
            if(node3Edges != null && node3Edges.containsKey(node2)){
                word3List.add(node3.getName());
            }
        }

        if(word3List.size() == 0){
            return null;
        } else {
            return word3List.get(0);
        }
    }

    // 功能需求4：根据桥接词生成新文本
    public String generateNewText(String inputText){
        String[] inputWords = inputText.split("\\s+");
        String newSentence = "";
        for (int i = 0; i<inputWords.length - 1; ++i){
            newSentence += inputWords[i] + " ";
            String bridgeWord = queryBridgeWord(inputWords[i].toLowerCase(), inputWords[i+1].toLowerCase());
            if(bridgeWord != null){
                newSentence += (bridgeWord + " ");
            }
        }
        newSentence += inputWords[inputWords.length-1];
        return newSentence;
    }

    // distance: 从起始节点到其他节点的最短距离。
    // previous: 最短路径中每个节点的前驱节点，用于重建路径。
    private record DijkstraResult(Map<Node, Integer> distance, Map<Node, Node> previous){
    }

    private DijkstraResult dijkstra(Node startNode) {
        Map<Node, Integer> distance = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        ArrayList<Node> visited = new ArrayList<>();

        // 初始化距离为无穷大
        for (Node node : nodes.values()) {
            distance.put(node, Integer.MAX_VALUE);
            previous.put(node, null);
        }
        distance.put(startNode, 0);

        while (visited.size() < nodes.size()) {
            // find the node with the minimum distance
            Node minNode = null;
            int minDistance = Integer.MAX_VALUE;
            for (Node node : nodes.values()) {
                if (!visited.contains(node) && distance.get(node) < minDistance) {
                    minNode = node;
                    minDistance = distance.get(node);
                }
            }
            if (minNode == null) {
                break;
            }
            visited.add(minNode);
            // update the distance of the neighbors of the minNode
            Map<Node, Integer> minNodeEdges = edges.get(minNode);
            if (minNodeEdges == null) {
                continue;
            }
            for (Entry<Node, Integer> neighborEntry : minNodeEdges.entrySet()) {
                Node neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();
                int newDistance = distance.get(minNode) + weight;
                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    previous.put(neighbor, minNode);
                }
            }
        }

        return new DijkstraResult(distance, previous);
    }

    // 功能需求5：计算两个单词之间的最短路径
    public String calcShortestPath(String word1, String word2) {
        /* Use Dijkstra to find the shortest path */
        StringBuilder path = new StringBuilder();
        if (Objects.equals(word1, "") && Objects.equals(word2, "")) {
            System.err.println("Please input at least one word!");
            return null;
        }
        if (Objects.equals(word1, "")) {
            // swap word1 and word2
            String temp = word1;
            word1 = word2;
            word2 = temp;
        }
        // now word1 must not be empty, word2 may be empty
        Node node1 = getNode(word1);
        Node node2 = getNode(word2);
        if (node1 == null || !Objects.equals(word2, "") && node2 == null) {
            System.err.println("No \"" + word1 + "\" or \"" + word2 + "\" in the graph!");
            return null;
        }
        // get the shortest paths from node1 to all other nodes
        DijkstraResult result = dijkstra(node1);
        Map<Node, Integer> distance = result.distance();
        Map<Node, Node> previous = result.previous();

        // if word2 is empty, display all the shortest paths from node1
        if (Objects.equals(word2, "")) {
            for (Map.Entry<Node, Integer> entry : distance.entrySet()) {
                Node node = entry.getKey();
                int dist = entry.getValue();
                if (node != node1 && dist != Integer.MAX_VALUE) {
                    StringBuilder singlePath = new StringBuilder();
                    Node current = node;
                    singlePath.append(current.getName());
                    while (current != node1) {
                        current = previous.get(current);
                        singlePath.insert(0, current.getName() + " -> ");
                    }
                    // 展示最短的路径长度
                    singlePath.append(", length = " + dist);
                    singlePath.append("\n");
                    path.append(singlePath);
                }
            }
            if (path.isEmpty()) {
                System.err.println("No path from \"" + word1 + "\" to any other words!");
                return null;
            }
            return path.toString();
        }

        if (distance.get(node2) == Integer.MAX_VALUE) {
            System.err.println("No path from \"" + word1 + "\" to \"" + word2 + "\"!");
            return null;
        }

        Node current = node2;
        path.append(current.getName());
        while (current != node1) {
            current = previous.get(current);
            // head insertion method
            path.insert(0, current.getName() + " -> ");
        }
        path.append(", length = " + distance.get(node2));
        return path.toString();
    }

    // 功能需求6：随机游走
    public String randomWalk(){
        // pick a random node as the start node

        int randomIndex = (int)(random.nextDouble() * nodes.size());
        Node startNode = (Node)nodes.values().toArray()[randomIndex];
        StringBuilder walk = new StringBuilder();
        walk.append(startNode.getName());
        // do random walk when receive Enter, until pass a visited edge, or reach a node without out-edges, or receive 'q'
        Node currentNode = startNode;
        // 记录边是否被访问过
        Map<Node, Map<Node, Boolean>> visited = new HashMap<>();
        InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(reader);
        while (true) {
            System.out.print(walk);
            if (scanner.nextLine().equals("q")) {
                break;
            }
            Map<Node, Integer> currentNodeEdges = edges.get(currentNode);
            if (currentNodeEdges == null || currentNodeEdges.isEmpty()) {
                break;
            }
            // 随机选取一条边
            int randomEdgeIndex = (int)(random.nextDouble() * currentNodeEdges.size());
            Node nextNode = (Node)currentNodeEdges.keySet().toArray()[randomEdgeIndex];

            if (visited.get(currentNode) == null) {
                visited.put(currentNode, new HashMap<>());
            }
            // reach a duplicated edge
            if (visited.get(currentNode).containsKey(nextNode)) {
                // 最后一条重复的边也输出
                walk.append(" -> ").append(nextNode.getName());
                System.out.println(walk);
                break;
            } else {
                visited.get(currentNode).put(nextNode, true);
                walk.append(" -> ").append(nextNode.getName());
            }
            currentNode = nextNode;
        }
        return walk.toString();
    }

    // overload toString() method
    @Override
    public String toString() {
        StringBuilder graphString = new StringBuilder();
        graphString.append("digraph G {\n");
        for (Map.Entry<Node, Map<Node, Integer>> sourceEntry : edges.entrySet()) {
            Node source = sourceEntry.getKey();
            Map<Node, Integer> sourceNodeEdges = sourceEntry.getValue();
            for (Map.Entry<Node, Integer> targetEntry : sourceNodeEdges.entrySet()) {
                Node target = targetEntry.getKey();
                int weight = targetEntry.getValue();
                graphString.append("  ").append(source.getName()).append(" -> ").append(target.getName()).append(" [label=\"").append(weight).append("\"];\n");
            }
        }
        graphString.append("}");
        return graphString.toString();
    }
}