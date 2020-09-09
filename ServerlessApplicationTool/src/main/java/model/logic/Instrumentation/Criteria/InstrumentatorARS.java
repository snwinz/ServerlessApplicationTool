package model.logic.Instrumentation.Criteria;


public class InstrumentatorARS implements CoverageCriterion {
    public final static String marker = "#ARS_";
    public final static String functionStartMarker = marker + "S_";
    public final static String functionInvocationMarker = marker + "FI_";
    public final static String dbWriteMarker = marker + "DBW_";
    public final static String dbAccessMarker = marker + "DBR_";


    @Override
    public String addCoverageStatementsHandler(String event) {
        String logLine = String.format(
                "{%n" +
                        "   var oldSeqDB = %s.Records;%n" +
                        "   if (oldSeqDB != undefined) {%n" +
                        "      try {%n" +
                        "         oldSeqDB = %s.Records[0].dynamodb.NewImage.oldSeq.S;%n" +
                        "      }%n" +
                        "       catch (error) {%n" +
                        "         oldSeqDB = undefined;%n" +
                        "         console.error(\"old sequence could not be found: \" + error);%n" +
                        "      }%n" +
                        "    }%n" +
                        "    var oldSeqFunctionCall = %s.oldSeq;%n" +
                        "    if(oldSeqDB == undefined && oldSeqFunctionCall== undefined){%n" +
                        "        console.log('%s' + context.functionName + ';' + ' ');%n" +
                        "    }else if(oldSeqDB != undefined){%n" +
                        "        console.log('%s' + oldSeqDB + context.functionName + ';' + ' ');%n" +
                        "    }else if(oldSeqFunctionCall != undefined){%n" +
                        "        console.log('%s' + oldSeqFunctionCall+ context.functionName + ';' + ' ');%n" +
                        "    }else{%n" +
                        "        console.log(\"Error: Function cannot be called by DB and Function. Check calling resource.\");%n" +
                        "    }%n" +
                        "}",
                event, event, event, functionStartMarker, functionStartMarker, functionStartMarker);
        return logLine;
    }
    
    
    @Override
	public String addCoverageStatementsInvocation(String param, String returnValue) {

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
                "}", param, functionInvocationMarker);
        return logLine;
    }
    
    


    @Override
   	public String addCoverageStatementDBisRead(String param, String returnValue) {
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
                           "}",
                   param, dbAccessMarker);
           return logLine;
       }

    

    @Override
    public String addCoverageStatementDBisWritten(String param) {
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
        return logLine;
    }
    
	
	@Override
	public String addDefOfInvocationVar(String defVar, int line) {
		return "";
	}


	@Override
	public String addDefOfWrites(String defVar, int line) {
		return "";
	}
	
	@Override
	public String addDefOfReturns(String defVar, int line) {
		return "";
	}


	@Override
	public String addUseOfEvents(String useVar, int line) {
		return "";
	}
	
	
	@Override
	public String addUseOfReturn(String useVar, int line) {
		return "";
	}


	@Override
	public String addUseOfReads(String useVar, int line) {
		return "";
	}

	
	@Override
	public String addCoverageStatementsReturn(String variable) {
		return "";
	}
}
