package antlr;

import org.antlr.v4.runtime.tree.ParseTree;

import antlr.autogenerated.JavaScriptParser;
import antlr.autogenerated.JavaScriptParser.ArgumentContext;
import antlr.autogenerated.JavaScriptParser.AssignableContext;
import antlr.autogenerated.JavaScriptParser.ExpressionSequenceContext;
import antlr.autogenerated.JavaScriptParser.FormalParameterArgContext;
import antlr.autogenerated.JavaScriptParser.FunctionBodyContext;
import antlr.autogenerated.JavaScriptParser.IdentifierContext;
import antlr.autogenerated.JavaScriptParser.IdentifierNameContext;
import antlr.autogenerated.JavaScriptParser.PropertyAssignmentContext;
import antlr.autogenerated.JavaScriptParser.PropertyNameContext;
import antlr.autogenerated.JavaScriptParser.SingleExpressionContext;
import antlr.autogenerated.JavaScriptParser.StatementContext;
import antlr.autogenerated.JavaScriptParser.VariableDeclarationContext;

public class GraphHelper {
	public static boolean isIdentifierUsage(ParseTree ctx) {
		ParseTree anchor = ctx;
		while (ctx.getParent() != null) {
			anchor = ctx;
			ctx = ctx.getParent();
			if (ctx instanceof ArgumentContext) {
				return true;
			}
			if (ctx instanceof PropertyNameContext) {
				return true;
			}
			if (ctx instanceof IdentifierNameContext) {
				return false;
			}
			if (ctx instanceof ExpressionSequenceContext) {
				return true;
			}
			if (ctx instanceof PropertyAssignmentContext) {
				return false;
			}
			if (ctx instanceof AssignableContext) {
				return false;
			}
			if ("=".equals(ctx.getText())) {
				if (anchor == ctx.getChild(0)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isIdentifierDefinition(ParseTree ctx) {
		ParseTree anchor = ctx;
		while (ctx.getParent() != null) {
			anchor = ctx;
			ctx = ctx.getParent();
			if (ctx instanceof SingleExpressionContext) {
				if ("=".equals(ctx.getText())) {
					return ctx.getChild(0) == anchor;
				}
			}
			if (ctx instanceof ArgumentContext) {
				return false;
			}
		}
		return true;
	}

	public static String getVariableNameOfDefinition(VariableDeclarationContext ctx) {
		ParseTree variable = ctx.getChild(0);
		if (variable != null) {
			return variable.getText();
		}
		return null;
	}

	public static StatementContext getStatementOfContext(ParseTree ctx) {
		while (ctx.getParent() != null) {
			ctx = ctx.getParent();
			if (ctx instanceof StatementContext) {
				return (StatementContext) ctx;
			}
		}
		return null;
	}

	public static StatementContext getNextStatement(ParseTree ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof StatementContext) {
				return (StatementContext) child;
			}
			StatementContext resultOfChild = getNextStatement(child);
			if (resultOfChild != null && resultOfChild instanceof StatementContext) {
				return (StatementContext) resultOfChild;
			}
		}

		return null;
	}

	public static FunctionBodyContext getNextFunctionBody(ParseTree ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof FunctionBodyContext) {
				return (FunctionBodyContext) child;
			}
			FunctionBodyContext resultOfChild = getNextFunctionBody(child);
			if (resultOfChild != null && resultOfChild instanceof FunctionBodyContext) {
				return (FunctionBodyContext) resultOfChild;
			}
		}
		return null;
	}

	public static SingleExpressionContext getSingleExpression(ParseTree ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof SingleExpressionContext) {
				return (SingleExpressionContext) child;
			}
			SingleExpressionContext resultOfChild = getSingleExpression(child);
			if (resultOfChild != null && resultOfChild instanceof SingleExpressionContext) {
				return (SingleExpressionContext) resultOfChild;
			}
		}
		return null;
	}

	public static FormalParameterArgContext getNextFormalParameterArg(ParseTree ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof FormalParameterArgContext) {
				return (FormalParameterArgContext) child;
			}
			FormalParameterArgContext resultOfChild = getNextFormalParameterArg(child);
			if (resultOfChild != null && resultOfChild instanceof FormalParameterArgContext) {
				return (FormalParameterArgContext) resultOfChild;
			}
		}

		return null;
	}

	public static boolean isStatementCallback(ParseTree ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof IdentifierContext) {
				return "callback".equals(child.getText());
			}
			if (isStatementCallback(child)) {
				return true;
			}
		}
		return false;
	}

	public static ArgumentContext getFirstArgument(ParseTree ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof ArgumentContext) {
				return (ArgumentContext) child;
			}
			ArgumentContext resultOfChild = getFirstArgument(child);
			if (resultOfChild != null && resultOfChild instanceof ArgumentContext) {
				return (ArgumentContext) resultOfChild;
			}
		}
		return null;
	}

	public static ArgumentContext getSecondArgument(ParseTree ctx, Boolean firstFound) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			ParseTree child = ctx.getChild(i);
			if (child instanceof ArgumentContext) {
				if (firstFound) {
					return (ArgumentContext) child;
				} else {
					firstFound = true;
				}
			}
			ArgumentContext resultOfChild = getSecondArgument(child, firstFound);
			if (resultOfChild != null && resultOfChild instanceof ArgumentContext) {
				return (ArgumentContext) resultOfChild;
			}
		}
		return null;
	}

	public static boolean checkIfFunctionIsInvoked(StatementContext ctx, String functionName) {
		ParseTree expressionStatement = ctx.getChild(0);
		if (expressionStatement == null) {
			return false;
		}
		ParseTree expressionSequence = expressionStatement.getChild(0);
		if (expressionSequence == null) {
			return false;
		}
		ParseTree singleExpression = expressionSequence.getChild(0);
		if (singleExpression == null) {
			return false;
		}
		ParseTree singleExpressionSub = singleExpression.getChild(0);
		if (singleExpressionSub == null) {
			return false;
		}
		ParseTree singeExpressionPoint = singleExpressionSub.getChild(1);
		if (singeExpressionPoint == null) {
			return false;
		}
		ParseTree singeExpressionInvoke = singleExpressionSub.getChild(2);
		if (singeExpressionInvoke == null) {
			return false;
		}
		if ((".").equals(singeExpressionPoint.getText()) && functionName.equals(singeExpressionInvoke.getText())) {
			return true;
		}
		return false;
	}

	public static ParseTree getFunctionExpression(StatementContext ctx, String functionName) {
		ParseTree expressionStatement = ctx.getChild(0);
		if (expressionStatement == null) {
			return null;
		}
		ParseTree expressionSequence = expressionStatement.getChild(0);
		if (expressionSequence == null) {
			return null;
		}
		ParseTree singleExpression = expressionSequence.getChild(0);
		if (singleExpression == null) {
			return null;
		}
		ParseTree singleExpressionSub = singleExpression.getChild(0);
		if (singleExpressionSub == null) {
			return null;
		}
		ParseTree singeExpressionPoint = singleExpressionSub.getChild(1);
		if (singeExpressionPoint == null) {
			return null;
		}
		ParseTree functionNode = singleExpressionSub.getChild(2);
		if (functionNode == null) {
			return null;
		}
		if ((".").equals(singeExpressionPoint.getText()) && functionName.equals(functionNode.getText())) {
			return functionNode;
		}
		return null;
	}

	public static String getReturnVariableOfFunctionInvocation(StatementContext ctx) {
		ParseTree expressionStatement = ctx.getChild(0);
		if (expressionStatement == null) {
			return null;
		}
		ParseTree expressionSequence = expressionStatement.getChild(0);
		if (expressionSequence == null) {
			return null;
		}
		ParseTree singleExpression = expressionSequence.getChild(0);
		if (singleExpression == null) {
			return null;
		}
		ParseTree arguments = singleExpression.getChild(1);
		if (arguments == null) {
			return null;
		}
		ParseTree argument = arguments.getChild(3);
		if (argument == null) {
			return null;
		}
		ParseTree singleExpression2 = argument.getChild(0);
		if (singleExpression2 == null) {
			return null;
		}
		ParseTree anonymousFunction = singleExpression2.getChild(0);
		if (anonymousFunction == null) {
			return null;
		}

		for (int i = 0; i < anonymousFunction.getChildCount(); i++) {
			ParseTree tmpChild = anonymousFunction.getChild(i);
			if (tmpChild instanceof JavaScriptParser.FormalParameterListContext) {
				ParseTree parameter = tmpChild.getChild(2);
				if (parameter != null) {
					return parameter.getText();
				}
			}
		}
		return null;
	}
}
