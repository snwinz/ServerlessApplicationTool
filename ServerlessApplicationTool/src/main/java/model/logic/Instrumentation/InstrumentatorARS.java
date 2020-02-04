package model.logic.Instrumentation;


public class InstrumentatorARS implements CoverageInstrumentator {
    public final static String marker = "#ARS_";
    public final static String functionStartMarker = marker + "S_";
    public final static String functionInvocationMarker = marker + "FI_";
    public final static String dbWriteMarker = marker + "DBW_";
    public final static String dbAccessMarker = marker + "DBR_";

    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format(
                "%n{%n" +
                        "   var oldSeqDB = event.Records;%n" +
                        "   if (oldSeqDB != undefined) {%n" +
                        "      try {%n" +
                        "         oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                        "      }%n" +
                        "       catch (error) {%n" +
                        "         oldSeqDB = undefined;%n" +
                        "         console.error(\"old sequence could not be found: \" + error);%n" +
                        "      }%n" +
                        "    }" +
                        "   if (oldSeqDB != undefined) {%n" +
                        "      oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                        "    }%n" +
                        "    var oldSeqFunctionCall = event.oldSeq;%n" +
                        "    if(oldSeqDB == undefined && oldSeqFunctionCall== undefined){%n" +
                        "        console.log('%s' + context.functionName + ';' + ' ');%n" +
                        "    }else if(oldSeqDB != undefined){%n" +
                        "        console.log('%s' + oldSeqDB + context.functionName + ';' + ' ');%n" +
                        "    }else if(oldSeqFunctionCall != undefined){%n" +
                        "        console.log('%s' + oldSeqFunctionCall+ context.functionName + ';' + ' ');%n" +
                        "    }else{%n" +
                        "        console.log(\"Error: Function cannot be called by DB and Function. Check calling resource.\");%n" +
                        "    }%n" +
                        "}%n",
                functionStartMarker, functionStartMarker, functionStartMarker);
        line += logLine;
        return line;
    }

    @Override
    public String addCoverageStatementsInvocation(String line, String nameOfFunction) {

        String logLine = String.format("{%n" +
                "    var passingParameter = %s;%n" +
                "    {%n" +
                "   var oldSeqDB = event.Records;%n" +
                "   if (oldSeqDB != undefined) {%n" +
                "      try {%n" +
                "         oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                "      }%n" +
                "       catch (error) {%n" +
                "         oldSeqDB = undefined;%n" +
                "         console.error(\"old sequence could not be found: \" + error);%n" +
                "      }%n" +
                "    }" +
                "   if (oldSeqDB != undefined) {%n" +
                "      oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                "    }" +
                "        var passedSequence = '';%n" +
                "        var oldSeqFunctionCall = event.oldSeq;%n" +
                "        if(oldSeqDB == undefined && oldSeqFunctionCall== undefined){%n" +
                "            passedSequence = '';%n" +
                "        }else if(oldSeqDB != undefined){%n" +
                "            passedSequence= oldSeqDB ;%n" +
                "        }else if(oldSeqFunctionCall != undefined){%n" +
                "            passedSequence =  oldSeqFunctionCall;%n" +
                "        }else{%n" +
                "            console.log(\"Error: Function is not called by DB or Lambda. Check calling resource.\");%n" +
                "        }%n" +
                "    }%n" +
                "    passingParameter.Payload = passingParameter.Payload.replace	('\\{','{\"oldSeq\" : \"' + passedSequence + context.functionName + ';\",');%n " +
                "    console.log('%s' + JSON.parse(passingParameter.Payload).oldSeq + passingParameter.FunctionName + ';' + ' ');%n" +
                "}%n", nameOfFunction, functionInvocationMarker);

        line = logLine + line;
        return line;
    }

    @Override
    public String addCoverageStatementDBisRead(String line, String param) {
        String logLine = String.format("{%n" +
                        "    var passingParameter = %s;%n" +
                        "    {%n" +
                        "   var oldSeqDB = event.Records;%n" +
                        "   if (oldSeqDB != undefined) {%n" +
                        "      oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                        "    }%n" +
                        "        var oldSeqFunctionCall = event.oldSeq;%n" +
                        "        var sequencePassed = \"\";%n" +
                        "        if(oldSeqDB == undefined && oldSeqFunctionCall== undefined){%n" +
                        "            sequencePassed = \"\";%n" +
                        "        }else if(oldSeqDB != undefined){%n" +
                        "            sequencePassed = oldSeqDB;%n" +
                        "        }else if(oldSeqFunctionCall != undefined){%n" +
                        "            sequencePassed = oldSeqFunctionCall;%n" +
                        "        }else{%n" +
                        "            console.log(\"Error: Function is not called by DB or Lambda. Check calling resource.\");%n" +
                        "        }%n" +
                        "         console.log('%s' + sequencePassed + context.functionName + ';' + passingParameter.TableName + ';' + ' ');%n" +
                        "    }%n" +
                        "}%n",
                param, dbAccessMarker);
        line = logLine + line;
        return line;
    }


    @Override
    public String addCoverageStatementDBisWritten(String line, String param) {
        String logLine = String.format("{%n" +
                        "    var passingParameter = %s;%n" +
                        "    {%n" +
                        "   var oldSeqDB = event.Records;%n" +
                        "   if (oldSeqDB != undefined) {%n" +
                        "      oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                        "    }%n" +
                        "        var oldSeqFunctionCall = event.oldSeq;%n" +
                        "        var sequencePassed = \"\";%n" +
                        "        if(oldSeqDB == undefined && oldSeqFunctionCall== undefined){%n" +
                        "            sequencePassed = \"\";%n" +
                        "        }else if(oldSeqDB != undefined){%n" +
                        "            sequencePassed = oldSeqDB;%n" +
                        "        }else if(oldSeqFunctionCall != undefined){%n" +
                        "            sequencePassed = oldSeqFunctionCall;%n" +
                        "        }else{%n" +
                        "            console.log(\"Error: Function is not called by DB or Lambda. Check calling resource.\");%n" +
                        "        }%n" +
                        "        passingParameter.Item.oldSeq = {S: sequencePassed + context.functionName +';'+ passingParameter.TableName +';'}%n" +
                        "    }%n" +
                        "    console.log('%s' + passingParameter.Item.oldSeq.S + ' ');%n" +
                        "}",
                param, dbWriteMarker);
        line = logLine + line;
        return line;
    }


}
