package lfbst;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeBST {
       
    public AtomicReference<Node> root;
    
    public LockFreeBST() {
		root = new AtomicReference<Node>( new Node(Integer.MAX_VALUE, new Node(Integer.MAX_VALUE - 1), new Node(Integer.MAX_VALUE)) );
    }
    
    private SearchInfo search(int key) {
		Node gp, p, l;
		Info gpUpdateInfo, pUpdateInfo;
		int gpState, pState;
		
		l = root.get();
		gp  = null;
		p = null;
		pState = 0;
		pUpdateInfo = null;
		gpState = 0;
		gpUpdateInfo = null;
		
		while(!l.isLeaf) {
			gp = p;
			p = l;
			gpState = pState;
			gpUpdateInfo = pUpdateInfo;
			pUpdateInfo = l.state.get(new int[1]);
			pState = l.state.getStamp();
			
			if(key < l.key) {
				l = p.left.get();
			}
			else {
				l = p.right.get();
			}
		}
		
		SearchInfo result = new SearchInfo();
		result.gp = gp;
		result.p = p;
		result.l = l;
		result.pUpdateInfo = pUpdateInfo;
		result.pState = pState;
		result.gpUpdateInfo = gpUpdateInfo;
		result.gpState = gpState;
		
		return result;

    }
    
    private class SearchInfo {
		public Node l = null;
		public Node p = null;
		public Node gp = null;
		public Info gpUpdateInfo, pUpdateInfo;
		public int gpState, pState;
    }
    
    public Node find(int key) {
    	SearchInfo leaf = search(key);

    	if(leaf.l.key == key) {
    		return leaf.l;
    	}
    	else {
    		return null;
    	}
    }

    public boolean insert(int key) {
	
		if (key == Integer.MAX_VALUE || key == Integer.MAX_VALUE - 1) {
		    return false;
		}
	
		Node p, newInternal, l, newSibling, newLeaf;
		Info pUpdateInfo;
		int pState;
		IInfo op;
		
		newLeaf = new Node(key);
		
		while(true) {
			SearchInfo searchResult = search(key);
			p = searchResult.p;
			l = searchResult.l;
			pUpdateInfo = searchResult.pUpdateInfo;
			pState = searchResult.pState;
			
			if(l.key == key) {
				return false;
			}
			
			if(pState != Node.CLEAN) {
				help(pUpdateInfo, pState);
			}
			else {
				newSibling = new Node(l.key);
				if(key > l.key) {
					newInternal = new Node(key, newSibling, newLeaf);
				}
				else {
					newInternal = new Node(l.key, newLeaf, newSibling);
				}
				op = new IInfo(p, l, newInternal);
				boolean result = p.state.compareAndSet(pUpdateInfo, op, pState, Node.IFLAG);
				if(result) {
					helpInsert(op);
					return true;
				}
				else {
					help(p.state.get(new int[1]), p.state.getStamp());
				}
			}
		}

    }
    
    private void helpInsert(IInfo op) {
    	CASChild(op.p, op.l, op.newInternal);
    	op.p.state.compareAndSet(op, op, Node.IFLAG, Node.CLEAN);
    }
    
    public boolean delete(int key) {
		if (key == Integer.MAX_VALUE || key == Integer.MAX_VALUE - 1) {
		    return false;
		}
	
		Node gp, p, l;
		Info pUpdateInfo, gpUpdateInfo;
		int pState, gpState;
		DInfo op;
		
		while(true) {
			SearchInfo searchResult = search(key);
			gp = searchResult.gp;
			p = searchResult.p;
			l = searchResult.l;
			pUpdateInfo = searchResult.pUpdateInfo;
			pState = searchResult.pState;
			gpUpdateInfo = searchResult.gpUpdateInfo;
			gpState = searchResult.gpState;
			
			if(l.key != key) {
				return false;
			}
			
			if(gpState != Node.CLEAN) {
				help(gpUpdateInfo, gpState);
			}
			else if(pState != Node.CLEAN) {
				help(pUpdateInfo, pState);
			}
			else {
				op = new DInfo(gp, p, l, pUpdateInfo, pState);
				boolean result = gp.state.compareAndSet(gpUpdateInfo, op, gpState, Node.DFLAG);
				if(result) {
					if(helpDelete(op)) {
						return true;
					}
					else {
						help(gp.state.get(new int[1]), gp.state.getStamp());
					}
				}
			}
		}

    }
    
    private boolean helpDelete(DInfo op) {

		boolean result = op.p.state.compareAndSet(op.pUpdateInfo, op, op.pState, Node.MARK);
		//boolean result = op.p.state.compareAndSet(op, op.pUpdateInfo, op.pState, Node.MARK);
		if(op.p.state.getStamp() == Node.MARK || result) {
			helpMarked(op);
			return true;
		}
		else {
			//int[] stamp = new int[1];
			help(op.p.state.get(new int[1]), Node.CLEAN);
			op.gp.state.compareAndSet(op, op, Node.DFLAG, Node.CLEAN);
			return false;
		}
    }
    
    private void helpMarked(DInfo op) {
		Node other;
		if (op.p.right.get() == op.l) {
		    other = op.p.left.get();
		}
		else {
		    other = op.p.right.get();
		}
	
		CASChild(op.gp, op.p, other);
		op.gp.state.compareAndSet(op, op, Node.DFLAG, Node.CLEAN);
    }

    private void help(Info info,int state) {
		if(state == Node.IFLAG) {
			helpInsert((IInfo)info);
		}
		else if(state == Node.MARK) {
			helpMarked((DInfo)info);
		}
		else if(state == Node.DFLAG) {
			helpDelete((DInfo)info);
		}
    }

    private void CASChild(Node parent, Node old, Node newNode) {
		if (newNode.key < parent.key) {
		    parent.left.compareAndSet(old,newNode);
		}
		else {
		    parent.right.compareAndSet(old,newNode);
		}
    }

   public boolean isBST() {
	   return root.get().isBST(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public void printTree() {
    	Node r = root.get().left.get();
    	if(r.isLeaf) {
    		System.out.println("empty");
    		return;
    	}
    	Queue<Node> q = new LinkedList<Node>();
    	q.add(r.left.get());
    	while(true) {
    		int nodeCount = q.size();
    		if(nodeCount == 0) {
    			break;
    		}
    		while(nodeCount > 0) {
    			Node n = q.peek();
    			System.out.printf(n.key + " ");
    			q.remove();
    			if(!n.isLeaf) {
    				q.add(n.left.get());
    			}
    			if(!n.isLeaf) {
    				q.add(n.right.get());
    			}
    			nodeCount = nodeCount - 1;
    		}
    		System.out.println();
    	}
    }

    public HashSet<Integer> getLeaves() {
    	return root.get().getLeaves();
    }

}

    
