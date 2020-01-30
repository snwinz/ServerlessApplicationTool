package gui.view;

import gui.controller.LogFileSelectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LogFileSelectionView {

    private Scene scene;
    private Stage stage;
    private LogFileSelectionController controller;

    public LogFileSelectionView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logFileSelection.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        scene = new Scene(root);
        this.stage = new Stage();
    }

    public void show() {
        controller.setViewAndSetup(this);
        stage.setTitle("Log file selection");
        stage.setScene(this.scene);
        stage.show();
    }

    public Stage getStage() {
        return this.stage;
    }
}
