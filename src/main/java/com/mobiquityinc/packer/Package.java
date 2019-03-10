package com.mobiquityinc.packer;

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
}
