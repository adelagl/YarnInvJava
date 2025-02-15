package utils.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.inventory.YarnInventory;
import utils.yarn.Yarn;
import utils.yarn.YarnWeight;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTests {
    private YarnInventory inventory;
    private Calculator calculator;

    @BeforeEach
    void setUp() throws IOException {
        // Reset inventory before each test
        try (FileWriter writer = new FileWriter("./src/main/resources/inventory.json")) {
            writer.write("[]");
        }
        inventory = new YarnInventory("./src/main/resources/inventory.json");
        calculator = new Calculator();

        // Add sample yarns
        inventory.addItem(new Yarn("Brand A", "Red", 200, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted"))));
        inventory.addItem(new Yarn("Brand B", "Blue", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted"))));
    }

    @Test
    void testRetrieveProjectInfo_ValidProject() {
        // Arrange
        String projectName = "baby hat";
        String yarnWeightName = "Medium";

        // Act
        Map<String, Integer> yarnData = calculator.getYarnRequirement(projectName, yarnWeightName);

        // Assert
        assertNotNull(yarnData, "Project should return yarn requirements.");
        assertTrue(yarnData.containsKey("min") && yarnData.containsKey("max"), "Should contain min/max yarn requirements.");
    }

    @Test
    void testRetrieveProjectInfo_InvalidProject() {
        // Act
        Map<String, Integer> yarnData = calculator.getYarnRequirement("Unknown Project", "Medium");

        // Assert
        assertNull(yarnData, "Invalid project should return null.");
    }

    @Test
    void testGetLengthRequirement_MinSelection() {
        // Arrange
        Project project = new Project("scarf", new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")),
                calculator.getYarnRequirement("scarf", "Medium"));

        Scanner scanner = new Scanner("min\n");

        // Act
        int length = ProjectHandler.getLengthRequirement(project, scanner);

        // Assert
        assertEquals(project.getYarnData().get("min"), length, "Should return minimum required length.");
    }

    @Test
    void testGetLengthRequirement_MaxSelection() {
        // Arrange
        Project project = new Project("scarf", new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")),
                calculator.getYarnRequirement("scarf", "Medium"));

        Scanner scanner = new Scanner("max\n");

        // Act
        int length = ProjectHandler.getLengthRequirement(project, scanner);

        // Assert
        assertEquals(project.getYarnData().get("max"), length, "Should return maximum required length.");
    }

    @Test
    void testGetAvailableYarns_FilterByWeight() {
        // Arrange
        Project project = new Project("hat", new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")),
                calculator.getYarnRequirement("hat", "Medium"));

        Scanner scanner = new Scanner("y\n");

        // Act
        List<Yarn> availableYarns = ProjectHandler.getAvailableYarns(project, scanner, inventory);

        // Assert
        assertEquals(2, availableYarns.size(), "Should return only Medium weight yarns.");
    }

    @Test
    void testGetAvailableYarns_AllWeights() {
        // Arrange
        Project project = new Project("hat", new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")),
                calculator.getYarnRequirement("hat", "Medium"));

        Scanner scanner = new Scanner("n\n");

        // Act
        List<Yarn> availableYarns = ProjectHandler.getAvailableYarns(project, scanner, inventory);

        // Assert
        assertEquals(inventory.getInventory().size(), availableYarns.size(), "Should return all yarns.");
    }

    @Test
    void testSelectYarnsForProject_SufficientYarn() {
        // Arrange
        int requiredLength = 250;
        List<Yarn> availableYarns = inventory.getInventory();
        Scanner scanner = new Scanner("1\n1\n");

        // Act
        List<Yarn> selectedYarns = ProjectHandler.selectYarnsForProject(requiredLength, availableYarns, scanner);

        // Assert
        assertNotNull(selectedYarns, "Should return selected yarns.");
        assertEquals(2, selectedYarns.size(), "Should select both available yarns.");
    }

    @Test
    void testSelectYarnsForProject_InsufficientYarn() {
        // Arrange
        int requiredLength = 500; // More than available
        List<Yarn> availableYarns = inventory.getInventory();
        Scanner scanner = new Scanner("1\n1\n");

        // Act
        List<Yarn> selectedYarns = ProjectHandler.selectYarnsForProject(requiredLength, availableYarns, scanner);

        // Assert
        assertNull(selectedYarns, "Should return null when yarn is insufficient.");
    }
}
