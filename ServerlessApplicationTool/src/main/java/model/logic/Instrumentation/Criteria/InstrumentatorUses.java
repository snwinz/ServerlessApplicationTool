package model.logic.Instrumentation.Criteria;


public class InstrumentatorUses implements CoverageCriterion {

	public final static String marker = "#ARU_";

	private final static String functionInvocationMarkerDef = marker + "FID_";
	private final static String databaseWriterMarkerDef = marker + "DBWD_";
	private final static String returnMarkerDef = marker + "RD_";

	public final static String functionInvocationMarker = marker + "FI_";
	public final static String databaseReadMarker = marker + "DBR_";
	public final static String functionStartMarker = marker + "S_";

	public final static String returnUseMarker = marker + "RU_";
	public final static String databaseUseReadMarker = marker + "DBRU_";
	public final static String functionStartUseMarker = marker + "SU_";

	private int idCounterUse = 0;
	private int idCounterDef = 0;
	
	
	@Override
	public String addCoverageStatementsHandler(String event) {
		String logLine = String.format("          console.log('%s' + context.functionName + '_%s');",
				functionStartMarker, event);
		return logLine;
	}

	
	@Override
	public String addCoverageStatementsInvocation(String var, String returnValue) {
		String logLine = String.format("          console.log('%s' + context.functionName + '_%s');",
				functionInvocationMarker, returnValue);
		return logLine;
	}


	@Override
	public String addCoverageStatementDBisRead(String param, String returnValue) {
		String logLine = String.format("          console.log('%s' + context.functionName + '_%s');",
				databaseReadMarker, returnValue);
		return logLine;
	}


	@Override
	public String addCoverageStatementDBisWritten(String var) {
		return "";
	}
	
	
	@Override
	public String addCoverageStatementsReturn(String variable) {
		return "";
	}



	@Override
	public String addDefOfInvocationVar(String defVar) {
		String logLine = String.format(
				"    %s.Payload = %s.Payload.replace('\\{','{\"funcDef\" : \"' + context.functionName + '_%s_%s\",');%n"
						+ "    console.log('%s' + JSON.parse(%s.Payload).funcDef + ' ');",
				defVar, defVar, defVar, idCounterDef++, functionInvocationMarkerDef, defVar);
		return logLine;
	}

	
	@Override
	public String addDefOfWrites( String defVar) {
		String logLine = String.format(
				"          %s.Item.funcDef = {S:  context.functionName + '_%s_%s'};%n"
						+ "          console.log('%s' + %s.Item.funcDef.S + ' ');",
				defVar, defVar, idCounterDef++, databaseWriterMarkerDef, defVar);
		return logLine;
	}

	
	@Override
	public String addDefOfReturns(String defVar) {
		String logLine = String.format(
				"    %s.returnDef = context.functionName + '_%s_%s';%n"
						+ "    console.log('%s' + (%s.returnDef) + ' ');",
				defVar, defVar, idCounterDef++, returnMarkerDef, defVar);
		return logLine;
	}

	
	@Override
	public String addUseOfEvents(String useVar) {
		String logLine = String.format(
				"   if (event.funcDef != undefined) {%n" + "     console.log('%s' + event.funcDef + '_' + context.functionName + '_%s_%s');%n" + "   } ",
				functionStartUseMarker, idCounterUse++, useVar);
		return logLine;
	}

	
	@Override
	public String addUseOfReturn(String useVar) {
		String logLine = String.format(
				"   if (%s.Payload != undefined) {%n" + 
		"     var answerOfReturn = JSON.parse(%s.Payload).returnDef;%n" +
		"     console.log('%s' + answerOfReturn + '_' + context.functionName + '_%s_%s');%n" + "   } ",
				useVar, useVar, returnUseMarker, idCounterUse++, useVar);
		return logLine;
	}

	@Override
	public String addUseOfReads(String useVar) {
		String logLine = String.format("if (%s != undefined) {%n" + "        if (%s.Item != undefined) {%n"
				+ "            const definition = %s.Item.funcDef;%n" + "            if (definition != undefined) {%n"
				+ "                console.log('%s' + definition.S + '_' + context.functionName + '_%s_%s');%n" + "            }%n" + "        }%n"
				+ "    }", useVar, useVar, useVar, databaseUseReadMarker, idCounterUse++, useVar);
		return logLine;
	}
	
	
	
}
