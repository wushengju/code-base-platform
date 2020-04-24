package com.wushengju.study.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sunny
 * @version 1.0
 * @className StreamTest
 * @date 2019-11-27 11:26
 */
public class StreamTest {

    public static void main(String[] args) {
        List<String> streamList = new ArrayList<>();
        streamList.add("a");
        streamList.add("b");
        streamList.add("v");
        streamList.add("d");

        System.out.println(streamList.stream().filter(x-> !"v".equals(x)).collect(Collectors.toList()));
        System.out.println(streamList.toString());
    }
}
