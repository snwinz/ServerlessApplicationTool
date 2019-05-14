package gui.view;

import gui.controller.ArrowCreatorController;
import gui.controller.GraphVisualisationController;
import gui.controller.NodeCreatorController;
import gui.view.nodes.DraggableArrow;
import gui.view.nodes.DraggableNode;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DTO.ArrowPositionDTO;
import model.DTO.CoordinatesDTO;
import model.DTO.GraphVisualisationDTO;
import model.logic.GraphManipulation;
import model.DTO.NodePositionDTO;
import model.structure.Arrow;
import model.structure.Graph;
import model.structure.Node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class GraphVisualisationView implements PropertyChangeListener {

    private static final int HEIGHT = 900;
    private static final int WIDTH = 1400;
    private GraphVisualisationController controller;
    private GraphManipulation model;
    private Stage stage;
    private CoordinatesDTO coordinates;

    private List<DraggableNode> draggableNodes = new ArrayList<>();
    private List<DraggableArrow> draggableDraggableArrows = new ArrayList<>();

    public void setup(GraphVisualisationController controller, GraphManipulation model) {
        this.controller = controller;
        this.model = model;
        this.stage = new Stage();
        stage.setOnCloseRequest(closeWindowHandler());
    }

    public void show() {
        model.addPropertyChangeListener(this);
        GraphVisualisationDTO graphDTO = model.getGraphVisualisationDTO();
        ScrollPane visualizedGraph = createGraphDisplay(graphDTO);

        BorderPane borderPane = createBorderPane(visualizedGraph);

        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        scene.setFill(Color.GHOSTWHITE);

        stage.setScene(scene);
        stage.show();
    }

    private EventHandler<ContextMenuEvent> contextMenuClicked() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem createNodeItem = new MenuItem("Create Node");
        createNodeItem.setOnAction(createEventHandlerCreateNode());

        MenuItem createArrowItem = new MenuItem("Create DraggableArrow");
        createArrowItem.setOnAction(createEventHandlerCreateArrow());


        contextMenu.getItems().addAll(createNodeItem, createArrowItem);

        return event -> {
            contextMenu.show(stage, event.getScreenX(), event.getScreenY());
            coordinates = new CoordinatesDTO(event.getX(), event.getY());
        };
    }

    private EventHandler<ActionEvent> createEventHandlerCreateNode() {
        return event -> {
            NodeCreatorController controller = new NodeCreatorController(model, draggableNodes, draggableDraggableArrows);
            NodeCreatorView createNodeView = new NodeCreatorView(controller);
            createNodeView.setup(coordinates);
        };
    }

    private EventHandler<ActionEvent> createEventHandlerCreateArrow() {
        return event -> {
            ArrowCreatorController controller = new ArrowCreatorController(model, draggableNodes, draggableDraggableArrows);
            ArrowCreatorView createArrowView = new ArrowCreatorView(controller);
            createArrowView.setup();
        };
    }


    private BorderPane createBorderPane(ScrollPane visualizedGraph) {
        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = createMenuBar();
        borderPane.setTop(menuBar);
        borderPane.setCenter(visualizedGraph);
        return borderPane;
    }

    private EventHandler<WindowEvent> closeWindowHandler() {
        return we -> model.removePropertyChangeListener(GraphVisualisationView.this);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu analyze = new Menu("Analyze");


        MenuItem saveGraphItem = new MenuItem("Save Graph");
        MenuItem closeItem = new MenuItem("Close");
        MenuItem openNewGraphItem = new MenuItem("Open Graph");
        MenuItem analyzeGraphItem = new MenuItem("Analyze Graph");
        closeItem.setOnAction(event -> {
            model.removePropertyChangeListener(GraphVisualisationView.this);
            stage.close();
        });

        analyzeGraphItem.setOnAction(event -> {
            controller.analyzeGraph();


        });
        saveGraphItem.setOnAction(event -> controller.saveGraph(draggableNodes, draggableDraggableArrows));
        openNewGraphItem.setOnAction(event -> controller.openGraph());

        file.getItems().addAll(saveGraphItem, closeItem, openNewGraphItem);
        analyze.getItems().addAll(analyzeGraphItem);

        menuBar.getMenus().addAll(file, analyze);
        return menuBar;
    }

    private ScrollPane createGraphDisplay(Graph graph) {
        Group group = new Group();
        createTitle(group);
        createNodes(graph.getNodes(), group);
        createArrows(group);
        setPositionsOfNodes(model.getGraphVisualisationDTO().getNodePositionDTOS());
        setPositionOfArrow(model.getGraphVisualisationDTO().getArrowPositionDTOS());
        return new ScrollPane(group);
    }

    private ScrollPane createGraphDisplay(GraphVisualisationDTO graphDTO) {
        draggableNodes = new ArrayList<>();
        draggableDraggableArrows = new ArrayList<>();
        Group group = new Group();
        createTitle(group);
        Graph graph = graphDTO.getGraph();
        createNodes(graph.getNodes(), group);
        createArrows(group);
        setPositionsOfNodes(graphDTO.getNodePositionDTOS());
        setPositionOfArrow(graphDTO.getArrowPositionDTOS());
        ScrollPane visualizedGraph = new ScrollPane(group);
        visualizedGraph.setOnContextMenuRequested(contextMenuClicked());
        return visualizedGraph;
    }


    private void setPositionOfArrow(List<ArrowPositionDTO> arrowPositionDTOS) {
        for (ArrowPositionDTO arrowPositionDTO : arrowPositionDTOS) {


            for (DraggableArrow draggableArrow : draggableDraggableArrows) {

                if (draggableArrow.getName().equals(arrowPositionDTO.getName())) {
                    draggableArrow.setStartXProperty(arrowPositionDTO.getStartXOffset());
                    draggableArrow.setStartYProperty(arrowPositionDTO.getStartYOffset());
                    draggableArrow.setEndXProperty(arrowPositionDTO.getEndXOffset());
                    draggableArrow.setEndProperty(arrowPositionDTO.getEndYOffset());
                }

            }

        }

    }

    private void setPositionsOfNodes(List<NodePositionDTO> nodePositionDTOS) {

        for (NodePositionDTO nodePositionDTO : nodePositionDTOS) {


            for (DraggableNode node : draggableNodes) {

                if (node.getNameOfNode().equals(nodePositionDTO.getName())) {
                    node.setLayoutX(nodePositionDTO.getX());
                    node.setLayoutY(nodePositionDTO.getY());
                }

            }

        }


    }

    private void createTitle(Group group) {
        Text title = new Text("New Graph");
        Group titleGroup = new Group();
        titleGroup.setLayoutX(0);
        titleGroup.setLayoutY(0);
        group.getChildren().add(title);
    }

    private Optional<DraggableNode> getNodeWithName(String name) {
        for (DraggableNode node : draggableNodes) {
            if (node.getNameOfNode().equals(name)) {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    private void createNodes(List<Node> nodes, Group group) {
        for (Node node : nodes) {
            int x = ThreadLocalRandom.current().nextInt(0, WIDTH + 1);
            int y = ThreadLocalRandom.current().nextInt(0, HEIGHT + 1);

            DraggableNode dragNode = new DraggableNode(node, x, y);
            dragNode.setupDraggableNode(controller);
            group.getChildren().add(dragNode);
            draggableNodes.add(dragNode);
        }
    }

    private void createArrows(Group group) {
        for (DraggableNode dragNode : draggableNodes) {
            Node node = dragNode.getAlias();
            List<Arrow> arrows = node.getOutgoingArrows();
            for (Arrow arrow : arrows) {
                Node predecessor = arrow.getPredecessor();
                Node successors = arrow.getSuccessor();

                Optional<DraggableNode> pre = getNodeWithName(predecessor.getName());
                Optional<DraggableNode> suc = getNodeWithName(successors.getName());

                if (pre.isPresent() && suc.isPresent()) {
                    DoubleProperty startX = pre.get().layoutXProperty();
                    DoubleProperty startY = pre.get().layoutYProperty();
                    DoubleProperty endX = suc.get().layoutXProperty();
                    DoubleProperty endY = suc.get().layoutYProperty();

                    Line line = new Line();
                    Line arrow1 = new Line();
                    Line arrow2 = new Line();
                    DraggableArrow createdDraggableArrow = new DraggableArrow(startX, startY, endX, endY, arrow, line, arrow1, arrow2);
                    createdDraggableArrow.setupArrow(controller);
                    draggableDraggableArrows.add(createdDraggableArrow);
                    group.getChildren().add(createdDraggableArrow);

                }

            }
        }
    }

    public Stage getStage() {
        return stage;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("graphUpdated")) {
            if (evt.getNewValue() instanceof GraphVisualisationDTO) {
                GraphVisualisationDTO graphV = (GraphVisualisationDTO) evt.getNewValue();

                ScrollPane visualizedGraph = createGraphDisplay(graphV);


                MenuBar menuBar = createMenuBar();

                BorderPane borderPane = new BorderPane();
                borderPane.setTop(menuBar);
                borderPane.setCenter(visualizedGraph);


                Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
                scene.setFill(Color.GHOSTWHITE);

                stage.setScene(scene);
                stage.show();


            }
        }
    }

    public List<DraggableNode> getDraggableNodes() {
        return draggableNodes;
    }

    public List<DraggableArrow> getDraggableDraggableArrows() {
        return draggableDraggableArrows;
    }

    public void showInfo(String info, String title) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Data of " + title);
        alert.setHeaderText("Content of arrow");
        alert.setContentText(info);
        alert.showAndWait();
    }


}
