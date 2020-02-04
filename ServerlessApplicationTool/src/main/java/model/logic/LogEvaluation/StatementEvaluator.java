package model.logic.LogEvaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StatementEvaluator {

    abstract public Map<String, Integer> getCoveredResources();


    public Map<String, Integer> countNumberOfOccurrences(List<String> inputList) {
        Map<String, Integer> resultMap = new HashMap<>();
        inputList.forEach(e -> resultMap.put(e, resultMap.getOrDefault(e, 0) + 1));
        return resultMap;
    }

    abstract public String getCriteriaName();

}
