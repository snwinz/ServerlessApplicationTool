package model.logic.modelcreation;

import gui.controller.MainViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.DTO.*;
import model.structure.Arrow;
import model.structure.Graph;
import model.structure.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

public class GraphManipulation {
    MainViewController mainViewController;
    private final StringProperty graphText = new SimpleStringProperty("");
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private GraphVisualisationDTO graphVisualisationDTO;

    public GraphManipulation(GraphVisualisationDTO graphVisualisationDTO) {
        this.graphVisualisationDTO = graphVisualisationDTO;
    }

    public void remove(GraphVisualisationDTO graphDTO, Arrow alias) {
        alias.getPredecessor().removeOutgoingArrow(alias);

        this.pcs.firePropertyChange("graphUpdated", null, graphDTO);
    }

    public void removeNode(GraphVisualisationDTO graphDTO, Node node) {
        Graph graph = graphDTO.getGraph();
        graph.getNodes().remove(node);
        removeArrowsFromNode(node);

        this.pcs.firePropertyChange("graphUpdated", null, graphDTO);
    }

    private void addNode(NodeDTO nodeDTO, CoordinatesDTO coordinatesDTO, GraphVisualisationDTO graphVisualisationDTO) {
        Node node = nodeDTO.createNode();
        Graph graph = graphVisualisationDTO.getGraph();
        List<Node> nodes = graph.getNodes();

        boolean nameIsFree = true;
        for (Node currentNode : nodes) {
            if (currentNode.getName().equals(node.getName())) {
                nameIsFree = false;
            }
        }
        if (nameIsFree) {
            nodes.add(node);
            NodePositionDTO nodePositionDTO = new NodePositionDTO(node.getName(), coordinatesDTO.getScreenX(), coordinatesDTO.getScreenY());
            graphVisualisationDTO.addNodePosition(nodePositionDTO);
            this.pcs.firePropertyChange("graphUpdated", null, graphVisualisationDTO);
        } else {
            System.err.println("Name is already used!");
        }
    }

    private void removeArrowsFromNode(Node node) {


        for (Node currentNode : graphVisualisationDTO.getGraph().getNodes()) {
            Iterator<Arrow> itr = currentNode.getOutgoingArrows().iterator();
            while (itr.hasNext()) {
                Arrow currentArrow = (Arrow) itr.next();
                if (currentArrow.getSuccessor().getName().equals(node.getName())
                ) {
                    itr.remove();
                }

            }
        }
    }

    public Graph getGraph() {
        return graphVisualisationDTO.getGraph();
    }

    public StringProperty getGraphTextProperty() {
        return graphText;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public GraphVisualisationDTO getGraphVisualisationDTO() {
        return graphVisualisationDTO;
    }

    public void addNode(String xmlText, CoordinatesDTO coordinatesDTO, GraphVisualisationDTO graphVisualisationDTO) {
        try {
            JAXBContext context = JAXBContext.newInstance(NodeDTO.class);
            Unmarshaller um = context.createUnmarshaller();
            StringReader reader = new StringReader(xmlText);
            NodeDTO node = (NodeDTO) um.unmarshal(reader);
            addNode(node, coordinatesDTO, graphVisualisationDTO);
        } catch (JAXBException e) {

            e.printStackTrace();
        }

    }

    public void addArrow(String xmlText, GraphVisualisationDTO graphVisualisationDTO) {

        try {
            JAXBContext context = JAXBContext.newInstance(ArrowDTO.class);
            Unmarshaller um = context.createUnmarshaller();
            StringReader reader = new StringReader(xmlText);
            ArrowDTO arrowDTO = (ArrowDTO) um.unmarshal(reader);
            addArrow(arrowDTO, graphVisualisationDTO);
        } catch (JAXBException e) {

            e.printStackTrace();
        }
    }


    private void addArrow(ArrowDTO arrowDTO, GraphVisualisationDTO graphVisualisationDTO) {
        this.graphVisualisationDTO = graphVisualisationDTO;
        Graph graph = graphVisualisationDTO.getGraph();
        List<Node> nodes = graph.getNodes();

        boolean arrowIsFree = true;
        for (Node currentNode : nodes) {
            for (Arrow currentArrow : currentNode.getOutgoingArrows()) {
                if (currentArrow.getName().equals(arrowDTO.getName())) {
                    arrowIsFree = false;
                }
            }

        }
        if (arrowIsFree) {
            Node predecessor = graph.getNodeByName(arrowDTO.getPredecessor());
            predecessor.addOutgoingArrow(createArrowFromArrowDTO(arrowDTO));
            this.pcs.firePropertyChange("graphUpdated", null, graphVisualisationDTO);
        } else {
            System.err.println("Name is already used!");
        }
    }

    private Arrow createArrowFromArrowDTO(ArrowDTO arrowDTO) {
        Arrow resultArrow = new Arrow(graphVisualisationDTO.getGraph().getNodeByName(arrowDTO.getPredecessor()),
                graphVisualisationDTO.getGraph().getNodeByName(arrowDTO.getSuccessor()));
        resultArrow.setAccessMode(arrowDTO.getAccessMode());
        resultArrow.setOrder(arrowDTO.getOrder());
        resultArrow.setCondition(arrowDTO.getCondition());
        resultArrow.setSynchronizedCall(arrowDTO.isSynchronizedCall());
        resultArrow.setMultiplicity(arrowDTO.getMultiplicity());
        return resultArrow;
    }

    public void analyzeGraph(GraphVisualisationDTO graphVisualisationDTO) {
        this.graphVisualisationDTO = graphVisualisationDTO;

        Analyzer analyzer = new Analyzer(graphVisualisationDTO.getGraph());
        analyzer.getHotspots();

        this.pcs.firePropertyChange("graphUpdated", null, graphVisualisationDTO);

    }
}
