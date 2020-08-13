package model.DTO;

import model.structure.Graph;
import model.structure.Node;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class GraphVisualisationDTO extends Node {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<NodePositionDTO> nodePositionDTOS = new ArrayList<>();

    private List<ArrowPositionDTO> arrowPositionDTOS = new ArrayList<>();

    private Graph graph;

    public GraphVisualisationDTO() {
    }

    public GraphVisualisationDTO(Graph graph) {
        this.graph = graph;
    }

    public void addNodePosition(NodePositionDTO nodePositionDTO) {
        nodePositionDTOS.add(nodePositionDTO);
    }

    public void addArrowPosition(ArrowPositionDTO arrowPositionDTO) {
        arrowPositionDTOS.add(arrowPositionDTO);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<NodePositionDTO> getNodePositionDTOS() {
        return nodePositionDTOS;
    }

    public void setNodePositionDTOS(List<NodePositionDTO> nodePositionDTOS) {
        this.nodePositionDTOS = nodePositionDTOS;
    }

    public List<ArrowPositionDTO> getArrowPositionDTOS() {
        return arrowPositionDTOS;
    }

    public void setArrowPositionDTOS(List<ArrowPositionDTO> arrowPositionDTOS) {
        this.arrowPositionDTOS = arrowPositionDTOS;
    }
}
