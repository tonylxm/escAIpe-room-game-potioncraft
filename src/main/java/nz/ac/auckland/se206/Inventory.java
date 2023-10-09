package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.layout.VBox;

public class Inventory {
  private Set<Items.Item> inventory;
  private VBox box;

  /**
   * Constructor for the inventory.
   */
  public Inventory() {
    inventory = new HashSet<Items.Item>();
    box = new VBox();
  }

  /** 
   * Adds an item to the inventory. 
   */
  public void add(Items.Item item) {
    inventory.add(item);
  }

  /** 
   * Removes an item from the inventory given the item.
   */
  public void remove(Items.Item item) {
    inventory.remove(item);
  }

  /** 
   * Checks if an item is in the inventory. 
   */
  public boolean contains(Items.Item item) {
    return inventory.contains(item);
  }

  /** 
   * Returns the number of items in the inventory. 
   */
  public int size() {
    return inventory.size();
  }

  /** 
   * Returns the inventory. 
   */
  public Set<Items.Item> getInventory() {
    return inventory;
  }

  /**
   * Sets the inventory.
   * 
   * @param inventory
   */
  public void setInventory(Set<Items.Item> inventory) {
    this.inventory = inventory;
  }

  /**
   * Returns the box.
   * 
   * @return
   */
  public VBox getBox() {
    return box;
  }

  /**
   * Sets the box.
   * 
   * @param box
   */
  public void setBox(VBox box) {
    this.box = box;
  }
}
