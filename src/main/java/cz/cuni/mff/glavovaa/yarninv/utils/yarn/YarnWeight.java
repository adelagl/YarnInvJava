package cz.cuni.mff.glavovaa.yarninv.utils.yarn;

import java.util.List;

/**
 * Class representing yarn weight. Contains name, alternative names (categories), and min and max sizes
 */
public class YarnWeight {
    private String name;
    private double minNeedleSizeMetric;
    private double maxNeedleSizeMetric;
    private double minHookSizeMetric;
    private double maxHookSizeMetric;
    private List<String> categories;

    /**
     * Constructor for yarn weight class
     * @param name name of the yarn (e.g. Lace)
     * @param minNeedleSizeMetric: minimum knitting needle size needed for this yarn
     * @param maxNeedleSizeMetric: maximum knitting needle size needed for this yarn
     * @param minHookSizeMetric: minimum crochet hook size needed for this yarn
     * @param maxHookSizeMetric: maximum crochet hook size needed for this yarn
     * @param categories: alternative names for the yarn weight
     */
    public YarnWeight(String name, double minNeedleSizeMetric, double maxNeedleSizeMetric,
                      double minHookSizeMetric, double maxHookSizeMetric, List<String> categories) {
        this.name = name;
        this.minNeedleSizeMetric = minNeedleSizeMetric;
        this.maxNeedleSizeMetric = maxNeedleSizeMetric;
        this.minHookSizeMetric = minHookSizeMetric;
        this.maxHookSizeMetric = maxHookSizeMetric;
        this.categories = categories;
    }

    /**
     * Retrieves the yarn weight name.
     * @return the yarn weight name
     */
    public String getName() { return name; }

    /**
     * Overrides the toString method with all information about yarn weight
     * @return the overriden string
     */
    @Override
    public String toString() {
        return name + " (" + minNeedleSizeMetric + "-" + maxNeedleSizeMetric + " mm needles, " +
                minHookSizeMetric + "-" + maxHookSizeMetric + " mm hooks)";
    }
}

