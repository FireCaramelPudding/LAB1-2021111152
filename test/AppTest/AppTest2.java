package AppTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//package AppTest;

import demo.App;
import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

class AppTest2 {
  App graph;

  // 在所有测试之前运行的 setup 方法
  @BeforeEach
  public void setUp() {
    // 初始化图对象
    this.graph = new App("To @ explore strange new worlds, To seek out new life and new civilizations?");
  }

  @Test
  void testValidqueryBridgeWords() {
    assertEquals("shortest path:\n" +
            "out -> new -> life -> and\n" +
            "the length of shortest:3", graph.calcShortestPath("out", "and"), "the case with shortest path is wrong!");
  }

  @Test
  void testIInvalidqueryBridgeWords() {
    assertEquals("there IS NO path", graph.calcShortestPath("civilizations", "to"), "the case with no shortest path is wrong!");
    assertEquals("No word\"ou\"", graph.calcShortestPath("ou", "and"), "the case that word1 is not in the graph is wrong!");
    assertEquals("No word\"an\"", graph.calcShortestPath("out", "an"), "the case that word2 is not in the graph is wrong!");
    assertEquals("No word\"ou\" and \"an\"", graph.calcShortestPath("ou", "an"), "the case that both words are not in the graph is wrong!");
  }


}
