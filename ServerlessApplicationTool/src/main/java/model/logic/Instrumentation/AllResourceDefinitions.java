package model.logic.Instrumentation;

public class AllResourceDefinitions implements CoverageInstrumentator {

    public final static String ARD_LOG_FUNCTIONSTART = "#ARD_S_";
    public final static String ARD_LOG_FUNCTIONINVOKATION = "#ARD_FI_";
    public final static String ARD_LOG_DBWRITE = "#ARD_DBW_";
    public final static String ARD_LOG_DBREAD = "#ARD_DBR_";

    private static int currentID = 0;

    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format("%n   if (event.funcDef != undefined) {%n" +
                "     console.log('%s' + event.funcDef);%n" +
                "   } ", ARD_LOG_FUNCTIONSTART);
        return line + logLine;
    }

    @Override
    public String addCoverageStatementsInvocation(String line, String nameOfFunction) {
        String logLine = String.format("    %s.Payload = %s.Payload.replace('\\{','{\"funcDef\" : \"' + context.functionName + '_%s\",');%n" +
                "    console.log('%s' + JSON.parse(%s.Payload).funcDef);%n", nameOfFunction, nameOfFunction, currentID, ARD_LOG_FUNCTIONINVOKATION, nameOfFunction);
        currentID++;
        line = logLine + line;
        return line;
    }

    @Override
    public String addCoverageStatementDBisRead(String line, String param) {
        String logLine = String.format("%nif (!err) {%n" +
                "        if (data.Item != undefined) {%n" +
                "            var definition = data.Item.funcDef;%n" +
                "            if (definition != undefined) {%n" +
                "                console.log('%s' + definition.S);%n" +
                "            }%n" +
                "        }%n" +
                "    }", ARD_LOG_DBREAD);

        line = line + logLine;
        return line;
    }

    @Override
    public String addCoverageStatementDBisWritten(String line, String param) {
        String logLine = String.format(
                "          %s.Item.funcDef = {S:  context.functionName + '_%s'};%n" +
                        "          console.log('%s' + %s.Item.funcDef.S);%n", param, currentID,
                ARD_LOG_DBWRITE, param);
        currentID++;
        line = logLine + line;
        return line;
    }
}
