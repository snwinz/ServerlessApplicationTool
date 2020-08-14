package model.structure;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
@XmlSeeAlso({Function.class, S3Bucket.class, DynamoDB.class})
public class Node implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name = "";


    private List<Arrow> outgoingArrows = new LinkedList<>();

   
    private List<Arrow> ingoingArrows = new LinkedList<>();

    @XmlIDREF
    public List<Arrow> getIngoingArrows() {
		return ingoingArrows;
	}


	public Node() {
    }


    @XmlID
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Arrow> getOutgoingArrows() {
        return outgoingArrows;
    }

//    public List<Arrow> getIngoingArrows() {
//        return ingoingArrows;
//    }

    public void setOutgoingArrows(List<Arrow> outgoingArrows) {
        this.outgoingArrows = outgoingArrows;
    }

    public void setIngoingArrows(List<Arrow> ingoingArrows) {
        this.ingoingArrows = ingoingArrows;
    }

    public void addOutgoingArrow(Arrow arrow) {
        outgoingArrows.add(arrow);
        arrow.getSuccessor().addIngoingArrow(arrow);
    }

    public void addIngoingArrow(Arrow arrow) {
        ingoingArrows.add(arrow);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Name: " + name + System.lineSeparator());
        for (Arrow arrow : outgoingArrows) {
            result.append("Successor: ").append(arrow.getSuccessor().getName()).append(System.lineSeparator());
            result.append("Order: ").append(arrow.getOrder()).append(System.lineSeparator());
            if (arrow.getAccessMode() != DBAccessMode.NOTSET) {
                result.append("AccessMode: ").append(arrow.getAccessMode()).append(System.lineSeparator());
            }
            if (arrow.isSynchronizedCall()) {
                result.append("Synchronization: yes").append(System.lineSeparator());
            }
        }

        for (Arrow arrow : ingoingArrows) {
            result.append("Predecessor: ").append(arrow.getPredecessor().getName()).append(System.lineSeparator());
            result.append("Order: ").append(arrow.getOrder()).append(System.lineSeparator());
            if (arrow.getAccessMode() != DBAccessMode.NOTSET) {
                result.append("AccessMode: ").append(arrow.getAccessMode()).append(System.lineSeparator());
            }
            if (arrow.isSynchronizedCall()) {
                result.append("Synchronization: yes").append(System.lineSeparator());
            }
        }

        return result.toString();
    }

    public void removeOutgoingArrow(Arrow arrow) {
        arrow.getSuccessor().removeIngoingArrow(arrow);
        outgoingArrows.remove(arrow);

    }

    private void removeIngoingArrow(Arrow arrow) {
        ingoingArrows.remove(arrow);
    }

}
