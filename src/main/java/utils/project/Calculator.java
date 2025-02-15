package utils.project;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Returns minimum and maximum needed yarn length for a project based on yarn_requirements.json file.
 */
public class Calculator {
    private static final String FILE_PATH = "./src/main/resources/yarn_requirements.json";
    private static final Gson gson = new Gson(); // for parsing json files
    private Map<String, Map<String, Map<String, Integer>>> yarnRequirements;

    /**
     * Constructor that loads yarn requirements from a JSON file.
     */
    public Calculator() {
        loadYarnRequirements();
    }

    /**
     * Returns min and max length needed for a project
     * @param project: project type, e.g. a hat
     * @param ywName: name of yarn weight
     * @return yarn weight and min and max length
     */
    public Map<String, Integer> getYarnRequirement(String project, String ywName){
        if (yarnRequirements.containsKey(project) && yarnRequirements.get(project).containsKey(ywName)) {

            return yarnRequirements.get(project).get(ywName);
        }
        return null;
    }

    /**
     * Returns project name based on its position in a JSON file
     * @param index: the position of a project
     * @return the project name
     */
    public String getProjectByIndex(int index) {
        if (index < 0 || index >= yarnRequirements.size()) {
            return null;
        }
        return new ArrayList<>(yarnRequirements.keySet()).get(index);
    }

    /**
     * Loads JSON file containing length requirements for projects
     */
    private void loadYarnRequirements() {
        // Attempts to read and parse JSON
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type type = new TypeToken<Map<String, Map<String, Map<String, Integer>>>>() {
            }.getType();
            yarnRequirements = gson.fromJson(reader, type);
            System.out.println("Yarn requirements loaded.");
        } catch (IOException e) {
            System.out.println("Failed to parse JSON file with yarn requirements" + e.getMessage());
            yarnRequirements = null;
        }
    }

    /**
     * Finds and returns a set of available projects
     * @return a set of all projects
     */
    public Set<String> getAvailableProjects() {
        if (yarnRequirements == null) {
            return Set.of();
        }
        return yarnRequirements.keySet();
    }

    /**
     * Returns the number of available projects (used for indexing)
     * @return the total number of all projects
     */
    public int getProjectCount() {
        if (yarnRequirements == null) {
            return 0;
        }
        return yarnRequirements.size();
    }
}
