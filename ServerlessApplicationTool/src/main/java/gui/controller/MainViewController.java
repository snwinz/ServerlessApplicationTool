package gui.controller;

import gui.view.GraphVisualisationView;
import gui.view.InstrumentationSelectionView;
import gui.view.LogFileSelectionView;
import gui.view.MainView;
import gui.view.ProjectSelectionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import model.DTO.GraphVisualisationDTO;
import model.logic.modelcreation.GraphManipulation;
import model.logic.modelcreation.ApplicationModel;
import model.structure.Graph;

import java.io.File;
import java.io.IOException;

public class MainViewController {

    private final FileChooser fileChooser = new FileChooser();
    private MainView view;
    @FXML
    private TextArea textArea;
    @FXML
    private ApplicationModel model;

    public void createProjectSelectionWindow(ActionEvent event) {
        try {
            createProjectSelectionWindow();
        } catch (IOException e) {
            System.err.println("Project selection window cannot be created.");
            e.printStackTrace();
        }
    }

    private void createProjectSelectionWindow() throws IOException {
        ProjectSelectionView view = new ProjectSelectionView(model);
        view.show();
    }

    private void createInstrumentationWindow() throws IOException {
        InstrumentationSelectionView view = new InstrumentationSelectionView();
        view.show();
    }


    private void createLogWindow() throws IOException {
        LogFileSelectionView view = new LogFileSelectionView();
        view.show();
    }


    public void setup(MainView view, ApplicationModel model) {
        this.model = model;
        this.view = view;
    }


    public void importGraphButtonAction() {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showOpenDialog(view.getStage());
        if (file != null) {
            model.loadGraph(file.getPath());
        }
    }


    public void graphChanged(Object graphObject) {
        if (graphObject instanceof GraphVisualisationDTO) {
            GraphVisualisationDTO graphV = (GraphVisualisationDTO) graphObject;
            textArea.setText(graphV.getGraph().toString());
            createNewGraphVisualisation(graphV);
        } else {
            textArea.setText("Passed object was not a graph!");
        }


    }

    private void createNewGraphVisualisation(GraphVisualisationDTO graph) {

        GraphManipulation graphManipulation = new GraphManipulation(graph);
        GraphVisualisationController controller = new GraphVisualisationController(graphManipulation, this);
        GraphVisualisationView view = controller.getView();
        view.setup(controller, graphManipulation);
        view.show();
    }

    public void createNewGraph(ActionEvent actionEvent) {
        Graph graph = new Graph();
        GraphVisualisationDTO graphDTO = new GraphVisualisationDTO(graph);
        createNewGraphVisualisation(graphDTO);
    }


    public void createInstrumentationWindow(ActionEvent actionEvent) {
        try {
            createInstrumentationWindow();
        } catch (IOException e) {

            System.err.println("Instrumentation selection window cannot be created.");
        }
    }

    public void createLogWindow(ActionEvent actionEvent) {
        try {
            createLogWindow();
        } catch (IOException e) {

            System.err.println("Instrumentation selection window cannot be created.");
        }
    }
}