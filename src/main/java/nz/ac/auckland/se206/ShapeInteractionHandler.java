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

  /**
   * Handles mouse events for shapes. When the mouse enters a shape, it glows. 
   * When the mouse exits a shape, it stops glowing.
   */
  @Override
  public void handle(MouseEvent event) {
    Shape shape = (Shape) event.getSource();

    if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
      glowThis(shape);
    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
      unglowThis(shape);
    }
  }

  /** Adding specified glow to images in a room. */
  public void glowThis(ImageView image) {
    image.setEffect(new Glow(1.2));
  }

  /** Adding specified glow to shapes in a room. */
  private void glowThis(Shape shape) {
    shape.setStroke(GLOW_COLOR);
    shape.setStrokeWidth(GLOW_STROKE_WIDTH);
  }

  /** Turning off the glow of an image no matter what. */
  public void unglowThis(ImageView image) {
    image.setEffect(new Glow(0));
  }

  /** Turning off the glow of a shape. */
  private void unglowThis(Shape shape) {
    shape.setStrokeWidth(0);
    shape.setStroke(HIGH_LIGHT_COLOR);
  }

  /** Turning off the glow of an image unless it's been clicked. */
  public void unglowThis(ImageView image, boolean clicked) {
    if (!clicked) {
      image.setEffect(new Glow(0));
    }
  }
}
