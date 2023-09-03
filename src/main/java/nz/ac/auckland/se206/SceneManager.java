package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppUi {
    MAIN_MENU,
    CAULDRON_ROOM,
    BOOK,
    BOOKSHELF,
    SHELF_LEFT,
    SHELF_RIGHT,
    GAME_OVER
  }

  public static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();
  public static AppUi currScene;
  public static AppUi timerScene;

  public static void addAppUi(AppUi appUi, Parent root) {
    sceneMap.put(appUi, root);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  public static void setTimerScene(AppUi appUi) {
    if (sceneMap.containsKey(appUi)) {
      timerScene = appUi;
    } else {
      System.out.println("Scene not found.");
    }
  }

  public static AppUi getTimerScene() {
    return timerScene;
  }
}
