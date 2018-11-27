package model;

public class Function extends Node {

	private String runtime;
	private String handler;
	private String policies;
	private String functionName;

	public Function(String nameOfNode) {
		this.setName(nameOfNode);
	}

	public String getPolicies() {
		return policies;
	}

	public void setPolicies(String policies) {
		this.policies = policies;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += "Function name: " + this.functionName + System.lineSeparator();
		result += "Handler: " + this.handler + System.lineSeparator();
		result += "Runtime: " + this.runtime + System.lineSeparator();
		result += "Policies: " + this.policies + System.lineSeparator();
		return result;
	}

}
