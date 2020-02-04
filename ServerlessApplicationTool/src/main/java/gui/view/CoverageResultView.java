package gui.view;

import gui.controller.CoverageResultController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CoverageResultView {
    private Scene scene;
    private Stage stage;
    private CoverageResultController controller;


    public CoverageResultView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("coverageResultWindow.fxml"));

        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            System.err.println("View could not be loaded: " + e.toString());
        }
        controller = loader.getController();
        scene = new Scene(root);
        this.stage = new Stage();
    }

    public void show(String coverageResult) {
        controller.setup(coverageResult);
        stage.setTitle("Coverage results");
        stage.setScene(this.scene);
        stage.show();
    }


    public Stage getStage() {
        return stage;
    }

}
