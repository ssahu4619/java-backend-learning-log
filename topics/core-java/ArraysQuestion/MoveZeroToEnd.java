package ArraysQuestion;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public class MoveZeroToEnd {
//    Input: nums = [0,1,0,3,12]
//    Output: [1,3,12,0,0]
    public static void moveZeroes(int[] nums) {
        if(nums.length==1){
            return;
        }
        int j=0;
        for(int i =0; i<nums.length;i++){
            if(nums[i]!=0){
                swap(nums,j,i);
                j++;

            }
            else if (nums[i]==0) {
                continue;
            }

        }
    }

    private static void swap(int[] nums, int j, int i) {
        int temp = nums[i];
         nums[i] = nums[j];
         nums[j] = temp;
    }

    //    Input: nums = [2, 3, 4, 5, 3], target = 3
    //    Output: 1

    public static int linearSearch(int nums[], int target) {
       int output = -1;
        for(int i =0 ; i<nums.length;i++){
           if(target == nums[i]){
              output=i;
               break;
           }
        }
        return output;
    }

    void main(){
        int[] inp = {0,1,0,3,12,0,6,0};
        MoveZeroToEnd.moveZeroes(inp);
        System.out.println("ouyput - "+ Arrays.toString(inp));

        int[] linearInp = {2,3,4,5,3};
        int i = MoveZeroToEnd.linearSearch(linearInp, 89);
        System.out.println("Linear output- "+ i);

    }
}
