package gui.view.nodes;

import gui.controller.GraphVisualisationController;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.Optional;

public class DraggableArrow extends Group {
    private static final double arrowLength = 20;
    private static final double arrowWidth = 7;
    private final Line line;
    private DraggableArrow draggableArrowThis = this;
    private model.structure.Arrow alias;
    private GraphVisualisationController controller;
    private final DoubleProperty startXOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty startYOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty endXOffset = new SimpleDoubleProperty(0);
    private final DoubleProperty endYOffset = new SimpleDoubleProperty(0);
    private String name;
    private double originalClickPositionX = 0;
    private double originalClickPositionY = 0;
    private double originalStartOffsetPositionX = 0;
    private double originalStartOffsetPositionY = 0;
    private double originalEndOffsetPositionX = 0;
    private double originalEndOffsetPositionY = 0;
    private boolean wasClickNearerToStartNode = true;

    public DraggableArrow(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY, model.structure.Arrow arrow,
                          Line line, Line arrow1, Line arrow2) {
        super(line, arrow1, arrow2);

        setName(arrow.getName());
        setAlias(arrow);
        line.strokeWidthProperty().set(3);
        arrow1.strokeWidthProperty().set(3);
        arrow2.strokeWidthProperty().set(3);
        this.line = line;
        InvalidationListener updater = o -> {
            double ex = getEndX();
            double ey = getEndY();
            double sx = getStartX();
            double sy = getStartY();

            arrow1.setEndX(ex);
            arrow1.setEndY(ey);
            arrow2.setEndX(ex);
            arrow2.setEndY(ey);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrow1.setStartX(ex);
                arrow1.setStartY(ey);
                arrow2.setStartX(ex);
                arrow2.setStartY(ey);
            } else {
                double factor = arrowLength / Math.hypot(sx - ex, sy - ey);
                double factorO = arrowWidth / Math.hypot(sx - ex, sy - ey);

                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrow1.setStartX(ex + dx - oy);
                arrow1.setStartY(ey + dy + ox);
                arrow2.setStartX(ex + dx + oy);
                arrow2.setStartY(ey + dy - ox);
            }
        };

        startXProperty().addListener(updater);
        startYProperty().addListener(updater);
        endXProperty().addListener(updater);
        endYProperty().addListener(updater);
        updater.invalidated(null);

        DoubleBinding startXBinding = getPositiveSummedBinding(startX, startXOffset);
        DoubleBinding startYBinding = getPositiveSummedBinding(startY, startYOffset);
        DoubleBinding endXBinding = getPositiveSummedBinding(endX, endXOffset);
        DoubleBinding endYBinding = getPositiveSummedBinding(endY, endYOffset);


        startXProperty().bind(startXBinding);
        startYProperty().bind(startYBinding);
        endXProperty().bind(endXBinding);
        endYProperty().bind(endYBinding);

        Text text = new Text(".");

        getChildren().add(text);

        DoubleBinding xPositionText = getMiddle(startXBinding, endXBinding);
        DoubleBinding yPositionText = getMiddle(startYBinding, endYBinding);


        text.xProperty().bind(xPositionText);
        text.yProperty().bind(yPositionText);

        this.setOnMousePressed(mouseClicked());
        this.setOnMouseDragged(dragMouse());


    }

    private DoubleBinding getMiddle(DoubleBinding start, DoubleBinding end) {
        return new DoubleBinding() {
            {
                super.bind(start, end);
            }

            @Override
            protected double computeValue() {
                double startValue = start.doubleValue();
                double endValue = end.doubleValue();
                double diff = Math.abs(startValue - endValue);
                return startValue > endValue ? endValue + diff / 2 : startValue + diff / 2;

            }
        };
    }

    private DoubleBinding getPositiveSummedBinding(DoubleProperty summand1, DoubleProperty summand2) {
        return new DoubleBinding() {
            {
                super.bind(summand1, summand2);
            }

            @Override
            protected double computeValue() {
                double result = summand1.getValue() + summand2.getValue();
                return (result > 0) ? result : 0;
            }
        };
    }

    private static double getShift(Double currentOffset, String textOfAffectedCoordinate) {

        TextInputDialog dialog = new TextInputDialog(currentOffset.toString());
        dialog.setTitle("Dialog to change " + textOfAffectedCoordinate);
        dialog.setHeaderText("Change" + textOfAffectedCoordinate + "!");
        dialog.setContentText("Enter a number:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Double.parseDouble(result.get());
            } catch (NumberFormatException e) {
                System.err.println("Input could not be parsed to an integer");
                System.err.println(Arrays.toString(e.getStackTrace()));
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Input could not be parsed to an integer");
                alert.setContentText("Please try it again!");
                alert.showAndWait();
                return currentOffset;
            }
        }
        return currentOffset;
    }

    public DoubleProperty endXOffsetProperty() {
        return endXOffset;
    }

    public double getEndYOffset() {
        return endYOffset.get();
    }

    public DoubleProperty endYOffsetProperty() {
        return endYOffset;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public void setupArrow(GraphVisualisationController controller) {
        this.controller = controller;
        this.setOnContextMenuRequested(contextMenuClicked());
    }

    private double getStartX() {
        return line.getStartX();
    }

    public final void setStartX(double value) {
        line.setStartX(value);
    }

    private DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    private double getStartY() {
        return line.getStartY();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
    }

    private DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    private double getEndX() {
        return line.getEndX();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
    }

    private DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    private double getEndY() {
        return line.getEndY();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
    }

    private DoubleProperty endYProperty() {
        return line.endYProperty();
    }

    private EventHandler<MouseEvent> dragMouse() {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getX() - originalClickPositionX;
                double deltaY = event.getY() - originalClickPositionY;

                if (wasClickNearerToStartNode) {

                    startXOffset.set(deltaX + originalStartOffsetPositionX);
                    startYOffset.set(deltaY + originalStartOffsetPositionY);
                } else {
                    endXOffset.set(deltaX + originalEndOffsetPositionX);
                    endYOffset.set(deltaY + originalEndOffsetPositionY);
                }
            }
        };
    }

    private EventHandler<MouseEvent> mouseClicked() {
        return event -> {
            if (event.getButton() == MouseButton.PRIMARY) {


                originalClickPositionX = event.getX();
                originalClickPositionY = event.getY();
                originalStartOffsetPositionX = startXOffset.getValue();
                originalStartOffsetPositionY = startYOffset.getValue();

                originalEndOffsetPositionX = endXOffset.getValue();
                originalEndOffsetPositionY = endYOffset.getValue();

                double deltaXTolerance = Math.abs((endXProperty().getValue() - startXProperty().getValue()) / 2);
                wasClickNearerToStartNode = Math.abs(event.getX() - startXProperty().getValue()) < Math.abs(deltaXTolerance);


            }
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    String contentOfArrow = alias.toString();
                    controller.showArrowInfoBox(contentOfArrow);
                }
            }
        };
    }

    private EventHandler<ContextMenuEvent> contextMenuClicked() {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemRemoveArrow = new MenuItem("Remove Arrow");
        MenuItem itemStartX = new MenuItem("Move starting point of X coordinates");
        MenuItem itemStartY = new MenuItem("Move starting point of Y coordinates");
        MenuItem itemEndX = new MenuItem("Move end point of X coordinates");
        MenuItem itemEndY = new MenuItem("Move end point of Y coordinates");
        MenuItem itemShowInfo = new MenuItem("Show information of arrow");

        itemRemoveArrow.setOnAction(event -> controller.remove(DraggableArrow.this));

        itemStartX.setOnAction(event -> {
            double shift =
                    getShift(startXOffset.getValue(), "starting point x");
            startXOffset.set(shift);
        });

        itemStartY.setOnAction(event -> {
            double shift =
                    getShift(startYOffset.getValue(), "starting point y");
            startYOffset.set(shift);
        });
        itemEndX.setOnAction(event -> {
            double shift =
                    getShift(endXOffset.getValue(), "ending point x");
            endXOffset.set(shift);
        });

        itemEndY.setOnAction(event -> {
            double shift =
                    getShift(endYOffset.getValue(), "ending point y");
            endYOffset.set(shift);
        });


        itemShowInfo.setOnAction(event -> {
            String contentOfArrow = alias.toString();
            controller.showArrowInfoBox(contentOfArrow);
        });



        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(itemRemoveArrow, itemStartX, itemStartY, itemEndX, itemEndY, itemShowInfo);

        return event -> {
            event.consume();
            contextMenu.show(this, event.getScreenX(), event.getScreenY());

        };
    }

    public model.structure.Arrow getAlias() {
        return alias;
    }

    private void setAlias(model.structure.Arrow alias) {
        this.alias = alias;
    }


    public double getStartXOffset() {
        return startXOffset.get();
    }

    public DoubleProperty startXOffsetProperty() {
        return startXOffset;
    }

    public double getStartYOffset() {
        return startYOffset.get();
    }

    public DoubleProperty startYOffsetProperty() {
        return startYOffset;
    }

    public double getEndXOffset() {
        return endXOffset.get();
    }

    public void setStartXProperty(double startXOffset) {
        this.startXOffset.set(startXOffset);
    }

    public void setStartYProperty(double startYOffset) {
        this.startYOffset.set(startYOffset);
    }

    public void setEndXProperty(double endXOffset) {
        this.endXOffset.set(endXOffset);
    }

    public void setEndProperty(double endYOffset) {
        this.endYOffset.set(endYOffset);
    }
}