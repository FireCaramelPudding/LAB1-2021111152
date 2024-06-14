import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppTestB {
  App graph;

  // 在所有测试之前运行的 setup 方法
  @BeforeEach
  public void setUp() {
    // 初始化图对象
    this.graph = new App("To @ explore strange new worlds, To seek out new life and new civilizations?");
  }

  @Test
  void testValidqueryBridgeWords() {
    assertEquals("shortest path:\nout -> new -> life -> and\nthe length of shortest:3", graph.calcShortestPath("out", "and"), "the case with shortest path is wrong!");
  }

  @Test
  void testIInvalidqueryBridgeWords() {
    assertEquals("there IS NO path", graph.calcShortestPath("civilizations", "to"), "the case with no shortest path is wrong!");
    assertEquals("No word\"ou\"", graph.calcShortestPath("ou", "and"), "the case that word1 is not in the graph is wrong!");
    assertEquals("No word\"an\"", graph.calcShortestPath("out", "an"), "the case that word2 is not in the graph is wrong!");
    assertEquals("No word\"ou\" and \"an\"", graph.calcShortestPath("ou", "an"), "the case that both words are not in the graph is wrong!");
  }


}
