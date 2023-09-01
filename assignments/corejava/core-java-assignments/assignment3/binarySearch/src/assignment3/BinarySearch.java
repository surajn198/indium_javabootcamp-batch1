package assignment3;

import java.util.Scanner;

public class BinarySearch {

    public static int binarySearch(int[] arr, int key) {
        int left = 0;
        int right = arr.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (arr[mid] == key) {
                return mid; // Found the key at index 'mid'
            } else if (arr[mid] < key) {
                left = mid + 1; // Search in the right half
            } else {
                right = mid - 1; // Search in the left half
            }
        }
        
        return -1; // Key not found
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of values in the array: ");
        int n = scanner.nextInt();
        
        int[] values = new int[n];
        
        System.out.println("Enter the values in sorted order:");
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
        }
        
        System.out.print("Enter the search key: ");
        int searchKey = scanner.nextInt();
        
        int result = binarySearch(values, searchKey);
        
        if (result != -1) {
            System.out.println("Key found at index: " + result);
        } else {
            System.out.println("Key not found in the array.");
        }
        
        scanner.close();
    }
}