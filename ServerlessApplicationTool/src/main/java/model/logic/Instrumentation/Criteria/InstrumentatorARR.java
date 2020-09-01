package model.logic.Instrumentation.Criteria;

import model.logic.Instrumentation.fileData.LineContainer;

public class InstrumentatorARR implements CoverageCriterion {

	public final static String marker = "#ARR_";
	public final static String functionStartMarker = marker + "S_";
	public final static String functionInvocationMarker = marker + "FI_";
	public final static String dbAccessMarker = marker + "DBA_";

	@Override
	public void addCoverageStatementsHandler(LineContainer enrichedLine, String event) {
		String logLine = String.format("\tif(event.Records != undefined){%n"
				+ "\t\tconsole.log('%s' + event.Records[0].eventSourceARN.split(':')[5].split('/')[1] + ';' + context.functionName + ' ');%n"
				+ " \t}", functionStartMarker);
		enrichedLine.addPostLine(logLine);
	}

	@Override
	public void addCoverageStatementsInvocation(LineContainer enrichedLine, String param, String returnValue) {
		String logLine = String.format("\tconsole.log('%s' + context.functionName + ';' + %s.FunctionName + ' ');",
				functionInvocationMarker, param);
		enrichedLine.addPreLine(logLine);
	}

	@Override
	public void addCoverageStatementDBisRead(LineContainer enrichedLine, String param, String returnValue) {
		this.addCoverageStatementDBisWritten(enrichedLine, param);
	}

	@Override
	public void addCoverageStatementDBisWritten(LineContainer enrichedLine, String param) {
		String logLine = String.format("\tconsole.log('%s'+ context.functionName + ';' + %s.TableName + ' ');",
				dbAccessMarker, param);
		enrichedLine.addPreLine(logLine);
	}

	@Override
	public void addDefOfInvocationVar(LineContainer enrichedLine, String defVar) {
	}

	@Override
	public void addDefOfWrites(LineContainer lastLine, String defVar) {
	}

	@Override
	public void addDefOfReturns(LineContainer lastLine, String defVar) {
	}

	@Override
	public void addUseOfEvents(LineContainer lastLine, String useVar) {
	}

	@Override
	public void addUseOfReturn(LineContainer lastLine, String useVar) {
	}

	@Override
	public void addUseOfReads(LineContainer lastLine, String useVar) {
	}

	@Override
	public void addCoverageStatementsReturn(LineContainer line, String variable) {
	}

}
