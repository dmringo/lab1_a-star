import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Node implements AStarNode {
    
    private static final String VALID_SYMBOLS = ">!X012";
    private Map <Node, Relation> neighborMap;
    private double cost;
    private char symbol;
    private boolean traversable;
    private Point2D location;
    
    enum Relation
    {
      ABOVE, BELOW, LEFT_OF, RIGHT_OF, NON_ADJASCENT
    }
    
    /**
     * Base constructor takes 
     * 
     * @param x
     * @param y
     * @param cost
     * @param traversable
     */
    public Node( double x, double y, double cost, boolean traversable)
    {
      neighborMap = new HashMap<>();
      location = new Point2D.Double(x, y);
      this.traversable = traversable;
      this.cost = cost; 
    }
    
    public Node(Point2D loc, double cost, boolean traversable)
    {
      this(loc.getX(), loc.getY(), cost, traversable);
    }
    
    public void addNeighbor( Node node, Relation relation)
    {
      neighborMap.put(node, relation);
    }
    
    public Relation getRelationTo(Node node)
    {
      if(neighborMap.containsKey(node)) return neighborMap.get(node);
      else return Relation.NON_ADJASCENT;          
    }
    
    @Override
    public double heuristicDistance(AStarNode node)
    {
      return node.getLocation().distance(location);
    }

    @Override
    public double getCost()
    {
      return cost;
    }
    
    @Override
    public boolean isTraversable()
    {
      return traversable;
    }

    @Override
    public List<? extends AStarNode> getNeighbors()
    {
      return new ArrayList<>(neighborMap.keySet());
    }

    @Override
    public Point2D getLocation()
    {
      return new Point2D.Double(location.getX(), location.getY());
    }
    
    public static List<Node> makeGraphFromFile(String fileName)
    {
      try(Scanner scanner = new Scanner(Paths.get(fileName)))
      {
        while(scanner.hasNext())
        {
          String str = scanner.next();
          System.out.println(str);      
        }
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
      
      return null;
    }
  }