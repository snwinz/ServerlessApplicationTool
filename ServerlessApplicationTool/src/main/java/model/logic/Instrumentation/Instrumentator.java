package model.logic.Instrumentation;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Instrumentator {

	public final static String ARR_LOG_FUNCTIONSTART = "#ARR_S_";
	public final static String ARR_LOG_FUNCTIONINVOKATION = "#ARR_FI_";
	public final static String ARR_LOG_DBACCESS = "#ARR_DBA_";

	public final static String[] accessPatternsDB = { ".putItem(", ".getItem(", ".deleteItem(", "updateItem(" };

	public static List<String> instrument(Path file) {
		List<String> allLinesOfFile = new ArrayList<String>();
		try {
			allLinesOfFile = Files.readAllLines(file);
			SourceFile sourceText = new SourceFile(allLinesOfFile);
			// sourceText.addInstrumentator(new AllResourcesMode());
			//sourceText.addInstrumentator(new AllResourceRelations());
			sourceText.addInstrumentator(new AllResourceSequences());
			sourceText.instrumentAllResources();
			// allLinesOfFile = addAllResourceRelationsCoverage(allLinesOfFile,
			// functionName);
			allLinesOfFile = sourceText.getSourceCode();
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
}
