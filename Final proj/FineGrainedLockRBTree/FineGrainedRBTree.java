import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FineGrainedRBTree {

    public  AtomicInteger size = new AtomicInteger(0);
    public FineGrainedRBNode root;

    public FineGrainedRBTree(){
        this.root=new FineGrainedRBNode();
    }

    public void LeftChild(FineGrainedRBNode parent, FineGrainedRBNode child){
        child.setParent(parent);
        parent.setLeft(child);
    }

    public void RightChild(FineGrainedRBNode parent, FineGrainedRBNode child){
        child.setParent(parent);
        parent.setRight(child);
    }

    public boolean Contains(int e){

        boolean found = false;
        FineGrainedRBNode node= root;
        //node.getReadLock().lock();

        while (!node.getReadLock().tryLock())
            node=root;

//        if(node.getValue()==Integer.MIN_VALUE) {
//            node.getReadLock().unlock();
//            return false;
//        }

        while ((!node.isLeaf())&&(!found)){
            FineGrainedRBNode parent= node;
            if(e==node.getValue())
                found=true;
            else if(e<node.getValue())
                node=node.getLeft();
            else
                node=node.getRight();
            node.getReadLock().lock();
            parent.getReadLock().unlock();
        }
        node.getReadLock().unlock();
        return found;
    }


    public FineGrainedRBNode find(int e){

        boolean found = false;
        FineGrainedRBNode node= root;
        //node.getReadLock().lock();

        while (!node.getReadLock().tryLock())
            node=root;

//        if(node.getValue()==Integer.MIN_VALUE) {
//            node.getReadLock().unlock();
//            return false;
//        }

        while ((!node.isLeaf())&&(!found)){
            FineGrainedRBNode parent= node;
            if(e==node.getValue())
                found=true;
            else if(e<node.getValue())
                node=node.getLeft();
            else
                node=node.getRight();
            node.getReadLock().lock();
            parent.getReadLock().unlock();
        }
        node.getReadLock().unlock();
        if(found)
            return node;
        return null;
    }
//    private void leftRotate(FineGrainedRBNode x) {
//        if (x == null)
//            return;
//        FineGrainedRBNode y = x.getRight(); // Check in our local area to see if
//        // two processes beneath use have
//
//        x.setRight(y.getLeft());
//        if (y.getLeft() != null) {
//            y.getLeft().setParent(x);
//        }
//        y.setParent(x.getParent());
//        if (x.getParent() == null)
//            this.root = y;
//        else {
//            if (x == x.getParent().getLeft())
//                x.getParent().setLeft(y);
//            else
//                x.getParent().setRight(y);
//        }
//        y.setLeft(x);
//        x.setParent(y);
//    }
//
//    private void rightRotate(FineGrainedRBNode y) {
//        if (y == null)
//            return;
//        FineGrainedRBNode x = y.getLeft();
//        y.setLeft(x.getRight());
//        if (x.getRight() != null) {
//            x.getRight().setParent(y);
//        }
//        x.setParent(y.getParent());
//        if (y.getParent() == null)
//            this.root = x;
//        else {
//            if (y == y.getParent().getLeft())
//                y.getParent().setLeft(x);
//            else
//                y.getParent().setRight(x);
//        }
//        x.setRight(y);
//        y.setParent(x);
//    }
//
//    public boolean delete(int value) {
//        FineGrainedRBNode z;
//        FineGrainedRBNode y;
//
//        z = this.root;
//        y = null;
////		while (!z.flag.compareAndSet(false, true));
//        while ((z.getValue() >= 0) && (z.getValue() != value)) { // Find insert
//            // point
//            y = z;
//            if (value < z.getValue()) {
//                z = z.getLeft();
//            } else {
//                z = z.getRight();
//            }
//            if (!z.flag.compareAndSet(false, true)) {
//                y.flag.set(false);
//                break;
//            }
//            if (z != null) {
//                y.flag.set(false);
//            }
//        }
//
//        if(z.getValue() != value)
//            return false;
//
//        if ((z.getLeft().getValue() == Integer.MIN_VALUE) || (z.getRight().getValue() == Integer.MIN_VALUE)) {
//            y = z;
//        } else {
//            y = findSuccessor(z);
//        }
////		if (!setupLocalAreaForDelete(y, z)) { // release flags
////			y.flag.set(false);
////			if (y != z)
////				z.flag.set(false);
////			return false;
////		}
//
//
//        FineGrainedRBNode x;
//        if(y.getLeft().getValue() != Integer.MIN_VALUE)
//            x = y.getLeft();
//        else
//            x = y.getRight();
//
//
//
//        //unlink y from tree
//        x.setParent(y.getParent());
//        if(y.getParent() == root.getParent())	// || y == root is new addition
//            root = x;
//        else{
//            if(y == y.getParent().getLeft())
//                y.getParent().setLeft(x);
//            else
//                y.getParent().setRight(x);
//        }
//
//        if(y != z){
//            z.setValue(y.getValue());
//            z.flag.set(false);
//        }
//        if(!y.isRed())
//            if(x.getValue() != Integer.MIN_VALUE)
//                RB_Delete_Fixup(x);
//            else{
//                // Release flags and markers held in local area. //This probably has a lot to deal with a lot.
//                y.flag.set(false);
//                z.flag.set(false);
//                x.flag.set(false);
//            }
//
//
//        return true;
//
//    }
//
//    private FineGrainedRBNode minValue(FineGrainedRBNode x){
//        FineGrainedRBNode current = x;
//
//        if(current.getLeft() == null)
//            return x;
//        while (current.getLeft().getValue() != Integer.MIN_VALUE) {
//            //if (current.getLeft().getValue() != Integer.MIN_VALUE)
//            current = current.getLeft();
//        }
//        return current;
//    }
//
//    private void RB_Delete_Fixup(FineGrainedRBNode x){
//        FineGrainedRBNode w;
//        while((x != root) && (!x.isRed())){
//            if(x == x.getParent().getLeft()){
//                w = x.getParent().getRight();
//                if(w.isRed()){		// Case 1
//                    w.setRed(false);
//                    x.getParent().setRed(true);
//                    leftRotate(x);
//                    w = x.getParent().getRight();
//                }
//                if(!w.getLeft().isRed() && !w.getRight().isRed()){ 		// Case 2
//                    w.setRed(true);
//                    x = x.getParent();					//TODO: maybe comment this out
//                }
//                else{
//                    if(!w.getRight().isRed()){		// Case 3
//                        w.getLeft().setRed(false);
//                        w.setRed(true);
//                        rightRotate(w);
//                        w = x.getParent().getRight();
//                    }
//                    w.setRed(x.getParent().isRed());		// Case 4
//                    x.getParent().setRed(false);
//                    w.getRight().setRed(false);
//                    leftRotate(x.getParent());
//                    x = root;
//                }
//            }
//            else{
//                w = x.getParent().getLeft();
//                if(w.isRed()){		// Case 1
//                    w.setRed(false);
//                    x.getParent().setRed(true);
//                    rightRotate(x);
//                    w = x.getParent().getLeft();
//                }
//                if(!w.getLeft().isRed() && !w.getRight().isRed()){ 		// Case 2
//                    w.setRed(true);
//                    x = x.getParent();
//                }
//                else{
//                    if(!w.getLeft().isRed()){		// Case 3
//                        w.getRight().setRed(false);
//                        w.setRed(true);
//                        leftRotate(w);
//                        w = x.getParent().getLeft();
//                    }
//                    w.setRed(x.getParent().isRed());		// Case 4
//                    x.getParent().setRed(false);
//                    w.getLeft().setRed(false);
//                    rightRotate(x.getParent());
//                    x = root;
//                }
//            }
//
//        }
//        x.setRed(false);
//    }
//
//    private FineGrainedRBNode findSuccessor(FineGrainedRBNode x){
//        // step 1 of the above algorithm
//        if ((x.getRight() != null) && (x.getRight().getValue() != Integer.MIN_VALUE)) { 		// Might need to change this to minValue.
//            return minValue(x.getRight());
//        }
//
//        // step 2 of the above algorithm
//        FineGrainedRBNode p = x.getParent();
//        while (p != null && x == p.getRight() && p.getValue() != Integer.MIN_VALUE) {
//            x = p;
//            p = p.getParent();
//        }
//        return p;
//    }
//




    public  boolean Add (int e){
        int mySize=size.getAndIncrement();
        boolean found = false;
        FineGrainedRBNode node = root;

        while (!node.getWriterExclusionLock().tryLock())
                node=root;

        //node.getWriterExclusionLock().lock();
        FineGrainedRBNode locked = node;
        //FineGrainedRBNode tempParent = null;

//        if(node.getValue()==Integer.MIN_VALUE){
//            node.getWriteLock().lock();
//            node.setValue(e);
//            node.setRed(false);
//            node.setParent(null);
//            node.setLeft(new FineGrainedRBNode());
//            node.setRight(new FineGrainedRBNode());
//            node.getWriteLock().unlock();
//            locked.getWriterExclusionLock().unlock();
//            return true;
//        }
//
//        if(node.getLeft().isLeaf()&&node.getRight().isLeaf()){
//            node.getWriteLock().lock();
//            if(node.getValue()==e) {
//                node.getWriteLock().unlock();
//                locked.getWriterExclusionLock().unlock();
//
//                return false;
//            }
//            else if(node.getValue()<e) {
//                node.setRight(new FineGrainedRBNode(e));
//                node.getRight().setParent(node);
//            }
//            else {
//                node.setLeft(new FineGrainedRBNode(e));
//                node.getLeft().setParent(node);
//            }
//            node.getWriteLock().unlock();
//            locked.getWriterExclusionLock().unlock();
//            return true;
//        }


        while ((!node.isLeaf())&&(!found)){
            FineGrainedRBNode parent= node;
            //tempParent=node;
            if(e==node.getValue())
                found=true;
            else if(e<node.getValue())
                node=node.getLeft();
            else
                node=node.getRight();
            if((!node.isRed())&&(!parent.isRed())&&(parent!=locked)){
                parent.getWriterExclusionLock().lock();
                locked.getWriterExclusionLock().unlock();
                locked=parent;
            }
        }
        if(!found){
            node.getWriteLock().lock();
            node.setRed(true);
            node.setValue(e);
            FineGrainedRBNode left = new FineGrainedRBNode();
            FineGrainedRBNode right = new FineGrainedRBNode();
            LeftChild(node,left);
            RightChild(node,right);
            //node.setParent(tempParent);
            //node.setRed(true);
            node.getWriteLock().unlock();

            while ((node!=root)&&node.getParent().isRed()){
                //&&(node.getParent().getParent()!=null)
                FineGrainedRBNode parent= node.getParent();
                FineGrainedRBNode grandParent= parent.getParent();

                if(grandParent==null)
                    System.out.println("Grand Parent null");

                if(grandParent.getLeft()==parent){
                    //parent is left child of grandparents
                    FineGrainedRBNode aunt = grandParent.getRight();

                    if(aunt.isRed()){
                        aunt.setRed(false);
                        parent.setRed(false);
                        grandParent.setRed(true);
                        node=grandParent;
                    }else if(parent.getLeft()==node){
                        //node is left child of parent
                        parent.setRed(false);
                        grandParent.setRed(true);
                        FineGrainedRBNode sister = parent.getRight();

                        if(grandParent==root){
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            sister.getWriteLock().lock();
                            root=parent;
                            //parent.setParent(null);
                            //parent.setRed(false);
                            RightChild(parent,grandParent);
                            LeftChild(grandParent,sister);
                            sister.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                        }else {
                            FineGrainedRBNode grandgrandparent = grandParent.getParent();
                            grandgrandparent.getWriteLock().lock();
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            sister.getWriteLock().lock();

                            if(grandgrandparent.getLeft()==grandParent)
                                LeftChild(grandgrandparent,parent);
                            else
                                RightChild(grandgrandparent,parent);
                            RightChild(parent,grandParent);
                            LeftChild(grandParent,sister);
                            sister.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                            grandgrandparent.getWriteLock().unlock();
                        }
                    }else {
                        //node is right child of parent
                        node.setRed(false);
                        grandParent.setRed(true);
                        left = node.getLeft();
                        right = node.getRight();
                        if(grandParent==root){
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            node.getWriteLock().lock();
                            left.getWriteLock().lock();
                            right.getWriteLock().lock();

                            root=node;
                            //node.setRed(false);
                            //node.setParent(null);
                            LeftChild(node,parent);
                            RightChild(node,grandParent);
                            RightChild(parent,left);
                            LeftChild(grandParent,right);

                            right.getWriteLock().unlock();
                            left.getWriteLock().unlock();
                            node.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                        }else {
                            FineGrainedRBNode grandgrandparent = grandParent.getParent();
                            grandgrandparent.getWriteLock().lock();
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            node.getWriteLock().lock();
                            left.getWriteLock().lock();
                            right.getWriteLock().lock();

                            if(grandgrandparent.getLeft()==grandParent)
                                LeftChild(grandgrandparent,node);
                            else
                                RightChild(grandgrandparent,node);

                            LeftChild(node,parent);
                            RightChild(node,grandParent);
                            RightChild(parent,left);
                            LeftChild(grandParent,right);
                            right.getWriteLock().unlock();
                            left.getWriteLock().unlock();
                            node.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                            grandgrandparent.getWriteLock().unlock();
                        }
                    }

                }else {
                    //parent is right child of grandparent
                    FineGrainedRBNode aunt = grandParent.getLeft();

                    if(aunt.isRed()){
                        aunt.setRed(false);
                        parent.setRed(false);
                        grandParent.setRed(true);
                        node=grandParent;
                    }else if(parent.getRight()==node){
                        //node is right child of parent
                        parent.setRed(false);
                        grandParent.setRed(true);
                        FineGrainedRBNode sister = parent.getLeft();

                        if(grandParent==root){
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            sister.getWriteLock().lock();
                            root=parent;
                            //parent.setParent(null);
                            //parent.setRed(false);
                            LeftChild(parent,grandParent);
                            RightChild(grandParent,sister);
                            sister.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                        }else {
                            FineGrainedRBNode grandgrandparent = grandParent.getParent();
                            grandgrandparent.getWriteLock().lock();
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            sister.getWriteLock().lock();

                            if(grandgrandparent.getLeft()==grandParent)
                                LeftChild(grandgrandparent,parent);
                            else
                                RightChild(grandgrandparent,parent);
                            LeftChild(parent,grandParent);
                            RightChild(grandParent,sister);

                            sister.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                            grandgrandparent.getWriteLock().unlock();
                        }
                    }else {
                        //node is left child of parent
                        node.setRed(false);
                        grandParent.setRed(true);
                        left = node.getLeft();
                        right = node.getRight();
                        if(grandParent==root){
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            node.getWriteLock().lock();
                            left.getWriteLock().lock();
                            right.getWriteLock().lock();

                            root=node;
                            //node.setParent(null);
                            //node.setRed(false);
                            RightChild(node,parent);
                            LeftChild(node,grandParent);
                            LeftChild(parent,right);
                            RightChild(grandParent,left);

                            right.getWriteLock().unlock();
                            left.getWriteLock().unlock();
                            node.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                        }else {
                            FineGrainedRBNode grandgrandparent = grandParent.getParent();
                            grandgrandparent.getWriteLock().lock();
                            grandParent.getWriteLock().lock();
                            parent.getWriteLock().lock();
                            node.getWriteLock().lock();
                            left.getWriteLock().lock();
                            right.getWriteLock().lock();

                            if(grandgrandparent.getLeft()==grandParent)
                                LeftChild(grandgrandparent,node);
                            else
                                RightChild(grandgrandparent,node);

                            RightChild(node,parent);
                            LeftChild(node,grandParent);
                            LeftChild(parent,right);
                            RightChild(grandParent,left);

                            right.getWriteLock().unlock();
                            left.getWriteLock().unlock();
                            node.getWriteLock().unlock();
                            parent.getWriteLock().unlock();
                            grandParent.getWriteLock().unlock();
                            grandgrandparent.getWriteLock().unlock();
                        }
                    }
                }
            }
        }
        root.setRed(false);
        root.setParent(null);
        locked.getWriterExclusionLock().unlock();
        return !found;

    }

    public int getheight(FineGrainedRBNode root) {
        if (root == null)
            return 0;
        return Math.max(getheight(root.getLeft()), getheight(root.getRight())) + 1;
    }


    public void preOrder(FineGrainedRBNode n ){

        if (n == null)
            return;
        //n.displayNode(n);
        preOrder(n.getLeft());
        preOrder(n.getRight());
    }

    public void breadth(FineGrainedRBNode n ){

        if (n == null)
            return;
        //n.displayNode(n);
        preOrder(n.getLeft());
        preOrder(n.getRight());
    }











    public synchronized boolean remove(int value){
        synchronized (FineGrainedRBTree.class) {
            FineGrainedRBNode curr = find(value);
            if (curr == null || curr.getValue() == Integer.MIN_VALUE) return false;

            //root.getWriterExclusionLock().lock();

            FineGrainedRBNode x, y;
            if (curr.getLeft().getValue() == Integer.MIN_VALUE || curr.getRight().getValue() == Integer.MIN_VALUE) {
                y = curr;
            } else {
                y = findMin(curr.getRight());
            }
            // y have left child
            if (y.getLeft().getValue() != Integer.MIN_VALUE) x = y.getLeft();
            else x = y.getRight();
            // exchange the parent
            x.setParent(y.getParent());
            if (y.getParent() == null) {
                root = x;
            } else {
                if (y == y.getParent().getLeft()) y.getParent().setLeft(x);
                else y.getParent().setRight(x);
            }

            if (y != curr) curr.setValue(y.getValue());
            if (!y.isRed()) {
                // Delete fix up
                deleteHelp(x);
            }

            //root.getWriterExclusionLock().unlock();

            return true;
        }
    }

    public FineGrainedRBNode findMin(FineGrainedRBNode node){
        if(node == null) return null;
        FineGrainedRBNode temp = node;
        while(temp.getLeft().getValue()!= Integer.MIN_VALUE){
            temp = temp.getLeft();
        }
        return temp;
    }

    public void deleteHelp(FineGrainedRBNode x){
        FineGrainedRBNode w;
        while(x != root && !x.isRed()){
            if(x == x.getParent().getLeft()){
                // is left child
                w = x.getParent().getRight();
                if(w.isRed()){
                    //case 1
                    w.setRed(false);
                    x.getParent().setRed(true);
                    leftRotate(x.getParent());
                    w = x.getParent().getRight();
                }
                if((!w.getLeft().isRed()) && (!w.getRight().isRed())){
                    //case 2
                    w.setRed(true);
                    x = x.getParent();
                }else{
                    if(!w.getRight().isRed()){
                        //case 3
                        w.getLeft().setRed(false);
                        w.setRed(true);
                        rightRotate(w);
                        w = x.getParent().getRight();
                    }
                    w.setRed( x.getParent().isRed()); // case 4
                    x.getParent().setRed(false);
                    w.getRight().setRed(false) ;
                    leftRotate(x.getParent());
                    x= root;
                }
            }else{
                // is right child
                w = x.getParent().getLeft();
                if(w.isRed()){
                    //case 1
                    w.setRed(false);
                    x.getParent().setRed(true);
                    rightRotate(x.getParent());
                    w = x.getParent().getLeft();
                }
                if((!w.getLeft().isRed()) && (!w.getRight().isRed())){
                    //case 2
                    w.setRed(true);
                    x = x.getParent();
                }else{
                    if(!w.getLeft().isRed()){
                        //case 3
                        w.getRight().setRed(false);
                        w.setRed(true);
                        leftRotate(w);
                        w = x.getParent().getLeft();
                    }
                    w.setRed(x.getParent().isRed()); // case 4
                    x.getParent().setRed(false);
                    w.getRight().setRed(false);
                    rightRotate(x.getParent());
                    x= root;
                }
            }
        }
        x.setRed(false);
    }
    public   void leftRotate(FineGrainedRBNode x){
        if(x == null) return;
        FineGrainedRBNode y = x.getRight();
        // Turn y's left sub-tree into x's right sub-tree
        x.setRight( y.getLeft());
        if(y.getLeft() != null){
            y.getLeft().setParent( x);
        }
        // y's new parent was x's parent
        y.setParent( x.getParent());
        // Set the parent to point to y instead of x
        // First see whether we are at the root
        if(x.getParent() == null) this.root = y;
        else{
            if(x == x.getParent().getLeft())
                x.getParent().setLeft(y);
            else
                x.getParent().setRight(  y);
        }
        // Finally, put x on y's left
        y.setLeft(x);
        x.setParent(y);
    }
    // symmetric to leftRotate 
    public void rightRotate(FineGrainedRBNode y){
        if(y == null) return;
        FineGrainedRBNode x = y.getLeft();
        y.setLeft(x.getRight());
        if(x.getRight() != null){
            x.getRight().setParent(y);
        }
        x.setParent(y.getParent());
        if(y.getParent() == null) this.root = x;
        else{
            if(y == y.getParent().getLeft())
                y.getParent().setLeft(x);
            else
                y.getParent().setRight(x);
        }
        x.setRight(y);
        y.setParent(x);
    }





}
