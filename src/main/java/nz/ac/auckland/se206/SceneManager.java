package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.BookController;
import nz.ac.auckland.se206.controllers.CauldronController;
import nz.ac.auckland.se206.controllers.CauldronRoomController;
import nz.ac.auckland.se206.controllers.LibraryRoomController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.TreasureRoomController;

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
    CHEST
  }

  public static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();
  public static AppUi currScene;
  public static AppUi timerScene;
  private static CauldronController cauldronControllerInstance;
  private static TreasureRoomController treasureRoomControllerInstance;
  private static BookController bookControllerInstance;
  private static LibraryRoomController  libraryRoomControllerInstance;
  private static TreasureRoomController treasureRoomControllerInstance;
  private static CauldronRoomController cauldronRoomControllerInstance;


  public static RoomController getCurrentController() {
    //switch case that returns the controller based on currScene
    RoomController controller = null;
    if (currScene == AppUi.LIBRARY_ROOM) {
      controller = libraryRoomControllerInstance;
    } else if (currScene == AppUi.TREASURE_ROOM) {
      controller = treasureRoomControllerInstance;
    } else if (currScene == AppUi.CAULDRON_ROOM) {
      controller = cauldronRoomControllerInstance;
    }
    return controller;
  }

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

  //getters and setters for the rest of the controllers
  public static void setLibraryRoomControllerInstance(LibraryRoomController controller) {
    libraryRoomControllerInstance = controller;
  }

  public static LibraryRoomController getLibraryRoomControllerInstance() {
    return libraryRoomControllerInstance;
  }

  public static void setTreasureRoomControllerInstance(TreasureRoomController controller) {
    treasureRoomControllerInstance = controller;
  }

  public static TreasureRoomController getTreasureRoomControllerInstance() {
    return treasureRoomControllerInstance;
  }

  public static void setCauldronRoomControllerInstance(CauldronRoomController controller) {
    cauldronRoomControllerInstance = controller;
  }

  public static CauldronRoomController getCauldronRoomControllerInstance() {
    return cauldronRoomControllerInstance;
  }

}
