package lab1;

import org.junit.Test;
import static org.junit.Assert.*;

public class BridgeWords_Test {

    String[] words = MyUtils.readFile("./Junit_test.txt");;
    Graph graph = new Graph(words);
    String word1;
    String word2;

    @Test
    public void test1(){
        word2 = "experiences";
        String result = graph.queryBridgeWords(word1,word2);
        assertEquals(null, result);
    }


    @Test
    public void test2(){
        word1 = "experiences";
        word2 = "lead";
        String result = graph.queryBridgeWords(word1,word2);
        //assertEquals("No \"\" or \"\" in the graph!", result);
        assertEquals("that ", result);
    }

    @Test
    public void test3(){
        word1 = "exp";
        word2 = "lead";
        String result = graph.queryBridgeWords(word1,word2);
        assertEquals(null, result);
    }

    @Test
    public void test4(){
        word1 = "experiences";
        word2 = "that";
        String result = graph.queryBridgeWords(word1,word2);
        assertEquals(null, result);
    }

    @Test
    public void test5(){
        word1 = "that";
        word2 = "one";
        String result = graph.queryBridgeWords(word1,word2);
        assertEquals("lead to ", result);
    }
}

