package model.DTO;

import model.structure.DynamoDB;
import model.structure.Node;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DynamoDBDTO extends NodeDTO {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableName = "";

    public DynamoDBDTO() {
    }

    public DynamoDBDTO(String nameOfNode) {
        this.setName(nameOfNode);
    }

    private String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        String result = super.toString();
        result += "Table name: " + this.tableName + System.lineSeparator();
        return result;
    }

    @Override
    public Node createNode() {
        DynamoDB node = new DynamoDB();
        node.setName(getName());
        node.setTableName(getTableName());
        return node;
    }


}
