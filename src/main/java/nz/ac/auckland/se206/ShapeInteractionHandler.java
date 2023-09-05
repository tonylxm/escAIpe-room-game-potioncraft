package nz.ac.auckland.se206;

import javafx.event.EventHandler;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ShapeInteractionHandler implements EventHandler<MouseEvent> {
  private static final double GLOW_STROKE_WIDTH = 5;
  private static final Color GLOW_COLOR = Color.BLACK;
  private static final Color HIGH_LIGHT_COLOR = Color.GOLD;

  @Override
  public void handle(MouseEvent event) {
    Shape shape = (Shape) event.getSource();

    if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
      glowThis(shape);
    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
      unglowThis(shape);
    }
  }

  public void glowThis(ImageView image) {
    image.setEffect(new Glow(0.8));
  }

  public void unglowThis(ImageView image) {
    image.setEffect(new Glow(0));
  }

  private void glowThis(Shape shape) {
    shape.setStroke(GLOW_COLOR);
    shape.setStrokeWidth(GLOW_STROKE_WIDTH);
  }

  private void unglowThis(Shape shape) {
    shape.setStrokeWidth(0);
    shape.setStroke(HIGH_LIGHT_COLOR);
  }
}
