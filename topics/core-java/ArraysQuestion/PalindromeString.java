package ArraysQuestion;

public class PalindromeString {
    void main(){
        String s = "madam";
         s = s.toLowerCase();
        System.out.println(s);
        int starting =0;
        int endIndex = s.length()-1;

        while (starting<endIndex){
            if(s.charAt(starting) != s.charAt(endIndex)){
                System.out.println("Not Palindrome");
                break;
            }
            System.out.println("Palindrome");
            starting++;
            endIndex--;
        }
//------------------------------------------------------------------------------------------------------------
        String str = "madam";

        String rev = new StringBuilder(str).reverse().toString();

        if(str.equals(rev)) {
            System.out.println("Palindrome");
        }
        else {
            System.out.println("Not Palindrome");
        }

    }
}
