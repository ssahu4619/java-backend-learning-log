package ArraysQuestion;

import java.util.Arrays;

public class RemoveDuplicateArray {
//    Input: nums = [0, 0, 3, 3, 5, 6]
//
//    Output: 4
//
//    Explanation:
//
//    Resulting array = [0, 3, 5, 6, _, _]
//    
//    [0,0,1,1,1,2,2,3,3,4] [1,2,3,3,4,5]
//    Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;

        // 'i' is the slow pointer (tracks the position of the last unique element)
        int i = 0;

        // 'j' is the fast pointer (scans the array)
        for (int j = 1; j < nums.length; j++) {
            // If we find a value different from our last unique value
            if (nums[j] != nums[i]) {
                i++;           // Move to the next slot
                nums[i] = nums[j]; // Update the slot with the new unique value
            }
        }

        // Return the count of unique elements (index + 1)
        return i + 1;

    }
    void main() {
        int[] arr = {0,5,5,6,7,8};
        System.out.println(removeDuplicates(arr));
        System.out.println(Arrays.toString(arr));
        Arrays.stream(arr).forEach(System.out::print);
    }
}
