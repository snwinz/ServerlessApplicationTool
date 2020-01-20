package model.logic.Instrumentation;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SourceFile {


    public final static String[] accessPatternsDBWrite = {".putItem(", ".deleteItem(", "updateItem("};

    public final static String[] writePatternsDB = {".putItem(", ".deleteItem(", "updateItem("};
    public final static String[] readPatternsDB = {".getItem("};
    public final List<CoverageInstrumentator> selectedCoverageModes = new LinkedList<>();


    private  List<String> allLines = new ArrayList<String>();;

    public SourceFile(List<String> allLinesOfFile) {
        this.allLines = allLinesOfFile;
    }

    public void instrument(CoverageInstrumentator instrumentator ) {

        List<String> resultList = new ArrayList<String>();
        for (String line : this.allLines) {
            // Start of function
            if (line.trim().contains("exports.handler")) {
                    line = instrumentator.addCoverageStatementsHandler(line);
            }
            // invocation of other lambda function
            if (line.trim().contains(".invoke(")) {
                String nameOfFunction = line.split("\\.invoke\\(")[1].split(",")[0];
                    line = instrumentator.addCoverageStatementsInvocation(line, nameOfFunction);
            }
            //write DB
            for (String pattern : writePatternsDB) {
                if (line.trim().contains(pattern)) {
                    pattern = pattern.replace(".", "\\.").replace("(", "\\(");
                    String parameter = line.split(pattern)[1].split(",")[0];
                        line = instrumentator.addCoverageStatementDBisWritten(line, parameter);
                }
            }
            //read DB
            for (String pattern : readPatternsDB) {
                if (line.trim().contains(pattern)) {
                    pattern = pattern.replace(".", "\\.").replace("(", "\\(");
                    String parameter = line.split(pattern)[1].split(",")[0];
                        line = instrumentator.addCoverageStatementDBisRead(line, parameter);
                }
            }

            resultList.add(line);
        }
        allLines = resultList;
    }

    public void addInstrumentator(CoverageInstrumentator mode) {
        this.selectedCoverageModes.add(mode);
    }


    public void instrumentAllResources() {
        for(CoverageInstrumentator instrumentator : selectedCoverageModes){
           this.instrument(instrumentator);
        }
    }

    public List<String> getSourceCode() {
        return  this.allLines;
    }
}
