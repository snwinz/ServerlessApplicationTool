package model.logic.Instrumentation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Instrumentator {
    public static List<String> instrument(Path file) {
        List<String> allLinesOfFile = new ArrayList<String>();
        try {
            allLinesOfFile = Files.readAllLines(file);
            //add statements here
        } catch (IOException e) {
            e.printStackTrace();
        }

       // allLinesOfFile.stream().collect(Collectors.joining(System.lineSeparator()));
        return  allLinesOfFile;
    }

    public static void instrumentFilesOfFolder(String directoryToSourceCodeOfFunctions) {
        Path folderOfInstrumentedFunctions = createFolderForResult(directoryToSourceCodeOfFunctions);

        try (DirectoryStream<Path> directoryStreamFunctions = Files
                .newDirectoryStream(Paths.get(directoryToSourceCodeOfFunctions), "*.{js}"
                )
        ) {
            for (Path file : directoryStreamFunctions) {

                System.out.println(file.toString());
                List<String> instrumentedFile =  Instrumentator.instrument(file);
                saveInstrumentedFile(instrumentedFile,folderOfInstrumentedFunctions, file.getFileName().toString() );
            }
        } catch (IOException ex) {
            System.err.println("Graph could not be created");
        }

    }

    private static void saveInstrumentedFile(List<String> instrumentedSourceCode, Path folderOfInstrumentedFunctions, String fileName) {
    Path instrumentedFile =    Paths.get(folderOfInstrumentedFunctions.toAbsolutePath()+ "/"+fileName);
        System.out.println(instrumentedFile.getFileName());

        try {
            Files.write(instrumentedFile, instrumentedSourceCode, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static Path createFolderForResult(String directoryToSourceCodeOfFunctions) {
        Path dirPathObj = Paths.get(directoryToSourceCodeOfFunctions+"/"+"instrumentedFunctions");
        boolean dirExists = Files.exists(dirPathObj);
        if(dirExists) {
            System.out.println("! Directory Already Exists !");
        } else {
            try {
               Files.createDirectories(dirPathObj);
                System.out.format("Created new directory for instrumented function in: %s", dirPathObj.normalize());

            } catch (IOException ioExceptionObj) {
                System.out.println("Problem Occured While Creating The Directory Structure= " + ioExceptionObj.getMessage());
            }
        }
        return dirPathObj;
    }
}
