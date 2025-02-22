package cz.cuni.mff.glavovaa.yarninv.utils;

import cz.cuni.mff.glavovaa.yarninv.utils.yarn.Yarn;
import cz.cuni.mff.glavovaa.yarninv.utils.yarn.YarnWeight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static cz.cuni.mff.glavovaa.yarninv.utils.yarn.YarnWeightLoader.loadYarnWeights;

/**
 * Validates user input from standard input. Once the input is validated, it returns it back to main as a value.
 */
public class UserInputValidator {
    static Scanner scanner = new Scanner(System.in);
    static final Path CONFIG_DIR = Paths.get(System.getProperty("user.dir"), "config");
    static final Path FILE_PATH = CONFIG_DIR.resolve("yarn_weights.json");
    static Map<String, YarnWeight> yarnWeights;

    static {
        try {
            Files.createDirectories(CONFIG_DIR); // ensures that the directory exists
            yarnWeights = loadYarnWeights(FILE_PATH);
            if (yarnWeights.isEmpty()) {
                System.out.println("Warning: Yarn weights file is empty or not found.");
            } else {
                //System.out.println("Yarn weights successfully loaded.");
            }
        } catch (IOException e) {
            System.out.println("Failed to create directories: " + e.getMessage());
            yarnWeights = Map.of();
        }
    }

    /**
     * Combines all functions of UIV to validate all parameters of Yarn
     * Used when adding a new yarn to inventory.
     * @return Yarn with validated parameters
     */
    public static Yarn validateYarn(){
        System.out.print("Enter brand name: ");
        String brandName = validateNonEmptyStr(scanner.nextLine());
        System.out.print("Enter colour: ");
        String color = validateNonEmptyStr(scanner.nextLine());
        System.out.print("Enter the length (in metres): ");
        int length = validatePositiveInteger(scanner.nextLine());
        System.out.print("Enter the yarn weight (name or alias): ");
        YarnWeight weight = validateYarnWeight();

        return new Yarn(brandName, color, length, weight);
    }

    /**
     * Repeatedly validates the index until the value is correct.
     * Used when listing items in main when user needs to choose an option.
     * @param input: user input
     * @param max: maximum index
     * @return the correct index
     */
    public static int validateIndex(String input, int max){
        int num = validatePositiveInteger(input);

        while (num > max){
            System.out.println("Index is out of range. Try again");
            num = validatePositiveInteger(scanner.nextLine());
        }

        return num;
    }

    /**
     * Repeatedly checks if the item is in list.
     * @param choice the user's choice
     * @param options options the user can choose from
     * @return the validated choice
     */
    public static String validateIsInList(String choice, String... options){
        while (true){
            for (String val : options){
                if (choice.equals(val)) return val;
            }

            System.out.print("Incorrect answer. Try again: ");
            choice = scanner.nextLine();
        }
    }

    /**
     * Repeatedly checks if input is not empty
     * @param str: user input
     * @return validated string
     */
    public static String validateNonEmptyStr(String str){
        while (str.isEmpty()){
            System.out.print("Input cannot be empty. Enter here: ");
            str = scanner.nextLine();
        }

        return str.toLowerCase();
    }

    /**
     * Checks if user input is a positive integer
     * @param input: user input
     * @return validated number
     */
    public static int validatePositiveInteger(String input){
        while (true){
            try{
                int num = Integer.parseInt(input);

                if (num <= 0){
                    throw new Exception("Number cannot be negative.");
                }
                else{
                    return num;
                }
            }
            catch (Exception ex){
                System.out.println("Incorrect number.");
            }

            System.out.print("Enter number here: ");
            input = scanner.nextLine();

        }
    }

    /**
     * Checks if the yarn weight name submitted by user is in the list of valid yarn weights
     * @return correct yarn weight
     */
    public static YarnWeight validateYarnWeight(){
        printYarnWeightOptions();
        String input = scanner.nextLine();

        YarnWeight yw = yarnWeights.get(input);
        while (yw == null){
            System.out.print("Incorrect yarn weight. Try again: ");
            yw = yarnWeights.get(scanner.nextLine());
        }

        return yw;
    }

    /**
     * Checks if input contains a . (dot)
     * @param input: user input
     * @return validated file format
     */
    public static String validateFileFormat(String input){
        while(true){
            if (input.contains(".")){
                return input;
            }
            System.out.print("Incorrect file. Please try again: ");
        }
    }

    private static void printYarnWeightOptions(){
        System.out.print("Options: ");
        boolean first = true;
        for (String yw : yarnWeights.keySet()){
            if (first){
                System.out.print(yw);
                first = false;
            }
            else{
                System.out.print(", " + yw);
            }
        }
        System.out.print("\n");
    }
}
