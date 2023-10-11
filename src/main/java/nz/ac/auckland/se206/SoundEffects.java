package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffects {
  private static MediaPlayer player;
  private static Media sound;

  public static void playSoundEffect(String fileName) throws URISyntaxException {
    sound = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    player = new MediaPlayer(sound);
    player.play();
  }

  public static void playTheme(String themeName) throws URISyntaxException {
    sound = new Media(App.class.getResource("/sounds/" + themeName).toURI().toString());
    Task<Void> musicTask =
        new Task<Void>() {
          @Override
          public Void call() throws Exception {
            player = new MediaPlayer(sound);
            player.play();
            return null;
          }
        };
    new Thread(musicTask, "Game Theme Thread").start();
  }

  public static void stop() {
    for (int i = 0; i < 10; i++) {
      player.setVolume(player.getVolume() - 0.1);
    }
    player.stop();
    System.out.println("Sound stopped");
  }
}
