package model.logic.Instrumentation;

public class AllResourceRelations implements CoverageInstrumentator {

    public final static String ARR_LOG_FUNCTIONSTART = "#ARR_S_";
    public final static String ARR_LOG_FUNCTIONINVOKATION = "#ARR_FI_";
    public final static String ARR_LOG_DBACCESS = "#ARR_DBA_";


    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format("%n\tif(event.Records != undefined){%n"
                + "\t\tconsole.log('%s' + event.Records[0].eventSourceARN.split(':')[5].split('/')[1] + ';' + context.functionName);%n"
                + " \t};", ARR_LOG_FUNCTIONSTART);
        line += logLine;
        return line;
    }

    @Override
    public String addCoverageStatementsInvocation(String line, String nameOfFunction) {
        String logLine = String.format("\tconsole.log('%s' + context.functionName + ';' + %s.FunctionName);%n",
                ARR_LOG_FUNCTIONINVOKATION, nameOfFunction);
        line = logLine + line;
        return line;
    }

    @Override
    public String addCoverageStatementDBisRead(String line, String param) {
       return this.addCoverageStatementDBisWritten(line,param);
    }

    @Override
    public String addCoverageStatementDBisWritten(String line, String param) {
        String parameter = line.split(param)[1].split(",")[0];
        String logLine = String.format("\tconsole.log('%s'+ context.functionName + ';' + %s.TableName);%n",
                ARR_LOG_DBACCESS, parameter);
        line = logLine + line;
        return  line;
    }
}
