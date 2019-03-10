package com.mobiquityinc.packer;

/**
 * A thing that can go to a package.
 */
public class Thing {
    /**
     * Index number.
     */
    private int id;
    /**
     * Weight.
     */
    private float weight;
    /**
     * Cost.
     */
    private float price;

    public Thing(int id, float weight, float price) {
        this.id = id;
        this.weight = weight;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public float getWeight() {
        return weight;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "(" + id + "," + weight + ",€" + price + ")";
    }
}
