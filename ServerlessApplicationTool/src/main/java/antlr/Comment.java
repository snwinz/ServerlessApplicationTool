package antlr;

public class Comment {
	private String startComment;
	private String endComment;
	private String logLine;
	private final String lineCommentSign = "//  ";
	
	Comment(String startComment, String endComment, String logLine) {
		this.startComment = lineCommentSign + startComment;
		this.endComment = lineCommentSign + endComment;
		this.logLine = logLine;
	}

	@Override
	public String toString() {
		return logLine.isEmpty() ? "" : String.join(System.lineSeparator(), "", startComment, logLine, endComment, "");

	}
}
