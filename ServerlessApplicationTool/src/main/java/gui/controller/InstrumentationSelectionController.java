package gui.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import gui.view.InstrumentationSelectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import model.logic.instrumentation.CoverageMode;
import model.logic.instrumentation.CoverageOption;
import model.logic.instrumentation.Instrumentator;

public class InstrumentationSelectionController {

	private final DirectoryChooser directoryChooser = new DirectoryChooser();

	@FXML
	TextField sourceCodeDirectoryPath;

	@FXML
	CheckBox checkbox_AR;
	@FXML
	CheckBox checkbox_ARR;
	@FXML
	CheckBox checkbox_ARS;
	@FXML
	CheckBox checkbox_ARD;
	@FXML
	CheckBox checkbox_ARDU;
	@FXML
	CheckBox checkbox_ARU;
	@FXML
	CheckBox checkbox_DELETEOption;

	private InstrumentationSelectionView view;

	public void setViewAndSetup(InstrumentationSelectionView view) {

		this.view = view;
		setDefaultPaths();

	}

	private void setDefaultPaths() {
		Properties loadProps = new Properties();
		try {
			loadProps.loadFromXML(new FileInputStream("settings.xml"));
			String sourceCodeDirectoryPathText = loadProps.getProperty("sourceCodeDirectoryPath");
			sourceCodeDirectoryPath.setText(sourceCodeDirectoryPathText);
		} catch (IOException e) {
			System.err.println("settings.xml could not be opened.");
		}

	}

	@FXML
	public void instrumentCode(ActionEvent actionEvent) {
		savePathsAsDefault();

		Instrumentator instrumentator = new Instrumentator();

		if (checkbox_AR.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.AR);
		}
		if (checkbox_ARR.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.ARR);
		}
		if (checkbox_ARS.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.ARS);
		}
		if (checkbox_ARD.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.ARD);
		}
		if (checkbox_ARDU.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.ARDU);
		}
		if (checkbox_ARU.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.ARU);
		}
		if (checkbox_DELETEOption.isSelected()) {
			instrumentator.addCoverageOption(CoverageOption.DELETE_INSTRUMENTATION);
		}

		instrumentator.instrumentFilesOfFolder(sourceCodeDirectoryPath.getText());

		view.getStage().close();

	}

	@FXML
	public void chooseSourceCodeDirectory(ActionEvent actionEvent) {
		File file = directoryChooser.showDialog(view.getStage());
		if (file != null) {
			sourceCodeDirectoryPath.setText(file.getPath());
		}
	}

	private void savePathsAsDefault() {
		Properties saveProps = new Properties();
		try {
			if (Files.notExists(Path.of("settings.xml"))) {
				Files.createFile(Path.of("settings.xml"));
			}
			saveProps.setProperty("sourceCodeDirectoryPath", sourceCodeDirectoryPath.getText());
			saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
		} catch (IOException e) {
			System.err.println("Default paths couldn't be saved!");
		}

	}
}