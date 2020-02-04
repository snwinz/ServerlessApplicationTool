package model.logic.Instrumentation;

public class InstrumentatorARR implements CoverageInstrumentator {


    public final static String marker = "#ARR_";
    public final static String functionStartMarker = marker + "S_";
    public final static String functionInvocationMarker = marker + "FI_";
    public final static String dbAccessMarker = marker + "DBA_";

    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format("%n\tif(event.Records != undefined){%n"
                + "\t\tconsole.log('%s' + event.Records[0].eventSourceARN.split(':')[5].split('/')[1] + ';' + context.functionName + ' ');%n"
                + " \t};", functionStartMarker);
        line += logLine;
        return line;
    }

    @Override
    public String addCoverageStatementsInvocation(String line, String nameOfFunction) {
        String logLine = String.format("\tconsole.log('%s' + context.functionName + ';' + %s.FunctionName + ' ');%n",
                functionInvocationMarker, nameOfFunction);
        line = logLine + line;
        return line;
    }

    @Override
    public String addCoverageStatementDBisRead(String line, String param) {
        return this.addCoverageStatementDBisWritten(line, param);
    }

    @Override
    public String addCoverageStatementDBisWritten(String line, String param) {
        String logLine = String.format("\tconsole.log('%s'+ context.functionName + ';' + %s.TableName + ' ');%n",
                dbAccessMarker, param);
        line = logLine + line;
        return line;
    }

}
