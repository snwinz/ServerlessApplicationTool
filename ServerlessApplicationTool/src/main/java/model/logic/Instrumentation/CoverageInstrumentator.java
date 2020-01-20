package model.logic.Instrumentation;

public interface CoverageInstrumentator {
    String addCoverageStatementsHandler(String line);

    String addCoverageStatementsInvocation(String line, String nameOfFunction);
    String addCoverageStatementDBisRead(String line, String param);


    String addCoverageStatementDBisWritten(String line, String param);
}
