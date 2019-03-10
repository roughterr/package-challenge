package com.mobiquityinc.packer;

import com.mobiquityinc.packer.com.mobiquityinc.exception.APIException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                    result.append(processOneLineAddLb(line));
                }
            }
            return result.toString();
        } catch (IOException e) {
            throw new APIException(e);
        }
    }

    /**
     * Processes a line of input. Accepts an empty line as the argument. Adds a line break in the end of the String.
     *
     * @param inputLine a line of package input
     * @return returns an empty line if the input was an empty line
     * @throws APIException incorrect parameters are being passed
     */
    private static String processOneLineAddLb(String inputLine) throws APIException {
        if (inputLine == null || inputLine.equals("")) {
            return "";
        }
        return processOneLine(inputLine) + "\n";
    }

    /**
     * Processes a line of input. Does not expect an empty line to be passed as the argument.
     *
     * @param inputLine a set of things, in the format of one line: {limit} : ({id},{weight},€{price}),({id},{weight},€{price})
     * @return the result in a format of the list of IDs or - symbol if the result is an empty list
     */
    public static String processOneLine(String inputLine) throws APIException {
        Package packageInputData = parseOnePackage(inputLine);
        List<Thing> bestCombination = calculate(packageInputData).stream().sorted(Comparator.comparing(Thing::getId)).collect(Collectors.toList());
        return getThingsIDs(bestCombination);
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
     * 1) Removes things that exceed the weight and cost limits.
     * 2) Sorts things by cost in reverse order.
     * 3) Calculates the maximum weight of the package.
     *
     * @param inputPackage input package
     * @return filtered and sorted things
     */
    private static Package sortAndFilterThings(Package inputPackage) {
        float packageWeightLimit = inputPackage.getWeightLimit() > MAX_WEIGHT_PACKAGE_CAN_TAKE ?
                MAX_WEIGHT_PACKAGE_CAN_TAKE : inputPackage.getWeightLimit();
        List<Thing> things = inputPackage.getThings().stream()
                .filter(thing -> thing.getPrice() <= MAX_WEIGHT_AND_COST_OF_ITEM && thing.getWeight() <= MAX_WEIGHT_AND_COST_OF_ITEM)
                .sorted(Comparator.comparing(Thing::getPrice).reversed())
                .collect(Collectors.toList());
        return new Package(things, packageWeightLimit);
    }

    /**
     * Calculates the most optimal combinations of things from one package (selected things should have weight
     * less than max weight that a package can take and have the biggest total cost.
     *
     * @param inputPackage
     * @return
     */
    public static List<Thing> calculate(Package inputPackage) {
        Package cleanPackage = sortAndFilterThings(inputPackage);
        return findBestCombination(cleanPackage.getThings(), cleanPackage.getWeightLimit(), 0f, 0);
    }

    /**
     * Selects a better thing list. The first criterion is a bigger price, and the second criterion is a smaller weight.
     *
     * @param thingList1 list of things
     * @param thingList2 list of things
     * @return thingList1 or thingList2
     */
    private static List<Thing> selectBetterCombination(List<Thing> thingList1, List<Thing> thingList2) {
        float thingList1WeightSum = sumThingsWeight(thingList1);
        float thingList1CostSum = sumThingsCost(thingList1);
        float thingList2WeightSum = sumThingsWeight(thingList2);
        float thingList2CostSum = sumThingsCost(thingList2);
        if (thingList1CostSum > thingList2CostSum) {
            return thingList1;
        } else if (thingList1CostSum < thingList2CostSum) {
            return thingList2;
        } else {
            if (thingList1WeightSum > thingList2WeightSum) {
                return thingList2;
            } else {
                return thingList1;
            }
        }
    }

    /**
     * Finds the most optimal combination of things.
     *
     * @param thingsToChooseFrom things to choose from
     * @param weightLimit the total weight limit
     * @param accumulatedWeight weight to add to
     * @param accumulatedItemQuantity item quantity to add to
     * @return the most optimal combination of things
     */
    private static List<Thing> findBestCombination(List<Thing> thingsToChooseFrom, float weightLimit, float accumulatedWeight, int accumulatedItemQuantity) {

        // if we have an empty list or we have reached the item quantity limit, then we should stop the search
        if (thingsToChooseFrom.isEmpty() || accumulatedItemQuantity == ITEM_QUANTITY_LIMIT) {
            return thingsToChooseFrom;
        }
        Thing firstThing = thingsToChooseFrom.get(0);
        List<Thing> withoutFirstList = cdr(thingsToChooseFrom);
        List<Thing> withoutFirstBestCombi = findBestCombination(withoutFirstList, weightLimit, accumulatedWeight, accumulatedItemQuantity);
        // if adding the first element will violate the weight constraint, then we should only consider the rest of items (reject the first item)
        float weightIfWeAddFirst = accumulatedWeight + firstThing.getWeight();
        if (weightIfWeAddFirst > weightLimit) {
            return withoutFirstBestCombi;
        }
        List<Thing> withFirstBestCombi = findBestCombination(withoutFirstList, weightLimit, weightIfWeAddFirst, accumulatedItemQuantity + 1);
        withFirstBestCombi.add(firstThing);
        return selectBetterCombination(withFirstBestCombi, withoutFirstBestCombi);
    }

    /**
     * Returns a string that contains IDs of things separated by comma.
     *
     * @return - symbol if the list of things is empty; numbers separated by commas if the list of things is not empty
     */
    public static String getThingsIDs(List<Thing> things) {
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

    public static <T> List<T> cdr(List<T> list) {
        if (list.isEmpty()) {
            return list;
        }
        List<T> newList = new ArrayList<>(list);
        newList.remove(0);
        return newList;
    }

    /**
     * Sum cost of things in the list.
     *
     * @param things list of things
     * @return cost of all the things that were in the list
     */
    public static float sumThingsCost(List<Thing> things) {
        float sum = 0;
        for (Thing thing : things) {
            sum += thing.getPrice();
        }
        return sum;
    }

    /**
     * Sum weight of things in the list.
     *
     * @param things list of things
     * @return weight of all the things that were in the list
     */
    public static float sumThingsWeight(List<Thing> things) {
        float sum = 0;
        for (Thing thing : things) {
            sum += thing.getWeight();
        }
        return sum;
    }
}
