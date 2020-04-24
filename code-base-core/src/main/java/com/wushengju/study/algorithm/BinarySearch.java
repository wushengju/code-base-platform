package com.wushengju.study.algorithm;

/**
 * 二分查找算法,要求待查找的序列有序
 * 第一次查找次数 n/2
 * 第二次查找次数 n/4
 * <p>
 * <p>
 * 第K次查找次数 n/2^K,假设 n/2^K = 1 则 2^K = n => k = log2N
 * 时间复杂度为Log(2N)
 *
 * @author Sunny
 * @version 1.0
 * @className BinarySearch
 * @date 2019-11-27 19:24
 */
public class BinarySearch {

    /**
     * 二分查找算法
     * @param array
     * @param a
     * @return
     */
    public static int binarySearch(int[] array, int a) {
        int low = 0;
        int high = array.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (array[mid] == a) {
                return mid + 1;
            } else if (array[mid] < a){
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] array = {2,3,4,5,6};
        System.out.println(binarySearch(array, 5));
    }

}
