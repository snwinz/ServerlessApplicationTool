package model.logic.LogEvaluation;

import model.logic.Instrumentation.InstrumentatorUses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatementEvaluatorUses extends StatementEvaluator {

    List<String> logs = new ArrayList<>();

    public StatementEvaluatorUses(List<String> logStatements) {
        for (String statement : logStatements) {
            if (isStatement(statement)) {
                logs.add(statement);
            }
        }
    }

    private boolean isStatement(String statement) {
        return statement.startsWith(InstrumentatorUses.marker);
    }

    public Map<String, Integer> getCoveredResources() {
        List<String> coveredResources =
                logs.stream().filter(s -> hasRelevantMarker(s)).map(s -> removeRelevantMarker(s)).collect(Collectors.toList());
        Map<String, Integer> unitsCovered = countNumberOfOccurrences(coveredResources);

        System.out.println("All uses coverage:");
        unitsCovered.forEach((k, v) -> {
            System.out.println(k);
            System.out.println("Occurrences: " + v);
        });
        return unitsCovered;
    }

    private boolean hasRelevantMarker(String s) {
        return
                s.startsWith(InstrumentatorUses.databaseReadMarker) || s.startsWith(InstrumentatorUses.functionStartMarker);

    }

    private String removeRelevantMarker(String s) {
        return s.replaceAll(InstrumentatorUses.databaseReadMarker, "").replaceAll(InstrumentatorUses.functionStartMarker, "");
    }

    @Override
    public String getCriteriaName() {
        return "All Uses";
    }


}


