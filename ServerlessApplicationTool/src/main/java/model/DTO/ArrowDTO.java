package model.DTO;

import model.structure.DBAccessMode;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ArrowDTO {

	private String predecessor = "";

	private String successor = "";;
	private String multiplicity = "";
	private String condition = "";
	private DBAccessMode accessMode = DBAccessMode.NOTSET;
	private int order = -1;
	private boolean synchronizedCall = false;

	private String name = "_:_";

	public ArrowDTO() {
	}

	public String getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(String predecessor) {
		this.predecessor = predecessor;
	}

	public String getSuccessor() {
		return successor;
	}

	public void setSuccessor(String successor) {
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

	public DBAccessMode getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(DBAccessMode accessMode) {
		this.accessMode = accessMode;
	}

	public boolean isSynchronizedCall() {
		return synchronizedCall;
	}

	public void setSynchronizedCall(boolean synchronizedCall) {
		this.synchronizedCall = synchronizedCall;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void updateName() {
		this.name = String.format("%s:%s", predecessor, successor);
	}

}
