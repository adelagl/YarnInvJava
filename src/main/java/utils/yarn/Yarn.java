package utils.yarn;

/**
 * Class representing Yarn. Contains the brand, color, length and weight.
 */
public class Yarn {
    private String brand;
    private String color;
    private int lengthMeters;
    private YarnWeight weight;

    /**
     * Constructor for yarn.
     * @param brand: brand name of yarn (e.g. Alaska)
     * @param color: color of yarn
     * @param lengthMeters: length of yarn in meters
     * @param weight: yarn weight
     */
    public Yarn(String brand, String color, int lengthMeters, YarnWeight weight) {
        this.brand = brand;
        this.color = color;
        this.lengthMeters = lengthMeters;
        this.weight = weight;
    }

    /**
     * Returns the brand of the yarn
     * @return the yarn brand
     */
    public String getBrand() { return brand; }

    /**
     * Gets the colour of the yarn
     * @return yarn colour
     */
    public String getColor() { return color; }

    /**
     * Gets the length of the yarn in meters
     * @return the length of yarn in meters
     */
    public int getLengthMeters() { return lengthMeters; }

    /**
     * Returns the weight of the yarn
     * @return yarn weight
     */
    public YarnWeight getWeight() { return weight; }

    /**
     * Decreases the yarn weight by user specified amount
     * @param amount: amount of yarn to be decreased
     */
    public void changeLength(int amount){
        this.lengthMeters -= amount;
    }

    /**
     * Overrides toString for yarn with useful information about the yarn
     * @return the overriden string
     */
    @Override
    public String toString() {
        return brand + " (" + color + ", " + lengthMeters + "m) - " + weight;
    }

    /**
     * Compares the brand, colour and weight of the other yarn. Does not take length into account.
     * @param otherYarn: The yarn we want to compare to
     * @return true/false
     */
    public boolean compare(Yarn otherYarn){
        return this.brand.equals(otherYarn.getBrand()) && this.color.equals(otherYarn.getColor())
                && this.weight.getName().equals(otherYarn.getWeight().getName());
    }
}

