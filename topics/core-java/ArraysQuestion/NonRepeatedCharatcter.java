package ArraysQuestion;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NonRepeatedCharatcter {
    void main(){
        String sw = "swiss";
        nonRepeated(sw);

        Character firstNonRepeated = sw.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        System.out.println("First non-repeated character (stream): " + firstNonRepeated);
    }

    private static void nonRepeated(String sw) {
        char[] arr = sw.toCharArray();
        HashMap<Character,Integer> linMap = new LinkedHashMap();
        for(int i =0; i < arr.length; i++){
            linMap.put(arr[i], linMap.getOrDefault(arr[i],0)+1);

        }
        for(Map.Entry<Character,Integer> entry : linMap.entrySet()){
            if(entry.getValue() == 1){
                System.out.println("----"+ entry.getKey());
                break;
            }
        }
    }
}
