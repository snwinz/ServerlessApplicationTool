package gui.controller;

import gui.view.nodes.DraggableArrow;
import gui.view.nodes.DraggableNode;
import model.DTO.CoordinatesDTO;
import model.DTO.GraphVisualisationDTO;
import model.DTO.ArrowPositionDTO;
import model.logic.GraphManipulation;
import model.DTO.NodePositionDTO;

import java.util.List;

public class NodeCreatorController {

    private final GraphManipulation model;
    private final List<DraggableNode> draggableNodes;
    private final List<DraggableArrow> draggableDraggableArrows;


    public NodeCreatorController(GraphManipulation model, List<DraggableNode> draggableNodes, List<DraggableArrow> draggableDraggableArrows) {
        this.model = model;
        this.draggableDraggableArrows = draggableDraggableArrows;
        this.draggableNodes = draggableNodes;
    }


    public void addNodeToGraph(String xmlText, CoordinatesDTO coordinatesDTO) {

        GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(model.getGraph());

        for (DraggableNode node : draggableNodes) {
            graphVisualisationDTO.addNodePosition(NodePositionDTO.getNodePosition(node));
        }

        for (DraggableArrow draggableArrow : draggableDraggableArrows) {
            graphVisualisationDTO.addArrowPosition(ArrowPositionDTO.getArrowPosition(draggableArrow));
        }

        model.addNode(xmlText, coordinatesDTO, graphVisualisationDTO);
    }
}
