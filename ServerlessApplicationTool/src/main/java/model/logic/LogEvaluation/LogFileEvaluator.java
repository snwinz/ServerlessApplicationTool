package model.logic.LogEvaluation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;

public class LogFileEvaluator {


    public String evaluate(String text) {

        StringBuilder result
                = new StringBuilder();

        try {
            List<String> lines = Files.readAllLines(Path.of(text));
            List<String> logStatements = getLogStatements(lines);

            LinkedList<StatementEvaluator> evaluators = new LinkedList<>();

            evaluators.add(new StatementEvaluatorAR(logStatements));
            evaluators.add(new StatementEvaluatorARR(logStatements));
            evaluators.add(new StatementEvaluatorARS(logStatements));
            evaluators.add(new StatementEvaluatorDefs(logStatements));
            evaluators.add(new StatementEvaluatorDefUse(logStatements));
            evaluators.add(new StatementEvaluatorUses(logStatements));


            for (StatementEvaluator evaluator : evaluators) {

                result.append(evaluator.getCriteriaName()).append(System.lineSeparator());
                Map<String, Integer> unitsCovered = evaluator.getCoveredResources();

                var unitsCoveredSortedByOccurrence = unitsCovered.entrySet().stream().sorted(Collections.reverseOrder(comparingByValue()))
                        .toList();
                for (var entry : unitsCoveredSortedByOccurrence) {
                    result.append(String.format("%s\t%sx%n", entry.getKey(), entry.getValue()));
                }
                result.append(String.format("%n%n"));
            }


        } catch (IOException e) {
            System.err.println("File could not be read.");
        }


        return result.toString();

    }

    private List<String> getLogStatements(List<String> lines) {

        List<String> statements = new ArrayList<>();

        for (String line : lines) {
            if (line.contains("INFO")) {
                line = line.substring(line.indexOf("INFO") + "INFO".length());
                line = line.strip();
                if (line.startsWith("#")) {
                    statements.add(line);
                }
            }
        }
        return statements;
    }
}
