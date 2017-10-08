package skeleton;
import graph.Node;

public class Police {

    private Node myNode;
    //private double speed;
    private Node goalNode;
    
	void Police(Node node, Node goalNode) {
		this.myNode=node;
		this.goalNode=goalNode;		
	}
	
	public Node currPosition()
	{
		return myNode;
	}
	public void updateGoalState(Node newNode)
	{
		this.goalState=newNode;
	}	
}
