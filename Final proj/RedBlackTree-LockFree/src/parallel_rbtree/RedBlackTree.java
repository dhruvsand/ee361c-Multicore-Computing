package parallel_rbtree;

import java.util.ArrayList;

public class RedBlackTree implements RBTree {
	public int size = 0;
	public RedBlackNode root;
	public ThreadLocal<LockFreeRBMoveUpStruct> localMoveUpStruct;

	public RedBlackTree() {
		this.root = new RedBlackNode();
		localMoveUpStruct = new ThreadLocal<LockFreeRBMoveUpStruct>();
	}

	public int search(int value) {
		if (root == null) {
			return Integer.MIN_VALUE;
		}
		RedBlackNode temp = root;
		while (temp != null && temp.getValue() >= 0) {
			if (value < temp.getValue()) {
				temp = temp.getLeft();
			} else if (value > temp.getValue()) {
				temp = temp.getRight();
			} else {
				return temp.getValue();
			}
		}
		return temp == null ? null : temp.getValue();
	}

	public void insert(int value) throws NullPointerException {
		RedBlackNode insertedNode = new RedBlackNode(value);
		RedBlackNode temp1, temp2;
		insertedNode.flag.set(true);

		while (true) {
			temp1 = this.root;
			temp2 = null;
			while (!temp1.flag.compareAndSet(false, true));
			while (temp1.getValue() >= 0) { // Find insert point
				temp2 = temp1;
				if (value < temp1.getValue()) {
					temp1 = temp1.getLeft();
				} else {
					temp1 = temp1.getRight();
				}
				if (!temp1.flag.compareAndSet(false, true)) {
					temp2.flag.set(false);
					break;
				}
				if (temp1 != null) {
					temp2.flag.set(false);
				}
			}
			if (!setupLocalAreaForInsert(temp2)) {
				temp2.flag.set(false);
				continue;
			} else {
				break;
			}
		}

		insertedNode.setParent(temp2);
		if (temp2 == null) {
			this.root = insertedNode;
		} else if (value < temp2.getValue()) {
			temp2.setLeft(insertedNode);
		} else {
			temp2.setRight(insertedNode);
		}
		insertedNode.getLeft().setParent(insertedNode);
		insertedNode.getRight().setParent(insertedNode);
		insertedNode.setRed(true);
		rbInsertFixup(insertedNode);
	}

	public boolean delete(int value) {
		RedBlackNode z;
		RedBlackNode y;
		
		z = this.root;
		y = null;
//		while (!z.flag.compareAndSet(false, true));
		while ((z.getValue() >= 0) && (z.getValue() != value)) { // Find insert
																	// point
			y = z;
			if (value < z.getValue()) {
				z = z.getLeft();
			} else {
				z = z.getRight();
			}
			if (!z.flag.compareAndSet(false, true)) {
				y.flag.set(false);
				break;
			}
			if (z != null) {
				y.flag.set(false);
			}
		}

		if(z.getValue() != value)
			return false;
		
		if ((z.getLeft().getValue() == Integer.MIN_VALUE) || (z.getRight().getValue() == Integer.MIN_VALUE)) {
			y = z;
		} else {
			y = findSuccessor(z);
		}
//		if (!setupLocalAreaForDelete(y, z)) { // release flags
//			y.flag.set(false);
//			if (y != z)
//				z.flag.set(false);
//			return false;
//		}
		
				
		RedBlackNode x;
		if(y.getLeft().getValue() != Integer.MIN_VALUE)
			x = y.getLeft();	
		else
			x = y.getRight();
		
		
		
		//unlink y from tree
		x.setParent(y.getParent());
		if(y.getParent() == root.getParent())	// || y == root is new addition
			root = x;
		else{
			if(y == y.getParent().getLeft())
				y.getParent().setLeft(x);
			else
				y.getParent().setRight(x);
		}
			
		if(y != z){
			z.setValue(y.getValue());
			z.flag.set(false);
		}
		if(y.isRed() == false)
			if(x.getValue() != Integer.MIN_VALUE)
				RB_Delete_Fixup(x);
		else{
			// Release flags and markers held in local area. //This probably has a lot to deal with a lot.
			y.flag.set(false);
			z.flag.set(false);
			x.flag.set(false);
		}
		
		
		return true;
		
	}
	
	private void transplant(RedBlackNode target, RedBlackNode with){
		if(target.getParent() == null)
			root = with;
		else if(target == target.getParent().getLeft())
			target.getParent().setLeft(with);
		else
			target.getParent().setRight(with);
		with.setParent(target.getParent());
	}
	
	private RedBlackNode findSuccessor(RedBlackNode x){
		// step 1 of the above algorithm  
        if ((x.getRight() != null) && (x.getRight().getValue() != Integer.MIN_VALUE)) { 		// Might need to change this to minValue.
            return minValue(x.getRight()); 
        } 
  
        // step 2 of the above algorithm 
        RedBlackNode p = x.getParent(); 
        while (p != null && x == p.getRight() && p.getValue() != Integer.MIN_VALUE) { 
            x = p; 
            p = p.getParent(); 
        } 
        return p; 
	}
	/*
	 * Find minValue starting at this node.
	 */
	private RedBlackNode minValue(RedBlackNode x){
		RedBlackNode current = x;
		
		if(current.getLeft() == null)
			return x;
		while (current.getLeft().getValue() != Integer.MIN_VALUE) {
			//if (current.getLeft().getValue() != Integer.MIN_VALUE)
				current = current.getLeft();
		}
		return current;
	}
	
	private void RB_Delete_Fixup(RedBlackNode x){
		RedBlackNode w;
		while((x != root) && (x.isRed() == false)){
			if(x == x.getParent().getLeft()){
				w = x.getParent().getRight();
				if(w.isRed() == true){		// Case 1
					w.setRed(false);
					x.getParent().setRed(true);
					leftRotate(x);
					w = x.getParent().getRight();
				}
				if(!w.getLeft().isRed() && !w.getRight().isRed()){ 		// Case 2
					w.setRed(true);
					x = x.getParent();					//TODO: maybe comment this out
				}
				else{
					if(!w.getRight().isRed()){		// Case 3
						w.getLeft().setRed(false);
						w.setRed(true);
						rightRotate(w);
						w = x.getParent().getRight();
					}
					w.setRed(x.getParent().isRed());		// Case 4
					x.getParent().setRed(false);
					w.getRight().setRed(false);
					leftRotate(x.getParent());
					x = root;
				}
			}
			else{
				w = x.getParent().getLeft();
				if(w.isRed() == true){		// Case 1
					w.setRed(false);
					x.getParent().setRed(true);
					rightRotate(x);
					w = x.getParent().getLeft();
				}
				if(!w.getLeft().isRed() && !w.getRight().isRed()){ 		// Case 2
					w.setRed(true);
					x = x.getParent();
				}
				else{
					if(!w.getLeft().isRed()){		// Case 3
						w.getRight().setRed(false);
						w.setRed(true);
						leftRotate(w);
						w = x.getParent().getLeft();
					}
					w.setRed(x.getParent().isRed());		// Case 4
					x.getParent().setRed(false);
					w.getLeft().setRed(false);
					rightRotate(x.getParent());
					x = root;
				}
			}
			
		}
		x.setRed(false);
	}
	
	private boolean setupLocalAreaForDelete(RedBlackNode y, RedBlackNode z){
		RedBlackNode x;
		
		if(y.getLeft() != null)
			x = y.getLeft();
		else
			x = y.getRight();
		if(x == null)
			return true;
		
		//TODO: Fix this CAS.
		// Try to get flags for the rest of the local area
//		if(!x.flag.compareAndSet(false, true) && x == z)
//			return false;
//		
		RedBlackNode parent;
		parent = y.getParent();
		if(parent == null)
			return true;
		if((parent != z) && !parent.flag.compareAndSet(false, true)){
			x.flag.set(false);
			return false;
		}
		
		RedBlackNode w;
		if(y == y.getParent().getLeft())
			w = y.getParent().getRight();
		else
			w = y.getParent().getLeft();
		if(!w.flag.compareAndSet(false, true)){
			x.flag.set(false);
			if(parent != z)
				parent.flag.set(false);
		}
		RedBlackNode wlc;
		RedBlackNode wrc;
		if(w != null){
			wlc = w.getLeft();
			wrc = w.getRight();
			if(wlc != null && !wlc.flag.compareAndSet(false, true)){
				x.flag.set(false);
				w.flag.set(false);
				if(parent != z)
					parent.flag.set(false);
				return false;
			}
			if(wrc != null && !wrc.flag.compareAndSet(false, true)){
				x.flag.set(false);
				w.flag.set(false);
				if(parent != z)
					parent.flag.set(false);
				return false;
				
			}
		}
		
//		if(!GetFlagsAndMarkersAbove(parent, z)){
//			x.flag.set(false);
//			w.flag.set(false);
//			wlc.flag.set(false);
//			wrc.flag.set(false);
//			if(parent != z)
//				parent.flag.set(false);
//			return false;
//		}
		
		return true;
	}

	/*
	 * setupLocalAreaForInsert: Acquires flags for flags in local area of x and
	 * set markers on four nodes above insertion point.
	 * 
	 * Inputs: RedBlackNode x - Node that will be parent of inserted node.
	 * 
	 * Outputs: 
	 * 	true - Able to properly setupLocalAreaForInsert. 
	 * 	false - Not able.
	 */
	private boolean setupLocalAreaForInsert(RedBlackNode x) {
		if (x == null) {
			return true;
		}
		RedBlackNode parent = x.getParent();
		RedBlackNode uncle;
		if (parent == null)
			return true;
		if (!x.flag.compareAndSet(false, true)) {
			return false;
		}
		if (!parent.flag.compareAndSet(false, true)) {
			return false;
		}
		if (parent != x.getParent()) {
			parent.flag.set(false);
			return false;
		}
		if (x == x.getParent().getLeft()) {
			uncle = x.getParent().getRight();
		} else {
			uncle = x.getParent().getLeft();
		}
		if (uncle != null && !uncle.flag.compareAndSet(false, true)) {
			x.getParent().flag.set(false);
			return false;
		}

		// Now try to get the four intention markers above p[z].
		// The second argument is useful only for deletes so we pass z
		// which is not an ancestor of x.parent and will have no efffect.
//		if (!GetFlagsAndMarkersAbove(parent, 0)) {
//			parent.flag.set(false);
//			uncle.flag.set(false);		
//			return false;
//		}

		return true;
	}

	private void rbInsertFixup(RedBlackNode x) {
		RedBlackNode temp, parent, uncle = null, gradparent = null;
		parent = x.getParent();
		ArrayList<RedBlackNode> local_area = new ArrayList<RedBlackNode>();
		local_area.add(x);
		local_area.add(parent);

		if (parent != null) {
			gradparent = parent.getParent();
		}

		if (gradparent != null) {
			if (gradparent.getLeft() == parent) {
				uncle = gradparent.getRight();
			} else {
				uncle = gradparent.getLeft();
			}
		}

		local_area.add(uncle);
		local_area.add(gradparent);

		while (x.getParent() != null && x.getParent().isRed()) {
			parent = x.getParent();
			gradparent = parent.getParent();

			if (x.getParent() == x.getParent().getParent().getLeft()) {
				temp = x.getParent().getParent().getRight();
				uncle = temp;
				local_area.add(x);
				local_area.add(parent);
				local_area.add(gradparent);
				local_area.add(uncle);

				if (temp.isRed()) {
					x.getParent().setRed(false);
					temp.setRed(false);
					x.getParent().getParent().setRed(true);
					x = moveLocalAreaUpward(x, local_area);
				} else {
					if (x == x.getParent().getRight()) {
						// Case 2
						x = x.getParent();
						leftRotate(x);
					}
					// Case 3
					x.getParent().setRed(false);
					x.getParent().getParent().setRed(true);
					rightRotate(x.getParent().getParent());
				}
			} else {
				temp = x.getParent().getParent().getLeft();
				uncle = temp;

				local_area.add(x);
				local_area.add(parent);
				local_area.add(gradparent);
				local_area.add(uncle);

				if (temp.isRed()) {
					// Case 1
					x.getParent().setRed(false);
					temp.setRed(false);
					x.getParent().getParent().setRed(true);
					x = moveLocalAreaUpward(x, local_area);
				} else {
					if (x == x.getParent().getLeft()) {
						// Case 2
						x = x.getParent();
						rightRotate(x);
					}
					// Case 3
					x.getParent().setRed(false);
					x.getParent().getParent().setRed(true);
					leftRotate(x.getParent().getParent());
				}
			}
		}

		this.root.setRed(false);

		for (RedBlackNode node : local_area) {
			if (node != null)
				node.flag.set(false);
		}
	}

	
	private RedBlackNode moveLocalAreaUpward(RedBlackNode x, ArrayList<RedBlackNode> working) {
		// Check for a moveUpStruct from another process (due to Move-Up rule)
		// Get direct pointers
		RedBlackNode parent = x.getParent();
		RedBlackNode grandparent = parent.getParent();
		RedBlackNode uncle;
		if (parent == grandparent.getLeft()) {
			uncle = grandparent.getRight();
		} else {
			uncle = grandparent.getLeft();
		}

		// Extend intention markers (getting flags to set them)
		// From oldgp to top and two more. Also convert markers on 
		// oldggp and oldgggp to flags.
		//while(!GetFlagsAndMarkersAbove(grandparent, 0));
		
		// Get flags on new local area
		RedBlackNode updated_x, updated_parent = null, updated_uncle = null, updated_grandparent = null;
		updated_x = grandparent;
		while (true && updated_x.getParent() != null) {
			updated_parent = updated_x.getParent();
			if (!updated_parent.flag.compareAndSet(false, true)) {
				continue;
			}
			updated_grandparent = updated_parent.getParent();
			if (updated_grandparent == null)
				break;
			if (!updated_grandparent.flag.compareAndSet(false, true)) {
				updated_parent.flag.set(false);
				continue;
			}
			if (updated_parent == updated_grandparent.getLeft()) {
				updated_uncle = updated_grandparent.getRight();
			} else {
				updated_uncle = updated_grandparent.getLeft();
			}

			if (updated_uncle != null && !updated_uncle.flag.compareAndSet(false, true)) {
				updated_grandparent.flag.set(false);
				updated_parent.flag.set(false);
				continue;
			}
			break;
		}

		working.add(updated_x);
		working.add(updated_parent);
		working.add(updated_grandparent);
		working.add(updated_uncle);

		/*
		 * if(moveUpStruct.contains(updated_uncle)
		 * 		while(!updated_uncle.flag.compareandset(false, true));
		 * //Release flags on old local area
		 */
//		if(localMoveUpStruct.get().contains(updated_uncle))
//			while(updated_uncle.flag.compareAndSet(false, true));
//		ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//		temp.add(x);
//		temp.add(parent);
//		temp.add(uncle);
//		temp.add(grandparent);
//		ReleaseFlags(localMoveUpStruct.get(), true, temp);
//		 
		return updated_x;
	}

	/*
	 * GetFlagsAndMarkersAbove: Responsible for acquiring additional markers as
	 * a process moves up the tree. First acquires flags on nodes corresponding
	 * to each of the four held intention markers. Then, attempt to add
	 * additional markers. If any flag/marker acquisition fails, back off and
	 * try again.
	 * 
	 * Inputs:
	 * 
	 * RedBlackNode start - Node to start looking at flags and markers above.
	 * int numAdditional - # of additional nodes to get flags and markers. 1 for
	 * deletion, two for insertion.
	 * 
	 */
//	private boolean GetFlagsAndMarkersAbove(RedBlackNode start, int numAdditional) {
//		// Check for a moveUpStruct provided by another process (due to Move-Up
//		// rule processing)
//		// and set 'PIDtoIgnore' to the PID provided in that structure.
//		// Use the 'IsIn' function to determine if a node is in the
//		// moveUpStruct.
//		// Start by getting flags on the four nodes we have markers on
//
//		ArrayList<RedBlackNode> GetFlagsForMarkersArray = new ArrayList<RedBlackNode>();
//		int PIDtoIgnore = 0;
//
//		if (!GetFlagsForMarkers(start, this.localMoveUpStruct.get(), GetFlagsForMarkersArray))
//			return false;
//
//		// Now get additional marker(s) above
//		if (numAdditional != 0) {
//			RedBlackNode firstNew = GetFlagsForMarkersArray.get(3).getParent();
//			ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//			temp.add(firstNew);
//			temp.addAll(GetFlagsForMarkersArray);
//			if (!this.localMoveUpStruct.get().contains(firstNew) && (firstNew.flag.compareAndSet(false, true))) {
//				ReleaseFlags(this.localMoveUpStruct.get(), false, temp);
//				return false;
//			}
//
//			if ((firstNew != GetFlagsForMarkersArray.get(3).getParent()) &&
//					!SpacingRuleIsSatisified(firstNew, start, PIDtoIgnore, this.localMoveUpStruct.get())) {
//				ReleaseFlags(this.localMoveUpStruct.get(), false, temp);
//				return false;
//			}
//
//			RedBlackNode secondNew = new RedBlackNode();
//			if (numAdditional == 2) {
//				secondNew = firstNew.getParent();
//				if ((!this.localMoveUpStruct.get().contains(secondNew)) && secondNew.flag.compareAndSet(false, true)) {
//					ReleaseFlags(this.localMoveUpStruct.get(), false, temp);
//					return false;
//				}
//				if ((secondNew != firstNew.getParent()) &&
//						(!SpacingRuleIsSatisified(firstNew, start, PIDtoIgnore, this.localMoveUpStruct.get()))) { 
//					temp.add(secondNew);
//					ReleaseFlags(this.localMoveUpStruct.get(), false, temp);
//					return false;
//				}
//			}
//			firstNew.setMarker((int) Thread.currentThread().getId());
//			
//			if (numAdditional == 2)
//				secondNew.setMarker((int) Thread.currentThread().getId());
//			
//			// Release the four topmost flags acquired to extend markers.
//			// This leaves flags on nodes now in the new local area.
//			if (numAdditional == 2) {
//				ArrayList<RedBlackNode> temp2 = new ArrayList<RedBlackNode>();
//				temp2.add(secondNew);
//				ReleaseFlags(this.localMoveUpStruct.get(), true, temp2);
//			}
//			temp.clear();
//			temp.add(firstNew);
//			temp.add(GetFlagsForMarkersArray.get(3));
//			temp.add(GetFlagsForMarkersArray.get(2));
//			ReleaseFlags(this.localMoveUpStruct.get(), true, temp);
//			
//			if (numAdditional == 1) {
//				temp.clear();
//				temp.add(GetFlagsForMarkersArray.get(1));
//				ReleaseFlags(this.localMoveUpStruct.get(), true, temp);
//			}
//		}
//		return true;
//	}
//
//	/*
//	 * GetFlagsForMarkers: Used to get flags on existing marked nodes.
//	 *
//	 * Inputs: RedBlackNode start - Start node LockFreeRBMoveUpStruct
//	 * moveUpStruct - MoveUpStruct provided (from reference of start node?)
//	 * RedBlackNode pos1-4 - Nodes to be referenced and passed back with flags
//	 * set.
//	 *
//	 * Output: true - able to get all available node flags. false - not able
//	 * 
//	 */
//	private boolean GetFlagsForMarkers(RedBlackNode start, LockFreeRBMoveUpStruct moveUpStruct, ArrayList<RedBlackNode> working){
//		RedBlackNode pos1 = start.getParent();
//		if (pos1 == null)
//			return true;
//		if (!moveUpStruct.contains(pos1) && (!pos1.flag.compareAndSet(false, true)))
//			return false;
//		if (pos1 != start.getParent()) { // verify that parent is unchanged
//			ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//			temp.add(pos1);
//			ReleaseFlags(moveUpStruct, false, temp);
//			return false;
//		}
//		working.add(pos1);
//		
//		RedBlackNode pos2 = pos1.getParent();
//		if (pos2 == null)
//			return true;
//		if (!moveUpStruct.contains(pos2) && (pos2.flag.compareAndSet(false, true))) {
//			pos1.flag.set(false);
//			return false;
//		}
//		if (pos2 != pos1.getParent()) { // verify that parent is unchanged
//			ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//			temp.add(pos1);
//			temp.add(pos2);
//			ReleaseFlags(moveUpStruct, false, temp);
//			return false;
//		}
//		working.add(pos2);
//
//		RedBlackNode pos3 = pos2.getParent();
//		if (pos3 == null)
//			return true;
//		if (!moveUpStruct.contains(pos3) && (pos1.flag.compareAndSet(false, true))) {
//			pos1.flag.set(false);
//			pos2.flag.set(false);
//			return false;
//		}
//		if (pos3 != pos2.getParent()) { // verify that parent is unchanged
//			ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//			temp.add(pos1);
//			temp.add(pos2);
//			temp.add(pos3);
//			ReleaseFlags(moveUpStruct, false, temp);
//			return false;
//		}
//		working.add(pos3);
//
//		RedBlackNode pos4 = pos3.getParent();
//		if (pos4 == null)
//			return true;
//		if (!moveUpStruct.contains(pos4) && (pos1.flag.compareAndSet(false, true))) {
//			pos1.flag.set(false);
//			pos2.flag.set(false);
//			pos3.flag.set(false);
//			return false;
//		}
//		if (pos4 != pos3.getParent()) { // verify that parent is unchanged
//			ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//			temp.add(pos1);
//			temp.add(pos2);
//			temp.add(pos3);
//			temp.add(pos4);
//			ReleaseFlags(moveUpStruct, false, temp);
//			return false;
//		}
//		working.add(pos4);
//		
//		return true;
//	}
//
//	/*
//	 * ReleaseFlags: Routine called to release flags either due to rollback
//	 * (!success) or completion of marker acquisition.
//	 * 
//	 * Inputs: LockFreeRBMoveUpStruct lockFreeRBMoveUpStruct - MoveUpStruct
//	 * provided by (?) that checks if node is present in moveupstruct and will
//	 * delete if node is not present or if goalnode is present.
//	 * 
//	 * boolean success - Indicates whether a rollback has occured.
//	 *
//	 * ArrayList<RedBlackNode> nodesToRelease - List of nodes whose flags are
//	 * to be released.
//	 *
//	 * TODO: Change lockFreeRBMoveUpStruct -> List<LockFreeRBMoveUpStruct>
//	 */
//	private void ReleaseFlags(LockFreeRBMoveUpStruct lockFreeRBMoveUpStruct, boolean success,
//			ArrayList<RedBlackNode> nodesToRelease) {
//		// Release flags identified in nodesToRelease
//		for (RedBlackNode node : nodesToRelease) {
//			if (success) { // Release flag after successfully moving up
//				if (!localMoveUpStruct.get().contains(node))
//					node.flag.set(false);
//				else { // Node is in the inherited local area
//					if (localMoveUpStruct.get().getGoalNode() == node) {
//						// release unneeded flags in moveUpStruct
//						localMoveUpStruct.get().getPos1().flag.set(false);
//						localMoveUpStruct.get().getPos2().flag.set(false);
//						localMoveUpStruct.get().getPos3().flag.set(false);
//						localMoveUpStruct.get().getPos4().flag.set(false);
//						// and discard moveUpStruct
//						localMoveUpStruct.remove();
//
//					}
//				}
//			} else { // Release flag after failing to move up
//				if (!localMoveUpStruct.get().contains(node))
//					node.flag.set(false);
//			}
//		}
//	}
//
//	// Used only for deletion
//	private boolean ApplyMoveupRule(RedBlackNode x, RedBlackNode w) {
//		// Check in our local area to see if two processes beneath use have
//		// been brought too close together by our rotations.
//
//		if (((w.getMarker() == w.getParent().getMarker()) && (w.getMarker() == w.getRight().getMarker())
//				&& (w.getMarker() != 0) && (w.getLeft().getMarker() != 0))
//
//				|| ((w.getMarker() == w.getRight().getMarker()) && (w.getMarker() != 0)
//						&& (w.getLeft().getMarker() != 0))
//
//				|| ((w.getMarker() == 0) && (w.getLeft().getMarker() != 0))) {
//			// Build structure listing the nodes we hold flags on
//			// (moveUpStruct) and specificying the PID of the other
//			// "too-close" process (marker[left[w]] and the goal (GP).
//			// Make structure available to process id: marker[right[w]].
//			LockFreeRBMoveUpStruct moveUpStruct;
//
//			return true;
//		}
//
//		return false;
//	}
//
//	private boolean SpacingRuleIsSatisified(RedBlackNode t, RedBlackNode z, int PIDtoIgnore, LockFreeRBMoveUpStruct moveUpStruct) {
//		// We hold flags on both t and z.
//		// Check that t has no marker set
//		if (t != z) {
//			if (t.getMarker() != 0) {
//				return false;
//			}
//		}
//		// check that t's parent has no flag or marker
//		RedBlackNode tParent = t.getParent();
//		if (tParent != z) {
//			if (!moveUpStruct.contains(tParent) && tParent.flag.compareAndSet(false, true)) {
//				return false;
//			}
//			if (tParent != t.getParent()) {
//				tParent.flag.set(false);
//				return false;
//			}
//			if (tParent.getMarker() != 0) {
//				tParent.flag.set(false);
//			}
//		}
//
//		// check that t's sibling has no flag or marker
//		RedBlackNode tSibling;
//		if (t == tParent.getLeft())
//			tSibling = tParent.getRight();
//		else
//			tSibling = tParent.getLeft();
//		ArrayList<RedBlackNode> temp = new ArrayList<RedBlackNode>();
//		if ((!moveUpStruct.contains(tSibling)) && (tSibling.flag.compareAndSet(false, true))) {
//			if (tParent != z) {
//				temp.add(tParent);
//				ReleaseFlags(moveUpStruct, false, temp);
//			}
//			return false;
//		}
//		if ((tSibling.getMarker() != 0) && (tSibling.getMarker() != PIDtoIgnore)) {
//			temp.clear();
//			temp.add(tSibling);
//			ReleaseFlags(moveUpStruct, false, temp);
//			if (tParent != z) {
//				temp.clear();
//				temp.add(tParent);
//				ReleaseFlags(moveUpStruct, false, temp);
//			}
//		}
//		if (tParent != z) {
//			temp.clear();
//			temp.add(tParent);
//			ReleaseFlags(moveUpStruct, false, temp);
//		}
//		temp.clear();
//		temp.add(tSibling);
//		ReleaseFlags(moveUpStruct, false, temp);
//
//		return true;
//	}

	private void leftRotate(RedBlackNode x) {
		if (x == null)
			return;
		RedBlackNode y = x.getRight(); // Check in our local area to see if
											// two processes beneath use have

		x.setRight(y.getLeft());
		if (y.getLeft() != null) {
			y.getLeft().setParent(x);
		}
		y.setParent(x.getParent());
		if (x.getParent() == null)
			this.root = y;
		else {
			if (x == x.getParent().getLeft())
				x.getParent().setLeft(y);
			else
				x.getParent().setRight(y);
		}
		y.setLeft(x);
		x.setParent(y);
	}

	private void rightRotate(RedBlackNode y) {
		if (y == null)
			return;
		RedBlackNode x = y.getLeft();
		y.setLeft(x.getRight());
		if (x.getRight() != null) {
			x.getRight().setParent(y);
		}
		x.setParent(y.getParent());
		if (y.getParent() == null)
			this.root = x;
		else {
			if (y == y.getParent().getLeft())
				y.getParent().setLeft(x);
			else
				y.getParent().setRight(x);
		}
		x.setRight(y);
		y.setParent(x);
	}

	public int getheight(RedBlackNode root) {
		if (root == null)
			return 0;
		return Math.max(getheight(root.getLeft()), getheight(root.getRight())) + 1;
	}

	public void inOrder(RedBlackNode n) {
		if (n == null)
			return;

		inOrder(n.getLeft());
		n.displayNode(n);
		inOrder(n.getRight());
	}

	public void preOrder(RedBlackNode n) {
		if (n == null)
			return;

		n.displayNode(n);
		preOrder(n.getLeft());
		preOrder(n.getRight());
	}

	public void postOrder(RedBlackNode n) {

		if (n == null)
			return;

		postOrder(n.getLeft());
		postOrder(n.getRight());
		n.displayNode(n);
	}

	public void printLevelOrder(RedBlackNode n) {
		for (int i = 1; i < n.height(); i++) {
			System.out.print("\nLevel" + i + ": ");
			printGivenLevel(n, i);
		}
	}

	private void printGivenLevel(RedBlackNode n, int level) {
		if (n == null)
			return;
		if (level == 1)
			n.displayNode(n);
		else {
			printGivenLevel(n.getLeft(), level - 1);
			printGivenLevel(n.getRight(), level - 1);
		}
	}

}
