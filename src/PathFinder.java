import java.awt.Point;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PathFinder
{
  private static final String DEFAULT_TEXT_MAP = "CS-351_Lab1_mapweight.txt";
  private static final String TEST_FILE = "/home/david/CS-351_Lab1_mapweight.txt";
  private static final String VALID_SYMBOLS = ">!X012";
  
  private Node start;
  private Node end;
  private List<Node> nodeList;
  
  public static void main(String[] args)
  {
    String fileToLoad = TEST_FILE;
    if(args.length > 0) fileToLoad = args[0];
    
    PathFinder pathFinder = new PathFinder(fileToLoad);    
    pathFinder.findAndPrintPath();
    
  }
  
  public PathFinder(String mapFile)
  {
    nodeList = new ArrayList<>();
    loadFile(mapFile);
  }
  
  public void findAndPrintPath()
  {
    AStarPathFinder a = new AStarPathFinder();
    List<Node> nodePath = new ArrayList<>();
    
    List<AStarNode> aStarNodePath = a.pathBetween(start, end);
    
    if(aStarNodePath != null)
    {
      for(AStarNode node : aStarNodePath)
      {
        /* necessary to allow AStarNode interface */
        if(node instanceof Node) nodePath.add((Node)node);
      }
      
      for (int i = 0; i < nodePath.size() - 1; i++)
      {
        Node from = nodePath.get(i);
        Node to = nodePath.get(i + 1);
        System.out.println(to.getRelationTo(from));
      }
      
    }
  }
  private void loadFile(String fileName)
  {
    
    try
    {
      int row = 0;
      int col = 0;
      Point point = new Point(0,0);
      Node node;
      Path path = Paths.get(fileName);
      
      for(String line : Files.readAllLines(path, Charset.defaultCharset()))
      {
        col = 0;
        for(char symbol : line.toCharArray())
        {
          /* basic error-checking */
          if(PathFinder.VALID_SYMBOLS.indexOf(symbol) < 0)
          {
            System.out.printf("Bad symbol (%c) found in file row:%d col:%d%n",
                symbol, row, col);
            System.out.println("Program will exit.");
          }
          
          point.setLocation(row, col);
          node = constructNode(point, symbol);
          nodeList.add(node);          
          col++;
        }
        row++;
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    connectNodes();
  }
  
  
  /*
   * Connects the nodes based on their coordinates interpreted from the
   * file describing the map.
   * This might be done more efficiently/cleanly with a Location->Node lookup
   * table or something, as the current process is currently O(n^2).
   */
  private void connectNodes()
  {
    for(Node nodeA : nodeList)
    {
      for(Node nodeB : nodeList)
      {
        Relation relation = findRelation(nodeA, nodeB);
        if(relation != Relation.NOT_ADJASCENT_TO)
        {
          nodeA.addNeighbor(nodeB, relation);
        }
      }
    }
  }

  /*
   * Finds the relation between nodeA and nodeB.  The relation satisfies the
   * sentence nodeB is [relation name] nodeB
   */
  private Relation findRelation(Node nodeA, Node nodeB)
  {
    if(nodeA == nodeB) return Relation.NOT_ADJASCENT_TO;
  
    double dx = nodeA.getLocation().getX() - nodeB.getLocation().getX();
    double dy = nodeA.getLocation().getY() - nodeB.getLocation().getY();
    double absDx = Math.abs(dx);
    double absDy = Math.abs(dy);
    
    /* nodes are more than one unit apart */
    if(absDx > 1 || absDy > 1) return Relation.NOT_ADJASCENT_TO;
    
    if(absDx > 0)
    {
      /* nodes are diagonal from one another */
      if(absDy > 0) return Relation.NOT_ADJASCENT_TO;
      
      return dx > 0 ? Relation.LEFT_OF: Relation.RIGHT_OF;
    }
    
    /* if no difference in X, difference is in Y */ 
    return dy > 0 ? Relation.ABOVE : Relation.BELOW;
  }

  /*
   * Parses the symbol read that represents a node on the map and constructs
   * a new Node using that information and the passed Point
   */
  private Node constructNode(Point point, char symbol)
  {
    double cost;
    switch(symbol)
    {
      case '0':
      case '1':
      case '2':
        cost = Math.pow(2, Integer.parseInt("" + symbol));
        return new Node(point, cost, true);
      case '>':
        cost = Double.MAX_VALUE;
        start = new Node(point, cost, true);
        return start;
      case '!':
        cost = Double.MAX_VALUE;
        end = new Node(point, cost, true);
        return end;
      case 'X':
        cost = Double.MAX_VALUE;
        return new Node(point, cost, false);
      default:
        return null; /* never reached */
    }
  }
}
