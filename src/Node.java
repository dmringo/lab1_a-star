import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Node{
    
    private Map <Node, Relation> neighborMap;
    private double cost;
    private boolean traversable;
    private Point location;
    
    public Node(int x, int y, double cost, boolean traversable)
    {
      neighborMap = new HashMap<>();
      location = new Point(x, y);
      this.traversable = traversable;
      this.cost = cost; 
    }
    
    public Node(Point loc, double cost, boolean traversable)
    {
      this(loc.x, loc.y, cost, traversable);
    }
    
    public void addNeighbor( Node node, Relation relation)
    {
      neighborMap.put(node, relation);
    }
    
    public Relation getRelationTo(Node node)
    {
      if(neighborMap.containsKey(node)) return neighborMap.get(node);
      else return Relation.NOT_ADJASCENT_TO;          
    }
    
    public double heuristicDistance(Node target)
    {
      Point targetLoc = target.getLocation(); 
      int dx = Math.abs(location.x - targetLoc.x);
      int dy = Math.abs(location.y - targetLoc.y);
      return dx + dy;
    }

    public double getCost()
    {
      return cost;
    }
    
    public boolean isTraversable()
    {
      return traversable;
    }

    public List<Node> getNeighbors()
    {
      return new ArrayList<>(neighborMap.keySet());
    }

    public Point getLocation()
    {
      return new Point(location.x, location.y);
    }
    
    
  }