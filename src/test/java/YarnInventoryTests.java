import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.inventory.YarnInventory;
import utils.yarn.Yarn;
import utils.yarn.YarnWeight;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class YarnInventoryTests {
    private YarnInventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new YarnInventory("src/main/resources/dummy.json"); // Initialize an empty inventory
    }

    @Test
    void testAddSingleYarn() {
        Yarn yarn = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));

        inventory.addItem(yarn);

        List<Yarn> allYarns = inventory.getInventory();
        assertEquals(1, allYarns.size());
        assertEquals(yarn, allYarns.get(0));
    }

    @Test
    void testAddMultipleYarns() {
        Yarn yarn1 = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));
        Yarn yarn2 = new Yarn("Brand B", "Blue", 150, new YarnWeight("Fine", 3.5, 4.5, 4.5, 5.5, List.of("Sport")));

        inventory.addItem(yarn1);
        inventory.addItem(yarn2);

        List<Yarn> allYarns = inventory.getInventory();
        assertEquals(2, allYarns.size());
        assertTrue(allYarns.contains(yarn1));
        assertTrue(allYarns.contains(yarn2));
    }

    @Test
    void testAddDuplicateYarn() {
        Yarn yarn = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));

        inventory.addItem(yarn);
        inventory.addItem(yarn); // Add duplicate

        List<Yarn> allYarns = inventory.getInventory();
        assertEquals(2, allYarns.size(), "Duplicates should be allowed");
    }


    @Test
    void testRemoveAt_ValidIndex() {
        // Arrange
        Yarn yarn1 = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));
        Yarn yarn2 = new Yarn("Brand B", "Blue", 150, new YarnWeight("Fine", 3.5, 4.5, 4.5, 5.5, List.of("Sport")));
        inventory.addItem(yarn1);
        inventory.addItem(yarn2);

        // Act
        inventory.removeAt(0); // Remove first yarn

        // Assert
        List<Yarn> allYarns = inventory.getInventory();
        assertEquals(1, allYarns.size());
        assertEquals(yarn2, allYarns.get(0), "The second yarn should now be at index 0.");
    }

    @Test
    void testRemoveAt_InvalidIndex_ThrowsException() {
        // Arrange
        Yarn yarn = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));
        inventory.addItem(yarn);

        // Act
        // Assert
        assertThrows(IndexOutOfBoundsException.class, () -> inventory.removeAt(5), "Should throw an exception for an invalid index.");
        assertThrows(IndexOutOfBoundsException.class, () -> inventory.removeAt(-1), "Should throw an exception for a negative index.");
    }

    @Test
    void testRemoveAt_LastElementLeavesEmptyList() {
        // Arrange
        Yarn yarn = new Yarn("Brand C", "Green", 200, new YarnWeight("Bulky", 5.5, 8.0, 6.5, 9.0, List.of("Chunky")));
        inventory.addItem(yarn);

        // Act
        inventory.removeAt(0);

        // Assert
        List<Yarn> allYarns = inventory.getInventory();
        assertTrue(allYarns.isEmpty(), "The inventory should be empty after removing the last item.");
    }

    @Test
    void testRemoveAt_MultipleRemovals() {
        // Arrange
        Yarn yarn1 = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));
        Yarn yarn2 = new Yarn("Brand B", "Blue", 150, new YarnWeight("Fine", 3.5, 4.5, 4.5, 5.5, List.of("Sport")));
        Yarn yarn3 = new Yarn("Brand C", "Green", 200, new YarnWeight("Bulky", 5.5, 8.0, 6.5, 9.0, List.of("Chunky")));
        inventory.addItem(yarn1);
        inventory.addItem(yarn2);
        inventory.addItem(yarn3);

        // Act
        inventory.removeAt(1); // Remove the second yarn (yarn2)

        // Assert
        List<Yarn> allYarns = inventory.getInventory();
        assertEquals(2, allYarns.size());
        assertEquals(yarn1, allYarns.get(0), "Yarn 1 should remain at index 0.");
        assertEquals(yarn3, allYarns.get(1), "Yarn 3 should now be at index 1 after removing Yarn 2.");
    }

    @Test
    void testReduceLength_ValidReduction() {
        // Arrange
        Yarn yarn = new Yarn("Brand A", "Red", 100, new YarnWeight("Medium", 4.5, 5.5, 5.5, 6.5, List.of("Worsted")));
        inventory.addItem(yarn);

        // Act
        inventory.changeLength(0, 40); // Reduce by 40 meters

        // Assert
        List<Yarn> allYarns = inventory.getInventory();
        assertEquals(1, allYarns.size(), "Yarn should still be in the inventory.");
        assertEquals(60, allYarns.get(0).getLengthMeters(), "Yarn length should be reduced to 60 meters.");
    }

    @Test
    void testReduceLength_MoreThanAvailable_RemovesYarn() {
        // Arrange
        Yarn yarn = new Yarn("Brand B", "Blue", 50, new YarnWeight("Fine", 3.5, 4.5, 4.5, 5.5, List.of("Sport")));
        inventory.addItem(yarn);

        // Act
        inventory.changeLength(0, 60); // Reduce more than available (50 meters)

        // Assert
        List<Yarn> allYarns = inventory.getInventory();
        assertTrue(allYarns.isEmpty(), "Yarn should be completely removed when reduced beyond its length.");
    }
}
