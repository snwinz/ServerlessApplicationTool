package model.logic.Instrumentation.Criteria;

public interface CoverageCriterion {

	String addCoverageStatementsHandler(String event);

	String addCoverageStatementsInvocation(String lambdaParameter, String returnParameter);

	String addCoverageStatementDBisRead(String parameter, String returnValue);

	String addCoverageStatementDBisWritten(String param);

	String addCoverageStatementsReturn(String variable);

	String addDefOfInvocationVar(String defVar);

	String addDefOfWrites(String defVar);

	String addDefOfReturns(String defVar);

	String addUseOfEvents(String useVar);

	String addUseOfReturn(String useVar);

	String addUseOfReads(String useVar);

}
