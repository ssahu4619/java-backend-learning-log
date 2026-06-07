package ArraysQuestion;

import java.util.Arrays;

import static java.util.Collections.reverse;


public class rotateIndexByK {
//    nums = [1,2,3,4,5,6,7], k = 3
//    Output: [5,6,7,1,2,3,4]
    public static int[] rotateByOne(int[] arr){
//        nums = [1,2,3,4,5,6,7], k = 1
//        result- [2, 3, 4, 5, 6, 7, 1]
        int temp = arr[0];
        for(int i = 1; i<arr.length; i++){
            arr[i-1] = arr[i];
        }
        arr[arr.length-1]=temp;
        return arr;
    }

    public static void rotateByKLeftBetter(int[] arr, int k){
        //    nums = [1,2,3,4,5,6,7], k = 3
//                    0 1 2 3 4 5 6
        //    Output: [5,6,7,1,2,3,4]

        k = k% arr.length;
//        moving till 2nd index in temp array... that is till K
        int[] temp = new int[k];
        for (int t=0; t<k; t++) {
            temp[t] = arr[t];
        }

//        Shifting the rest number form n-k to end
//        [1,2,3,4,5,6,7]
//         0 1 2 3 4 5 6
//        [4,5,6,7,_,_,_] 3-3=0,4-3=1,5-3=2,6-3=3
        int length = arr.length;

        for(int i = k; i< length; i++){
           arr[i-k] = arr[i];
     }
//         [1,2,3,4,5,6,7]
//         0 1 2 3 4 5 6
//        [4,5,6,7,_,_,_]
        for(int i = length-k; i< length; i++){
            arr[i] = temp[i-(length-k)];
        }

    }
    public static void rotateByKLeftOptimal(int[] arr, int k) {

        //    nums = [1,2,3,4,5,6,7], k = 3
//    Output: [5,6,7,1,2,3,4]
               int n = arr.length;
                k = k % n;

                Reverse(arr, 0, k - 1);
                Reverse(arr, k, n - 1);
                Reverse(arr, 0, n - 1);


    }
    static void Reverse(int[] nums, int s, int e) {
        while (e > s) {
            int temp = nums[s];
            nums[s] = nums[e];
            nums[e] = temp;
            s++;
            e--;
        }
    }

        void main(){
        int[] inp = {1,2,3,4,5,6,7};
//        int[] result = rotateIndexByK.rotateByOne(inp);
//        System.out.println("result- "+Arrays.toString(result));
//        rotateIndexByK.rotateByKLeftBetter(inp,4);
        rotateIndexByK.rotateByKLeftOptimal(inp,3);
        System.out.println(Arrays.toString(inp));
    }

}
