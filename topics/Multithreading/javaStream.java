import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class javaStream {

 class Employee{
     private String name;
     private int salary;
     String department;

     public Employee(String name, String department , int salary) {
         this.name = name;
         this.salary = salary;
         this.department = department;
     }

     public Employee(String name, int salary) {
         this.name = name;
         this.salary = salary;
     }

     public int getSalary() {
         return salary;
     }

     public void setSalary(int salary) {
         this.salary = salary;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getDepartment() {
         return department;
     }

     public void setDepartment(String department) {
         this.department = department;
     }

     @Override
     public String toString() {
         return "Employee{" +
                 "name='" + name + '\'' +
                 ", salary=" + salary +
                 ", department='" + department + '\'' +
                 '}';
     }
 }
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

//        12. Find the second-highest number in a list.
        System.out.println("Second highest number-- ");
        eNumber.stream()
                .sorted(Comparator.reverseOrder())
                .limit(2).skip(1).forEach(System.out::println);

//        13.Join a list of strings with a separator.
        String collect3 = grpNames.stream().collect(Collectors.joining(",","{","}"));
        System.out.println("joining  name- "+collect3);
//        14.Find the average of a list of integers.
      double collect1 = dubNumber.stream()
              .collect(Collectors.averagingInt
                      (Integer::intValue));
        double average = dubNumber.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        System.out.println("\n Number List-- " + dubNumber.toString());
        System.out.println("Average of number- "+ collect1);

//        15. Filter employees with salary greater than 50000.
        List<Employee> employees = Arrays.asList(
                new Employee("Rahul", 60000),
                new Employee("Amit", 45000),
                new Employee("Priya", 75000)
        );
//        employees.stream().collect(Collectors.groupingBy(Employee::getSalary,));
        List<Employee> listOfEmployee = employees.stream().filter(e -> e.getSalary() > 50000).toList();
        System.out.println("\nEmployee List- "+ employees.toString());
        System.out.println("Empl with higher than 50000" +listOfEmployee.toString());

//        16.Count occurrences of each character in a string
         String str = "programming";
         IntStream array = str.chars();
         array.mapToObj(c->(char)c).collect(Collectors.groupingBy(c->c, Collectors.counting()));

        Map<Character, Long> charCount =
        str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

                System.out.println("\nCharacter count: " + charCount);

        // 17.Find the total length of all strings starting with 'A'
        int totalLength = aNames.stream()
                .filter(s -> s.startsWith("A"))
                .mapToInt(String::length)
                .sum();

                System.out.println("Total length of strings starting with 'A': " + totalLength);

                // 18. Group employees by department

        List<Employee> empList = Arrays.asList(
                new Employee("Rahul", "HR", 50000),
                new Employee("Amit", "IT", 60000),
                new Employee("Priya", "HR", 55000),
                new Employee("Vikram", "IT", 62000)
        );

        Map<String, List<Employee>> byDept = empList.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));

        System.out.println("\nGrouped by department: " + byDept);



        /*
         Mid-level Questions for Stream
        */



//        1. Find the top 3 highest paid employees.\
        empList.stream().forEach(s-> System.out.print("\nsalary -"+s.getSalary()));
        System.out.println();
        empList.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(3)
                .map(e -> e.getName() + ": " + e.getSalary())
                .forEach(System.out::println);
        System.out.println("-------------------------");
        empList.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(3)
                .forEach(System.out::println);

//        2.Partition a list into even and odd numbers.
        List<Integer> Pnumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println("\nNumber to be partition by even and old - "+ Pnumbers.toString());
        Map<Boolean, List<Integer>> collect2 = Pnumbers.stream()
                .collect(Collectors.partitioningBy(s -> s % 2 == 0));
        collect2.entrySet().iterator();
        collect2.forEach((isEven, list) -> {
    System.out.println((isEven ? "Even Numbers: " : "Odd Numbers: ") + list);
});

//        3. Find duplicate elements in a list.

        List<Integer> duplicatesMapWay = dubNumber.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Set<Integer> set = new HashSet<>();

        List<Integer> duplicates = numbers.stream()
                .filter(n -> !set.add(n)) // add() returns false if already exists
                .collect(Collectors.toList());

//    4.Sort employees by salary, then by name alphabetically.
        List<Employee> collect5 = empList.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary)
                        .thenComparing(Employee::getName))
                .collect(Collectors.toList());
        System.out.println("\nsorted name- "+ collect5);

//        5.Get names of employees grouped by department.
        Map<String, List<String>> employeeByDepartment = empList.stream()
                .collect(Collectors
                        .groupingBy(Employee::getDepartment,
                                Collectors.mapping(Employee::getName,Collectors.toList())));

        System.out.println("\n"+employeeByDepartment.toString());

//        6.Find the longest string in a list.
        List<String> words = Arrays.asList("Java", "Stream", "Lambda", "API");

        String longestString = words.stream()
                .collect(Collectors.groupingBy(String::length)).toString();

        System.out.println("\n Longest String"+ longestString);

        Optional<Map.Entry<Integer, List<String>>> max1 = words.stream()
                .collect(Collectors.groupingBy(String::length))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey());

        System.out.println("\n longest String - "+max1.get());

        Optional<String> longest = words.stream()
                .max(Comparator.comparingInt(String::length));

        longest.ifPresent(System.out::println);

//        7. Reverse each word in a list of strings.
        List<String> list = words.stream()
                .map(s -> new StringBuilder(s)
                        .reverse().toString())
                .toList();
        System.out.println("\nList to be reverse - "+ words.toString());
        System.out.println(list);

//        9.Find department with the highest average salary.
        Map<String, Double> avgSalaryByDept = empList.stream()
                .collect(Collectors.
                        groupingBy(Employee::getDepartment,
                                Collectors.averagingInt(Employee::getSalary)));

        System.out.println("\n"+avgSalaryByDept);

        Optional<Map.Entry<String, Double>> highestDept = avgSalaryByDept
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        highestDept.ifPresent(e ->
                System.out.println(e.getKey() + " = " + e.getValue()));

        List<Employee> Employees = Arrays.asList(
                new Employee( "Rahul", 60000),
                new Employee( "Priya", 75000),
                new Employee( "Priya", 7000)
        );

       Map<String, Integer> empMap = Employees.stream()
               .collect(Collectors.toMap(
                       Employee::getName,    // key
                       Employee::getSalary,  // value
                       (existing, newVal) -> existing // keep existing on duplicate
               ));
        Map<String, Integer> empMap2 = Employees.stream()
                .collect(Collectors.toMap(
                        Employee::getName,
                        Employee::getSalary,
                        (existing, newVal) -> existing // keep existing on duplicate
                ));

        System.out.println("\nEmployee Map:" + empMap);
        empMap.forEach((name, salary) -> {
            System.out.println("Name: " + name + ", Salary: " + salary);
        });

//       11. Check if all, any, or no elements match a condition.
        boolean allEven = numbers.stream()
                .allMatch(n -> n % 2 == 0);   // true — all are even

        boolean anyGreaterThan8 = numbers.stream()
                .anyMatch(n -> n > 8);         // true — 10 exists

        boolean noneNegative = numbers.stream()
                .noneMatch(n -> n < 0);

        String sentence = "java is great and java is fun";

        Map<String, Long> frequency = Arrays.stream(sentence.split(" "))
                .collect(Collectors.groupingBy(
                        w -> w,
                        Collectors.counting()
                ));

        List<Employee> skillEmployees = Arrays.asList(
                new Employee("Rahul", Arrays.asList("Java", "Spring", "SQL")),
                new Employee("Priya", Arrays.asList("Java", "Python", "AWS")),
                new Employee("Amit", Arrays.asList("Spring", "Docker", "AWS"))
        );

        List<String> uniqueSkills = employees.stream()
                .flatMap(e -> e.getSkills().stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

// Output: [AWS, Docker, Java, Python, Spring, SQL]

        List<String> words = Arrays.asList("Banana", "Apple", "Kiwi", "Fig", "Mango");

        List<String> sorted = words.stream()
                .sorted(Comparator.comparingInt(String::length)
                        .thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());

// Output: [Fig, Kiwi, Apple, Mango, Banana]

        List<Integer> prNumbers = Arrays.asList(1, 5, 10, 15, 20, 25, 30);

        Predicate<Integer> greaterThan10 = n -> n > 10;
        Predicate<Integer> lessThan25    = n -> n < 25;

        List<Integer> result = prNumbers.stream()
                .filter(greaterThan10.and(lessThan25)) // chained predicates
                .collect(Collectors.toList());

// Output: [15, 20]
    }
}


