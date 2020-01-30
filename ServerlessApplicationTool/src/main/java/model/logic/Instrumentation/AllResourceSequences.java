package model.logic.Instrumentation;




public class AllResourceSequences implements CoverageInstrumentator {
    public final static String ARS_LOG_FUNCTIONSTART = "#ARS_S_";
    public final static String ARS_LOG_FUNCTIONINVOKATION = "#ARS_FI_";
    public final static String ARS_LOG_DBACCESS = "#ARS_DBA_";
    public final static String ARS_LOG_DBREAD= "#ARS_DBR_";
    
    @Override
    public String addCoverageStatementsHandler(String line) {
        String logLine = String.format(
                "{%n" +
                        "   var oldSeqDB = event.Records;%n" + 
                        "   if (oldSeqDB != undefined) {%n" + 
                        "      try {%n" + 
                        "         oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" + 
                        "      }%n" + 
                        "       catch (error) {%n" + 
                        "         oldSeqDB = undefined;%n" + 
                        "         console.error(\"old sequence could not be found: \" + error);%n" + 
                        "      }%n" + 
                        "    }"+
                        "   if (oldSeqDB != undefined) {%n" +
                        "      oldSeqDB = event.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                        "    }%n" +
                        "    var oldSeqFunctionCall = event.oldSeq;%n" +
                        "    if(oldSeqDB == undefined && oldSeqFunctionCall== undefined){%n" +
                        "        console.log('%s' + context.functionName + ';');%n" +
                        "    }else if(oldSeqDB != undefined){%n" +
                        "        console.log('%s' + oldSeqDB + context.functionName + ';');%n" +
                        "    }else if(oldSeqFunctionCall != undefined){%n" +
                        "        console.log('%s' + oldSeqFunctionCall+ context.functionName + ';');%n" +
                        "    }else{%n" +
                        "        console.log(\"Error: Function cannot be called by DB and Function. Check calling resource.\");%n" +
                        "    }%n" +
                        "}%n",
                ARS_LOG_FUNCTIONSTART, ARS_LOG_FUNCTIONSTART, ARS_LOG_FUNCTIONSTART);
        line += logLine;
        return  line;
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
                "    }"+
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
                "    console.log('%s' + JSON.parse(passingParameter.Payload).oldSeq + passingParameter.FunctionName + ';');%n" +
                "}%n", nameOfFunction, ARS_LOG_FUNCTIONINVOKATION);

        line = logLine + line;
        return  line;
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
                        "         console.log('%s' + sequencePassed + context.functionName + ';' + passingParameter.TableName + ';');%n" +
                        "        passingParameter.Item.oldSeq = {S: sequencePassed + context.functionName +';'+ passingParameter.TableName +';'}%n" +
                        "    }%n" +
                        "}%n",
                param, ARS_LOG_DBACCESS);
        line = logLine + line;
        return line;
    }


    @Override
    public String addCoverageStatementDBisWritten(String line, String param){
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
                        "    console.log('%s' + passingParameter.Item.oldSeq.S);%n" +
                        "}",
                param, ARS_LOG_DBACCESS);
        line = logLine + line;
        return line;
}

}
