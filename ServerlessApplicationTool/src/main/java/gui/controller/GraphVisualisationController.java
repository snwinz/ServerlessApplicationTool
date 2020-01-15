package gui.controller;

import gui.view.GraphVisualisationView;
import gui.view.nodes.DraggableArrow;
import gui.view.nodes.DraggableNode;
import javafx.stage.FileChooser;
import model.DTO.ArrowPositionDTO;
import model.DTO.GraphVisualisationDTO;
import model.DTO.NodePositionDTO;
import model.logic.modelcreation.GraphManipulation;
import model.logic.modelcreation.Saver;

import java.io.File;
import java.util.List;

public class GraphVisualisationController {

    private final GraphManipulation model;
    private final MainViewController mvController;
    private GraphVisualisationView view;


    public GraphVisualisationController(GraphManipulation model, MainViewController mainViewController) {
        this.setView(new GraphVisualisationView());
        this.model = model;
        mvController = mainViewController;
    }


    public GraphVisualisationView getView() {
        return view;
    }

    private void setView(GraphVisualisationView view) {
        this.view = view;
    }


    public void saveGraph(List<DraggableNode> draggableNodes, List<DraggableArrow> draggableDraggableArrows) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(view.getStage());
        GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(model.getGraph());

        for (DraggableNode node : draggableNodes) {
            graphVisualisationDTO.addNodePosition(NodePositionDTO.getNodePosition(node));
        }

        for (DraggableArrow draggableArrow : draggableDraggableArrows) {
            graphVisualisationDTO.addArrowPosition(ArrowPositionDTO.getArrowPosition(draggableArrow));
        }


        if (file != null) {
            Saver.saveGraph(graphVisualisationDTO, file.getPath());
        }

    }


    public void remove(DraggableArrow draggableArrow) {
        GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(model.getGraph());

        for (DraggableNode node : view.getDraggableNodes()) {
            graphVisualisationDTO.addNodePosition(NodePositionDTO.getNodePosition(node));
        }

        for (DraggableArrow currentDraggableArrow : view.getDraggableDraggableArrows()) {
            if (!currentDraggableArrow.getAlias().getName().equals(draggableArrow.getName())) {
                graphVisualisationDTO.addArrowPosition(ArrowPositionDTO.getArrowPosition(currentDraggableArrow));
            }
        }


        model.remove(graphVisualisationDTO, draggableArrow.getAlias());
    }

    public void remove(DraggableNode draggableNode) {
        GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(model.getGraph());

        for (DraggableNode node : view.getDraggableNodes()) {
            if (!draggableNode.getAlias().getName().equals(node.getNameOfNode())) {
                graphVisualisationDTO.addNodePosition(NodePositionDTO.getNodePosition(node));
            }
        }

        for (DraggableArrow draggableArrow : view.getDraggableDraggableArrows()) {

            graphVisualisationDTO.addArrowPosition(ArrowPositionDTO.getArrowPosition(draggableArrow));
        }


        model.removeNode(graphVisualisationDTO, draggableNode.getAlias());


    }


    public void openGraph() {
        mvController.importGraphButtonAction();
    }

    public void showArrowInfoBox(String info) {
        view.showInfo(info, "Arrow");
    }

    public void showNodeInfoBox(String info) {
        view.showInfo(info, "Node");
    }

    public void analyzeGraph() {
        GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(model.getGraph());
        for (DraggableNode node : view.getDraggableNodes()) {
            graphVisualisationDTO.addNodePosition(NodePositionDTO.getNodePosition(node));
        }
        for (DraggableArrow draggableArrow : view.getDraggableDraggableArrows()) {
            graphVisualisationDTO.addArrowPosition(ArrowPositionDTO.getArrowPosition(draggableArrow));
        }
        model.analyzeGraph(graphVisualisationDTO);
    }
}
