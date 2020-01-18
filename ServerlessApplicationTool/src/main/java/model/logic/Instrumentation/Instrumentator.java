package model.logic.Instrumentation;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Instrumentator {

	public final static String AR_LOG_FUNCTIONSTART = "#AR_S_";
	public final static String AR_LOG_FUNCTIONINVOKATION = "#AR_FI_";
	public final static String AR_LOG_DBWRITE = "#AR_DBWR_";

	public final static String ARR_LOG_FUNCTIONSTART = "#ARR_S_";
	public final static String ARR_LOG_FUNCTIONINVOKATION = "#ARR_FI_";
	public final static String ARR_LOG_DBACCESS = "#ARR_DBA_";

	public final static String ARS_LOG_FUNCTIONSTART = "#ARS_S_";
	public final static String ARS_LOG_FUNCTIONINVOKATION = "#ARS_FI_";
	public final static String ARS_LOG_DBACCESS = "#ARS_DBA_";

	public final static String[] accessPatternsDB = { ".putItem(", ".getItem(", ".deleteItem(", "updateItem(" };

	public final static String[] accessPatternsDBWrite = { ".putItem(", ".deleteItem(", "updateItem(" };

	public static List<String> instrument(Path file) {
		List<String> allLinesOfFile = new ArrayList<String>();
		try {
			allLinesOfFile = Files.readAllLines(file);
			String functionName = getFunctionName(file);
			// allLinesOfFile = addAllResourcesCoverage(allLinesOfFile, functionName);
			// allLinesOfFile = addAllResourceRelationsCoverage(allLinesOfFile,
			// functionName);
			allLinesOfFile = addAllResourceSequencesCoverage(allLinesOfFile, functionName);

		} catch (IOException e) {
			System.err.format("file %s could not be read.%n", file.toString());
		}

		return allLinesOfFile;
	}

	private static String getFunctionName(Path file) {
		String filename = file.getFileName().toString();
		if (filename.contains(".")) {
			filename = filename.split("\\.")[0];
		}
		return filename;
	}

	public static void instrumentFilesOfFolder(String directoryToSourceCodeOfFunctions) {
		Path folderOfInstrumentedFunctions = createFolderForResult(directoryToSourceCodeOfFunctions);

		try (DirectoryStream<Path> directoryStreamFunctions = Files
				.newDirectoryStream(Paths.get(directoryToSourceCodeOfFunctions), "*.{js}")) {
			for (Path file : directoryStreamFunctions) {

				System.out.println(file.toString());
				List<String> instrumentedFile = Instrumentator.instrument(file);
				saveInstrumentedFile(instrumentedFile, folderOfInstrumentedFunctions, file.getFileName().toString());
			}
		} catch (IOException ex) {
			System.err.println("Graph could not be created");
		}

	}

	private static void saveInstrumentedFile(List<String> instrumentedSourceCode, Path folderOfInstrumentedFunctions,
			String fileName) {
		Path instrumentedFile = Paths.get(folderOfInstrumentedFunctions.toAbsolutePath() + "/" + fileName);
		System.out.println(instrumentedFile.getFileName());

		try {
			Files.write(instrumentedFile, instrumentedSourceCode, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Path createFolderForResult(String directoryToSourceCodeOfFunctions) {
		Path dirPathObj = Paths.get(directoryToSourceCodeOfFunctions + "/" + "instrumentedFunctions");
		boolean dirExists = Files.exists(dirPathObj);
		if (dirExists) {
			System.out.println("! Directory Already Exists !");
		} else {
			try {
				Files.createDirectories(dirPathObj);
				System.out.format("Created new directory for instrumented function in: %s", dirPathObj.normalize());

			} catch (IOException ioExceptionObj) {
				System.out.println(
						"Problem Occured While Creating The Directory Structure= " + ioExceptionObj.getMessage());
			}
		}
		return dirPathObj;
	}

	private static List<String> addAllResourcesCoverage(List<String> allLinesOfFile, String functionName) {

		List<String> resultList = new ArrayList<String>();
		for (String line : allLinesOfFile) {
			// Start of function
			if (line.trim().contains("exports.handler")) {
				String logLine = String.format("%n\tconsole.log('%s' + context.functionName);", AR_LOG_FUNCTIONSTART);
				line += logLine;
			}
			// invocation of other lambda function
			if (line.trim().contains(".invoke(")) {
				String parameter = line.split("\\.invoke\\(")[1].split(",")[0];
				String logLine = String.format("\tconsole.log('%s' + %s.FunctionName);%n", AR_LOG_FUNCTIONINVOKATION,
						parameter);
				line = logLine + line;
			}

			line = adaptLineIfDBisAccessed(line);
			resultList.add(line);
		}

		return resultList;
	}

	private static String adaptLineIfDBisAccessed(String line) {
		for (String pattern : accessPatternsDB) {
			if (line.trim().contains(pattern)) {
				pattern = pattern.replace(".", "\\.").replace("(", "\\(");
				String parameter = line.split(pattern)[1].split(",")[0];
				String logLine = String.format("\tconsole.log('%s' + %s.TableName);%n", AR_LOG_DBWRITE, parameter);
				line = logLine + line;
				break;
			}
		}
		return line;
	}

	private static List<String> addAllResourceRelationsCoverage(List<String> allLinesOfFile, String functionName) {
		List<String> resultList = new ArrayList<String>();
		for (String line : allLinesOfFile) {
			// Start of function
			if (line.trim().contains("exports.handler")) {
				String logLine = String.format("%n\tif(event.Records != undefined){%n"
						+ "\t\tconsole.log('%s' + event.Records[0].eventSourceARN.split(':')[5].split('/')[1] + ';' + context.functionName);%n"
						+ " \t};", ARR_LOG_FUNCTIONSTART);
				line += logLine;
			}
			// invocation of other lambda function
			if (line.trim().contains(".invoke(")) {
				String parameter = line.split("\\.invoke\\(")[1].split(",")[0];

				// not needed since logged when calling
				// String additionalPayload = String.format("if(params.Payload==undefined){%n"
				// + " %s.Payload = '{'caller' : '' + context.functionName + ''}';%n" +
				// "}else{%n"
				// + " %s.Payload = params.Payload.replace('\\{','{'caller' : '' +
				// context.functionName + '', ');%n"
				// + "}", parameter, parameter);

				String logLine = String.format("\tconsole.log('%s' + context.functionName + ';' + %s.FunctionName);%n",
						ARR_LOG_FUNCTIONINVOKATION, parameter);
				line = logLine + line;
			}
			line = adaptLineIfDBisAccessed_ARR(line);
			resultList.add(line);
		}

		return resultList;
	}

	private static String adaptLineIfDBisAccessed_ARR(String line) {
		for (String pattern : accessPatternsDB) {
			if (line.trim().contains(pattern)) {
				pattern = pattern.replace(".", "\\.").replace("(", "\\(");
				String parameter = line.split(pattern)[1].split(",")[0];
				String logLine = String.format("\tconsole.log('%s'+ context.functionName + ';' + %s.TableName);%n",
						ARR_LOG_DBACCESS, parameter);
				line = logLine + line;
				break;
			}
		}
		return line;
	}

	private static List<String> addAllResourceSequencesCoverage(List<String> allLinesOfFile, String functionName) {
		List<String> resultList = new ArrayList<String>();
		for (String line : allLinesOfFile) {
			// Start of function
			if (line.trim().contains("exports.handler")) {
				String logLine = String.format(
						"\tvar oldSeq = '';%n" + "\tif(event.oldSeq != undefined){%n" + "\toldSeq = event.oldSeq;%n"
								+ "\t}%n" + "\tconsole.log(oldSeq + context.functionName + ';');",
						ARS_LOG_FUNCTIONSTART);

				line += logLine;
			}

			// invocation of other lambda function
			if (line.trim().contains(".invoke(")) {
				String parameter = line.split("\\.invoke\\(")[1].split(",")[0];
				String additionalPayload = String.format("if(%s.Payload==undefined){%n" 
						+"%s.Payload = '{'oldSeq' : '' + context.functionName + ''}';%n",null);
				
				// String additionalPayload = String.format("if(params.Payload==undefined){%n"
				// + " %s.Payload = '{'caller' : '' + context.functionName + ''}';%n" +
				// "}else{%n"
				// + " %s.Payload = params.Payload.replace('\\{','{'caller' : '' +
				// context.functionName + '', ');%n"
				// + "}", parameter, parameter);

				String logLine = String.format("\tconsole.log('%s' + context.functionName + ';' + %s.FunctionName);%n",
						ARS_LOG_FUNCTIONINVOKATION, parameter);
				line = logLine + line;
			}
			line = adaptLineIfDBisWritten(line);
			resultList.add(line);
		}

		return resultList;
	}

	private static String adaptLineIfDBisWritten(String line) {
		for (String pattern : accessPatternsDB) {
			if (line.trim().contains(pattern)) {
				pattern = pattern.replace(".", "\\.").replace("(", "\\(");
				String parameter = line.split(pattern)[1].split(",")[0];

				String logLine = String.format("\tvar oldSeq = '';%n" + "\tif(event.oldSeq != undefined){%n"
						+ "\toldSeq = event.oldSeq;    %n" + "\t}%n"
						+ "\t%s.Item.tracevalue = {S: oldSeq + context.functionName +';'+ %s.TableName +';'};%n"+
						"\tconsole.log(oldSeq + context.functionName + ';' + %s.TableName + ';');%n",
						parameter, parameter,parameter);
				line = logLine + line;
				break;
			}
		}
		return line;
	}

}
