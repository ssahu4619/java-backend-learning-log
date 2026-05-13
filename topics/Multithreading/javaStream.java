import java.util.*;
import java.util.stream.Collectors;

public class javaStream {

    void main() {
//       1. Find all even numbers from a list.
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> l1 = numbers.stream().filter(n -> n % 2 == 0).toList();
        System.out.println("even number list- " + l1.toString());

//       2. Find the sum of all elements in a list.
        List<Integer> eNumber = Arrays.asList(10, 5, 96, 14, 3, 68, 75);
        int sum = eNumber.stream().reduce(0, Integer::sum);
        System.out.println("Sum of int- " + sum);
        int sum2 = eNumber.stream().mapToInt(Integer::intValue).sum();

//       3. Convert a list of strings to uppercase.
        List<String> names = Arrays.asList("me", "yoU", "She", "HER", "hIs", "MINE", "");
        List<String> nameList = names.stream().filter(s -> !s.isEmpty()).map(String::toUpperCase).collect(Collectors.toList());
        System.out.println("name List" + names.toString());
        System.out.println("Name List --" + nameList.toString());
        names.stream().filter(s -> !s.isEmpty()).map(String::toUpperCase).forEach(System.out::print);

//       4. Find the first element greater than 5
        Optional<Integer> k = eNumber.stream().filter(i -> i > 5).findFirst();
        System.out.println("\nNumber List" + eNumber.toString());
        System.out.println("fist element greter than 5- " + k.get());
        eNumber.stream().filter(i -> i > 5).findFirst().ifPresent(System.out::println);

//        5. Check if all elements are even.
        boolean b1 = eNumber.stream().allMatch(n -> n % 2 == 0);
        System.out.println(b1 ? "All are even" : "Not all are even");

//       6. Count strings starting with "A".
        List<String> aNames = Arrays.asList("Amit", "Rahul", "Anita", "Priya", "Arjun");

        Long count = aNames.stream()
                .filter(s -> s.charAt(0) == 'A')
                .collect(Collectors.counting());
        long count1 = names.stream()
                .filter(name -> name.startsWith("A"))
                .count();
        System.out.println("\nName List" + aNames.toString());
        System.out.println("Number of String starting with A count- " + count);

//        7.Remove duplicates from a list.
        List<Integer> dubNumber = Arrays.asList(1, 1, 4, 2, 5, 5, 6, 7, 7, 8, 8, 23, 56);
        List<Integer> dubList = dubNumber.stream().distinct().toList();
        System.out.println("\nDublicate Number List-- " + dubNumber.toString());
        System.out.println("Removing dublicates from List--" + dubList);

//        8 . Sort a list of strings in alphabetical order.
        List<String> sortedName = aNames.stream()
                .sorted()
                .toList();
        System.out.println("\nName List" + aNames.toString());
        System.out.println("String sorted in alphabetical order- " + sortedName);

//        9.Find the maximum and minimum number in a list.
        List<Integer> mNumbers = Arrays.asList(3, 1, 7, 5, 9, 2);
        int max = mNumbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt(); // 9

        int min = mNumbers.stream()
                .mapToInt(Integer::intValue)
                .min()
                .getAsInt();
        System.out.println("\nMax and min - "+max+" "+min);

//        10. Flatten a list of lists into a single list.
        List<List<Integer>> nested = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8)
        );
        List<Integer> flatList = nested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println("\nNested List-- "+ nested.toString());
        System.out.println("Flat List-- "+ flatList.toString());

//        11.Group a list of strings by their length
        List<String> grpNames = Arrays.asList("Amit", "Rahul", "Ali", "Priya", "Bob");
        Map<Integer, List<String>> collect = grpNames.stream().collect(Collectors.groupingBy(String::length));
        System.out.println("--------"+collect.toString());

//        12. Find the second highest number in a list.
        System.out.println("Second highest number-- ");
        eNumber.stream()
                .sorted(Comparator.reverseOrder())
                .limit(2).skip(1).forEach(System.out::println);

//        13.Join a list of strings with a separator.
        String collect3 = grpNames.stream().collect(Collectors.joining(",","{","}"));
        System.out.println("joining  name- "+collect3);

        //        14
        List<String> dNames = Arrays.asList("Amit", "Rahul", "Anita", "Priya", "Arjun");
        dNames.stream().map(String::toUpperCase).forEach(System.out::print);
    }
}


