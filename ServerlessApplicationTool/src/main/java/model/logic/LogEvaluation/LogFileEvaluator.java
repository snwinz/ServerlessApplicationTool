package model.logic.LogEvaluation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class LogFileEvaluator {
    HashMap<String, Integer> allResourcesCovered = new HashMap<>();



    public String evaluate(String text) {


        try {
            List<String> lines = Files.readAllLines(Path.of(text));
            for(String line : lines){



            }


        } catch (IOException e) {
            System.err.println("File could not be read.");
        }


        return "";

    }
}
