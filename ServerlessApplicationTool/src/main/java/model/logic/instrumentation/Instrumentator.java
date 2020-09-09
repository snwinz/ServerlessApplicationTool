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
import model.logic.Instrumentation.logic.InstrumentationController;

public class Instrumentator {

	private List<CoverageMode> coverageModes = new ArrayList<CoverageMode>();
	public final List<CoverageCriterion> selectedCoverageModes = new LinkedList<>();

	public void addCoverageMode(CoverageMode mode) {
		coverageModes.add(mode);
	}

	public void instrument(Path file) {
		try {
			InstrumentationController instrumentation = new InstrumentationController();
			String instrumentedFileText = instrumentation.instrumentWithAntlr(file.toAbsolutePath().toString(),
					selectedCoverageModes);
			saveInstrumentedFile(instrumentedFileText, file);
		} catch (IOException e) {
			System.err.format("file %s could not be saved or read: %n", file.toString());
		}
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

	public void instrumentFilesOfFolder(String directoryToSourceCodeOfFunctions) {
		addInstrumentors();
		if (!isFolderAvailable(directoryToSourceCodeOfFunctions)) {
			throw new IllegalArgumentException("Path not available: " + directoryToSourceCodeOfFunctions);
		}

		for (CoverageMode mode : coverageModes) {
			System.out.println(mode);
		}
		System.out.println();
		try (DirectoryStream<Path> directoryStreamFunctions = Files
				.newDirectoryStream(Paths.get(directoryToSourceCodeOfFunctions), "*.{js}")) {
			for (Path file : directoryStreamFunctions) {

				System.out.println(file.toString());
				instrument(file);
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


	
	private void saveInstrumentedFile(String instrumentedFileText, Path path) {
		Path folderOfInstrumentedFunctions = createFolderForResult(path.getParent().toString());
		Path instrumentedFile = Paths.get(folderOfInstrumentedFunctions.toAbsolutePath() + "/" + path.getFileName().toString());
		System.out.println(instrumentedFile.getFileName());

		try {
			Files.writeString(instrumentedFile, instrumentedFileText, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			System.err.println("Could not write result of file " +  path.toAbsolutePath());
		}
		
	}
	

	private Path createFolderForResult(String directoryToSourceCodeOfFunctions) {
		Path dirPathObj = Paths.get(directoryToSourceCodeOfFunctions + "/" + "instrumentedFunctionsWithAntlr");
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
