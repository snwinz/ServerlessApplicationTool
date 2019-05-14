package gui.view.nodes;

import gui.controller.GraphVisualisationController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import model.structure.DynamoDB;
import model.structure.Function;
import model.structure.Node;
import model.structure.S3Bucket;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DraggableNode extends Group {

    private double mouseClickPositionX = 0;
    private double mouseClickPositionY = 0;
    private Node alias;
    private String nameOfNode = "";
    private GraphVisualisationController controller;

    public DraggableNode(Node origin, double xPosition, double yPosition) {


        Text t = new Text(origin.getName());
        getChildren().add(t);
        setOnMousePressed(clickMouse());
        setOnMouseDragged(dragMouse());
        layoutXProperty().set(xPosition);
        layoutYProperty().set(yPosition);
        setAlias(origin);
        setNameOfNode(origin.getName());


        if (origin instanceof Function) {
            createSymbol("images/lambda.png");
        } else if (origin instanceof S3Bucket) {
            createSymbol("images/s3.png");
        } else if (origin instanceof DynamoDB) {
            createSymbol("images/dynamo.png");
        } else {
            Sphere shape = new Sphere(10);
            getChildren().add(shape);
        }

    }

    public void setupDraggableNode(GraphVisualisationController controller) {
        this.controller = controller;
        this.setOnContextMenuRequested(contextMenuClicked());

    }


    private void createSymbol(String pathToImage) {
        Path myImagePath = Paths.get(pathToImage);
        if (Files.exists(myImagePath)
        ) {
            try {
                String path = new File(myImagePath.toAbsolutePath().toString()).toURI().toURL().toExternalForm();
                Image image = new Image(path);
                ImageView iv2 = new ImageView();
                iv2.setImage(image);
                getChildren().add(iv2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    private EventHandler<MouseEvent> clickMouse() {

        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mouseClickPositionX = event.getSceneX();
                mouseClickPositionY = event.getSceneY();
            }
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    String info = alias.toString();
                    controller.showNodeInfoBox(info);
                }
            }

        };
    }

    private EventHandler<MouseEvent> dragMouse() {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getSceneX() - mouseClickPositionX;
                double deltaY = event.getSceneY() - mouseClickPositionY;
                double updatedPositionX = getLayoutX() + deltaX;
                double updatedPositionY = getLayoutY() + deltaY;
                if (updatedPositionX >= 0 && updatedPositionY >= 0) {

                    layoutXProperty().set(getLayoutX() + deltaX);
                    layoutYProperty().set(getLayoutY() + deltaY);
                    mouseClickPositionX = event.getSceneX();
                    mouseClickPositionY = event.getSceneY();
                }
            }
        };
    }

    public String getNameOfNode() {
        return nameOfNode;
    }

    private void setNameOfNode(String nameOfNode) {
        this.nameOfNode = nameOfNode;
    }

    public Node getAlias() {
        return alias;
    }

    private void setAlias(Node alias) {
        this.alias = alias;
    }


    private EventHandler<ContextMenuEvent> contextMenuClicked() {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem removeNodeItem = new MenuItem("Remove Node");
        MenuItem infoOfNodeItem = new MenuItem("Show information of node");

        removeNodeItem.setOnAction(event -> controller.remove(DraggableNode.this));
        infoOfNodeItem.setOnAction(event -> {
            String info = alias.toString();
            controller.showNodeInfoBox(info);
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(removeNodeItem, infoOfNodeItem);

        return event -> {
            event.consume();
            contextMenu.show(this, event.getScreenX(), event.getScreenY());

        };
    }


}