package demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
//import java.util.*;


public class App {

  final int[][] adjacencyMatrix; //邻接矩阵
  final int numOfVertices; //邻接矩阵阶数
  private HashMap<String, Integer> wordOrderMap; //key为单词，value为对应节点编号的hashmap

  /*目的：构造图的三个数据
    输入：读入的文本字符串
  */
  public App(String txt) {
    this.numOfVertices = processText(txt); //初始化
    this.adjacencyMatrix = new int[numOfVertices][numOfVertices]; //初始化
    //
    for (int i = 0; i < numOfVertices; i++) {
      for (int j = 0; j < numOfVertices; j++) {
        adjacencyMatrix[i][j] = 0;
      }
    } //邻接矩阵初始化为全0，代表无边
    String[] lines = txt.split("\\n"); //按换行分隔
    StringBuilder processedWord = new StringBuilder(); //字符串，用于储存处理完成之后的文本
    for (String line : lines) {
      //
      line = line.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase(); //每行遍历替换

      //
      String[] words = line.trim().split("\\s+");
      for (String word : words) {
        if (!word.isEmpty()) {
          processedWord.append(" ").append(word); //将处理完的文本存入processedWord中
        }
      }
    }
    String[] words = processedWord.toString().trim().split("\\s+"); //处理后的文本按空格分隔
    for (int i = 0; i < words.length - 1; i++) {
      addEdge(wordOrderMap.get(words[i]), wordOrderMap.get(words[i + 1])); //相邻单词之间加入边
    }

  }

  /*目的：处理文本初始化单词与节点序号对应的hashmap
   * 输入：文本字符串
   * 返回值：节点最大编号（单词种类数-1）
   */
  public Integer processText(String text) {
    wordOrderMap = new HashMap<>();
    int order = 0; // 节点编号从0开始

    String[] lines = text.split("\\n"); //按行分隔

    for (String line : lines) {
      // 每行处理替换
      line = line.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();

      // 按空格分隔
      String[] words = line.trim().split("\\s+");

      for (String word : words) {
        if (!word.isEmpty() && !wordOrderMap.containsKey(word)) {
          // 插入hashmap
          wordOrderMap.put(word, order++);
        }
      }
    }

    return order; //返回最大编号
  }

  /*目的：插入节点
   * 方法：输入前驱节点编号，后继节点编号，权值加1
   */
  public void addEdge(int from, int to) {
    // 权值加1
    adjacencyMatrix[from][to] += 1;
  }


  /*目的：根据节点编号查询单词名
   * 输入：hashmap，单词名
   * 返回值：单词字符串，若没有节点返回null
   */
  public static String findKeyByValue(HashMap<String, Integer> map, int searchValue) {
    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      if (entry.getValue().equals(searchValue)) {
        return entry.getKey(); //遍历查询，由于不会有重复的键值，故只返回查到的第一个键值
      }
    }
    return null;
  }

  /*目的：利用展示每个节点边的方式展示图（可以调用其他库是生成图像文件）
   * 输入：邻接矩阵
   * 输出：屏幕打印
   */
  void showDirectedGraph(int[][] g) {
    for (int i = 0; i < g.length; i++) {
      System.out.print(" -> " + findKeyByValue(wordOrderMap, i) + " : ");
      for (int j = 0; j < g[i].length; j++) {
        //遍历每个节点是否有后继节点
        if (g[i][j] > 0) {
          System.out.print("(" + g[i][j] + ") -> " + findKeyByValue(wordOrderMap, j) + " ");
        }
      }
      System.out.println();
    }
  }

  /*目的：根据输入的两个节点查询桥接节点，并且存入list nodes中
   * 输入，A，C节点编号，nodes（存储中间节点）
   * 返回值：bool数组，第0位表示是否有桥接节点，有为true，第1位表示是否有多个桥接节点，有为true
   */
  boolean[] dfs(int a, int c, List<Integer> nodes) {
    boolean[] flag = {false, false};

    for (int next = 0; next < adjacencyMatrix.length; next++) {
      if (adjacencyMatrix[a][next] > 0) {
        //依次查询A的后继节点
        if (adjacencyMatrix[next][c] > 0) {
          //判断后继节点是否是C，若是则存入nodes
          nodes.add(next);
          if (flag[0]) { //如果之前已经存在桥接节点则置为true
            flag[1] = true;
          }
          flag[0] = true; //存在桥接节点，置为true
        }
      }
    }
    return flag;
  }

   /*目的：根据输入两个单词查询桥接词
   * 输入：两个单词字符串
   * 返回值：屏幕输出的字符串，提醒桥接词信息
   */
  public String queryBridgeWords(String word1, String word2) {
    StringBuilder returnWords = new StringBuilder(); //返回字符串初始化
    word1 = word1.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
    word2 = word2.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
    if (wordOrderMap.containsKey(word2) && wordOrderMap.containsKey(word1)) {
      //判断输入是否在之前输入的文本中存在
      List<Integer> nodes = new ArrayList<>();
      int a = wordOrderMap.get(word1);
      int c = wordOrderMap.get(word2);
      boolean[] flag = dfs(a, c, nodes); //调用dfs方法
      if (flag[1]) { //存在多个桥接词
        returnWords.append("The bridge words from \"").append(word1).append("\" to \"").append(word2).append("\" are:\n");
        for (int node : nodes) {
          returnWords.append(findKeyByValue(wordOrderMap, node)).append(" ");
        }
      } else if (flag[0]) { //只有一个桥接词
        returnWords.append("The bridge word from \"").append(word1).append("\" to \"").append(word2).append("\" is:\n");
        returnWords.append(findKeyByValue(wordOrderMap, nodes.get(0)));
      } else { //不存在桥接词
        returnWords = new StringBuilder("No bridge words from \"" + word1 + "\" to \"" + word2 + "\"");
      }
    } else { //查询词不存在
      if (!wordOrderMap.containsKey(word1)) {
        returnWords.append("No \"").append(word1).append("\"").append(" ");
      }
      if (!wordOrderMap.containsKey(word2)) {
        returnWords.append("No \"").append(word2).append("\"").append(" ");
      } //v1.1添加No \
      returnWords.append("in the graph!\n");
    }


    return returnWords.toString();
  }

  /**
   * @noinspection checkstyle:Indentation
   */ /* */
  public String generateNewText(String inputText) {
    String[] words = inputText.split("\\s+"); //按空格划分
    StringBuilder ans = new StringBuilder(); //返回的字符串数组
    Random random = new Random();
    for (int i = 0; i <= words.length - 2; i++) {  //v1.1这里应该是减2,要保证j=i+1不越界
      int j = i + 1;
      ans.append(words[i]).append(' ');
      // System.out.println(words[i]);
      // System.out.println(words[j]);
      String qe = queryBridgeWords(words[i], words[j]);
      // System.out.println("qe的值是"+qe);
      String[] myquery = qe.split("\\n+"); //按行划分
      String[] spliteStrings = qe.split("\\s+"); //按空格划分
      if (!spliteStrings[0].equalsIgnoreCase("No")) { //查到了桥接词，桥接词从第二行开始
        // System.out.println("桥接词的值是"+myquery[1]);
        boolean flag = false;
        for (int str = 1; str <= myquery.length - 1; str++) {
          int rad = random.nextInt(myquery.length);
          if (rad % 2 == 0) { //模拟随机选取一个桥接词添加进去
            ans.append(myquery[str]).append(" ");
            flag = true;
            break;
          }
        }
        if (!flag) {
          //加入桥接词
          ans.append(myquery[myquery.length - 1]).append(" ");
          flag = true;
        }

      }
    }
    return ans.append(words[words.length - 1]).toString(); //v1.1由于遍历到length-2就停止，返回时还需要将最后一个字符加上
  }

  public String calcShortestPath(String word1, String word2) {
    /*
        * 用户输入两个单词，程序计算它们之间在图中的最短路径（路径上所有边权值之和最小），
        * 以某种突出的方式将路径标注在原图并展示在屏幕上，同时展示路径的长度（所有边权值之和）。
        – 例如：输入to和and，则其最短路径为to→explore→strange→new→life→and
            ? 如果有多条最短路径，只需要展示一条即可。
        – 可选：计算出所有的最短路径，并以不同的突出显示方式展示出来。
        – 例如to和and之间还有另一条路径：to→seek→out→new→life→and。
            ? 如果输入的两个单词“不可达”，则提示。
            ? 可选功能：如果用户只输入一个单词，则程序计算出该单词到图中其他任一单词的最短路径，并逐项展示出来
     */

    if (!wordOrderMap.containsKey(word1) && !wordOrderMap.containsKey(word2)) {

      return "No word" + "\"" + word1 + "\"" + " and " + "\"" + word2 + "\""; //修改添加打印内容
    } else if (!wordOrderMap.containsKey(word1) && wordOrderMap.containsKey(word2)) {
      return "No word" + "\"" + word1 + "\"";
    } else if (wordOrderMap.containsKey(word1) && !wordOrderMap.containsKey(word2)) {
      return "No word" + "\"" + word2 + "\"";
    } else {
      ArrayList[] parent = new ArrayList[numOfVertices]; // 记录每个顶点的前驱顶点列表
      for (int i = 0; i < numOfVertices; i++) {
        parent[i] = new ArrayList<>(); //对前驱列表初始化
      }
      int[] ans = new int[numOfVertices]; //追踪最短长度
      Arrays.fill(ans, Integer.MAX_VALUE);
      boolean[] sptSet = new boolean[numOfVertices]; //是否添加到最短路径中
      Arrays.fill(sptSet, false);
      int w1 = wordOrderMap.get(word1);

      ans[w1] = 0;
      boolean add; // 每个顶点的前驱顶点列表初始化为它自己
      add = parent[w1].add(w1);
      /*接下来是dijskra算法实现 */
      for (int count = 0; count < numOfVertices - 1; count++) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1; //最小距离和最小顶点索引
        for (int index = 0; index < numOfVertices; index++) //遍历所有顶点
        {
          if (!sptSet[index] && ans[index] <= min) //迭代寻找下一个充当原点的顶点
          {
            min = ans[index];
            minIndex = index;
          }
        }
        int u = minIndex;
        sptSet[u] = true; //添加到集合中

        for (int i = 0; i < numOfVertices; i++) {
          if (!sptSet[i] && adjacencyMatrix[u][i] != 0 && ans[u] != Integer.MAX_VALUE && ans[u] + adjacencyMatrix[u][i] < ans[i]) {
            ans[i] = ans[u] + adjacencyMatrix[u][i];
            parent[i].clear(); // 清除所有旧的前驱顶点
            parent[i].add(u); // 添加新的前驱顶点
          }
        }
      }
      /*算法结束，此时ans中保存原点到各顶点的最短路径*/
      int w2 = wordOrderMap.get(word2);
      if (ans[w2] == Integer.MAX_VALUE) {
        return "there IS NO path"; // 没有路径
      }
      // 回溯找到从 word1 到 word2 的所有最短路径,v1.1修改
      List<List<String>> allPaths = new ArrayList<>();

      generateAllShortestPaths(w1, w2, new ArrayList<>(), allPaths, parent, ans);

      // 构建结果字符串
      StringBuilder result = new StringBuilder();
      for (List<String> path : allPaths) {
        Collections.reverse(path); // 反转路径以获得正序
        result.append("最短路径：\n").append(String.join(" -> ", path)).append("\n");
      }
      result.append("最短路径长度：").append(ans[w2]);

      return result.toString();
    }

  }

  /*函数名：generateAllShortestPaths
   * 输入：图中两个顶点编号，当前路径，保存路径的字符串数组，前驱节点数组parent，保存原点到各顶点的最短路径的数组ans
   * 输出：空
   * 功能：给出给定两点的最短路径，保存在allPaths中
   */
  private void generateAllShortestPaths(int start, int end, List<String> currentPath, List<List<String>> allPaths, List<Integer>[] parent, int[] ans) {
    // 将当前顶点添加到路径中
    currentPath.add(findKeyByValue(wordOrderMap, end));
    // 如果到达起始顶点，将当前路径添加到所有路径的列表中
    if (end == start) {
      allPaths.add(new ArrayList<>(currentPath));
    } else {
      // 遍历所有前驱顶点
      for (int pred : parent[end]) {
        if (ans[end] == ans[pred] + adjacencyMatrix[pred][end]) {
          generateAllShortestPaths(start, pred, new ArrayList<>(currentPath), allPaths, parent, ans);

        }
      }
    }
  }

  /**
   * @noinspection checkstyle:LeftCurly, checkstyle:Indentation
   */ /* */
  public String randomWalk()
    /*
     * ? 进入该功能时，程序随机的从图中选择一个点，以此为起点沿出边进行随机遍历，记录经过的所有节点和边，
     *   直到出现第一条重复的边为止，或者进入的某个节点不存在出边为止。
     *   在遍历过程中，用户也可随时停止遍历。
     * ? 将遍历的节点输出为文本，并以文件形式写磁盘。
     * ? 例如：– to seek out new life and new worlds to explore strange new civilizations
     * – to explore strange new worlds to explore
     */
    {
    List<Integer> visited = new ArrayList<>(); // 已访问的顶点
    int startVertex = new Random().nextInt(numOfVertices); // 从随机顶点开始遍历
    visited.add(startVertex);
    System.out.println(findKeyByValue(wordOrderMap, startVertex) + " ");
    // int[][]edge = adjacencyMatrix;//记录已经访问过的边
    int[][] edge = new int[numOfVertices][numOfVertices];
    for (int i = 0; i < numOfVertices; i++) {
      Arrays.fill(edge[i], 0);
    } //这时edge全被置为0
    Scanner scanner = new Scanner(System.in);
    boolean traversalRunning = true; // 遍历是否正在进行的标志
    while (traversalRunning) {
      // System.out.println("现在进入循环");
      // 随机选择下一个顶点
      Random random = new Random();
      int nextVertex = -1;
      do {
        List<Integer> exist = new ArrayList<>(); // 已访问的顶点
        for (int i = 0; i < numOfVertices; i++) {
          if (adjacencyMatrix[startVertex][i] != 0) {
            exist.add(i);
          }
        }
        // System.out.println("exist.size是" + exist.size());
        if (exist.size() == 0) {
          traversalRunning = false;
          break;
        }

        nextVertex = exist.get(random.nextInt(numOfVertices) % exist.size());

        visited.add(nextVertex); //将顶点加入到遍历集中
        System.out.println(findKeyByValue(wordOrderMap, nextVertex) + " ");


        if (edge[startVertex][nextVertex] == 1) {
          traversalRunning = false;
          break;
        }
        edge[startVertex][nextVertex] = 1;
        startVertex = nextVertex;
        System.out.println("Input \"c\" to continue or any other words " + "like \"q\" to quit the random walk");

        String text = scanner.nextLine();
        if (!text.equals("c")) {
          traversalRunning = false;
          break;
        }
      } while ((true));


    }
    // scanner.close();
    StringBuilder ans = new StringBuilder();
    for (int i : visited) {
      ans.append(findKeyByValue(wordOrderMap, i)).append(" ");
    }
    return ans.toString();

  }

  /* */
  public static void main(String[] args) throws Exception {

    java.util.Scanner scanner = new java.util.Scanner(System.in);
    System.out.println("请输入文件路径：");
    String filePath = scanner.nextLine();

    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String currentLine;
      while ((currentLine = br.readLine()) != null) {
        contentBuilder.append(currentLine).append(System.lineSeparator());
      }
    } catch (IOException e) {
      System.err.println("文件读取失败或文件不存在。");
      e.printStackTrace();
      scanner.close();
      return;
    }
    System.out.println("文件内容：");
    System.out.println(contentBuilder.toString());
    String txt = contentBuilder.toString();
    App graph = new App(txt); //实例化App（图）
    // Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("*********************************************");
      System.out.println("show: show the graph");
      System.out.println("bridge: search bridge words");
      System.out.println("generate: generate new text by bridge words");
      System.out.println("shortpath: search the shortest path between two words");
      System.out.println("walk: walk in the graph randomly");
      System.out.println("exit: exit the program");
      System.out.println("*********************************************");
      System.out.println("please input the command.");
      String command = scanner.nextLine();
      if (command.equalsIgnoreCase("exit")) {
        break;
      } else if (command.equalsIgnoreCase("show")) {

        //展示有向图
        graph.showDirectedGraph(graph.adjacencyMatrix); //展示图
      } else if (command.equalsIgnoreCase("bridge")) {
        //查询桥接词
        final String word1;
        final String word2;
        System.out.println("please input word1");
        word1 = scanner.nextLine().replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
//        word1 = word1.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
        System.out.println("please input word2");
        word2 = scanner.nextLine().replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
//        word2 = word2.replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
        System.out.println(graph.queryBridgeWords(word1, word2)); //查询桥接词
      } else if (command.equalsIgnoreCase("generate")) {
        //根据bridge word生成新文本
        /*
         * – 如果两个单词无bridge word，则保持不变，不插入任何单词；
         * – 如果两个单词之间存在多个bridge words，则随机从中选择一个插入进去形成新文本。
         * ? 例如用户输入：Seek to explore new and exciting synergies
         * ? 则输出结果为：Seek to explore strange new life and exciting synergies
         */
        System.out.println("please input the text.");
        String newText = scanner.nextLine();
        String ans = graph.generateNewText(newText);
        System.out.println(ans);
      } else if (command.equalsIgnoreCase("shortpath")) {
        //计算两个单词之间的最短路径
        //dijskla算法
        System.out.println("please input two words with black:");
        String newText = scanner.nextLine().replaceAll("[^a-zA-Z\\s]", " ").toLowerCase();
        String[] words = newText.split("\\s+"); //按空格划分
        if (words.length != 2) {
          System.out.println("Input wrong!Please input two words!");
        } else {
          String ans = graph.calcShortestPath(words[0], words[1]);
          System.out.println(ans);
        }

      } else if (command.equalsIgnoreCase("walk")) {
        //随机游走
        String ans = graph.randomWalk();
        System.out.println(ans);
        // String filePath = "file.txt"; //文件路径
        try (
                FileWriter writer = new FileWriter("file.txt")
        ) {
          writer.write(ans);
          System.out.println("内容已成功写入文件file.txt。");
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("写入文件时出错：" + e.getMessage());
        }
      } else {
        System.out.println("Invalid command.Please input again!");
      }
    }
    scanner.close();
  }
}

