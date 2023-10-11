package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffects {
  private MediaPlayer player;

  public void playSound(String fileName) throws URISyntaxException {
    Media sound = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    player = new MediaPlayer(sound);
    player.play();
  }

  public void stop() {
    // for (int i = 0; i < 10; i++) {
    //   player.setVolume(player.getVolume() - 0.1);
    // }
    player.stop();
    System.out.println("Sound stopped");
  }
}
