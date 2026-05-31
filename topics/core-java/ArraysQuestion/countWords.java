package ArraysQuestion;

import java.util.*;

public class countWords {
    public static void main(String[] args) {

        String str ="Java Spring Boot Microservices";
        String str2 = "java is good java is powerful";

        String[] s = str.split(" ");
        String[] s2 = str2.split(" ");

        for( int c = s.length-1; c >= 0; c--) {
            System.out.print(s[c] + (c > 0 ? " " : ""));
        }
        System.out.println();
        Map<String,Integer> map = new HashMap<>();
        for(String sq : s2){
            map.put(sq, map.getOrDefault(sq,0)+1);
        }
        for (Map.Entry<String,Integer> entry : map.entrySet()){
            System.out.println("key= "+entry.getKey()+" value- "+entry.getValue());
        }

        // Optimized stream-based approach
        String reversed = Arrays.stream(str.split(" "))
                .reduce((word1, word2) -> word2 + " " + word1)
                .orElse("");

        System.out.println("Reversed (stream): " + reversed);
    }
}
