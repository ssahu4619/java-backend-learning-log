package ArraysQuestion;

import java.util.LinkedHashSet;
import java.util.Set;

public class RemoveDublicateString {



        public static void main(String[] args) {

            String str = "programming";

            Set<Character> set = new LinkedHashSet<>();

            for(char ch : str.toCharArray()) {
                set.add(ch);
            }

            for(char ch : set) {
                System.out.print(ch);
            }
        }

}
