package app;

import utils.project.Project;
import utils.UserInputValidator;
import utils.inventory.YarnInventory;
import utils.project.ProjectHandler;
import utils.yarn.Yarn;
import utils.yarn.YarnWeight;

import java.util.List;
import java.util.Scanner;

/**
app.Main class for running the yarn inventory app
 */
public class App {
    /**
     * Default constructor
     */
    public App(){

    }
    /**
     * Path to the inventory save file
     */
    public static final String INVENTORY_PATH = "./src/main/resources/inventory.json";
    /**
     * Path to the directory containing projects
     */
    public static final String PROJECTS_DIR = "./src/main/resources/projects/";

    /**
     * app.Main class for running the app
     * @param args: user arguments (not used)
     */
    public static void main(String[] args) {
        YarnInventory inv = new YarnInventory(INVENTORY_PATH);

        try (Scanner scanner = new Scanner(System.in)){
            boolean running = true;

            // Runs until the user decides to exit the app
            while (running){
                printOptions();
                running = processRequest(scanner, inv);

                if (running){
                    System.out.println("Press any key to continue...");
                    scanner.nextLine();
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Scanner is closed unexpectedly.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            inv.saveInventory();
        }
    }

    /**
    Prints menu options for the user
     */
    public static void printOptions(){
        System.out.println("--- YARN INVENTORY ---");
        System.out.println("1. Add yarn");
        System.out.println("2. Remove yarn");
        System.out.println("3. Change the length of yarn");
        System.out.println("4. Merge yarns");
        System.out.println("5. Print inventory");
        System.out.println("6. Filter through inventory");
        System.out.println("7. Estimate yarn for a project");
        System.out.println("8. Plan a project");
        System.out.println("9. Manage projects");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Method for processing an individual request. Decides on the action based on the switch statement.
     * @param scanner for reading user input
     * @param inv reference to the inventory
     * @return true/false based on whether the user decided to exit the app
     */
    public static boolean processRequest(Scanner scanner, YarnInventory inv){
        String command = scanner.nextLine();

        switch (command) {
            // Adds yarn
            case "1" -> {
                Yarn yarn = UserInputValidator.validateYarn();
                inv.addItem(yarn);
            }
            // Lists all yarns in inventory and asks for an index of the yarn the user wishes to remove
            case "2" -> {
                inv.printInventory();
                System.out.print("Enter the index of the item you want to remove: ");
                int index = UserInputValidator.validatePositiveInteger(scanner.nextLine()) -1;
                inv.removeAt(index);
            }
            // Lists all yarns in inventory and asks for an index of the yarn to decrease its length
            case "3" -> {
                inv.printInventory();
                System.out.print("Enter the index of the item whose length you want to change: ");
                int index = UserInputValidator.validatePositiveInteger(scanner.nextLine()) -1;
                System.out.print("Enter the amount you want to reduce: ");
                int amount = UserInputValidator.validatePositiveInteger(scanner.nextLine());
                inv.changeLength(index, amount);
            }
            // Merges duplicate yarns into one
            case "4" -> {
                inv.mergeDuplicateYarns();
                inv.printInventory();
            }
            // Prints the entire inventory
            case "5" -> inv.printInventory();
            // Filters the inventory
            case "6" -> processFilterQuery(scanner, inv);
            // Asks for the project name (e.g. hat) and yarn weight and returns min and max length of yarn needed
            case "7" -> {
                Project projectInfo = ProjectHandler.retrieveProjectInfo(scanner);

                // Prints min and max length
                if (projectInfo.getYarnData() != null) {
                    int minMeters = projectInfo.getYarnData().get("min");
                    int maxMeters = projectInfo.getYarnData().get("max");
                    System.out.println("Min: " + minMeters + " meters");
                    System.out.println("Max: " + maxMeters + " meters");
                } else {
                    System.out.println("No data available for this combination.");
                }
            }
            // Asks the user to select yarns they want to use in a project
            case "8" -> {
                // Retrieves project name, yarn weight and min/max length
                Project projectInfo = ProjectHandler.retrieveProjectInfo(scanner);
                int remainingLength = ProjectHandler.getLengthRequirement(projectInfo, scanner);
                List<Yarn> availableYarns = ProjectHandler.getAvailableYarns(projectInfo, scanner, inv);

                System.out.println("You need " + remainingLength + " meters of yarn.");

                // Helps user select yarns for a project
                List<Yarn> selectedYarns = ProjectHandler.selectYarnsForProject(remainingLength, availableYarns, scanner);
                if (selectedYarns == null) break; // If none are selected

                // Prints all selected yarns
                ProjectHandler.printSelectedYarns(selectedYarns);

                // Saves the selected yarn into a (preferably) txt file
                System.out.println("Would you like to save this project? (y/n)");
                if (UserInputValidator.validateIsInList(scanner.nextLine(), "y", "n").equals("y")) {
                    System.out.print("Enter file name: ");
                    String path = PROJECTS_DIR + UserInputValidator.validateFileFormat(scanner.nextLine());
                    ProjectHandler.saveSelectedYarns(path, selectedYarns, projectInfo);
                }
            }
            // Allows the user to see details of a project (reads a text file) or to delete a project
            case "9" -> {
                // Prints inventory and options
                System.out.println("Here is a list of all your projects:");
                ProjectHandler.listProjects();
                System.out.println("---------------------\n What would you like to do?");
                System.out.println("1. See project detail");
                System.out.println("2. Delete project");
                System.out.println("3. Return to menu");

                System.out.print("Enter here: ");
                String cmd = UserInputValidator.validateNonEmptyStr(scanner.nextLine());

                switch (cmd){
                    case "1" -> {
                        System.out.print("Enter project index: ");
                        int index = UserInputValidator.validateIndex(scanner.nextLine(), 3) -1;
                        ProjectHandler.viewProject(index);
                    }
                    case "2" -> {
                        System.out.print("Enter project index: ");
                        int index = UserInputValidator.validateIndex(scanner.nextLine(), 3) -1;
                        ProjectHandler.deleteProject(index);
                    }
                    case "3" -> System.out.println("Returning back");
                }
            }
            // Exit
            case "10" -> {
                return false;
            }
            default -> System.out.println("Unknown command");
        }

        return true;
    }

    /**
     * Asks the user what they want to filter by. Then filters through the inventory.
     * Uses inventory's method "filter".
     * @param sc for reading user input
     * @param inv for accessing the inventory
     */
    public static void processFilterQuery(Scanner sc, YarnInventory inv){
        System.out.println("What would you like to filter by?");
        System.out.println("1. By colour");
        System.out.println("2. By yarn weight");
        System.out.println("3. By brand");

        String command = sc.nextLine();
        List<Yarn> filtered;
        switch (command){
            // Filters by colour
            case "1" -> {
                System.out.print("Enter colour: ");
                String colour = UserInputValidator.validateNonEmptyStr(sc.nextLine());
                filtered = inv.filter(yarn -> yarn.getColor().equalsIgnoreCase(colour));
            }
            // Filters by yarn weight
            case "2" -> {
                System.out.print("Enter yarn weight: ");
                YarnWeight yw = UserInputValidator.validateYarnWeight(sc.nextLine());
                filtered = inv.filter(yarn -> yarn.getWeight().getName().equalsIgnoreCase(yw.getName()));
            }
            // Filters by brand
            case "3" -> {
                System.out.print("Enter brand: ");
                String brand = UserInputValidator.validateNonEmptyStr(sc.nextLine());
                filtered = inv.filter(yarn -> yarn.getBrand().equalsIgnoreCase(brand));
            }
            default -> { System.out.println("Unknown command"); return; }
        }
        // Prints the results
        for (Yarn y : filtered){
            System.out.println(y);
        }
        if (filtered.isEmpty()){
            System.out.println("Nothing found.");
        }
    }
}