package model.logic.Instrumentation.fileData;

import java.util.LinkedList;
import java.util.List;

public class LineContainer {

	private String line = "";
	private List<String> preLines = new LinkedList<String>();
	private List<String> postLines = new LinkedList<String>();
	private LineContainer nextLine = null;
	private LineContainer previousLine = null;
	private int lineNumber;

	public LineContainer(String line, int lineNumber) {
		this.setLine(line);
		this.lineNumber = lineNumber;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public List<String> getPreLines() {
		List<String> copyOfPreLines = new LinkedList<String>();
		for (String preLine : preLines) {
			copyOfPreLines.add(preLine);
		}
		return copyOfPreLines;
	}

	public void addPreLine(String preLine) {
		if (!preLines.contains(preLine)) {
			this.preLines.add(preLine);
		}
	}

	public List<String> getPostLines() {
		List<String> copyOfPostLines = new LinkedList<String>();
		for (String postLine : postLines) {
			copyOfPostLines.add(postLine);
		}
		return copyOfPostLines;
	}

	public void addPostLine(String postLine) {
		if (!postLines.contains(postLine)) {
			this.postLines.add(postLine);
		}
	}

	public String getFullLine() {
		String startMarkerBeforeStatement = "//Start Instrumentation before statement";
		String endMarkerBeforeStatement = "//End Instrumentation before statement";
		String startMarkerAfterStatement = "//Start Instrumentation after statement";
		String endMarkerAfterStatement = "//End Instrumentation after statement";
		StringBuilder allLinesAdded = new StringBuilder();
		if (preLines.size() > 0) {
			allLinesAdded.append(startMarkerBeforeStatement);
			allLinesAdded.append(System.lineSeparator());
			for (String preLine : preLines) {
				allLinesAdded.append(preLine);
				allLinesAdded.append(System.lineSeparator());
			}
			allLinesAdded.append(endMarkerBeforeStatement);
			allLinesAdded.append(System.lineSeparator());
		}
		allLinesAdded.append(line);
		if (postLines.size() > 0) {
			allLinesAdded.append(System.lineSeparator());
			allLinesAdded.append(startMarkerAfterStatement);
			allLinesAdded.append(System.lineSeparator());
			for (String postLine : postLines) {
				allLinesAdded.append(postLine);
				allLinesAdded.append(System.lineSeparator());
			}
			allLinesAdded.append(endMarkerAfterStatement);
		}
		return allLinesAdded.toString();
	}

	public LineContainer getNextLine() {
		return nextLine;
	}

	public void setNextLine(LineContainer nextLine) {
		this.nextLine = nextLine;
	}

	public LineContainer getPreviousLine() {
		return previousLine;
	}

	public void setPreviousLine(LineContainer previousLine) {
		this.previousLine = previousLine;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
