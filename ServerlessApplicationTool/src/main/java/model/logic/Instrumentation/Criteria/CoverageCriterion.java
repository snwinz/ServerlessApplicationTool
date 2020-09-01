package model.logic.Instrumentation.Criteria;

import model.logic.Instrumentation.fileData.LineContainer;

public interface CoverageCriterion {
	void addCoverageStatementsHandler(LineContainer enrichedLine, String event);

	void addCoverageStatementsInvocation(LineContainer line, String param, String returnValue);

	void addCoverageStatementDBisRead(LineContainer enrichedLine, String parameter, String returnValue);

	void addCoverageStatementDBisWritten(LineContainer line, String param);

	void addCoverageStatementsReturn(LineContainer line, String variable);
	
	void addDefOfInvocationVar(LineContainer enrichedLine, String defVar);

	void addDefOfWrites(LineContainer lastLine, String defVar);

	void addDefOfReturns(LineContainer lastLine, String defVar);

	void addUseOfEvents(LineContainer line, String useVar);

	void addUseOfReturn(LineContainer line, String useVar);

	void addUseOfReads(LineContainer line, String useVar);

}
