package antlr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.autogenerated.JavaScriptParser;
import antlr.autogenerated.JavaScriptParserBaseVisitor;
import antlr.autogenerated.JavaScriptParser.ArgumentContext;
import antlr.autogenerated.JavaScriptParser.IdentifierContext;
import antlr.autogenerated.JavaScriptParser.SingleExpressionContext;
import antlr.autogenerated.JavaScriptParser.StatementContext;
import model.logic.Instrumentation.Criteria.CoverageCriterion;

public class InstrumentationIdentifier<T> extends JavaScriptParserBaseVisitor<T> {

	public TokenStreamRewriter rewriter;

	public Map<String, Set<StatementContext>> potentialDefs = new HashMap<String, Set<StatementContext>>();
	private Map<String, StatementContext> useVariablesOfEvents = new HashMap<String, StatementContext>();
	private Map<String, StatementContext> useVariablesOfReads = new HashMap<String, StatementContext>();
	private Map<String, StatementContext> useVariablesOfReturns = new HashMap<String, StatementContext>();

	private List<CoverageCriterion> criteria;

	public InstrumentationIdentifier(TokenStream tokens, List<CoverageCriterion> criteria) {
		this.rewriter = new TokenStreamRewriter(tokens);
		this.criteria = criteria;
	}

	@Override
	public T visitStatement(JavaScriptParser.StatementContext ctx) {
		if (ctx.getText().startsWith("exports")) {
			StatementContext firstLineOfLambda = GraphHelper.getNextStatement(ctx);
			ParseTree event = GraphHelper.getNextFormalParameterArg(ctx);
			if (event != null && firstLineOfLambda != null) {
				useVariablesOfEvents.put(event.getText(), ctx);
				String invokationTracker = createLinesforExportHandler(event.getText());
				rewriter.insertBefore(firstLineOfLambda.start, invokationTracker);
			}
		}

		String invocationFunctionName = "invoke";
		if (ctx.getText().contains(invocationFunctionName)) {
			if (GraphHelper.checkIfFunctionIsInvoked(ctx, invocationFunctionName)) {
				ArgumentContext lambdaParameter = GraphHelper.getFirstArgument(ctx);
				String returnParameter = GraphHelper.getReturnVariableOfFunctionInvocation(ctx);
				useVariablesOfReturns.put(returnParameter, ctx);
				if (lambdaParameter != null) {
					addDefsForEvents(lambdaParameter.getText());
					String logLines = createLinesForFunctionInvocation(lambdaParameter.getText(), returnParameter);
					rewriter.insertBefore(ctx.start, logLines);
				}

			}
		}

		String dbReadFunctionName = "getItem";
		if (ctx.getText().contains(dbReadFunctionName)) {
			if (GraphHelper.checkIfFunctionIsInvoked(ctx, dbReadFunctionName)) {
				ArgumentContext accessParameter = GraphHelper.getFirstArgument(ctx);
				String returnParameter = GraphHelper.getReturnVariableOfFunctionInvocation(ctx);
				useVariablesOfReads.put(returnParameter, ctx);
				if (accessParameter != null) {
					String logLines = createLinesForDBRead(accessParameter.getText(), returnParameter);
					rewriter.insertBefore(ctx.start, logLines);
				}
			}
		}
		String dbWriteFunctionName = "putItem";
		if (ctx.getText().contains(dbWriteFunctionName)) {
			if (GraphHelper.checkIfFunctionIsInvoked(ctx, dbWriteFunctionName)) {
				ArgumentContext writeParameter = GraphHelper.getFirstArgument(ctx);
				if (writeParameter != null) {
					addDefsForWrite(writeParameter.getText());
					String logLines = createLinesForDBWrite(writeParameter.getText());
					rewriter.insertBefore(ctx.start, logLines);
				}
			}
		}

		if (ctx.getText().startsWith("callback")) {
			if (GraphHelper.isStatementCallback(ctx)) {
				ArgumentContext returnVariable = GraphHelper.getSecondArgument(ctx, false);
				if (returnVariable != null) {
					addDefsForReturns(returnVariable.getText());
				}
			}
		}
		return super.visitStatement(ctx);
	}

	public T visitReturnStatement(JavaScriptParser.ReturnStatementContext ctx) {
		SingleExpressionContext returnVariable = GraphHelper.getSingleExpression(ctx);
		if (returnVariable != null) {
			addDefsForReturns(returnVariable.getText());
		}
		return super.visitReturnStatement(ctx);
	}

	@Override
	public T visitIdentifier(JavaScriptParser.IdentifierContext ctx) {
		if (potentialDefs.containsKey(ctx.getText())) {
			if (GraphHelper.isIdentifierDefinition(ctx)) {
				Set<StatementContext> listOfDefs = potentialDefs.get(ctx.getText());
				StatementContext statement = GraphHelper.getStatementOfContext(ctx);
				if (statement != null) {
					listOfDefs.add(statement);
				}
			}

		}
		if (useVariablesOfReads.containsKey(ctx.getText())) {
			if (GraphHelper.isIdentifierUsage(ctx)) {
				addUsesForRead(ctx, ctx.getText());
			}
		}
		if (useVariablesOfEvents.containsKey(ctx.getText())) {
			if (GraphHelper.isIdentifierUsage(ctx)) {
				addUsesForEvents(ctx, ctx.getText());
			}
		}
		if (useVariablesOfReturns.containsKey(ctx.getText())) {
			if (GraphHelper.isIdentifierUsage(ctx)) {
				addUsesForReturns(ctx, ctx.getText());
			}
		}
		return super.visitIdentifier(ctx);
	}

	@Override
	public T visitVariableDeclaration(JavaScriptParser.VariableDeclarationContext ctx) {

		String varName = GraphHelper.getVariableNameOfDefinition(ctx);
		StatementContext statement = GraphHelper.getStatementOfContext(ctx);
		if (statement != null && varName != null) {
			Set<StatementContext> statementsOfDefs = new HashSet<StatementContext>();
			statementsOfDefs.add(statement);
			potentialDefs.put(varName, statementsOfDefs);
		}
		return super.visitVariableDeclaration(ctx);
	}

	private void addUsesForEvents(IdentifierContext ctx, String useVar) {
		String logLine = createLinesForUsesOfEvents(useVar, ctx.start.getLine());
		StatementContext statement = GraphHelper.getStatementOfContext(ctx);
		rewriter.insertBefore(statement.start, logLine);
	}

	private void addUsesForRead(IdentifierContext ctx, String useVar) {
		String logLine = createLinesForUsesOfReads(useVar, ctx.start.getLine());
		StatementContext statement = GraphHelper.getStatementOfContext(ctx);
		rewriter.insertBefore(statement.start, logLine);
	}

	private void addUsesForReturns(IdentifierContext ctx, String useVar) {
		String logLine = createLinesForUsesOfReturns(useVar, ctx.start.getLine());
		StatementContext statement = GraphHelper.getStatementOfContext(ctx);
		rewriter.insertBefore(statement.start, logLine);
	}

	private void addDefsForWrite(String writeParameter) {
		if (potentialDefs.containsKey(writeParameter)) {
			Set<StatementContext> defsToBeInstrumented = potentialDefs.get(writeParameter);
			for (StatementContext statement : defsToBeInstrumented) {
				String logLine = createLinesForDefsForWrites(writeParameter, statement.start.getLine());
				rewriter.insertAfter(statement.stop, logLine);
			}
		}
	}

	private void addDefsForEvents(String lambdaParameter) {
		if (potentialDefs.containsKey(lambdaParameter)) {
			Set<StatementContext> defsToBeInstrumented = potentialDefs.get(lambdaParameter);
			for (StatementContext statement : defsToBeInstrumented) {
				String logLine = createLinesForDefsForInvocations(lambdaParameter, statement.start.getLine());
				rewriter.insertAfter(statement.stop, logLine);

			}
		}
	}

	private void addDefsForReturns(String parameterOfReturn) {
		if (potentialDefs.containsKey(parameterOfReturn)) {
			Set<StatementContext> defsToBeInstrumented = potentialDefs.get(parameterOfReturn);
			for (StatementContext statement : defsToBeInstrumented) {
				String logLine = createLinesForDefsOfReturns(parameterOfReturn, statement.start.getLine());
				rewriter.insertAfter(statement.stop, logLine);
			}
		}
	}

	private String createLinesForDBWrite(String writeParameter) {
		StringBuilder result = new StringBuilder();
		String startComment = "DB write start";
		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addCoverageStatementDBisWritten(writeParameter));
		}
		String endCommend = "DB write end";
		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForDBRead(String accessParameter, String returnParameter) {
		StringBuilder result = new StringBuilder();
		String startComment = "DB read start";
		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addCoverageStatementDBisRead(accessParameter, returnParameter));
		}
		String endCommend = "DB read end";
		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForFunctionInvocation(String lambdaParameter, String returnParameter) {
		StringBuilder result = new StringBuilder();
		String startComment = "Function invocation start";

		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addCoverageStatementsInvocation(lambdaParameter, returnParameter));
		}
		String endCommend = "Function invocation end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesforExportHandler(String event) {
		StringBuilder result = new StringBuilder();
		String startComment = "Function event handling start";

		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addCoverageStatementsHandler(event));
		}
		String endCommend = "Function event handling end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForDefsForWrites(String writeParameter, int line) {
		StringBuilder result = new StringBuilder();
		String startComment = "Def write start";

		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addDefOfWrites(writeParameter, line));
		}
		String endCommend = "Def write end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForDefsForInvocations(String lambdaParameter, int line) {
		StringBuilder result = new StringBuilder();
		String startComment = "Def invocation start";

		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addDefOfInvocationVar(lambdaParameter, line));
		}
		String endCommend = "Def invocation end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForDefsOfReturns(String defVar, int line) {
		StringBuilder result = new StringBuilder();
		String startComment = "Def return start";

		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addDefOfReturns(defVar, line));
		}
		String endCommend = "Def return end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForUsesOfReads(String useVar, int line) {
		StringBuilder result = new StringBuilder();
		String startComment = "Use read start";
		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addUseOfReads(useVar, line));
		}
		String endCommend = "Use read end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForUsesOfEvents(String useVar, int line) {
		StringBuilder result = new StringBuilder();
		String startComment = "Use event start";
		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addUseOfEvents(useVar, line));
		}
		String endCommend = "Use event end";

		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}

	private String createLinesForUsesOfReturns(String useVar, int line) {
		StringBuilder result = new StringBuilder();
		String startComment = "Use return start";
		for (CoverageCriterion criterion : criteria) {
			result.append(criterion.addUseOfReturn(useVar, line));
		}
		String endCommend = "Use return end";
		String logLine = result.toString();
		Comment comment = new Comment(startComment, endCommend, logLine);
		return comment.toString();
	}
}