package gui.controller;

import gui.view.ProjectSelectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import model.logic.modelcreation.Creator;
import model.logic.modelcreation.ApplicationModel;
import model.structure.Graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ProjectSelectionController {

    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    @FXML
    TextField cloudFormationPath;
    @FXML
    TextField serverlessFunctionsDirectoryPath;
    @FXML
    TextField logDirectoryPath;

    private ProjectSelectionView view;
    private ApplicationModel model;

    public void setViewAndSetup(ProjectSelectionView view) {

        this.view = view;


        setDefaultPaths();


    }

    private void setDefaultPaths() {
        Properties loadProps = new Properties();
        try {
            loadProps.loadFromXML(new FileInputStream("settings.xml"));
            String cloudFormationPathText = loadProps.getProperty("cloudFormationPath");
            String logDirectoryPathText = loadProps.getProperty("logDirectoryPath");
            String functionsDirectoryPathText = loadProps.getProperty("functionsDirectoryPath");
            cloudFormationPath.setText(cloudFormationPathText);
            logDirectoryPath.setText(logDirectoryPathText);
            serverlessFunctionsDirectoryPath.setText(functionsDirectoryPathText);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @FXML
    private void chooseCloudFormationFile(ActionEvent event) {

        File file = fileChooser.showOpenDialog(view.getStage());
        if (file != null) {
            cloudFormationPath.setText(file.getPath());
        }
    }

    @FXML
    private void chooseServerlessFunctions(ActionEvent event) {

        File file = directoryChooser.showDialog(view.getStage());
        if (file != null) {
            serverlessFunctionsDirectoryPath.setText(file.getPath());
        }
    }

    @FXML
    private void chooseLogDirectory(ActionEvent event) {

        File file = directoryChooser.showDialog(view.getStage());
        if (file != null) {
            logDirectoryPath.setText(file.getPath());
        }
    }

    @FXML
    private void createModel(ActionEvent event) {
        savePathsAsDefault();
        Graph graph = Creator.createGraph(cloudFormationPath.getText(), serverlessFunctionsDirectoryPath.getText(),
                logDirectoryPath.getText());

        model.createNewGraphVisualisation(graph);

        view.getStage().close();
    }

    private void savePathsAsDefault() {
        Properties saveProps = new Properties();
        saveProps.setProperty("cloudFormationPath", cloudFormationPath.getText());
        saveProps.setProperty("logDirectoryPath", logDirectoryPath.getText());
        saveProps.setProperty("functionsDirectoryPath", serverlessFunctionsDirectoryPath.getText());
        try {
            saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
        } catch (IOException e) {
            System.err.println("Default paths couldn't be saved!");
            e.printStackTrace();
        }

    }

    public void setup(ApplicationModel model) {
        this.model = model;
    }

}