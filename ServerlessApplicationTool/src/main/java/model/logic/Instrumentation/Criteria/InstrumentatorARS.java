package model.logic.Instrumentation.Criteria;

import model.logic.Instrumentation.fileData.LineContainer;

public class InstrumentatorARS implements CoverageCriterion {
    public final static String marker = "#ARS_";
    public final static String functionStartMarker = marker + "S_";
    public final static String functionInvocationMarker = marker + "FI_";
    public final static String dbWriteMarker = marker + "DBW_";
    public final static String dbAccessMarker = marker + "DBR_";

    @Override
    public void addCoverageStatementsHandler(LineContainer enrichedLine, String event) {
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
                        "}",
                functionStartMarker, functionStartMarker, functionStartMarker);
        enrichedLine.addPostLine(logLine);
    }

    @Override
	public void addCoverageStatementsInvocation(LineContainer enrichedLine, String param, String returnValue) {

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

        enrichedLine.addPreLine(logLine);
    }

    @Override
	public void addCoverageStatementDBisRead(LineContainer enrichedLine, String param, String returnValue) {
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
        enrichedLine.addPreLine(logLine);
    }


    @Override
    public void addCoverageStatementDBisWritten(LineContainer enrichedLine, String param) {
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
        enrichedLine.addPreLine(logLine);
    }

	@Override
	public void addDefOfInvocationVar(LineContainer enrichedLine, String defVar) {
	}

	@Override
	public void addDefOfWrites(LineContainer lastLine, String defVar) {
	}

	@Override
	public void addDefOfReturns(LineContainer lastLine, String defVar) {
	}

	@Override
	public void addUseOfEvents(LineContainer lastLine, String useVar) {
	}

	@Override
	public void addUseOfReturn(LineContainer lastLine, String useVar) {
	}

	@Override
	public void addUseOfReads(LineContainer lastLine, String useVar) {
	}

	@Override
	public void addCoverageStatementsReturn(LineContainer line, String variable) {
	}


}
