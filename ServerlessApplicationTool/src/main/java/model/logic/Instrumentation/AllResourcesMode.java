package model.logic.Instrumentation;

public class AllResourcesMode implements CoverageInstrumentator {
    public final static String AR_LOG_FUNCTIONSTART = "#AR_S_";
    public final static String AR_LOG_FUNCTIONINVOKATION = "#AR_FI_";
    public final static String AR_LOG_DBACCESS = "#AR_DBWR_";

    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format("%n\tconsole.log('%s' + context.functionName);", AR_LOG_FUNCTIONSTART);
        line += logLine;
        return line;
    }

    @Override
    public String addCoverageStatementsInvocation(String line, String nameOfFunction) {
        String logLine = String.format("\tconsole.log('%s' + %s.FunctionName);%n", AR_LOG_FUNCTIONINVOKATION,
                nameOfFunction);
        line = logLine + line;
        return  line;
    }


    @Override
    public String addCoverageStatementDBisRead(String line, String param) {
        return this.addCoverageStatementDBisWritten(line,param);
    }

    @Override
    public String addCoverageStatementDBisWritten(String line, String param) {
        String logLine = String.format("\tconsole.log('%s' + %s.TableName);%n", AR_LOG_DBACCESS, param);
        line = logLine + line;
        return line;
    }


}
