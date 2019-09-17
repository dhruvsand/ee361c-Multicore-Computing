package lfbst;

public class DInfo extends Info{

	public Node gp;
	public Info pUpdateInfo;
	public int pState;

    public DInfo(Node gp, Node p, Node l, Info pInfo, int pState) {
		super(p,l);
		this.gp = gp;
		this.pUpdateInfo = pInfo;
		this.pState = pState;
    }
}
    
    
