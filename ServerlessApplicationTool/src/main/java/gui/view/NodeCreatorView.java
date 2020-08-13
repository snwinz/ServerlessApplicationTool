package gui.view;

import gui.controller.NodeCreatorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DTO.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Arrays;

public class NodeCreatorView extends Dialog<Object> {

    private final NodeCreatorController controller;
    private CoordinatesDTO coordinatesDTO;
    private final Stage stage;
    private final TextArea textAreaXML = new TextArea();

    public NodeCreatorView(NodeCreatorController controller) {
        this.controller = controller;
        this.stage = new Stage();


        Label typeOfNode = new Label("Type of node: ");
        Label xmlText = new Label("New node as XML (please add relevant information): ");
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Node",
                        "Function",
                        "S3Bucket", "DynamoDB"
                );
        final ComboBox<String> comboBox = new ComboBox<>(options);
        comboBox.valueProperty().addListener( (ov, t, t1) -> {
            switch (t1) {
                case "DynamoDB": {
                    DynamoDBDTO node = new DynamoDBDTO();
                    setTextToNodeType(node);
                    break;
                }
                case "Function": {
                    FunctionDTO node = new FunctionDTO();
                    setTextToNodeType(node);
                    break;
                }
                case "S3Bucket": {
                    S3BucketDTO node = new S3BucketDTO();
                    setTextToNodeType(node);
                    break;
                }
                default:
                    NodeDTO node = new NodeDTO();
                    setTextToNodeType(node);
            }
        });

        GridPane grid = new GridPane();
        grid.add(typeOfNode, 1, 1);
        grid.add(comboBox, 2, 1);
        grid.add(xmlText, 1, 2);
        grid.add(textAreaXML, 2, 2);


        Button createButton = new Button("Add node");
        createButton.setOnAction(getCreateButtonHandler());
        Button cancelButton = new Button("cancel");
        cancelButton.setOnAction(getCancelButtonHandler());
        grid.add(createButton, 1, 3);
        grid.add(cancelButton, 2, 3);
        Scene scene = new Scene(grid);
        stage.setScene(scene);


    }

    private void setTextToNodeType(NodeDTO node) {
        try {
            String xmlOfNode = getXMLofNode(node);
            textAreaXML.setText(xmlOfNode);
        } catch (JAXBException e) {
            System.err.println("xml for the node type couldn't be created");
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("xml for the node type couldn't be created");
            alert.setContentText(Arrays.toString(e.getStackTrace()));
            alert.showAndWait();
        }
    }

    private EventHandler<ActionEvent> getCancelButtonHandler() {
        return event -> stage.close();
    }

    private EventHandler<ActionEvent> getCreateButtonHandler() {
        return event -> {
            controller.addNodeToGraph(textAreaXML.getText(), coordinatesDTO);
            stage.close();
        };
    }


    public void setup(CoordinatesDTO coordinatesDTO) {
        this.coordinatesDTO = coordinatesDTO;
        stage.show();

    }

    private String getXMLofNode(NodeDTO node) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(NodeDTO.class);
        StringWriter sw = new StringWriter();
        Marshaller ms = context.createMarshaller();
        ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ms.marshal(node, sw);
        return sw.toString();
    }

}
