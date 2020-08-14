package model.structure;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;



public class Arrow {

    private Node predecessor;

    private Node successor;
    private String multiplicity = "";
    private String condition = "";
    private DBAccessMode accessMode = DBAccessMode.NOTSET;
    private int order = -1;
    private boolean synchronizedCall = false;

    @XmlID
    private String arrowName;

    public Arrow() {
    }


    public Arrow(Node predecessor, Node successor) {
        this.predecessor = predecessor;
        this.successor = successor;
        this.arrowName = predecessor.getName() + ":" + successor.getName();
    }

    @XmlIDREF
    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    @XmlIDREF
    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        this.successor = successor;
    }

    private String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    private String getCondition() {
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

    public boolean isUnequal(Arrow arrowTmp) {
        return arrowTmp.getOrder() - 1 != this.order || arrowTmp.getPredecessor() != this.predecessor
                || arrowTmp.getSuccessor() != this.successor || arrowTmp.getAccessMode() != this.accessMode
                || arrowTmp.isSynchronizedCall() != this.isSynchronizedCall();
    }

    public String getName() {
        return this.arrowName;
    }

    public void setName(String name) {
        this.arrowName = name;
    }

    public String toString() {
        String result = "Name: " + arrowName + System.lineSeparator();
        result += "Predecessor: " + getPredecessor().getName() + System.lineSeparator();
        result += "Successor: " + getSuccessor().getName() + System.lineSeparator();

        if (getAccessMode() != DBAccessMode.NOTSET) {
            result += "AccessMode: " + getAccessMode() + System.lineSeparator();
        }
        if (isSynchronizedCall()) {
            result += "Synchronization: yes" + System.lineSeparator();
        }
        if (getOrder() != -1) {

            result += "Order: " + getOrder() + System.lineSeparator();
        }
        if (!getCondition().equals("")) {
            result += "Condition: " + getCondition() + System.lineSeparator();
        }
        if (getMultiplicity() != null) {
            result += "Multiplicity: " + getMultiplicity() + System.lineSeparator();
        }
        return result;

    }


}
