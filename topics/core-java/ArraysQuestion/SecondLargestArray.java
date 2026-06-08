package ArraysQuestion;

public class SecondLargestArray {
//    Input: nums = [8, 8, 7, 6, 5]
//
//    Output: 7
    public int secondLargestElement(int[] nums) {
     int maXNumber = Integer.MIN_VALUE;
     int secondMaxNumber = Integer.MIN_VALUE;
     for(int i=0;i<nums.length;i++){
         if(nums[i] > maXNumber){
             secondMaxNumber= maXNumber;
             maXNumber= nums[i];
         }else if (nums[i] > secondMaxNumber && nums[i] < maXNumber) {
             secondMaxNumber = nums[i];
         }
     }
     return secondMaxNumber ==Integer.MIN_VALUE ?0 : secondMaxNumber;
    }
    void main(){
        int[] nums = {8, 8, 7, 6, 5};
        SecondLargestArray l = new SecondLargestArray();
        int result = l.secondLargestElement(nums);
        System.out.println(result);
    }
}
