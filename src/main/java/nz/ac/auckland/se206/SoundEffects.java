package nz.ac.auckland.se206;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Class for playing sound effects and music in the game. Only one sound effect
 * can be played at a time. The music is played in the background and can be
 * played at the same time as a sound effect.
 */
public class SoundEffects {
  private MediaPlayer player;

  /**
   * Plays a sound effect give the string for the file name that the 
   * sound needs to be played from.
   * 
   * @param fileName the name of the sound file to be played.
   * @throws URISyntaxException if the sound file is not found.
   */
  public void playSound(String fileName) throws URISyntaxException {
    Media sound = new Media(App.class.getResource("/sounds/" + fileName).toURI().toString());
    player = new MediaPlayer(sound);
    player.play();
  }
  
  /**
   * Stops all sound being played
   */
  public void stop() {
    player.stop();
    System.out.println("Sound stopped");
  }
}
