package model.logic.LogEvaluation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.logic.Instrumentation.Criteria.InstrumentatorDefUse;

public class StatementEvaluatorDefUse extends StatementEvaluator {

	List<String> logs = new ArrayList<>();

	public StatementEvaluatorDefUse(List<String> logStatements) {
		for (String statement : logStatements) {
			if (isStatement(statement)) {
				logs.add(statement);
			}
		}
	}

	private boolean isStatement(String statement) {
		return statement.startsWith(InstrumentatorDefUse.marker);
	}

	public Map<String, Integer> getCoveredResources() {
		List<String> coveredResources = new LinkedList<String>();

		Collection<String> defsOfReturns = getDefsAndUsesForMarkers(InstrumentatorDefUse.functionInvocationMarker,
				InstrumentatorDefUse.returnUseMarker);
		Collection<String> defsOfDBwrites = getDefsAndUsesForMarkers(InstrumentatorDefUse.databaseReadMarker,
				InstrumentatorDefUse.databaseUseReadMarker);
		Collection<String> defsOfEvents = getDefsAndUsesForMarkers(InstrumentatorDefUse.functionStartMarker,
				InstrumentatorDefUse.functionStartUseMarker);

	

		coveredResources.addAll(defsOfReturns);
		coveredResources.addAll(defsOfDBwrites);
		coveredResources.addAll(defsOfEvents);

		Map<String, Integer> unitsCovered = countNumberOfOccurrences(coveredResources);

		return unitsCovered;
	}

	private List<String> getDefsAndUsesForMarkers(String startMarker, String useMarker) {
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
						String[] defandUse = defUsed.split("_##");
						if (defandUse.length > 1) {
							coveredResources.add("Def: " +defandUse[0]);
							coveredResources.add("Use: " +defandUse[1]);
						}
						logIterator = logs.iterator();
						break;
					}
				}
			}
		}
		return coveredResources;
	}

	private String removeVariable(String log, String variable) {
		log = log.substring(0, log.length() - variable.length() - 1);
		return log;
	}

	private String removeIrrelevantMarker(String s) {
		return s.replaceAll(InstrumentatorDefUse.functionStartUseMarker, "")
				.replaceAll(InstrumentatorDefUse.databaseUseReadMarker, "")
				.replaceAll(InstrumentatorDefUse.returnUseMarker, "");
	}

	@Override
	public String getCriteriaName() {
		return "All Def Uses";
	}

}
