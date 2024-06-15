package lab1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class calcShortTest {
    //String[] words = MyUtils.readFile("C:\\Users\\11152\\Desktop\\Junit_test2.txt");;
    String[] words = MyUtils.readFile("./Junit_test.txt");;
    Graph graph = new Graph(words);
    String word1;
    String word2;

    @Test
    public void testCalcShortestPathWord2Empty() {

        String result = graph.calcShortestPath("experiences", "");
        assertEquals("experiences -> that -> to, length = 2\n" +
                "experiences -> that -> to -> one, length = 3\n" +
                "experiences -> that, length = 1\n" +
                "experiences -> that -> to -> one -> two, length = 4\n" +
                "experiences -> that -> lead, length = 2\n",result);
        // 预期输出为包含从 "word1" 到其他所有单词的最短路径的字符串
    }

    @Test
    public void calcShortest1() {
    }
    @Test
    public void testCalcShortestPathBothWordsEmpty() {
        String result = graph.calcShortestPath("", "");
        assertNull(result);
        // 预期输出为 "Please input at least one word!"
    }

    @Test
    public void testCalcShortestPathWord1Empty() {
        String result = graph.calcShortestPath("", "not in graph");
        assertNull(result);
        // 预期输出为 "No path from "word2" to any other words!"
    }

    @Test
    public void testCalcShortestPathWord1Empty2() {
        String result = graph.calcShortestPath("", "that");
        //assertNull(result);
        assertEquals("""
                that -> lead, length = 1
                that -> to -> one -> two, length = 3
                that -> to -> one, length = 2
                that -> to, length = 1\n""",result);
        // 预期输出为 "No path from "word2" to any other words!"
    }

    @Test
    public void testCalcShortestPathWordNotInGraph() {
        String result = graph.calcShortestPath("wordNotInGraph", "word2");
        assertNull(result);
        // 预期输出为 "No "wordNotInGraph" or "word2" in the graph!"
    }

    @Test
    public void testCalcShortestPathWordNotInGraph2() {
        String result = graph.calcShortestPath("experiences", "word2");
        assertNull(result);
        // 预期输出为 "No "wordNotInGraph" or "word2" in the graph!"
    }

    @Test
    public void testCalcShortestPathNoPath() {
        // 假设图中存在一条从 "word1" 到 "word2" 的路径
        // 这里假设该路径不存在
        String result = graph.calcShortestPath("word1", "word2");
        assertNull(result);
        // 预期输出为 "No path from "word1" to "word2"!"
    }

    @Test
    public void testCalcShortestPathWithValidPath() {
        // 假设图中存在一条从 "word1" 到 "word2" 的路径
        // 这里假设该路径存在且长度为 5
        String result = graph.calcShortestPath("that", "experiences");
        assertNull(result);
        //assertEquals("experiences -> that, length = 1", result);
        // 预期输出为包含从 "word1" 到 "word2" 的最短路径及其长度的字符串
    }

    @Test
    public void testCalcShortestPathWithValidPath1() {
        // 假设图中存在一条从 "word1" 到 "word2" 的路径
        // 这里假设该路径存在且长度为 5
        String result = graph.calcShortestPath("experiences", "that");
        //assertNull(result);
        assertEquals("experiences -> that, length = 1", result);
        // 预期输出为包含从 "word1" 到 "word2" 的最短路径及其长度的字符串
    }

    @Test
    public void testCalcShortestPathWithValidPath2() {
        // 假设图中存在一条从 "word1" 到 "word2" 的路径
        // 这里假设该路径存在且长度为 5
        //Graph graph1 = new Graph();
        String result = graph.calcShortestPath("two", "");
        assertNull(result);
        //assertEquals("experiences -> that, length = 1", result);
        // 预期输出为包含从 "word1" 到 "word2" 的最短路径及其长度的字符串
    }
}