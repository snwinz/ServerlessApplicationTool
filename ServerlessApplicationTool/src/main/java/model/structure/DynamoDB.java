package model.structure;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DynamoDB extends Node {
    private String tableName = "";

    public DynamoDB() {
    }

    public DynamoDB(String nameOfNode) {
        this.setName(nameOfNode);
    }

    public String getTableName() {
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

}
