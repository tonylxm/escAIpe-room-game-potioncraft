package nz.ac.auckland.se206;
import java.util.HashSet;
import java.util.Set;

public class Inventory {
    public Set<Items.Item> inventory;
    
    public Inventory() {
        inventory = new HashSet<Items.Item>();
    }

    /** Adds an item to the inventory */
    public void add(Items.Item item) {
        inventory.add(item);
    }

    /** Removes an item from the inventory given the item*/
    public void remove(Items.Item item) {
        inventory.remove(item);
    }

    /** Checks if an item is in the inventory */
    public boolean contains(Items.Item item) {
        return inventory.contains(item);
    }

    /** Returns the number of items in the inventory */
    public int size() {
        return inventory.size();
    }
}
