package model.DTO;

import gui.view.nodes.DraggableArrow;

public class ArrowPositionDTO {
    private String name;

    private double xOffset;
    private double yOffset;
    private double startXOffset;
    private double startYOffset;
    private double endXOffset;
    private double endYOffset;


    public ArrowPositionDTO() {
    }

    private ArrowPositionDTO(String name, double startXOffset, double startYOffset, double endXOffset, double endYOffset) {
        this.name = name;
        this.startXOffset = startXOffset;
        this.startYOffset = startYOffset;
        this.endXOffset = endXOffset;
        this.endYOffset = endYOffset;
    }

    public static ArrowPositionDTO getArrowPosition(DraggableArrow draggableArrow) {
        String name = draggableArrow.getName();
        double startXOffset = draggableArrow.getStartXOffset();
        double startYOffset = draggableArrow.getStartYOffset();
        double endXOffset = draggableArrow.getEndXOffset();
        double endYOffset = draggableArrow.getEndYOffset();
        return new ArrowPositionDTO(name, startXOffset, startYOffset, endXOffset, endYOffset);
    }

    public double getStartXOffset() {
        return startXOffset;
    }

    public void setStartXOffset(double startXOffset) {
        this.startXOffset = startXOffset;
    }

    public double getStartYOffset() {
        return startYOffset;
    }

    public void setStartYOffset(double startYOffset) {
        this.startYOffset = startYOffset;
    }

    public double getEndXOffset() {
        return endXOffset;
    }

    public void setEndXOffset(double endXOffset) {
        this.endXOffset = endXOffset;
    }

    public double getEndYOffset() {
        return endYOffset;
    }

    public void setEndYOffset(double endYOffset) {
        this.endYOffset = endYOffset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getxOffset() {
        return xOffset;
    }

    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public double getyOffset() {
        return yOffset;
    }

    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

}
