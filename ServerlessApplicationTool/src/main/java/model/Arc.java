package model;

public class Arc {
	private Node predecessor;
	private Node successor;
	private String multiplicity;
	private String condition;
	private dbAccessmode accessMode = dbAccessmode.NOTSET;
	private int order;
	private boolean synchronizedCall = false;

	public Arc(Node predecessor, Node successor) {
		this.predecessor = predecessor;
		this.successor = successor;
	}

	public Node getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}

	public Node getSuccessor() {
		return successor;
	}

	public void setSuccessor(Node successor) {
		this.successor = successor;
	}

	public String getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public dbAccessmode getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(dbAccessmode accessMode) {
		this.accessMode = accessMode;
	}

	public boolean isSynchronizedCall() {
		return synchronizedCall;
	}

	public void setSynchronizedCall(boolean synchronizedCall) {
		this.synchronizedCall = synchronizedCall;
	}

	public boolean isSimilar(Arc arcTmp) {
		return arcTmp.getOrder() - 1 == this.order && arcTmp.getPredecessor() == this.predecessor
				&& arcTmp.getSuccessor() == this.successor && arcTmp.getAccessMode() == this.accessMode
				&& arcTmp.isSynchronizedCall() == this.isSynchronizedCall();
	}

}
