package com.example.demo.clr.utils;

public class TestUtils {
    private static int COUNTER = 0;

    public static void printTestInfo(String testName) {
        System.out.println(String.format("Test #%d : %s", ++COUNTER, testName));
    }
}
