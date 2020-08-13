package model.DTO;

public class CoordinatesDTO extends NodeDTO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
