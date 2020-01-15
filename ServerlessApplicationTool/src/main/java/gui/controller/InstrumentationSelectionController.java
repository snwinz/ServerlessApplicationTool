package gui.controller;

import gui.view.InstrumentationSelectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.logic.modelcreation.ApplicationModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class InstrumentationSelectionController {

    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    TextField sourceCodeDirectoryPath;

    private InstrumentationSelectionView view;
    private ApplicationModel model;

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
        saveProps.setProperty("sourceCodeDirectoryPath", sourceCodeDirectoryPath.getText());
        try {
            saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
        } catch (IOException e) {
            System.err.println("Default paths couldn't be saved!");
            e.printStackTrace();
        }

    }
}