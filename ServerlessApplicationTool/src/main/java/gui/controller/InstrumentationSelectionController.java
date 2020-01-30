package gui.controller;

import gui.view.InstrumentationSelectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import model.logic.Instrumentation.CoverageMode;
import model.logic.Instrumentation.Instrumentator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
	CheckBox checkbox_ARU;

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
			e.printStackTrace();
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
		if (checkbox_ARU.isSelected()) {
			instrumentator.addCoverageMode(CoverageMode.ARU);
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
			saveProps.loadFromXML(new FileInputStream("settings.xml"));
			saveProps.setProperty("sourceCodeDirectoryPath", sourceCodeDirectoryPath.getText());
			saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
		} catch (IOException e) {
			System.err.println("Default paths couldn't be saved!");
			e.printStackTrace();
		}

	}
}