package parallel_rbtree;

import java.util.concurrent.atomic.*;

public class RedBlackNode {
	private int value;
	private int marker;
	private RedBlackNode left;
	private RedBlackNode right;
	private RedBlackNode parent;
	private boolean isRed;
	public AtomicBoolean flag;

	public RedBlackNode() {
		this.value = Integer.MIN_VALUE;
		this.left = null;
		this.right = null;
		this.parent = null;
		this.isRed = false;
		this.flag = new AtomicBoolean(false);
	}

	public RedBlackNode(int value) {
		this.value = value;
		this.left = new RedBlackNode();
		this.right = new RedBlackNode();
		this.parent = null;
		this.isRed = true;
		this.flag = new AtomicBoolean(false);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public RedBlackNode getLeft() {
		return left;
	}

	public void setLeft(RedBlackNode left) {
		this.left = left;
	}

	public RedBlackNode getRight() {
		return right;
	}

	public void setRight(RedBlackNode right) {
		this.right = right;
	}

	public RedBlackNode getParent() {
		return parent;
	}

	public void setParent(RedBlackNode parent) {
		this.parent = parent;
	}
	
	public int getMarker() {
		return marker;
	}

	public void setMarker(int marker) {
		this.marker = marker;
	}

	public boolean isRed() {
		return isRed;
	}

	public void setRed(boolean isRed) {
		this.isRed = isRed;
	}

	public int height() {
		int leftHeight = 0, rightHeight = 0;
		if(this.getLeft() != null){
			leftHeight = this.getLeft().height();
		}
		if(this.getRight() != null){
			rightHeight = this.getRight().height();
		}
		return (1 + Math.max(leftHeight, rightHeight));
	}

	public void displayNode(RedBlackNode n) {
		if (n.getValue() != Integer.MIN_VALUE) {
			if (n.isRed())
				System.out.print(n.getValue() + "Red ");
			else
				System.out.print(n.getValue() + "Black ");
		}
	}


}
