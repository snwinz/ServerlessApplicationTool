package model.logic.Instrumentation.fileData;

import java.util.LinkedList;
import java.util.List;

public class SourceFile {

	public final static String[] accessPatternsDBWrite = { ".putItem(", ".deleteItem(" };

	public final static String[] writePatternsDB = { ".putItem(", ".deleteItem(" };
	public final static String[] readPatternsDB = { ".getItem(" };

	private List<LineContainer> allLines = new LinkedList<LineContainer>();

	public List<LineContainer> getAllLines() {
		return allLines;
	}

	public void setAllLines(List<LineContainer> allLines) {
		this.allLines = allLines;
	}

	public SourceFile(List<String> allLinesOfFile) {
		LineContainer anchorPreviousLine = null;

		int lineNumber = 0;
		for (String line : allLinesOfFile) {
			LineContainer enrichedLine = new LineContainer(line, lineNumber++);
			enrichedLine.setPreviousLine(anchorPreviousLine);
			allLines.add(enrichedLine);
			if (anchorPreviousLine != null) {
				anchorPreviousLine.setNextLine(enrichedLine);
			}

			anchorPreviousLine = enrichedLine;
		}
	}

	public List<String> getSourceWithInstrumentation() {
		List<String> instrumentationsAdded = new LinkedList<String>();
		for (LineContainer containerLine : allLines) {
			instrumentationsAdded.add(containerLine.getFullLine());
		}
		return instrumentationsAdded;
	}
}
