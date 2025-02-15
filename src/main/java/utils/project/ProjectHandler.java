package utils.project;

import utils.UserInputValidator;
import utils.inventory.YarnInventory;
import utils.yarn.Yarn;
import utils.yarn.YarnWeight;

import java.io.*;
import java.util.*;

/**
 * Class for handling requests regarding project lengths and choosing yarn for projects.
 */
public class ProjectHandler {
    private static final String PROJECTS_DIR = "./src/main/resources/projects/";

    /**
     * Default constructor.
     */
    public ProjectHandler(){

    }
    /**
     * Retrieves project name (e.g. hat), yarn weight and min and max length of yarn needed.
     * @param scanner: for reading user input
     * @return Project containing the information
     */
    public static Project retrieveProjectInfo(Scanner scanner) {
        Calculator calc = new Calculator();

        // Lists all available projects
        System.out.println("Here is a list of available projects: ");
        int index = 1;
        for (String project : calc.getAvailableProjects()) {
            System.out.println(index + ": " + project);
            index++;
        }

        // Asks the user to choose a project
        System.out.print("Which project would you like to make? ");
        int projectIndex = UserInputValidator.validateIndex(scanner.nextLine(), calc.getProjectCount()) - 1;
        String selectedProject = calc.getProjectByIndex(projectIndex);

        // Asks for yarn weight
        System.out.println("Which yarn weight do you want to use?");
        YarnWeight yw = UserInputValidator.validateYarnWeight(scanner.nextLine());

        // Gets yarn requirement from calculator
        Map<String, Integer> yarnData = calc.getYarnRequirement(selectedProject, yw.getName());

        return new Project(selectedProject, yw, yarnData);
    }

    /**
     * Gets the length required to finish a project
     * @param projectInfo: information containing name, min and max length and weight
     * @param scanner: for reading user input
     * @return returns the final length
     */
    public static int getLengthRequirement(Project projectInfo, Scanner scanner){
        int minNeeded = projectInfo.getYarnData().get("min");
        int maxNeeded = projectInfo.getYarnData().get("max");

        System.out.println("Do you want to use the minimum or maximum required length? (min/max)");
        String choice = UserInputValidator.validateIsInList(scanner.nextLine(), "min", "max");
        if (choice.equals("min")){
            return minNeeded;
        }
        else{
            return maxNeeded;
        }
    }

    /**
     * Returns yarns filtered by yarn weight
     * @param proj: information about project
     * @param scanner: for reading user input
     * @param inv: for accessing yarn in inventory
     * @return list of filtered yarns
     */
    public static List<Yarn> getAvailableYarns(Project proj, Scanner scanner, YarnInventory inv){
        System.out.println("Would you like to only use yarn of the same yarn weight? (y/n)");
        String answer = UserInputValidator.validateIsInList(scanner.nextLine(), "y","n");

        String selectedWeight = proj.getYarnWeight().getName();

        if (answer.equals("y")){
            // Filters yarns based on yarn weight
            return inv.filter(yarn -> yarn.getWeight().getName().equals(selectedWeight));
        }
        else{
            // If user does not want to filter by weight, return all yarns in the inventory
            return inv.filter(yarn -> true);
        }
    }

    /**
     * Prints yarns.
     * @param selectedYarns yarns that were selected
     */
    public static void printSelectedYarns(List<Yarn> selectedYarns){
        System.out.println("You have enough yarn for the project!");
        System.out.println("Selected yarns:");
        for (Yarn yarn : selectedYarns) {
            System.out.println("- " + yarn.getBrand() + " (" + yarn.getColor() + ", " + yarn.getLengthMeters() + "m)");
        }
    }

    /**
     * Saves yarns into a (text) file.
     * @param filePath: path to the file
     * @param projectInv: inventory of yarns
     * @param project: info about project (name, weight, ...)
     */
    public static void saveSelectedYarns(String filePath, List<Yarn> projectInv, Project project){
        // Attempts to write the information into a file
        try (Writer writer = new FileWriter(filePath)) {
            writer.write(project.getProjectName() + "\n");
            writer.write("Required yarn:\n");
            for (Yarn y : projectInv){
                writer.write(y.toString());
            }

            System.out.println("The yarn required for this project was saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing to file." + e.getMessage());
        }
    }

    /**
     * Repeatedly asks user to choose yarn until it meets the required length. The yarn needs to have the same yarn weight.
     * @param remainingLength: length needed to reach the end goal
     * @param availableYarns: list of yarns with the concrete yarn weight
     * @param scanner: for reading user's input
     * @return the list of yarns used in the project
     */
    public static List<Yarn> selectYarnsForProject(int remainingLength, List<Yarn> availableYarns, Scanner scanner){
        boolean sufficient = true;
        List<Yarn> selectedYarns = new ArrayList<>();
        while (remainingLength > 0) {
            // If the user doesn't have enough yarn left in the inventory, the loop breaks
            if (availableYarns.isEmpty()) {
                System.out.println("You do not have any more yarn.");
                sufficient = false;
                break;
            }

            // Lists all available yarns
            for (int i = 0; i < availableYarns.size(); i++) {
                System.out.println(i+1 + ": " + availableYarns.get(i));
            }

            // Asks the user to choose an index of yarn
            System.out.print("Choose a yarn to add by entering its number: ");
            int yarnIndex = UserInputValidator.validateIndex(scanner.nextLine(), availableYarns.size()) - 1;

            // Adds the yarn to selectedYarns and removes it from availableYarns
            Yarn chosenYarn = availableYarns.get(yarnIndex);
            selectedYarns.add(chosenYarn);
            remainingLength -= chosenYarn.getLengthMeters();
            availableYarns.remove(yarnIndex);

            System.out.println("Added " + chosenYarn.getBrand() + " (" + chosenYarn.getLengthMeters() + "m).");
            System.out.println("Remaining length needed: " + Math.max(remainingLength, 0) + " meters.");
        }

        // Prints the selected yarns
        if (sufficient) {
            return selectedYarns;
        }
        return null;
    }

    /**
     * Goes through all files in the .resources/projects directory and prints the first line
     */
    public static void listProjects() {
        File dir = new File(PROJECTS_DIR);
        File[] files = dir.listFiles(); // Saves all files in an array

        if (files == null || files.length == 0) {
            System.out.println("No saved projects found.");
            return;
        }

        // Goes through files and attempts to read the first line (containing the project type)
        System.out.println("Saved Projects:");
        for (int i = 0; i < files.length; i++) {
            try (BufferedReader reader = new BufferedReader(new FileReader(files[i]))) {
                String firstLine = reader.readLine();
                System.out.println((i + 1) + ". " + (firstLine != null ? firstLine : files[i].getName()));
            } catch (IOException e) {
                System.out.println("Error reading project: " + files[i].getName());
            }
        }
    }

    /**
     * Displays the full content of a project file (containing name, weight and yarns used).
     * @param index The project index
     */
    public static void viewProject(int index) {
        File dir = new File(PROJECTS_DIR);
        File[] files = dir.listFiles();

        if (files == null || index < 0 || index > files.length) {
            System.out.println("Invalid project index.");
            return;
        }

        File projectFile = files[index];

        // Attempts to read and prints the contents of the file
        System.out.println("\n--- " + projectFile.getName() + " ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(projectFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading project file.");
        }
    }

    /**
     * Deletes a project file. Gets the index of the file from a user and deletes the file on that index.
     * @param index The project index
     */
    public static void deleteProject(int index) {
        File dir = new File(PROJECTS_DIR);
        File[] files = dir.listFiles();

        if (files == null || index < 0|| index > files.length) {
            System.out.println("Invalid project index.");
            return;
        }

        File projectFile = files[index];

        if (projectFile.delete()) {
            System.out.println("Deleted project: " + projectFile.getName());
        } else {
            System.out.println("Failed to delete project.");
        }
    }
}
