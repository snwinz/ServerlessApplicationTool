package model.logic.modelcreation;

import model.DTO.GraphVisualisationDTO;
import model.structure.Graph;

import javax.xml.bind.JAXBException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ApplicationModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ApplicationModel() {
    }

    private void createNewGraphVisualisation(GraphVisualisationDTO graphVisualisationDTO) {

        this.pcs.firePropertyChange("newGraphSet", null, graphVisualisationDTO);
    }

    public void createNewGraphVisualisation(Graph graph) {
        GraphVisualisationDTO graphVisualisationDTO = new GraphVisualisationDTO(graph);
        createNewGraphVisualisation(graphVisualisationDTO);
    }


    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }


    public void loadGraph(String path) {

        try {
            GraphVisualisationDTO graphVisualisationDTO = Loader.loadGraph(path);
            createNewGraphVisualisation(graphVisualisationDTO);
        } catch (JAXBException e) {
            System.out.println("Graph couldn't be loaded");
            e.printStackTrace();
        }
    }
}
