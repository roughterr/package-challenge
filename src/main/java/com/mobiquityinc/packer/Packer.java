package com.mobiquityinc.packer;

import com.mobiquityinc.packer.com.mobiquityinc.exception.APIException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Packer {
    /**
     * Max weight that a package can take.
     */
    public static final int MAX_WEIGHT_PACKAGE_CAN_TAKE = 100;
    /**
     * Max weight and cost of an item.
     */
    public static final int MAX_WEIGHT_AND_COST_OF_ITEM = 100;
    /**
     * Maximum number of items you need to choose from.
     */
    public static final int ITEM_QUANTITY_LIMIT = 15;

    /**
     * Determines which	things to put into the package so that the total weight is less than or equal to the package
     * limit and the total cost is as large as possible.
     *
     * @param filePath a path to a filename. The input file contains several lines. Each line is one test case.
     *                 Each line contains the weight that the package can take (before the colon) and the list of things you need to choose.
     *                 Each	thing is enclosed in parentheses where the 1st number is a thing's index number, the 2nd is its weight and the 3rd is its cost. E.g.
     *                 <p>81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)</p>
     *                 <p>8 : (1,15.3,€34)</p>
     *                 <p>75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)</p>
     *                 <p>56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)</p>
     * @return the solution if the format of a string (items’ index numbers are separated by comma). E.g.
     * <p>4</p>
     * <p>-</p>
     * <p>2,7</p>
     * <p>8,9</p>
     * @throws APIException incorrect parameters are being passed
     */
    public static String pack(String filePath) throws APIException {
        try {
            StringBuilder result = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // process the line.
                    result.append(packOnePackage(line) + "\n");
                }
            }
            return result.toString();
        } catch (IOException e) {
            throw new APIException(e);
        }
    }

    /**
     * Determines which	things to put into one package.
     *
     * @param inputLine a set of things, in the format of one line: {limit} : ({id},{weight},€{price}),({id},{weight},€{price})
     * @return the result in a format of the list of IDs or - symbol if the result is an empty list
     */
    public static String packOnePackage(String inputLine) throws APIException {
        System.out.println("Processing line: " + inputLine);
        Package packageInputData = parseOnePackage(inputLine);
        return calculate(packageInputData).getThingsIDs();
    }

    /**
     * Parses a line from the input file.
     *
     * @param inputLine a line that contains a weight limit and a list of things that may be put in the package
     * @return parsed data
     * @throws APIException incorrect parameters are being passed
     */
    public static Package parseOnePackage(String inputLine) throws APIException {
        String[] limitAndThings = inputLine.split(":");
        if (limitAndThings.length < 2) {
            throw new APIException("The input string contains an incorrect format - it doesn't have a : separator: " + inputLine);
        } else if (limitAndThings.length > 2) {
            throw new APIException("The input string contains an incorrect format - it has more than one : separator: " + inputLine);
        }
        //first word is the weight limit
        float limit = Float.valueOf(limitAndThings[0]);
        String[] thingStrArray = limitAndThings[1].split("[\\(||\\)]");
        List<Thing> things = new ArrayList<>();
        for (String thing : thingStrArray) {
            // skip an empty line
            if (!thing.equals(" ")) {
                things.add(parseOneThing(thing));
            }
        }
        return new Package(things, limit);
    }

    /**
     * Parses one thing.
     *
     * @param thing thing is a string format: {id},{weight},€{price}
     * @return parsed thing
     * @throws APIException incorrect parameters are being passed
     */
    public static Thing parseOneThing(String thing) throws APIException {
        String[] thingArray = thing.split(",");
        if (thingArray.length != 3) {
            throw new APIException("Incorrect format of a thing - must have 3 elements separated by commas: " + thing);
        }
        int thingID = Integer.valueOf(thingArray[0]);
        float weight = Float.valueOf(thingArray[1]);
        String priceStr = thingArray[2];
        if (priceStr.charAt(0) != '€') {
            throw new APIException("Price must be in format €{price} but it was: " + priceStr);
        }
        float price = Float.valueOf(priceStr.substring(1));
        return new Thing(thingID, weight, price);
    }

    /**
     * Calculates the most optimal combinations of things from one package (selected things should have weight
     * less than max weight that a package can take and have the most total cost.
     * @param packageInputData
     * @return
     */
    public static Package calculate(Package packageInputData) {
        //TODO
        return null;
    }
}
