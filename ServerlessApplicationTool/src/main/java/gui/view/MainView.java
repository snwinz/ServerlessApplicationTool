package gui.view;

import gui.controller.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.logic.modelcreation.ApplicationModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class MainView implements PropertyChangeListener {
    private Scene scene;
    private Stage stage;


    private MainViewController controller;

    public MainView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        scene = new Scene(root);
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Serverless Application Tool");
        // model of view is set
        ApplicationModel model = new ApplicationModel();
        controller.setup(this, model);
        model.addPropertyChangeListener(this);
        stage.setScene(this.scene);
        stage.show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("newGraphSet")) {
            controller.graphChanged(evt.getNewValue());
            System.out.println("Graph Changed!");
        }
    }

    public Stage getStage() {
        return stage;
    }

}
