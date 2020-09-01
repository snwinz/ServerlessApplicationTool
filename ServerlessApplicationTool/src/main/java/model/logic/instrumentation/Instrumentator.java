package model.logic.instrumentation;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.logic.Instrumentation.Criteria.CoverageCriterion;
import model.logic.Instrumentation.Criteria.InstrumentatorAR;
import model.logic.Instrumentation.Criteria.InstrumentatorARR;
import model.logic.Instrumentation.Criteria.InstrumentatorARS;
import model.logic.Instrumentation.Criteria.InstrumentatorDefs;
import model.logic.Instrumentation.Criteria.InstrumentatorUses;
import model.logic.Instrumentation.fileData.SourceFile;
import model.logic.Instrumentation.logic.InstrumentationController;

public class Instrumentator {

	private List<CoverageMode> coverageModes = new ArrayList<CoverageMode>();
	public final List<CoverageCriterion> selectedCoverageModes = new LinkedList<>();
	private SourceFile sourceFile;

	public void addCoverageMode(CoverageMode mode) {
		coverageModes.add(mode);
	}

	public List<String> instrument(Path file) {
		List<String> allLinesOfFile = new ArrayList<String>();
		try {
			allLinesOfFile = Files.readAllLines(file);
			sourceFile = new SourceFile(allLinesOfFile);
			instrumentAllResources();
			allLinesOfFile = sourceFile.getSourceWithInstrumentation();
		} catch (IOException e) {
			System.err.format("file %s could not be read.%n", file.toString());
		}

		return allLinesOfFile;
	}

	private void addInstrumentors() {
		for (CoverageMode mode : coverageModes) {
			switch (mode) {
			case AR:
				addInstrumentator(new InstrumentatorAR());
				break;
			case ARR:
				addInstrumentator(new InstrumentatorARR());
				break;
			case ARS:
				addInstrumentator(new InstrumentatorARS());
				break;
			case ARD:
				addInstrumentator(new InstrumentatorDefs());
				break;
			case ARU:
				addInstrumentator(new InstrumentatorUses());
				break;
			default:
				break;
			}

		}
	}

	public void addInstrumentator(CoverageCriterion mode) {
		this.selectedCoverageModes.add(mode);
	}

	public void instrumentAllResources() {
		for (CoverageCriterion instrumentator : selectedCoverageModes) {
			InstrumentationController instrumentation = new InstrumentationController(sourceFile);
			instrumentation.instrument(instrumentator);
		}
	}

	public void instrumentFilesOfFolder(String directoryToSourceCodeOfFunctions) {
		addInstrumentors();
		if (!isFolderAvailable(directoryToSourceCodeOfFunctions)) {
			throw new IllegalArgumentException("Path not available: " + directoryToSourceCodeOfFunctions);
		}

		for (CoverageMode mode : coverageModes) {
			System.out.println(mode);
		}
		System.out.println();

		Path folderOfInstrumentedFunctions = createFolderForResult(directoryToSourceCodeOfFunctions);

		try (DirectoryStream<Path> directoryStreamFunctions = Files
				.newDirectoryStream(Paths.get(directoryToSourceCodeOfFunctions), "*.{js}")) {
			for (Path file : directoryStreamFunctions) {

				System.out.println(file.toString());
				List<String> instrumentedFile = instrument(file);
				saveInstrumentedFile(instrumentedFile, folderOfInstrumentedFunctions, file.getFileName().toString());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("Graph could not be created");
		}

	}

	private boolean isFolderAvailable(String directoryToSourceCodeOfFunctions) {
		Path path = Path.of(directoryToSourceCodeOfFunctions);
		return Files.exists(path) && Files.isDirectory(path);
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
