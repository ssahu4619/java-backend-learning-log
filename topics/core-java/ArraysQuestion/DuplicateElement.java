package ArraysQuestion;

import java.util.HashSet;
import java.util.Set;

public class DuplicateElement {
    void main(){
        int[] arr = {1,1,3,4,5,5,6,7,87,9,87};
        Set<Integer> set = new HashSet<>();

        for(int i=0; i<arr.length;i++){
            if(!set.contains(arr[i])){
                set.add(arr[i]);
            }else {
                System.out.println(arr[i]);
            }
        }
    }
}
