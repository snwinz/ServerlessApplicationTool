package model.logic.Instrumentation.Criteria;


public class InstrumentatorAR implements CoverageCriterion {


    public final static String marker = "#AR_";
    public final static String functionStartMarker = marker + "S_";
    public final static String functionInvocationMarker = marker + "FI_";
    public final static String dbAccessMarker = marker + "DBA_";
    
    
	@Override
	public String addCoverageStatementsHandler(String event) {
		 String logLine = String.format( "{%n"
                 + "        if(%s.Records != undefined){%n"
                 + "            console.log('%s' + %s.Records[0].eventSourceARN.split(':')[5].split('/')[1]);\r\n"
                 + "        }%n"
                 + "        console.log('%s' + context.functionName);%n"
                 + "}%n",
         event, functionStartMarker, event, functionStartMarker, functionStartMarker);
		return logLine;
	}

    @Override
    public String addCoverageStatementsInvocation(String param, String returnValue) {
        String logLine = String.format("\tconsole.log('%s' + %s.FunctionName);", functionInvocationMarker,
                param);
        return logLine;
    }

    @Override
    public String addCoverageStatementDBisRead(String param, String returnValue) {
    	return this.addCoverageStatementDBisWritten(param);
    }


    @Override
    public String addCoverageStatementDBisWritten(String param) {
        String logLine = String.format("\tconsole.log('%s' + %s.TableName);", dbAccessMarker, param);
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

	@Override
	public void activateDeletion() {
		
	}
	@Override
	public String addDefOfDeletes(String deleteParameter, int line) {
		return "";
	}

	@Override
	public boolean isDeletionInstrumentation() {
		return false;
	}

}
