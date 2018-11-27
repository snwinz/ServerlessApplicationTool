package model;

import java.util.LinkedList;
import java.util.List;

public class Graph {

	private List<Node> nodes = new LinkedList<Node>();

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public void addNode(Node node) {
		nodes.add(node);
	}

	public Node getNode(String nodeName) {
		for (Node node : nodes) {
			if (node.getName().equals(nodeName)) {
				return node;
			}
		}
		return null;
	}

	public S3Bucket getS3ByName(String dbName) {
		for (Node node : nodes) {
			if (node instanceof S3Bucket) {
				if (((S3Bucket) node).getBucketName().equals(dbName)) {
					return (S3Bucket) node;
				}
			}
		}

		return null;
	}

	public void print() {

		for (Node node : nodes) {
			System.out.println(node.toString() + System.lineSeparator());

		}

	}

	public Function getFunctionByHandler(String handlerName) {
		for (Node node : nodes) {
			if (node instanceof Function) {
				if (((Function) node).getHandler().endsWith(handlerName)) {
					return (Function) node;
				}
			}
		}

		return null;
	}

	public DynamoDB getDynamoDBbyName(String dbname) {
		for (Node node : nodes) {
			if (node instanceof DynamoDB) {
				if (((DynamoDB) node).getTableName().equals(dbname)) {
					return (DynamoDB) node;
				}
			}
		}

		return null;

	}

	public Function getFunctionByName(String functionName) {
		for (Node node : nodes) {
			if (node instanceof Function) {
				if (((Function) node).getFunctionName().equals(functionName)) {
					return (Function) node;
				}
			}
		}
		return null;
	}

}
