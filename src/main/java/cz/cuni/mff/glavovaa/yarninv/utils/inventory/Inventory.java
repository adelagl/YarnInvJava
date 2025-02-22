package cz.cuni.mff.glavovaa.yarninv.utils.inventory;

import java.util.List;

/**
 * Interface defining basic methods of an inventory
 */
public interface Inventory {
    /**
     * Adds an item to the inventory
     * @param item: item to be added
     * @param <T>: type of the item
     */
    <T> void addItem(T item);

    /**
     * Removes the item at an index
     * @param index:index of the item
     */
    void removeAt(int index);

    /**
     * Prints the contents of the inventory.
     */
    void printInventory();

    /**
     * Method for loading the inventory from a file
     * @return the loaded contents of the inventory
     * @param <T>: type of items in the inventory
     */
    <T> List<T> loadInventory();

    /**
     * Saves the inventory into a file
     */
    void saveInventory();
}
