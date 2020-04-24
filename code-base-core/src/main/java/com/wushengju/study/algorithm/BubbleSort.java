package com.wushengju.study.algorithm;

/**
 * 冒泡排序算法
 * 比较相邻的元素，如果前面的比后面的大 则进行交换
 * 最好的时间复杂度为O(n)，即原先就是正序的，只需要进行n-1次比较 0次移动
 * 平均时间复杂度为O(n^2)，比较次数 Count = n-1 + n-2 + ...+ 1 = n(n-1)/2
 * 移动次数 Move = 3(n-1 + n-2 + ... + 1) = 3n(n-1)/2
 *
 * @author Sunny
 * @version 1.0
 * @className BubbleSort
 * @date 2019-11-28 09:12
 */
public class BubbleSort {

    public static void bubbleSort(int[] array){
        int length = array.length;
        for (int i = 0; i < length ; i++) {
            for (int j = 1; j < length - i; j++) {
                if (array[j-1] > array[j]){
                    int tmp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] array = {5,2,4,6,7,1,9};
        bubbleSort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

    }
}
