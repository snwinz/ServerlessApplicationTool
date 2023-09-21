package model.logic.Instrumentation.Criteria;

public class InstrumentatorDefs implements CoverageCriterion {

	private final static String coverageAbbreviation = "AD";
	public final static String marker = "#" + coverageAbbreviation + "_";
	private final static String functionInvocationMarkerDef = marker + "FID_";
	private final static String databaseWriterMarkerDef = marker + "DBWD_";
	private final static String databaseDeleteMarkerDef = marker + "DBDD_";
	private final static String returnMarkerDef = marker + "RD_";

	public final static String functionInvocationMarker = marker + "FI_";
	public final static String databaseReadMarker = marker + "DBR_";
	public final static String functionStartMarker = marker + "S_";

	public final static String returnUseMarker = marker + "RU_";
	public final static String databaseUseReadMarker = marker + "DBRU_";
	public final static String functionStartUseMarker = marker + "SU_";

	private boolean deletionInstrumentation = false;

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
	public String addCoverageStatementDBisDeleted(String param) {
		return this.addCoverageStatementDBisWritten(param);
	}

	@Override
	public String addCoverageStatementsReturn(String variable) {
		return "";
	}

	@Override
	public String addDefOfInvocationVar(String defVar, int line) {
		String logLine = String.format(
				"    %s.Payload = %s.Payload.replace('\\{','{\"funcDef" + coverageAbbreviation + "\" : \"' "
						+ "+ context.functionName + '_%s_line%s\",');%n"
						+ "    console.log('%s' + JSON.parse(%s.Payload).funcDef" + coverageAbbreviation + ");",
				defVar, defVar, defVar, line, functionInvocationMarkerDef, defVar);
		return logLine;
	}

	@Override
	public String addDefOfWrites(String defVar, int line) {
		String logLine = String.format(
				"          %s.Item.funcDef" + coverageAbbreviation + "  = {S:  context.functionName + '_%s_line%s'};%n"
						+ "          console.log('%s' + %s.Item.funcDef" + coverageAbbreviation + ".S);",
				defVar, defVar, line, databaseWriterMarkerDef, defVar);
		return logLine;
	}

	@Override
	public String addDefOfReturns(String defVar, int line) {
		String logLine = String.format("    %s.returnDef" + coverageAbbreviation + " = context.functionName + '_%s_line%s';%n"
				+ "    console.log('%s' + (%s.returnDef" + coverageAbbreviation + "));%n", defVar, defVar, line, returnMarkerDef, defVar);
		return logLine;
	}

	@Override
	public String addUseOfEvents(String useVar, int line) {
		String logLine = String.format("   if (event.funcDef" + coverageAbbreviation + " != undefined) {%n"
				+ "     console.log('%s' + event.funcDef" + coverageAbbreviation + "  + '_%s');%n" + "   } "
				+ "   if (event.Records != undefined) {%n" + "   if (event.Records[0] != undefined) {%n"
				+ "   	if (event.Records[0].dynamodb != undefined) {%n"
				+ "   		if (event.Records[0].dynamodb.NewImage != undefined) {%n"
				+ "   			if (event.Records[0].dynamodb.NewImage.funcDef" + coverageAbbreviation
				+ " != undefined) {%n"
				+ "					let funcDef" + coverageAbbreviation + "  = event.Records[0].dynamodb.NewImage.funcDef" + coverageAbbreviation + ".S;%n"
				+ "    				console.log('%s' + funcDef" + coverageAbbreviation + " + '_%s');%n" + "\t\t\t\t}%n\t\t\t}%n\t\t}%n\t}%n}%n ",
				functionStartUseMarker, useVar, functionStartUseMarker, useVar);
		return logLine;
	}

	@Override
	public String addUseOfReturn(String useVar, int line) {
		String logLine = String.format(
				"   if (%s.Payload != undefined) {%n" + "     let answerOfReturn = JSON.parse(%s.Payload).returnDef"
						+ coverageAbbreviation + ";%n" + "     console.log('%s' + answerOfReturn + '_%s');%n" + "   } ",
				useVar, useVar, returnUseMarker, useVar);
		return logLine;
	}

	@Override
	public String addUseOfReads(String useVar, int line) {
		String deletionUse = "";
		if (isDeletionInstrumentation()) {
			deletionUse = String.format("             else{%n" + "                 definition = %s.Item.funcDel"
					+ coverageAbbreviation + ";%n" + "                     if(definition != undefined) {%n"
					+ "                     	console.log('%s' + definition.S + '_%s');%n"
					+ "                     	%s = {};%n" + "						}%n" + "                 }%n",
					useVar, databaseUseReadMarker, useVar, useVar);

		}
		String logLine = String.format(
				"if (%s != undefined) {%n" + "        if (%s.Item != undefined) {%n"
						+ "            let definition = %s.Item.funcDef" + coverageAbbreviation + ";%n"
						+ "            if (definition != undefined) {%n"
						+ "                console.log('%s' + definition.S + '_%s');%n" + "            }%n"
						+ "            %s" + "        }%n" + "    }%n",
				useVar, useVar, useVar, databaseUseReadMarker, useVar, deletionUse);
		return logLine;
	}

	@Override
	public void activateDeletion() {
		this.deletionInstrumentation = true;
	}

	@Override
	public String addDefOfDeletes(String deleteVar, int line) {
		String logLine = String.format(
				"if(%s.Key != undefined){%n" + "          %s.Item = %s.Key;%n" + "      }%n" + "      %s.Item.funcDel"
						+ coverageAbbreviation + " = {S:  context.functionName + '_%s_line%s'};%n"
						+ "      delete %s['Key'];%n" + "      console.log('%s' + %s.Item.funcDel" + coverageAbbreviation + ".S);",
				deleteVar, deleteVar, deleteVar, deleteVar, deleteVar, line, deleteVar, databaseDeleteMarkerDef,
				deleteVar);
		return logLine;
	}

	@Override
	public boolean isDeletionInstrumentation() {
		return deletionInstrumentation;
	}
}
