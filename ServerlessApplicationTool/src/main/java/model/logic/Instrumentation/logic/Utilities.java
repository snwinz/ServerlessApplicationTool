package model.logic.Instrumentation.logic;

import model.logic.Instrumentation.fileData.LineContainer;

public class Utilities {
	public static String getFirstParameter(String pattern, String line) {
		pattern = pattern.replace(".", "\\.").replace("(", "\\(");
		String parameter = line.split(pattern)[1].split(",")[0];
		return parameter;
	}

	public static boolean isVariableUsedForDefinition(String variable, String line) {
		if (line.contains("=") && line.contains(variable)) {
			String firstHalf[] = line.split("=")[0].split("\\.")[0].split(" ");
			String myVar = firstHalf[firstHalf.length - 1].trim();
			return myVar.equals(variable);
		} else {
			return false;
		}

	}

	public static boolean isVariableUsedForUsage(String variable, String line) {
		if (line.contains("exports.handler")) {
			return false;
		}

		if (line.contains("=")) {
			String secondHalf = line.split("=")[1];
			if (secondHalf.contains(variable)) {
				return true;
			}
		}
		if (line.contains("(")) {
			String secondHalf = line.split("\\(")[1];
			secondHalf = secondHalf.split("\\)")[0].split("\\.")[0];
			if (secondHalf.contains(variable)) {
				return true;
			}
		}
		return false;
	}

	public static String removeNonVariableCharacters(String name) {
		name = name.replaceAll("\\.", "").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\}", "")
				.replaceAll("\\{", "").trim();
		return name;
	}

	public static boolean isVariable(String variable) {
		boolean isNonVariableSignContained = variable.contains("\"") || variable.contains("(") || variable.contains(")")
				|| variable.contains("'") || variable.contains("{");
		return !isNonVariableSignContained;
	}

	static LineContainer getLastLineOfStatement(LineContainer enrichedLine) {
		String line = enrichedLine.getLine();
		int openingCurlyBrackets = 0;
		openingCurlyBrackets += line.chars().filter(ch -> ch == '{').count();
		openingCurlyBrackets -= line.chars().filter(ch -> ch == '}').count();

		while (openingCurlyBrackets > 0 && enrichedLine != null) {
			enrichedLine = enrichedLine.getNextLine();
			line = enrichedLine.getLine();
			openingCurlyBrackets += line.chars().filter(ch -> ch == '{').count();
			openingCurlyBrackets -= line.chars().filter(ch -> ch == '}').count();
		}
		return enrichedLine;
	}

	public static LineContainer getFirstLineOfStatement(LineContainer enrichedLine) {
		while (enrichedLine.getPreviousLine() != null) {

			String line = enrichedLine.getLine();
			if (line.contains("var ") || line.contains("const") || line.contains(";")) {
				return enrichedLine;
			}
			if (enrichedLine.getPreviousLine().getLine().contains(";")) {
				return enrichedLine;
			}
			enrichedLine = enrichedLine.getPreviousLine();
		}
		return enrichedLine;
	}

	public static String getReturnParameter(String pattern, String line) {
		pattern = pattern.replace(".", "\\.").replace("(", "\\(");
		String returnValue = "";
		
		String secondHalf = line.split(pattern)[1];
		if (secondHalf.contains("function")) {
			String[] splitArray = secondHalf.split("function")[1].split(",");
			String variableData = splitArray[splitArray.length - 1];
			variableData = Utilities.removeNonVariableCharacters(variableData);
			returnValue = variableData;
		}
		return returnValue;
	}

}
