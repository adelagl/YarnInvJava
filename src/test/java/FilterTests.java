
import cz.cuni.mff.glavovaa.yarninv.utils.inventory.YarnInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import cz.cuni.mff.glavovaa.yarninv.utils.yarn.Yarn;
import cz.cuni.mff.glavovaa.yarninv.utils.yarn.YarnWeight;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterTests {
    private YarnInventory inventory;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        Path tempInventoryFile = tempDir.resolve("dummy.json");

        try (Writer writer = Files.newBufferedWriter(tempInventoryFile)) {
            writer.write("[]"); // represents an empty inventory
        }

        inventory = new YarnInventory(tempInventoryFile);

        inventory.addItem(new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted"))));
        inventory.addItem(new Yarn("Brand B", "Blue", 150, new YarnWeight("Fine", 3.5, 4.5, 4.5, 5.5, List.of("Sport"))));
        inventory.addItem(new Yarn("Brand C", "Red", 200, new YarnWeight("Bulky", 5.5, 8.0, 6.5, 9.0, List.of("Chunky"))));
        inventory.addItem(new Yarn("Brand A", "Green", 120, new YarnWeight("Fine", 3.5, 4.5, 4.5, 5.5, List.of("Sport"))));
    }

    @Test
    void testFilterByColor() {
        // Act
        List<Yarn> redYarns = inventory.filter(yarn -> yarn.getColor().equalsIgnoreCase("Red"));

        // Assert
        assertEquals(2, redYarns.size(), "Should return 2 red yarns.");
        assertTrue(redYarns.stream().allMatch(y -> y.getColor().equalsIgnoreCase("Red")), "All results should be red.");
    }

    @Test
    void testFilterByYarnWeight() {
        // Act
        List<Yarn> fineYarns = inventory.filter(yarn -> yarn.getWeight().getName().equalsIgnoreCase("Fine"));

        // Assert
        assertEquals(2, fineYarns.size(), "Should return 2 yarns of weight 'Fine'.");
        assertTrue(fineYarns.stream().allMatch(y -> y.getWeight().getName().equalsIgnoreCase("Fine")), "All results should have weight 'Fine'.");
    }

    @Test
    void testFilterByBrand() {
        // Act
        List<Yarn> brandAYarns = inventory.filter(yarn -> yarn.getBrand().equalsIgnoreCase("Brand A"));

        // Assert
        assertEquals(2, brandAYarns.size());
        assertTrue(brandAYarns.stream().allMatch(y -> y.getBrand().equalsIgnoreCase("Brand A")), "All results should be from 'Brand A'.");
    }

    @Test
    void testFilterWithNoMatches() {
        // Act
        List<Yarn> nonExistentColor = inventory.filter(yarn -> yarn.getColor().equalsIgnoreCase("Purple"));

        // Assert
        assertTrue(nonExistentColor.isEmpty(), "Should return an empty list for non-existent color.");
    }
}
