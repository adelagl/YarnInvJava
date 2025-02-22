package cz.cuni.mff.glavovaa.yarninv.utils.project;

import cz.cuni.mff.glavovaa.yarninv.utils.yarn.YarnWeight;

import java.util.Map;

/**
 * Represents user's project, e.g. a hat or a sweater. Contains project name, yarn weight, minimum and maximum length needed
 */
public class Project {
    private final String projectName;
    private final YarnWeight yarnWeight;
    private final Map<String, Integer> yarnData;

    /**
     * Constructor of a project that initializes its fields.
     * @param projectName: name of the project
     * @param yarnWeight: yarn weight used in the project
     * @param yarnData: Map of yarns used in the project
     */
    public Project(String projectName, YarnWeight yarnWeight, Map<String, Integer> yarnData) {
        this.projectName = projectName;
        this.yarnWeight = yarnWeight;
        this.yarnData = yarnData;
    }

    /**
     * Finds the name of the project
     * @return the name of the project
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Finds the yarn weight used in the project
     * @return the yarn weight used in the project
     */
    public YarnWeight getYarnWeight() {
        return yarnWeight;
    }

    /**
     * Finds the yarn data used in the project
     * @return the map of yarns used in the project
     */
    public Map<String, Integer> getYarnData() {
        return yarnData;
    }
}

