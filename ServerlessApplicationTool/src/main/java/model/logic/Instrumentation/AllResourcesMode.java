package model.logic.Instrumentation;

public class AllResourcesMode implements CoverageInstrumentator {
	public final static String AR_LOG_FUNCTIONSTART = "#AR_S_";
	public final static String AR_LOG_FUNCTIONINVOKATION = "#AR_FI_";
	public final static String AR_LOG_DBACCESS = "#AR_DBA_";

	@Override
	public String addCoverageStatementsHandler(String line) {

		String logLine = String.format("%n" 
				+"{%n" 
				+ "        if(event.Records != undefined){%n"
				+ "            console.log('%s' + event.Records[0].eventSourceARN.split(':')[5].split('/')[1]);\r\n"
				+ "        }%n" 
				+ "        console.log('%s' + context.functionName);%n" 
				+ "}",
				AR_LOG_FUNCTIONSTART, AR_LOG_FUNCTIONSTART, AR_LOG_FUNCTIONSTART);
		line += logLine;
		return line;
	}

	@Override
	public String addCoverageStatementsInvocation(String line, String nameOfFunction) {
		String logLine = String.format("\tconsole.log('%s' + %s.FunctionName);%n", AR_LOG_FUNCTIONINVOKATION,
				nameOfFunction);
		line = logLine + line;
		return line;
	}

	@Override
	public String addCoverageStatementDBisRead(String line, String param) {
		return this.addCoverageStatementDBisWritten(line, param);
	}

	@Override
	public String addCoverageStatementDBisWritten(String line, String param) {
		String logLine = String.format("\tconsole.log('%s' + %s.TableName);%n", AR_LOG_DBACCESS, param);
		line = logLine + line;
		return line;
	}

}
