package ArraysQuestion;

import java.util.HashMap;
import java.util.Map;

public class FrequencyOfString {
    void main(){
        extracted();

    }

    private static void extracted() {
        String s = "deloitte";
        char[] arr = s.toCharArray();
        HashMap<Character,Integer> map = new HashMap<>();
        for(char c: arr){
           if(!map.containsKey(c)){
               map.put(c,1);
           }else {
              int val= map.get(c);
              map.put(c, val+1);
           }
        }
        System.out.println(map.toString());

//        ------------------------------------------------------------------------
//        optimised
        for(char ch : s.toCharArray()) {

            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }

        for(Map.Entry<Character, Integer> entry : map.entrySet()) {

            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
