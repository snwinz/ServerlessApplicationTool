package model.logic.Instrumentation.Criteria;

import model.logic.Instrumentation.fileData.LineContainer;

public class InstrumentatorDefs implements CoverageCriterion {

	public final static String marker = "#ARD_";

	private final static String functionInvocationMarkerDef = marker + "FID_";
	private final static String databaseWriterMarkerDef = marker + "DBWD_";
	private final static String returnMarkerDef = marker + "RD_";

	public final static String functionInvocationMarker = marker + "FI_";
	public final static String databaseReadMarker = marker + "DBR_";
	public final static String functionStartMarker = marker + "S_";

	public final static String returnUseMarker = marker + "RU_";
	public final static String databaseUseReadMarker = marker + "DBRU_";
	public final static String functionStartUseMarker = marker + "SU_";

	@Override
	public void addCoverageStatementsHandler(LineContainer enrichedLine, String event) {
		String logLine = String.format("          console.log('%s' + context.functionName + '_%s');",
				functionStartMarker, event);
		enrichedLine.addPostLine(logLine);
	}

	@Override
	public void addCoverageStatementsInvocation(LineContainer enrichedLine, String var, String returnValue) {
		String logLine = String.format("          console.log('%s' + context.functionName + '_%s');",
				functionInvocationMarker, returnValue);
		enrichedLine.addPreLine(logLine);
	}

	@Override
	public void addCoverageStatementDBisRead(LineContainer line, String param, String returnValue) {
		String logLine = String.format("          console.log('%s' + context.functionName + '_%s');",
				databaseReadMarker, returnValue);
		line.addPreLine(logLine);
	}


	@Override
	public void addCoverageStatementDBisWritten(LineContainer enrichedLine, String var) {
	}

	@Override
	public void addCoverageStatementsReturn(LineContainer line, String variable) {
	}

	@Override
	public void addDefOfInvocationVar(LineContainer enrichedLine, String defVar) {
		String logLine = String.format(
				"    %s.Payload = %s.Payload.replace('\\{','{\"funcDef\" : \"' + context.functionName + '_%s_line%s\",');%n"
						+ "    console.log('%s' + JSON.parse(%s.Payload).funcDef + ' ');",
				defVar, defVar, defVar, enrichedLine.getLineNumber(), functionInvocationMarkerDef, defVar);
		enrichedLine.addPostLine(logLine);
	}

	@Override
	public void addDefOfWrites(LineContainer lastLine, String defVar) {
		String logLine = String.format(
				"          %s.Item.funcDef = {S:  context.functionName + '_%s_line%s'};%n"
						+ "          console.log('%s' + %s.Item.funcDef.S + ' ');",
				defVar, defVar, lastLine.getLineNumber(), databaseWriterMarkerDef, defVar);
		lastLine.addPostLine(logLine);
	}

	@Override
	public void addDefOfReturns(LineContainer lastLine, String defVar) {
		String logLine = String.format(
				"    %s.returnDef = context.functionName + '_%s_line%s';%n"
						+ "    console.log('%s' + (%s.returnDef) + ' ');",
				defVar, defVar, lastLine.getLineNumber(), returnMarkerDef, defVar);
		lastLine.addPostLine(logLine);
	}

	@Override
	public void addUseOfEvents(LineContainer line, String useVar) {
		String logLine = String.format(
				"   if (event.funcDef != undefined) {%n" + "     console.log('%s' + event.funcDef + '_%s');%n" + "   } ",
				functionStartUseMarker, useVar);
		line.addPreLine(logLine);
	}

	@Override
	public void addUseOfReturn(LineContainer line, String useVar) {
		String logLine = String.format(
				"   if (%s.Payload != undefined) {%n" + 
		"     var answerOfReturn = JSON.parse(%s.Payload).returnDef;%n" +
		"     console.log('%s' + answerOfReturn + '_%s');%n" + "   } ",
				useVar, useVar, returnUseMarker, useVar);
		line.addPreLine(logLine);
	}

	@Override
	public void addUseOfReads(LineContainer line, String useVar) {
		String logLine = String.format("if (%s != undefined) {%n" + "        if (%s.Item != undefined) {%n"
				+ "            const definition = %s.Item.funcDef;%n" + "            if (definition != undefined) {%n"
				+ "                console.log('%s' + definition.S + '_%s');%n" + "            }%n" + "        }%n"
				+ "    }", useVar, useVar, useVar, databaseUseReadMarker, useVar);

		line.addPreLine(logLine);
	}

}
