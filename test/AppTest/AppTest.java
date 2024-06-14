package AppTest;

import demo.App;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;


public class AppTest {

  private App graph;

  // 在所有测试之前运行的 setup 方法
  @BeforeMethod
  public void setUp() {
    // 初始化图对象
    graph = new App("""
             Alice was beginning to get very tired of sitting by her sister
            on the bank, and of having nothing to do:  once or twice she had
            peeped into the book her sister was reading, but it had no
            pictures or conversations in it, `and what is the use of a book,'
            thought Alice `without pictures or conversation?get so tired'""");
  }

  // 第一个测试用例:存在一个桥接词
  @Test
  public void testqueryBridgeWordsCase1() {
    // 准备测试数据
    String word1 = "and";
    String word2 = "having";
    // 调用queryBridgeWords方法
    String[] result = graph.queryBridgeWords(word1, word2).split("\\n+");
    // 定义期望的结果
    String expected = "of";
    // 断言结果是否符合预期
    assertEquals(result[0], "The bridge word from \"and\" to \"having\" is:", "Case 1: The queryBridgeWords method did not return the expected bridge word.");
    assertEquals(result[1], expected, "Case 1: The queryBridgeWords method did not return the expected bridge word.");
  }

  // 第二个测试用例：存在多个桥接词
  @Test
  public void testqueryBridgeWordsCase2() {
    // 准备测试用例的数据
    String word1 = "get";
    String word2 = "tired";
    // 调用queryBridgeWords方法
    String[] result = graph.queryBridgeWords(word1, word2).split("\\n+");
    // 定义期望的结果
    String expected1 = "very so ";
//    String expected2 = "so";
    // 断言结果是否符合预期
    assertEquals(result[0], "The bridge words from \"get\" to \"tired\" are:", "Case 1: The queryBridgeWords method did not return the expected bridge word.");
    assertEquals(result[1], expected1, "Case 2: The queryBridgeWords method did not return the expected bridge word.");
//    assertEquals(result[2], expected2, "Case 2: The queryBridgeWords method did not return the expected bridge word.");

  }

  // 第三个测试用例：第一个词不在图中
  @Test
  public void testqueryBridgeWordsCase3() {
    // 准备测试用例的数据
    String word1 = "Bob";
    String word2 = "Alice";
    // 调用queryBridgeWords方法
    String[] result = graph.queryBridgeWords(word1, word2).split("\\n+");
    // 定义期望的结果
    String expected= "No \"bob\" in the graph!";
    // 断言结果是否符合预期
    assertEquals(result[0], expected, "Case 3: The queryBridgeWords method did not return the expected message.");
  }

  // 第四个测试用例：第二个词不在图中
  @Test
  public void testqueryBridgeWordsCase4() {
    // 准备测试用例的数据
    String word1 = "Alice";
    String word2 = "teacher";
    // 调用queryBridgeWords方法
    String[] result = graph.queryBridgeWords(word1, word2).split("\\n+");
    // 定义期望的结果
    String expected= "No \"teacher\" in the graph!";
    // 断言结果是否符合预期
    assertEquals(result[0], expected, "Case 4: The queryBridgeWords method did not return the expected message.");
  }

  // 第五个测试用例：两个词均不在图中
  @Test
  public void testqueryBridgeWordsCase5() {
    // 准备测试用例的数据
    String word1 = "hello";
    String word2 = "world";
    // 调用queryBridgeWords方法
    String[] result = graph.queryBridgeWords(word1, word2).split("\\n+");
    // 定义期望的结果
    String expected= "No \"hello\" No \"world\" in the graph!";
    // 断言结果是否符合预期
    assertEquals(result[0], expected, "Case 5: The queryBridgeWords method did not return the expected message.");
  }

  // 第六个测试用例：不存在桥接词
  @Test
  public void testqueryBridgeWordsCase6() {
    // 准备测试用例的数据
    String word1 = "it";
    String word2 = "Alice";
    // 调用queryBridgeWords方法
    String[] result = graph.queryBridgeWords(word1, word2).split("\\n+");
    // 定义期望的结果
    String expected= "No bridge words from \"it\" to \"alice\"";
    // 断言结果是否符合预期
    assertEquals(result[0], expected, "Case 6: The queryBridgeWords method did not return the expected message.");
  }

}