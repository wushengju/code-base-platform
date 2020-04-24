package com.wushengju.study.algorithm;

/**
 * 插入排序算法
 *  对于有序的序列中插入元素，从后往前遍历若比当前的元素小 则进行移动，直到到第一个元素
 *  比较次数 1 + 2 + 3 + ...+ n-1 = n(n-1)/2  时间复杂度为 O(n^2)
 * @author Sunny
 * @version 1.0
 * @className InsertSort
 * @date 2019-12-01 15:53
 */
public class InsertSort {

    public static void insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int insertVal = array[i];
            int index = i - 1;
            while (index >= 0 && insertVal < array[index]) {
                array[index + 1] = array[index];
                index--;
            }
            array[index + 1] = insertVal;
        }
    }

    public static void main(String[] args) {
        int[] array = {5, 2, 4, 6, 7, 1, 9};
        insertSort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
