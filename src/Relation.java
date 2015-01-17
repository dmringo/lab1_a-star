public enum Relation
{
  ABOVE ("U"),
  BELOW ("D"),
  LEFT_OF ("L"),
  RIGHT_OF ("R"),
  NOT_ADJASCENT_TO ("Not adjacent");
  
  private final String directionString;
  
  Relation(String directionString)
  {
    this.directionString = directionString;
  }
  
  public String toString()
  {
    return directionString;
  }
}