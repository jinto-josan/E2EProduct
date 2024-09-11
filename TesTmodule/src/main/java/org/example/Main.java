package org.example;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
//
//
//        TestClass.getArrayList().parallelStream().collect(
//                ArrayList::new,
//                ArrayList::add,
//                ArrayList::addAll
//        );

        CompletableFuture.supplyAsync(Main::threadTestMethod).whenComplete((result, exception) -> {
            if (exception != null) {
                System.out.println("Exception occurred: " + exception.getMessage());
            } else {
                System.out.println("Result: " + result);
            }
        });


    }

    public static int threadTestMethod(){
        System.out.println("Hello world!");
        throw new RuntimeException("Exception occurred");
    }

    public static class TestClass{

        public static List<String> getArrayList(){
            var testList=  new ArrayList<String>();
            testList.add("Hello");
            testList.add("World");
            testList.add("!");
            return testList;
        }

    }
}