import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathFinder
{
  private Map<Node, Node> originationMap;
  private Map<Node, Double> costFromStart;
  private List<? extends Node> nodes;
  
  public AStarPathFinder()
  {
    originationMap = new HashMap<>();
    costFromStart = new HashMap<>();
  }
  
  public List<Node> pathBetween(final Node start, final Node end)
  {
    costFromStart.clear(); /*must clear with each new path calculation */
    
    Set<Node> visited = new HashSet<>();
    
    Comparator<Node> comparator = new Comparator<Node>()
    {
      @Override
      public int compare(Node nodeA, Node nodeB)
      {
        double costA = costFromStart.get(nodeA) + nodeA.heuristicDistance(end);
        double costB = costFromStart.get(nodeB) + nodeB.heuristicDistance(end);
        return (int)Math.round(costA - costB);
      }
    };
    
    PriorityQueue<Node> open = new PriorityQueue<>(1, comparator);
    
    Node current = start;
    costFromStart.put(start, new Double(0));
    
    while(current != end)
    {
      visited.add(current);
      for(Node node : current.getNeighbors())
      {
        if(node == end)
        {
          originationMap.put(node, current);
          return retraceAndBuildPath(node);
        }
        
        if(!node.isTraversable() || visited.contains(node)) continue;
        
        double tmpCostFromStart = costFromStart.get(current) + node.getCost();
        
        if(!open.contains(node) || tmpCostFromStart < costFromStart.get(node) )
        {
          costFromStart.put(node, tmpCostFromStart);
          open.add(node); /* no effect if already contained */
        }
      }
      Node next = open.poll();
      originationMap.put(current, next);
      current = next;
    }
    
    /* Bad practice?  not sure of the best way to indicate disconnect nodes */
    return null; 
  }

  private List<Node> retraceAndBuildPath(Node node)
  {
    ArrayList<Node> path = new ArrayList<>();
    Node current = node;
    while(originationMap.containsKey(current))
    {
      path.add(0, current);
      current = originationMap.get(current);
    }
    return path;
  }
  

}
