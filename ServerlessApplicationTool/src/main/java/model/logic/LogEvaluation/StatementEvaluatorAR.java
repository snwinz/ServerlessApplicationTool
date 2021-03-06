package model.logic.LogEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.logic.Instrumentation.Criteria.InstrumentatorAR;

public class StatementEvaluatorAR extends StatementEvaluator {

    List<String> logs = new ArrayList<>();

    public StatementEvaluatorAR(List<String> logStatements) {
        for (String statement : logStatements) {
            if (isStatement(statement)) {
                logs.add(statement);
            }
        }
    }

    private boolean isStatement(String statement) {
        return statement.startsWith(InstrumentatorAR.marker);
    }

    @Override
    public Map<String, Integer> getCoveredResources() {
        List<String> coveredResources =
                logs.stream().filter(s -> s.startsWith(InstrumentatorAR.functionStartMarker) || s.startsWith(InstrumentatorAR.dbAccessMarker)).map(a -> a.replaceAll(InstrumentatorAR.functionStartMarker, "").replaceAll(InstrumentatorAR.dbAccessMarker, "")).collect(Collectors.toList());

        Map<String, Integer> unitsCovered = countNumberOfOccurrences(coveredResources);

        return unitsCovered;
    }

    @Override
    public String getCriteriaName() {
        return "All Resources";
    }

}
