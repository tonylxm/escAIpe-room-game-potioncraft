package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.BookController;
import nz.ac.auckland.se206.controllers.CauldronController;

public class SceneManager {

  public enum AppUi {
    MAIN_MENU,
    CAULDRON_ROOM,
    BOOK,
    BOOKSHELF,
    LIBRARY_ROOM,
    TREASURE_ROOM,
    YOU_WIN,
    YOU_LOSE,
    CAULDRON,
  }

  public static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();
  public static AppUi currScene;
  public static AppUi timerScene;
  private static CauldronController cauldronControllerInstance;
  private static BookController bookControllerInstance;

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

  // Set the CauldronController instance
  public static void setCauldronControllerInstance(CauldronController controller) {
    cauldronControllerInstance = controller;
  }

  // Get the CauldronController instance
  public static CauldronController getCauldronControllerInstance() {
    return cauldronControllerInstance;
  }

  // Set the BookController instance
  public static void setBookControllerInstance(BookController controller) {
    bookControllerInstance = controller;
  }

  // Get the BookController instance
  public static BookController getBookControllerInstance() {
    return bookControllerInstance;
  }
}
