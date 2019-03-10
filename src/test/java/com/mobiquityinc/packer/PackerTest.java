package com.mobiquityinc.packer;

import com.mobiquityinc.packer.com.mobiquityinc.exception.APIException;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class PackerTest {
    @Test
    public void testInitialSample() throws APIException, IOException {
        testWithInputAndExpectedFile("src\\test\\resources\\initial-sample\\input.txt",
                "src\\test\\resources\\initial-sample\\expected-packages.txt");
    }

    @Test
    public void testSamePriceDifferentWeightPackages() throws APIException, IOException {
        testWithInputAndExpectedFile("src\\test\\resources\\same-price-different-weight\\input.txt",
                "src\\test\\resources\\same-price-different-weight\\expected-packages.txt");
    }

    @Test
    public void test17Things() throws APIException, IOException {
        testWithInputAndExpectedFile("src\\test\\resources\\more-than-15-things\\input.txt",
                "src\\test\\resources\\more-than-15-things\\expected-packages.txt");
    }

    /**
     * Tests the packer.
     *
     * @param inputFilePath          the absolute path to a test file with input data
     * @param expectedResultFilePath the absolute path to a test file with expected result
     * @throws APIException problems with the packer
     * @throws IOException  problems reading file
     */
    public void testWithInputAndExpectedFile(String inputFilePath, String expectedResultFilePath) throws APIException, IOException {
        String actualResult = Packer.pack(inputFilePath);
        String expectedResult = new String(Files.readAllBytes(Paths.get(expectedResultFilePath)));
        assertEquals(expectedResult, actualResult);
    }
}
