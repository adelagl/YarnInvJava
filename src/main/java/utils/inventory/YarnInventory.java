package utils.inventory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import utils.yarn.Yarn;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class representing the yarn inventory. Implements the Inventory interface and contains methods for
 * adding, removing and adjusting yarn.
 * Loads and saves the inventory to/from s JSON file.
 */
public class YarnInventory implements Inventory{
    private ArrayList<Yarn> inventory;
    private final String FILE_PATH;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Constructor for initializing fields in yarn inventory
     * @param filePath: path to the save file of the inventory
     */
    public YarnInventory(String filePath){
        inventory = new ArrayList<>();
        FILE_PATH = filePath;
        inventory = loadInventory();
    }

    /**
     * Method for serializing the inventory into a JSON file using GSON.
     */
    public void saveInventory() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(this.inventory, writer);
            System.out.println("Inventory saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Failed to save the inventory to a JSON file" + e.getMessage());
        }
    }

    /**
     * Method for testing purposes
     * @return the inventory
     */
    public List<Yarn> getInventory(){
        return inventory;
    }

    /**
     * Method for loading inventory from a JSON file using GSON.
     * @return the list of yarns in the inventory
     */
    public ArrayList<Yarn> loadInventory() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            System.out.println("here");
            Type listType = new TypeToken<List<Yarn>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("Failed to load the inventory from a JSON file" + e.getMessage());
            return null;
        }
    }

    /**
     * Method for adding yarn
     * @param y: yarn to be added
     */
    public <Yarn> void addItem(Yarn y){
        inventory.add((utils.yarn.Yarn) y);
    }

    /**
     * Method for removing yarn at an index within the list of yarns
     * @param index: the index of yarn to be removed
     */
    public void removeAt(int index){
        inventory.remove(index);
    }

    /**
     * Method for decreasing length of yarn. Useful if the user only used e.g. half of the yarn in a project
     * @param index: index of yarn to be modified
     * @param amount:
     */
    public void changeLength(int index, int amount){
        Yarn y = inventory.get(index);
        y.changeLength(amount);
        if (y.getLengthMeters() <= 0){
            inventory.remove(index);
        }
    }

    /**
     * Prints the inventory items with a distinct index
     */
    public void printInventory(){
        int index = 1;
        for (Yarn yarn : inventory){
            System.out.println(index + ": " + yarn);
            index++;
        }
    }

    /**
     * Filters yarn based on a condition
     * @param condition accepts a lambda expression which is a condition that it filters by
     * @return the list of filtered yarns
     */
    public List<Yarn> filter(Predicate<Yarn> condition) {
        return inventory.stream()
                .filter(condition) // Apply the passed lambda function
                .collect(Collectors.toList());

    }

    /**
     * Method for merging yarns with the same name, brand and yarn weight. Traverses the entire inventory and merges
     * all possible yarns.
     */
    public void mergeDuplicateYarns() {
        List<Yarn> mergedList = new ArrayList<>();

        for (Yarn yarn : inventory) {
            boolean merged = false;

            // Check if this yarn matches an existing one in mergedList
            for (Yarn existingYarn : mergedList) {
                if (existingYarn.compare(yarn)) {
                    existingYarn.changeLength(-yarn.getLengthMeters());
                    merged = true;
                    break;
                }
            }

            // Adds as a new entry if no matches were found
            if (!merged) {
                mergedList.add(new Yarn(yarn.getBrand(), yarn.getColor(), yarn.getLengthMeters(), yarn.getWeight()));
            }
        }

        // Replaces the inventory with the new mergedList
        inventory = new ArrayList<>(mergedList);

        System.out.println("Merged duplicate yarns successfully.");
    }
}
