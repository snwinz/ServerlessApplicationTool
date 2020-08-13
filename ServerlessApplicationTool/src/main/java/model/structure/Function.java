package model.structure;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;


@XmlRootElement
public class Function extends Node {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String runtime = "";
    private String handler = "";
    private String policies = "";
    private String functionName = "";
    private List<String> logs = new LinkedList<>();

    public Function() {


    }

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

    public void addLog(List<String> logs) {
        if (this.logs == null) {
            this.logs = logs;
        } else {
            this.logs.addAll(logs);
        }
    }

}
