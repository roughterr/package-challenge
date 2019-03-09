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
    private float weidht;
    /**
     * Cost.
     */
    private float price;

    public Thing(int id, float weidht, float price) {
        this.id = id;
        this.weidht = weidht;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public float getWeidht() {
        return weidht;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "(" + id + "," + weidht + ",€" + price + ")";
    }
}
