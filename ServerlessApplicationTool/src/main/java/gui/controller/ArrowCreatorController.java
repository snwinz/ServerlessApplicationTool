package gui.controller;

import gui.view.ArrowCreatorView;
import gui.view.nodes.DraggableArrow;
import gui.view.nodes.DraggableNode;
import javafx.scene.control.ComboBox;
import model.DTO.GraphVisualisationDTO;
import model.DTO.ArrowPositionDTO;
import model.logic.modelcreation.GraphManipulation;
import model.DTO.NodePositionDTO;

import java.util.List;

public class ArrowCreatorController {

	private final GraphManipulation model;
	private final List<DraggableNode> draggableNodes;
	private final List<DraggableArrow> draggableDraggableArrows;
	private ArrowCreatorView view;

	public ArrowCreatorController(GraphManipulation model, List<DraggableNode> draggableNodes,
			List<DraggableArrow> draggableDraggableArrows) {
		this.model = model;
		this.draggableDraggableArrows = draggableDraggableArrows;
		this.draggableNodes = draggableNodes;
	}

	public void addArrowToGraph(String xmlText) {

		GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(model.getGraph());

		for (DraggableNode node : draggableNodes) {

			graphVisualisationDTO.addNodePosition(NodePositionDTO.getNodePosition(node));
		}

		for (DraggableArrow draggableArrow : draggableDraggableArrows) {
			graphVisualisationDTO.addArrowPosition(ArrowPositionDTO.getArrowPosition(draggableArrow));
		}

		model.addArrow(xmlText, graphVisualisationDTO);
	}

	public void fillComboBoxWithAllNodes(ComboBox<String> comboBoxPredecessor) {
		for (DraggableNode node : draggableNodes) {
			comboBoxPredecessor.getItems().add(node.getNameOfNode());
		}

	}

	public void closeWindow() {
		view.closeWindow();
	}

	public void setup() {
		this.view = new ArrowCreatorView(this);
		view.setup();
	}
}
