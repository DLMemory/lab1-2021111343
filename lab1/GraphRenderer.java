package lab1;

import java.io.IOException;

@SuppressWarnings("checkstyle:MissingJavadocType")
public class GraphRenderer {
    private final Graph graph;

    public GraphRenderer(Graph graph) {
        //this.graph = graph;
        this.graph = new Graph(graph);
    }

    private void writeDotFile(String content, String fileName) {
        MyUtils.writeWalkToFile(content, fileName);
    }

    private void convertGraphToDotAndWriteToFile() {
        String dotContent = graph.toString();
        writeDotFile(dotContent, "./output/graph.dot");
    }

    public void renderGraph() {
        convertGraphToDotAndWriteToFile();
        try {
            Process process = Runtime.getRuntime().exec("dot ./output/graph.dot -T png -o ./output/graph.png");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

