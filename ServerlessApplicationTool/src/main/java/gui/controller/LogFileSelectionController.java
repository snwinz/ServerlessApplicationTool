package gui.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import gui.view.CoverageResultView;
import gui.view.LogFileSelectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.logic.LogEvaluation.LogFileEvaluator;

public class LogFileSelectionController {
	private final FileChooser fileChooser = new FileChooser();

	@FXML
	TextField logFilePath;
	private LogFileSelectionView view;

	public void setViewAndSetup(LogFileSelectionView view) {
		this.view = view;
		setDefaultPaths();
	}

	private void setDefaultPaths() {
		Properties loadProps = new Properties();
		try {
			loadProps.loadFromXML(new FileInputStream("settings.xml"));
			String logFilePathText = loadProps.getProperty("logFilePath");
			logFilePath.setText(logFilePathText);
		} catch (IOException e) {
		}
	}

	@FXML
	public void evaluateCoverage(ActionEvent actionEvent) {
		savePathsAsDefault();
		LogFileEvaluator evaluator = new LogFileEvaluator();
		String result = evaluator.evaluate(logFilePath.getText());

		CoverageResultView view = new CoverageResultView();

		view.show(result);

	}

	@FXML
	public void chooselogFilePath(ActionEvent actionEvent) {
		String pathOfInput = logFilePath.getText();
		Path selectedFile = Path.of(pathOfInput);
		if (Files.exists(selectedFile)) {
			fileChooser.setInitialDirectory(selectedFile.toAbsolutePath().getParent().toFile());
		} else {
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		}
		File file = fileChooser.showOpenDialog(view.getStage());
		if (file != null) {
			logFilePath.setText(file.getPath());
		}

	}

	private void savePathsAsDefault() {
		Properties saveProps = new Properties();
		try {
			saveProps.loadFromXML(new FileInputStream("settings.xml"));
			saveProps.setProperty("logFilePath", logFilePath.getText());
			saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
		} catch (IOException e) {
			System.err.println("Default paths couldn't be saved!");
			e.printStackTrace();
		}

	}

}
