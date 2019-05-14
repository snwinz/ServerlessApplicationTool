package gui.view;

import gui.controller.ProjectSelectionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.logic.ApplicationModel;

import java.io.IOException;

public class ProjectSelectionView {

    private Scene scene;
    private Stage stage;
    private ProjectSelectionController controller;

    public ProjectSelectionView(ApplicationModel model) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("projectSelection.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setup(model);
        scene = new Scene(root);
        this.stage = new Stage();
    }

    public void show() {
        controller.setViewAndSetup(this);
        stage.setTitle("AWS Project Paths");
        stage.setScene(this.scene);
        stage.show();
    }

    public Stage getStage() {
        return this.stage;
    }
}
