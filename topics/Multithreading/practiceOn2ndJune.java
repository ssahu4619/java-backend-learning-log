import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class practiceOn2ndJune {

    void main() {
        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        List<Integer> evenList = nums.stream().filter(n -> (int) n % 2 == 0)
                .toList();
        System.out.println("Even list- "+evenList);
        Map<Boolean, List<Integer>> mapEven = nums.stream()
                .collect(Collectors
                        .partitioningBy(n -> n % 2 == 0));
        List<Integer> evenListPartitioned = mapEven.get(true);
        System.out.println("Even list using partitioningBy- " + evenListPartitioned);

        int sum = nums.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Sum of Int= "+ sum);

        Integer reduceSum = nums.stream().reduce(0, Integer::sum);
        System.out.println("Reduce Sum- "+ reduceSum);

        List<String> names = Arrays.asList("alice","bob","charlie");
        List<String> nameList = names.stream().map(String::toUpperCase).toList();
        System.out.println("NameList- "+ nameList);

        List<Integer> dubNums = Arrays.asList(1,2,2,3,3,3,4);
        Set<Integer> set = new HashSet<>();
        List<Integer> UniList = dubNums.stream()
                .filter(n -> set.add(n))
                .toList();
        System.out.println("Uniques List- "+ UniList);

        List<String> nameA = Arrays.asList("Alice","Bob","Anna","Charlie","Amy");
        nameA.stream()
                .filter(m -> m.startsWith("A")).forEach(s -> System.out.print(" "+s));
//        System.out.println("Name starting with A- "+ aLis);

//-------------------------------Medium Question------------------------

//        Find the first non-repeated character in a String

        String str = "aabbcdee";
        System.out.println("\nFind the first character that appears only once.- ");
        str.chars()
                .mapToObj(c->(char)c).collect(
                        Collectors.groupingBy(Function.identity()
                                ,LinkedHashMap::new,Collectors.counting()))
                .entrySet()
                .stream()
                .filter(en->en.getValue()==1)
                .findFirst()
                .ifPresent(s-> System.out.println(""+s));

                 System.out.println("2nd Way--- ");

                str.chars()
                .mapToObj(c->(char)c).collect(
                        Collectors.groupingBy(Function.identity()
                                ,LinkedHashMap::new,Collectors.counting()))
                .entrySet()
                .stream()
                .filter(en->en.getValue()==1)
                        .forEach(s->System.out.print(" "+s));



        Optional<Character> first = str.chars()
                .mapToObj(c -> (char) c).collect(
                        Collectors.groupingBy(Function.identity()
                                , LinkedHashMap::new, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();

        System.out.println("\n 3rd way -- "+first.get()+"\n");


//        Group a list of employees by department
        List<javaStream.Employee> employees = Arrays.asList(
                new javaStream.Employee("Rahul", "",60000),
                new javaStream.Employee("Amit", "IT",75000),
                new javaStream.Employee("Priya","HR", 2000));

        Map<String, List<javaStream.Employee>> emplByDept = employees.
                stream()
                .collect(Collectors.groupingBy(javaStream.Employee::getDepartment));
        emplByDept.forEach((dept, emps) -> {
            System.out.println(dept + ": " + emps);
        });


//        Find the second highest salary from a list of employees
        System.out.println("\n Second Highest Salary");
                 employees.stream().map(javaStream.Employee::getSalary).distinct()
                .sorted(Comparator.reverseOrder())
                .limit(2).skip(1).forEach(System.out::print);

        Optional secondHighest = employees.stream()
                .map(javaStream.Employee::getSalary)
                .distinct()              // remove duplicate salaries
                .sorted(Comparator.reverseOrder())
                .skip(1)                 // skip the highest
                .findFirst();
        System.out.println(secondHighest.orElse(-1.0));


//        Flatten a list of lists into a single list
        List<List<List<Integer>>> nested = Arrays.asList(
                Arrays.asList(Arrays.asList(1, 2, 3)),
                Arrays.asList(Arrays.asList(4, 5)),
                Arrays.asList(Arrays.asList(6, 7, 8, 9))
        );
        List<List<Integer>> collect = nested.stream()
                .flatMap(Collection::stream)
                .toList();
        System.out.println("Collect "+collect);

        List<Integer> list2Flat = collect.stream().flatMap(Collection::stream)
                .toList();
        System.out.println(list2Flat);

//        Find the most frequently occurring element in a list
        List<String> items = Arrays.asList("a","b","a","c","a","b");
        System.out.println(items.toString());
        String s = items.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("Most frequesntly element- "+ s);

        employees.stream().collect(Collectors.partitioningBy(e->e.getSalary()>5000))
                .forEach((key, value) -> System.out.println("key: " + key + " Value: " + value));

//        Find all duplicate elements and their count from a list
        List<String> words = Arrays.asList("hi","hello","hi","world","hello","hi");
        Set set1 = new HashSet();
//        words.stream()
//                .collect(Collectors.groupingBy(Function.identity(),Map::new ,Collectors.counting()));

        int [] arr = {1,2,3,4,5,6,7,8,9,10};
        Arrays.stream(arr).filter(i->i%2!=0).forEach(System.out::print);
//        List<List<Integer>> l = Collections.singletonList(new ArrayList<Integer>());
//        l.add(1);
//        l.add(2);
//        l.add(3);
//        l.add(4);
//        l.add(5);
//        l.add(6);
//        l.stream().filter(i->i%2!=0).forEach(System.out::print);
    }
}
