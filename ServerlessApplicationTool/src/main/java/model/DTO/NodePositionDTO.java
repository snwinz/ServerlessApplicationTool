package model.DTO;

import gui.view.nodes.DraggableNode;

public class NodePositionDTO {
    private String name;

    private double x;
    private double y;

    public NodePositionDTO() {
    }

    public NodePositionDTO(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public static NodePositionDTO getNodePosition(DraggableNode draggableNode) {

        String name = draggableNode.getNameOfNode();
        double xPos = draggableNode.layoutXProperty().getValue();
        double yPos = draggableNode.layoutYProperty().getValue();
        return new NodePositionDTO(name, xPos, yPos);
    }


}
