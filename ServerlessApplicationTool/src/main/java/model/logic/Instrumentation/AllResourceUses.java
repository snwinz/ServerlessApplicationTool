package model.logic.Instrumentation;

public class AllResourceUses implements CoverageInstrumentator {

    public final static String ARU_LOG_FUNCTIONSTART = "#ARU_S_";
    public final static String ARU_LOG_FUNCTIONINVOKATION = "#ARU_FI_";
    public final static String ARU_LOG_DBWRITE = "#ARU_DBW_";
    public final static String ARU_LOG_DBREAD = "#ARU_DBR_";

    private static int currentIDDef = 0;
    private static int currentIDUse = 0;
    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format("%n   if (event.funcDef != undefined) {%n" +
                "     console.log('%s' + event.funcDef + '_%s');%n" +
                "   } ", ARU_LOG_FUNCTIONSTART, currentIDUse);
        currentIDUse++;
        return line + logLine;
    }

    @Override
    public String addCoverageStatementsInvocation(String line, String nameOfFunction) {
        String logLine = String.format("    %s.Payload = %s.Payload.replace('\\{','{\"funcDef\" : \"' + context.functionName + '_%s\",');%n" +
                "    console.log('%s' + JSON.parse(%s.Payload).funcDef);%n", nameOfFunction, nameOfFunction, currentIDDef, ARU_LOG_FUNCTIONINVOKATION, nameOfFunction);
        currentIDDef++;
        line = logLine + line;
        return line;
    }

    @Override
    public String addCoverageStatementDBisRead(String line, String param) {
        String logLine = String.format("%nif (!err) {%n" +
                "        if (data.Item != undefined) {%n" +
                "            var definition = data.Item.funcDef;%n" +
                "            if (definition != undefined) {%n" +
                "                console.log('%s' + definition.S + '_%s');%n" +
                "            }%n" +
                "        }%n" +
                "    }",ARU_LOG_DBREAD, currentIDUse);
        currentIDUse++;
        line = line + logLine;
        return line;
    }

    @Override
    public String addCoverageStatementDBisWritten(String line, String param) {
        String logLine = String.format(
                "          %s.Item.funcDef = {S:  context.functionName + '_%s'};%n" +
                        "          console.log('%s' + %s.Item.funcDef.S);%n", param, currentIDDef,
                ARU_LOG_DBWRITE, param);
        currentIDDef++;
        line = logLine + line;
        return line;
    }
}
