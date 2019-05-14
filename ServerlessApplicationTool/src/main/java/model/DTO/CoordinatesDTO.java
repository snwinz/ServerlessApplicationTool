package model.DTO;

public class CoordinatesDTO extends NodeDTO {

    private final double screenX;
    private final double screenY;

    public CoordinatesDTO(double screenX, double screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public double getScreenX() {
        return screenX;
    }

    public double getScreenY() {
        return screenY;
    }
}
