package model.logic.Instrumentation.Criteria;


public class InstrumentatorARR implements CoverageCriterion {

	public final static String marker = "#ARR_";
	public final static String functionStartMarker = marker + "S_";
	public final static String functionInvocationMarker = marker + "FI_";
	public final static String dbAccessMarker = marker + "DBA_";


	@Override
	public String addCoverageStatementsHandler(String event) {
		String logLine = String.format("\tif(%s.Records != undefined){%n"
				+ "\t\tconsole.log('%s' + %s.Records[0].eventSourceARN.split(':')[5].split('/')[1] + ';' + context.functionName + ' ');%n"
				+ " \t}",event, functionStartMarker, event);
		return logLine;
	}
	

	@Override
	public String addCoverageStatementsInvocation(String param, String returnValue) {
		String logLine = String.format("\tconsole.log('%s' + context.functionName + ';' + %s.FunctionName + ' ');",
				functionInvocationMarker, param);
		return logLine;
	}
	

	@Override
	public String addCoverageStatementDBisRead(String param, String returnValue) {
		return this.addCoverageStatementDBisWritten(param);
	}
	
	
	@Override
	public String addCoverageStatementDBisWritten(String param) {
		String logLine = String.format("\tconsole.log('%s'+ context.functionName + ';' + %s.TableName + ' ');",
				dbAccessMarker, param);
		return logLine;
	}

	@Override
	public String addDefOfInvocationVar(String defVar) {
		return "";
	}


	@Override
	public String addDefOfWrites(String defVar) {
		return "";
	}
	
	
	@Override
	public String addDefOfReturns(String defVar) {
		return "";
	}

	@Override
	public String addUseOfEvents(String useVar) {
		return "";
	}
	
	@Override
	public String addUseOfReturn(String useVar) {
		return "";
	}


	@Override
	public String addUseOfReads(String useVar) {
		return "";
	}

	
	@Override
	public String addCoverageStatementsReturn(String variable) {
		return "";
	}

}
