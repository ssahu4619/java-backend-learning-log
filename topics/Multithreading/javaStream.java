import java.util.Arrays;
import java.util.List;

public class javaStream {

    void main() {
//        Find all even numbers from a list.
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> l1 = numbers.stream().filter(n->n%2==0).toList();
        System.out.println(l1.toString());
    }
}

