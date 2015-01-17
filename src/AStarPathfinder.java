import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathfinder
{
  private Map<AStarNode, AStarNode> originationMap;
  private Map<AStarNode, Double> costFromStart;
  
  public AStarPathfinder()
  {
    originationMap = new HashMap<>();
    costFromStart = new HashMap<>();
  }
  
  public List<AStarNode> pathBetween(final AStarNode start, final AStarNode end)
  {
    costFromStart.clear(); /*must clear with each new path calculation */
    
    Set<AStarNode> visited = new HashSet<>();
    
    Comparator<AStarNode> comparator = new Comparator<AStarNode>()
    {
      @Override
      public int compare(AStarNode nodeA, AStarNode nodeB)
      {
        double costA = costFromStart.get(nodeA) + nodeA.heuristicDistance(end);
        double costB = costFromStart.get(nodeB) + nodeB.heuristicDistance(end);
        return (int)Math.round(costA - costB);
      }
    };
    
    PriorityQueue<AStarNode> open = new PriorityQueue<>(0, comparator);
    
    AStarNode current = start;
    
    while(current != end)
    {
      visited.add(current);
      for(AStarNode node : current.getNeighbors())
      {
        if(node == end)
        {
          originationMap.put(node, current);
          return buildPathFrom(node);
        }
        
        if(!node.isTraversable() || visited.contains(node)) continue;
        
        double tmpCostFromStart = costFromStart.get(current) + node.getCost();
        
        if(!open.contains(node) || tmpCostFromStart < costFromStart.get(node) )
        {
          costFromStart.put(node, tmpCostFromStart);
          open.add(node); /* no effect if already contained */
        }
      }
      current = open.poll(); 
    }
    
    /* Bad practice?  not sure of the best way to indicate disconnect nodes */
    return null; 
  }

  private List<AStarNode> buildPathFrom(AStarNode node)
  {
    ArrayList<AStarNode> path = new ArrayList<>();
    AStarNode current = node;
    while(originationMap.containsKey(current))
    {
      path.add(0, current);
      current = originationMap.get(current);
    }
    return path;
  }
  

}
