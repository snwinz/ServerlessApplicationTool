package model.logic.Instrumentation.Criteria;

public interface CoverageCriterion {

	String addCoverageStatementsHandler(String event);

	String addCoverageStatementsInvocation(String lambdaParameter, String returnParameter);

	String addCoverageStatementDBisRead(String parameter, String returnValue);

	String addCoverageStatementDBisWritten(String param);

	String addCoverageStatementDBisDeleted(String param);

	String addCoverageStatementsReturn(String variable);

	String addDefOfInvocationVar(String defVar, int line);

	String addDefOfWrites(String defVar, int line);

	String addDefOfReturns(String defVar, int line);

	String addUseOfEvents(String useVar, int line);

	String addUseOfReturn(String useVar, int line);

	String addUseOfReads(String useVar, int line);

	String addDefOfDeletes(String deleteParameter, int line);
	
	void activateDeletion();

	boolean isDeletionInstrumentation();

	

}
