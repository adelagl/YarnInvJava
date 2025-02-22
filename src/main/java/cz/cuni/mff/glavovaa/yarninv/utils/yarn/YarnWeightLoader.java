package cz.cuni.mff.glavovaa.yarninv.utils.yarn;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Contains a static method for loading yarn weight from a JSON file (yarn_weights.json)
 */
public class YarnWeightLoader {
    /**
     * Default constructor.
     */
    public YarnWeightLoader(){

    }
    /**
     * Method for loading yarn weights from a JSON file.
     * @param filePath: path to the json file
     * @return map of name and yarnweights
     */
    public static Map<String, YarnWeight> loadYarnWeights(Path filePath) {
        Gson gson = new Gson();

        // Check if the file exists before trying to read it
        if (!Files.exists(filePath)) {
            System.out.println("Error: Yarn weights file not found at " + filePath.toAbsolutePath());
            return Map.of();
        }

        try (Reader reader = Files.newBufferedReader(filePath)) {
            Type type = new TypeToken<Map<String, YarnWeight>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.out.println("Failed to load yarn weights from " + filePath.toAbsolutePath() + ": " + e.getMessage());
            return Map.of();
        }
    }
}

