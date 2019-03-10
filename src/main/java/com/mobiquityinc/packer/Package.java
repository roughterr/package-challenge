package com.mobiquityinc.packer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Package {
    /**
     * Things in the package.
     */
    private List<Thing> things;
    /**
     * Weight limit.
     */
    private float weightLimit;

    public Package(List<Thing> things, float weightLimit) {
        this.things = Collections.unmodifiableList(things);
        this.weightLimit = weightLimit;
    }

    public List<Thing> getThings() {
        return things;
    }

    public float getWeightLimit() {
        return weightLimit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(weightLimit + ":");
        things.forEach(thing -> {
            sb.append(thing.toString() + " ");
        });
        if (things.size() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Returns a string that contains IDs of things separated by comma.
     *
     * @return - symbol if the list of things is empty; numbers separated by commas if the list of things is not empty
     */
    public String getThingsIDs() {
        if (things.size() == 0) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        things.forEach(thing -> {
            sb.append(thing.getId() + ",");
        });
        //remove the last comma
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
