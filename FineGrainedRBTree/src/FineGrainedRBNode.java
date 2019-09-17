import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FineGrainedRBNode {

    private int value;
    private FineGrainedRBNode left;
    private FineGrainedRBNode right;
    private FineGrainedRBNode parent;
    private boolean isRed;
    private ReadWriteLock readWriteLock;
    private Lock writerExclusion;


    public FineGrainedRBNode(){
        this.value = Integer.MIN_VALUE;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.isRed = false;
        readWriteLock = new ReentrantReadWriteLock();
        writerExclusion = new ReentrantLock();
    }

    public boolean isLeaf(){
        return ((this.left==null)&&(this.right)==null);
    }

    public FineGrainedRBNode(int value){
        this.value = value;
        this.left = new FineGrainedRBNode();
        this.right = new FineGrainedRBNode();
        this.parent = null;
        this.isRed = true;
        readWriteLock = new ReentrantReadWriteLock();
        writerExclusion = new ReentrantLock();

    }

    public Lock getReadLock(){
        return readWriteLock.readLock();
    }

    public Lock getWriteLock(){
        return readWriteLock.writeLock();
    }

    public Lock getWriterExclusionLock(){
        return writerExclusion;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public FineGrainedRBNode getLeft() {
        return left;
    }

    public void setLeft(FineGrainedRBNode left) {
        this.left = left;
    }

    public FineGrainedRBNode getRight() {
        return right;
    }

    public void setRight(FineGrainedRBNode right) {
        this.right = right;
    }

    public FineGrainedRBNode getParent() {
        return parent;
    }

    public void setParent(FineGrainedRBNode parent) {
        this.parent = parent;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }
    public void displayNode(FineGrainedRBNode n) {
        System.out.print(n.getValue() + " ");
    }
    
}
