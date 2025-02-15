package utils.yarn;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
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
    public static Map<String, YarnWeight> loadYarnWeights(String filePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<Map<String, YarnWeight>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            System.out.println("Failed to load yarn weights from a JSON file" + e.getMessage());
            return null;
        }
    }
}

