package model.DTO;

import model.structure.Function;
import model.structure.Node;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FunctionDTO extends NodeDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String runtime = "";
	private String handler = "";
	private String policies = "";
	private String functionName = "";
	// private List<String> logs = new LinkedList<>();

	public FunctionDTO() {

	}

	private String getPolicies() {
		return policies;
	}

	public void setPolicies(String policies) {
		this.policies = policies;
	}

	private String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	private String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	private String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Override
	public Node createNode() {
		Function node = new Function();
		node.setName(getName());
		node.setPolicies(getPolicies());
		node.setHandler(getHandler());
		node.setFunctionName(getFunctionName());
		node.setRuntime(getRuntime());
		node.setFunctionName(getFunctionName());
		return node;
	}

}
