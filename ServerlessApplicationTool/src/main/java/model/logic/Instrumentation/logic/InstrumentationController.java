package model.logic.Instrumentation.logic;

import java.util.LinkedList;
import java.util.List;

import model.logic.Instrumentation.Criteria.CoverageCriterion;
import model.logic.Instrumentation.fileData.LineContainer;
import model.logic.Instrumentation.fileData.SourceFile;

public class InstrumentationController {
	private final SourceFile sourceFile;

	public final static String[] accessPatternsDBWrite = { ".putItem(", ".deleteItem(" };
	public final static String[] writePatternsDB = { ".putItem(", ".deleteItem(" };
	public final static String[] readPatternsDB = { ".getItem(" };

	public InstrumentationController(SourceFile sourceFile) {
		this.sourceFile = sourceFile;
	}

	public void instrument(CoverageCriterion instrumentator) {

		for (LineContainer enrichedLine : sourceFile.getAllLines()) {
			trackMethodStart(instrumentator, enrichedLine);
			trackInvocations(instrumentator, enrichedLine);
			trackWrites(instrumentator, enrichedLine);
			trackReads(instrumentator, enrichedLine);
			trackReturns(instrumentator, enrichedLine);
		}

		List<String> useVarsOfEvents = identifyUseVarsOfEvents();
		List<String> useVarsOfReturns = identifyUseVarsOfReturns();
		List<String> useVarsOfReads = identifyUseVarsOfReads();

		List<String> defVarsForInvokations = identifyDefsForInvocations();
		List<String> defVarsForReturns = identifyDefsVarsOfReturns();
		List<String> defVarsForWrites = identifyDefVarsOfWrites();

		trackDefVarsOfInvokations(defVarsForInvokations, instrumentator);
		trackDefVarsOfWrites(defVarsForWrites, instrumentator);
		trackDefVarsOfReturns(defVarsForReturns, instrumentator);

		trackUseOfEvents(useVarsOfEvents, instrumentator);
		trackUseOfReturns(useVarsOfReturns, instrumentator);
		trackUseOfReads(useVarsOfReads, instrumentator);
	}

	private void trackUseOfReads(List<String> useVarsOfEvents, CoverageCriterion instrumentator) {
		for (String useVar : useVarsOfEvents) {
			for (LineContainer enrichedLine : sourceFile.getAllLines()) {
				if (Utilities.isVariableUsedForUsage(useVar, enrichedLine.getLine())) {
					LineContainer firstLine = Utilities.getFirstLineOfStatement(enrichedLine);
					instrumentator.addUseOfReads(firstLine, useVar);
				}
			}
		}
	}

	private void trackUseOfReturns(List<String> useVarsOfEvents, CoverageCriterion instrumentator) {
		for (String useVar : useVarsOfEvents) {

			for (LineContainer enrichedLine : sourceFile.getAllLines()) {
				if (Utilities.isVariableUsedForUsage(useVar, enrichedLine.getLine())) {
					LineContainer firstLine = Utilities.getFirstLineOfStatement(enrichedLine);
					instrumentator.addUseOfReturn(firstLine, useVar);
				}

			}
		}

	}

	private void trackUseOfEvents(List<String> useVarsOfEvents, CoverageCriterion instrumentator) {
		for (String useVar : useVarsOfEvents) {
			for (LineContainer enrichedLine : sourceFile.getAllLines()) {
				if (Utilities.isVariableUsedForUsage(useVar, enrichedLine.getLine())) {
					instrumentator.addUseOfEvents(enrichedLine, useVar);
				}

			}
		}
	}

	private void trackDefVarsOfReturns(List<String> defVarsForReturns, CoverageCriterion instrumentator) {
		for (String defVar : defVarsForReturns) {
			for (LineContainer enrichedLine : sourceFile.getAllLines()) {
				if (Utilities.isVariableUsedForDefinition(defVar, enrichedLine.getLine())
						&& enrichedLine.getLine().contains("{")) {
					LineContainer lastLine = Utilities.getLastLineOfStatement(enrichedLine);
					instrumentator.addDefOfReturns(lastLine, defVar);
				}
			}
		}
	}

	private void trackDefVarsOfWrites(List<String> defVarsForWrites, CoverageCriterion instrumentator) {
		for (String defVar : defVarsForWrites) {

			for (LineContainer enrichedLine : sourceFile.getAllLines()) {
				if (Utilities.isVariableUsedForDefinition(defVar, enrichedLine.getLine())) {
					LineContainer lastLine = Utilities.getLastLineOfStatement(enrichedLine);
					instrumentator.addDefOfWrites(lastLine, defVar);
				}
			}
		}
	}

	private void trackDefVarsOfInvokations(List<String> defVarsForInvokations, CoverageCriterion instrumentator) {
		for (String defVar : defVarsForInvokations) {
			for (LineContainer enrichedLine : sourceFile.getAllLines()) {
				if (Utilities.isVariableUsedForDefinition(defVar, enrichedLine.getLine())) {
					LineContainer lastLine = Utilities.getLastLineOfStatement(enrichedLine);
					instrumentator.addDefOfInvocationVar(lastLine, defVar);
				}
			}
		}
	}

	private List<String> identifyDefsVarsOfReturns() {
		List<String> defVars = new LinkedList<String>();
		for (LineContainer enrichedLine : sourceFile.getAllLines()) {
			String line = enrichedLine.getLine();
			if (line.contains("callback(")) {
				String separator = ("callback(");
				separator = separator.replace(".", "\\.").replace("(", "\\(");
				String[] paramsOfCallback = line.split(separator)[1].split(",");
				if (paramsOfCallback.length > 1) {
					String variable = paramsOfCallback[1].split("\\)")[0].trim();
					if (Utilities.isVariable(variable)) {
						if (!defVars.contains(variable)) {
							defVars.add(variable);
						}
					}
				}
			}
			if (line.contains("return")) {
				String[] returnValue = line.split("=");
				if (returnValue.length > 1) {
					String returnVariable = returnValue[1].split(";")[0];
					if (Utilities.isVariable(returnVariable)) {
						if (!defVars.contains(returnVariable)) {
							defVars.add(returnVariable);
						}
					}
				}
			}
		}
		return defVars;
	}

	private List<String> identifyDefVarsOfWrites() {
		List<String> defVars = new LinkedList<String>();
		for (LineContainer enrichedLine : sourceFile.getAllLines()) {
			String line = enrichedLine.getLine();
			for (String pattern : writePatternsDB) {
				if (isPatternUsed(enrichedLine.getLine(), pattern)) {
					String firstParameter = Utilities.getFirstParameter(pattern, line);
					defVars.add(firstParameter);
				}
			}
		}
		return defVars;
	}

	private List<String> identifyDefsForInvocations() {
		List<String> defVars = new LinkedList<String>();
		for (LineContainer enrichedLine : sourceFile.getAllLines()) {
			if (isInvoker(enrichedLine.getLine())) {
				String firstParmeter = Utilities.getFirstParameter(".invoke(", enrichedLine.getLine());
				if (Utilities.isVariable(firstParmeter)) {
					defVars.add(firstParmeter.trim());
				}
			}
		}
		return defVars;
	}

	private void trackMethodStart(CoverageCriterion instrumentator, LineContainer enrichedLine) {
		if (isStartLine(enrichedLine.getLine())) {
			String parameters = enrichedLine.getLine().split("\\(")[1];
			String event = parameters.split(",")[0];
			instrumentator.addCoverageStatementsHandler(enrichedLine, event);
		}
	}

	private boolean isStartLine(String line) {
		return line.trim().contains("exports.handler");
	}

	private void trackInvocations(CoverageCriterion instrumentator, LineContainer enrichedLine) {
		if (isInvoker(enrichedLine.getLine())) {
			String firstParmeter = Utilities.getFirstParameter(".invoke(", enrichedLine.getLine());
			String returnValue = Utilities.getReturnParameter(".invoke(", enrichedLine.getLine());
			instrumentator.addCoverageStatementsInvocation(enrichedLine, firstParmeter, returnValue);
		}
	}

	private boolean isInvoker(String line) {
		return line.trim().contains(".invoke(");
	}

	private void trackReads(CoverageCriterion instrumentator, LineContainer enrichedLine) {
		for (String pattern : readPatternsDB) {
			if (isPatternUsed(enrichedLine.getLine(), pattern)) {
				String parameter = Utilities.getFirstParameter(pattern, enrichedLine.getLine());
				String returnValue = Utilities.getReturnParameter(pattern, enrichedLine.getLine());
				instrumentator.addCoverageStatementDBisRead(enrichedLine, parameter, returnValue);
			}
		}
	}

	private void trackReturns(CoverageCriterion instrumentator, LineContainer enrichedLine) {
		String line = enrichedLine.getLine();
		if (line.contains("callback(")) {
			String separator = ("callback(");
			separator = separator.replace(".", "\\.").replace("(", "\\(");
			String[] paramsOfCallback = line.split(separator)[1].split(",");
			if (paramsOfCallback.length > 1) {
				String variable = paramsOfCallback[1].split("\\)")[0].trim();
				if (Utilities.isVariable(variable)) {
					instrumentator.addCoverageStatementsReturn(enrichedLine, variable);
				}
			}
			if (line.contains("return")) {
				String[] returnValue = line.split("=");
				if (returnValue.length > 1) {
					String returnVariable = returnValue[1].split(";")[0];
					if (Utilities.isVariable(returnVariable)) {
						instrumentator.addCoverageStatementsReturn(enrichedLine, returnVariable);
					}
				}
			}
		}

	}

	private boolean isPatternUsed(String line, String pattern) {
		return line.trim().contains(pattern);
	}

	private void trackWrites(CoverageCriterion instrumentator, LineContainer enrichedLine) {
		for (String pattern : writePatternsDB) {
			if (isPatternUsed(enrichedLine.getLine(), pattern)) {
				String parameter = Utilities.getFirstParameter(pattern, enrichedLine.getLine());
				instrumentator.addCoverageStatementDBisWritten(enrichedLine, parameter);
			}
		}
	}

	private List<String> identifyUseVarsOfEvents() {
		List<String> useVars = new LinkedList<String>();
		addUseVarsCausedByEvent(useVars);
		return useVars;
	}

	private void addUseVarsCausedByEvent(List<String> useVars) {
		for (LineContainer lineContainer : sourceFile.getAllLines()) {
			String line = lineContainer.getLine();
			if (line.trim().contains("exports.handler") && line.contains("(")) {
				String parameters = line.split("\\(")[1];
				String event = parameters.split(",")[0];
				useVars.add(event.trim());
			}
		}
	}

	private List<String> identifyUseVarsOfReturns() {
		List<String> useVars = new LinkedList<String>();
		addReturnValueOfLambda(useVars);
		return useVars;
	}

	private List<String> identifyUseVarsOfReads() {
		List<String> useVars = new LinkedList<String>();
		addReturnValueOfReads(useVars);
		return useVars;
	}

	private void addReturnValueOfReads(List<String> useVars) {
		for (LineContainer lineContainer : sourceFile.getAllLines()) {
			for (String pattern : readPatternsDB) {
				String line = lineContainer.getLine();
				if (isPatternUsed(line, pattern)) {
					addVariableOfGetter(useVars, pattern, line);
				}
			}
		}
	}

	private void addVariableOfGetter(List<String> useVars, String pattern, String line) {
		pattern = pattern.replace(".", "\\.").replace("(", "\\(");
		String secondHalf = line.split(pattern)[1];
		if (secondHalf.contains("function")) {
			String[] splitArray = secondHalf.split("function")[1].split(",");
			String variableData = splitArray[splitArray.length - 1];
			variableData = Utilities.removeNonVariableCharacters(variableData);
			useVars.add(variableData);
		}
	}

	private void addReturnValueOfLambda(List<String> useVars) {
		for (LineContainer lineContainer : sourceFile.getAllLines()) {
			String line = lineContainer.getLine();
			if (line.trim().contains(".invoke(")) {
				String secondHalf = line.split(".invoke\\(")[1];
				if (secondHalf.contains("function")) {
					String[] splitArray = secondHalf.split("function")[1].split(",");
					String variableData = splitArray[splitArray.length - 1];
					variableData = Utilities.removeNonVariableCharacters(variableData);
					useVars.add(variableData);
				}
			}
		}
	}

}
