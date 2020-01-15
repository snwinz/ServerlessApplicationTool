package gui.view;

import gui.controller.InstrumentationSelectionController;
import gui.controller.ProjectSelectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.logic.ApplicationModel;

import java.io.IOException;

public class InstrumentationSelectionView {

    private Scene scene;
    private Stage stage;
    private InstrumentationSelectionController controller;

    public InstrumentationSelectionView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("instrumentationSelection.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        scene = new Scene(root);
        this.stage = new Stage();
    }

    public void show() {
        controller.setViewAndSetup(this);
        stage.setTitle("Instrumentation");
        stage.setScene(this.scene);
        stage.show();
    }

    public Stage getStage() {
        return this.stage;
    }
}
