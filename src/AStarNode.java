import java.awt.geom.Point2D;
import java.util.List;

public interface AStarNode
{
  double heuristicDistance(AStarNode node);
  
  double getCost();

  boolean isTraversable();
  
  Point2D getLocation();
  
  List<? extends AStarNode> getNeighbors();

}