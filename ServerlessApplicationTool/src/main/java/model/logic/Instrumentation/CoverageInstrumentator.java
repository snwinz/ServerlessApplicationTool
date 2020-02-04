package model.logic.Instrumentation;

public interface CoverageInstrumentator {
    public String addCoverageStatementsHandler(String line);

    public String addCoverageStatementsInvocation(String line, String nameOfFunction);

    public String addCoverageStatementDBisRead(String line, String param);


    public String addCoverageStatementDBisWritten(String line, String param);

}
