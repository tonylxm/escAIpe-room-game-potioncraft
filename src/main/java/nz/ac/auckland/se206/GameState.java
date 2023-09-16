package nz.ac.auckland.se206;

import java.util.ArrayList;

/** Represents the state of the game. */
public class GameState {

  private static ArrayList<Items.Item> recipe = new ArrayList<Items.Item>();

  /** Indicates whether the riddle has been resolved. */
  public static boolean isBookRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  /** Indicates whether the book riddle has been given. */
  public static boolean isBookRiddleGiven = false;

  //getter for recipe
  public static ArrayList<Items.Item> getRecipe() {
    return recipe;
  }

}
