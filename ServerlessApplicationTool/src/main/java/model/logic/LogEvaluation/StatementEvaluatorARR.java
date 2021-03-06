package model.logic.LogEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.logic.Instrumentation.Criteria.InstrumentatorARR;

public class StatementEvaluatorARR extends StatementEvaluator{

    List<String> logs = new ArrayList<>();

    public StatementEvaluatorARR(List<String> logStatements) {
        for (String statement : logStatements) {
            if (isStatement(statement)) {
                logs.add(statement);
            }
        }
    }

    private boolean isStatement(String statement) {
        return statement.startsWith(InstrumentatorARR.marker);
    }

    @Override
    public Map<String, Integer> getCoveredResources() {
        List<String> coveredResources =
                logs.stream().filter(s -> s.startsWith(InstrumentatorARR.functionInvocationMarker) || s.startsWith(InstrumentatorARR.dbAccessMarker)).map(a -> a.replaceAll(InstrumentatorARR.functionInvocationMarker, "").replaceAll(InstrumentatorARR.dbAccessMarker, "")).collect(Collectors.toList());

        Map<String, Integer> unitsCovered = countNumberOfOccurrences(coveredResources);

        return unitsCovered;
    }
    @Override
    public String getCriteriaName() {
        return "All Resources Relations";
    }

}
