package gui.view;

import gui.controller.ArrowCreatorController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.DTO.ArrowDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Arrays;

public class ArrowCreatorView extends Dialog<Object> {

	private final ArrowCreatorController controller;
	private final ComboBox<String> comboBoxSuccessor = new ComboBox<>();
	private final ComboBox<String> comboBoxPredecessor = new ComboBox<>();
	private final Stage stage;
	private final TextArea textAreaXML = new TextArea();

	public ArrowCreatorView(ArrowCreatorController controller) {
		this.controller = controller;
		this.stage = new Stage();

		Label predecessorText = new Label("Name of predecessor node: ");
		Label successorText = new Label("Name of successor node: ");

		Label xmlText = new Label("New arrow as XML (please add relevant information): ");

		comboBoxSuccessor.valueProperty().addListener((ov, t, t1) -> updateTextArea());
		controller.fillComboBoxWithAllNodes(comboBoxSuccessor);

		controller.fillComboBoxWithAllNodes(comboBoxPredecessor);

		GridPane grid = new GridPane();
		grid.add(predecessorText, 1, 1);
		grid.add(comboBoxPredecessor, 2, 1);
		grid.add(successorText, 1, 2);
		grid.add(comboBoxSuccessor, 2, 2);
		grid.add(xmlText, 1, 3);
		grid.add(textAreaXML, 2, 3);

		Button createButton = new Button("Add arrow");
		createButton.setOnAction(getCreateButtonHandler());
		Button cancelButton = new Button("cancel");
		cancelButton.setOnAction(getCancelButtonHandler());
		grid.add(createButton, 1, 4);
		grid.add(cancelButton, 2, 4);
		Scene scene = new Scene(grid);
		stage.setScene(scene);

	}

	private void updateTextArea() {

		ArrowDTO arrowDTO = new ArrowDTO();
		if (comboBoxPredecessor.getValue() != null) {
			arrowDTO.setPredecessor(comboBoxPredecessor.getValue());
		}
		if (comboBoxSuccessor.getValue() != null) {
			arrowDTO.setSuccessor(comboBoxSuccessor.getValue());
		}
		setTextArrow(arrowDTO);
	}

	public void setup() {
		stage.showAndWait();
	}

	private void setTextArrow(ArrowDTO arrowDTO) {
		try {
			String xmlOfNode = getXMLofArrow(arrowDTO);
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
		return event -> controller.closeWindow();
	}

	private EventHandler<ActionEvent> getCreateButtonHandler() {
		return event -> {
			controller.addArrowToGraph(textAreaXML.getText());
			stage.close();
		};
	}

	private String getXMLofArrow(ArrowDTO node) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ArrowDTO.class);
		StringWriter sw = new StringWriter();
		Marshaller ms = context.createMarshaller();
		ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ms.marshal(node, sw);
		return sw.toString();
	}

	public void closeWindow() {
		stage.close();
	}

}
