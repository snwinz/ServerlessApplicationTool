package model.logic.LogEvaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.logic.Instrumentation.Criteria.InstrumentatorDefs;

public class StatementEvaluatorDefs extends StatementEvaluator {

	List<String> logs = new ArrayList<>();

	public StatementEvaluatorDefs(List<String> logStatements) {
		for (String statement : logStatements) {
			if (isStatement(statement)) {
				logs.add(statement);
			}
		}
	}

	private boolean isStatement(String statement) {
		return statement.startsWith(InstrumentatorDefs.marker);
	}

	public Map<String, Integer> getCoveredResources() {
		List<String> coveredResources = new LinkedList<String>();

		Collection<String> defsUsedOfReturns = getFirstUsageOfDef(InstrumentatorDefs.functionInvocationMarker,
				InstrumentatorDefs.returnUseMarker);
		Collection<String> defUsedOfDBwrites = getFirstUsageOfDef(InstrumentatorDefs.databaseReadMarker,
				InstrumentatorDefs.databaseUseReadMarker);
		Collection<String> defUsedOfEvents = getFirstUsageOfDef(InstrumentatorDefs.functionStartMarker,
				InstrumentatorDefs.functionStartUseMarker);
		
		coveredResources.addAll(defsUsedOfReturns);
		coveredResources.addAll(defUsedOfDBwrites);
		coveredResources.addAll(defUsedOfEvents);

		Map<String, Integer> unitsCovered = countNumberOfOccurrences(coveredResources);

		System.out.println("All defs coverage:");
		unitsCovered.forEach((k, v) -> {
			System.out.println(k);
			System.out.println("Occurrences: " + v);
		});
		return unitsCovered;
	}

	private List<String> getFirstUsageOfDef(String startMarker, String useMarker) {
		List<String> coveredResources = new LinkedList<String>();
		Iterator<String> logIterator = logs.iterator();
		while (logIterator.hasNext()) {
			String log = logIterator.next();
			if (log.startsWith(startMarker)) {
				logIterator.remove();
				String variable = log.split("_")[log.split("_").length - 1];
				while (logIterator.hasNext()) {
					String potentialUseLog = logIterator.next();
					if (potentialUseLog.startsWith(useMarker) && potentialUseLog.endsWith(variable)) {
						logIterator.remove();
						String defUsed = removeIrrelevantMarker(potentialUseLog);
						defUsed = removeVariable(defUsed, variable);
						coveredResources.add(defUsed);
						logIterator = logs.iterator();
						break;
					}
				}
			}
		}
		return coveredResources;
	}

	private String removeVariable(String log, String variable) {
		log = log.substring(0, log.length()-variable.length()-1);
		return log;
	}

	private String removeIrrelevantMarker(String s) {
		return s.replaceAll(InstrumentatorDefs.functionStartUseMarker, "")
				.replaceAll(InstrumentatorDefs.databaseUseReadMarker, "")
				.replaceAll(InstrumentatorDefs.returnUseMarker, "");}
	

	@Override
	public String getCriteriaName() {
		return "All Defs";
	}

}
