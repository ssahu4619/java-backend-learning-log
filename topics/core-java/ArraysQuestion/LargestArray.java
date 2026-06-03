package ArraysQuestion;

class LargestArray {
//    Given an array of integers nums, return the value of the largest element in the array
//    Input: nums = [3, 3, 6, 9]
//    Output: 6
    public int largestElement(int[] nums) {
        int maxNumber = Integer.MIN_VALUE;
        for(int a =0;a<nums.length; a++){
            if(nums[a]> maxNumber) maxNumber=nums[a];
        }
        return maxNumber;
    }
    void main(){
        int[] nums = {3,3,6,9};
        LargestArray l = new LargestArray();
       int result = l.largestElement(nums);
       System.out.println(result);
    }
}