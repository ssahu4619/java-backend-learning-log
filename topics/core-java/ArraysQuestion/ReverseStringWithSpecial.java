package ArraysQuestion;

import java.util.Arrays;

public class ReverseStringWithSpecial {
    public String ReverseStringWithSpecial(String s) {
        int j = s.length()-1;
        int i = 0;
        char[] ch = s.toCharArray();
        while(j>i){
            if(!Character.isLetter(ch[j]) && !Character.isLetter(ch[i])){
                j--;
                i++;
            } else if(Character.isLetter(ch[j]) && Character.isLetter(ch[i])){
                swaping(ch,j,i);
                i++;
                j--;
            }
        }
        return Arrays.toString(ch);
    }

    private void swaping(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    void main(){
//        c,b$a
        String s = "a,b$c";
        ReverseStringWithSpecial l = new ReverseStringWithSpecial();
        String result = l.ReverseStringWithSpecial(s);
        System.out.println("answer --- "+ result);
        System.out.println(result);
    }
}
