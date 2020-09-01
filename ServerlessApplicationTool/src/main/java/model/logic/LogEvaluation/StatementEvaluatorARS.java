package model.logic.LogEvaluation;

import java.util.*;
import java.util.stream.Collectors;

import model.logic.Instrumentation.Criteria.InstrumentatorARS;

public class StatementEvaluatorARS extends StatementEvaluator {

    List<String> logs = new ArrayList<>();

    public StatementEvaluatorARS(List<String> logStatements) {
        for (String statement : logStatements) {
            if (isStatement(statement)) {
                logs.add(statement);
            }
        }
    }

    private boolean isStatement(String statement) {
        return statement.startsWith(InstrumentatorARS.marker);
    }

    public Map<String, Integer> getCoveredResources() {
        List<String> coveredResources =
                logs.stream().filter(s -> hasRelevantMarker(s)).map(s -> removeRelevantMarker(s)).collect(Collectors.toList());
        Map<String, Integer> unitsCovered = countNumberOfOccurrences(coveredResources);

        System.out.println("ARS coverage:");
        unitsCovered.forEach((k, v) -> {
            System.out.println(k);
            System.out.println("Occurrences: " + v);
        });
        return unitsCovered;
    }

    private boolean hasRelevantMarker(String s) {
        return
                s.startsWith(InstrumentatorARS.functionStartMarker) || s.startsWith(InstrumentatorARS.dbAccessMarker) || s.startsWith(InstrumentatorARS.dbWriteMarker);
    }

    private String removeRelevantMarker(String s) {
        return s.replaceAll(InstrumentatorARS.functionStartMarker, "").replaceAll(InstrumentatorARS.dbAccessMarker, "").replaceAll(InstrumentatorARS.dbWriteMarker, "");
    }
    @Override
    public String getCriteriaName() {
        return "All Resource Sequences";
    }


}


