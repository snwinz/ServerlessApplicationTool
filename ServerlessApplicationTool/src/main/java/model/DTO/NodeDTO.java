package model.DTO;

import model.structure.Node;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

@XmlRootElement
@XmlSeeAlso({FunctionDTO.class, S3BucketDTO.class, DynamoDBDTO.class})
public class NodeDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     *
     */
    private String name = "";


    public NodeDTO() {
    }


    @XmlID
    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }


    public Node createNode() {
        Node node = new Node();
        node.setName(name);
        return node;
    }
}
