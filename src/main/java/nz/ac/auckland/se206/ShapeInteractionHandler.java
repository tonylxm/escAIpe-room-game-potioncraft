package nz.ac.auckland.se206;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

public class ShapeInteractionHandler implements EventHandler<MouseEvent> {
  private static final double GLOW_STROKE_WIDTH = 5;

  @Override
  public void handle(MouseEvent event) {
    Shape shape = (Shape) event.getSource();

    if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
      glowThis(shape);
    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
      unglowThis(shape);
    }
  }

  private void glowThis(Shape shape) {
    shape.setStrokeWidth(GLOW_STROKE_WIDTH);
  }

  private void unglowThis(Shape shape) {
    shape.setStrokeWidth(0);
  }
}
