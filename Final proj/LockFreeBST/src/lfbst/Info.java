package lfbst;

public abstract class Info {

    public Node p;
    public Node l;

    public Info(Node p, Node l) {
		this.p = p;
		this.l = l;
    }
}
