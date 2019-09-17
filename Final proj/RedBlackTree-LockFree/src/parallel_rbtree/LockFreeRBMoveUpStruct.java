package parallel_rbtree;

public class LockFreeRBMoveUpStruct {
	private RedBlackNode goalNode;
	private RedBlackNode pos1;
	private RedBlackNode pos2;
	private RedBlackNode pos3;
	private RedBlackNode pos4;
	
	LockFreeRBMoveUpStruct(RedBlackNode goalNode, RedBlackNode pos1, RedBlackNode pos2, RedBlackNode pos3, RedBlackNode pos4){
		this.setGoalNode(goalNode);
		this.setPos1(pos1);
		this.setPos2(pos2);
		this.setPos3(pos3);
		this.setPos4(pos4);
	}
	
	LockFreeRBMoveUpStruct(){
		this.setGoalNode(null);
		this.setPos1(null);
		this.setPos2(null);
		this.setPos3(null);
		this.setPos4(null);
	}

	public RedBlackNode getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(RedBlackNode goalNode) {
		this.goalNode = goalNode;
	}
	
	public RedBlackNode getPos1() {
		return pos1;
	}

	public void setPos1(RedBlackNode pos1) {
		this.pos1 = pos1;
	}
	public RedBlackNode getPos2() {
		return pos2;
	}

	public void setPos2(RedBlackNode pos2) {
		this.pos2 = pos2;
	}

	public RedBlackNode getPos3() {
		return pos3;
	}

	public void setPos3(RedBlackNode pos3) {
		this.pos3 = pos3;
	}

	public RedBlackNode getPos4() {
		return pos4;
	}

	public void setPos4(RedBlackNode pos4) {
		this.pos4 = pos4;
	}
	
	public boolean contains(RedBlackNode x){
		if((x == pos1) || (x == pos2) || (x == pos3) || (x == pos4)){
			return true;
		}
		return false;
	}
}
