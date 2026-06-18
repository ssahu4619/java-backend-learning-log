import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class flatMapQue {
    public class Department {
        String deptName;
        String location;

        // constructor, getters

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Department(String deptName, String location) {
            this.deptName = deptName;
            this.location = location;
        }

        @Override
        public String toString() {
            return "Department{" +
                    "deptName='" + deptName + '\'' +
                    ", location='" + location + '\'' +
                    '}';
        }
    }


   public class Employee {
        String name;
        String gender;
        int age;
        double salary;
        Department department;

        // constructor, getters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        public Employee(String name, String gender, int age, double salary, Department department) {
            this.name = name;
            this.gender = gender;
            this.age = age;
            this.salary = salary;
            this.department = department;
        }

       @Override
       public String toString() {
           return "Employee{" +
                   "name='" + name + '\'' +
                   ", gender='" + gender + '\'' +
                   ", age=" + age +
                   ", salary=" + salary +
                   ", department=" + department +
                   '}';
       }
   }





     void main() {
        List<String> words = Arrays.asList("Hello World", "Java is good", "Java stream");
//        Hello
//        World
//        Java
//        is
//        good
//        Java
//        stream
        words.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .forEach(s -> System.out.println(s));



        List<List<String>> listLetter = Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("c", "d", "e"),
                Arrays.asList("e", "d", "f", "g")
        );
//        list of letter--a,b,c,c,d,e,e,d,f,g
        String collect = listLetter
                .stream()
                .flatMap(l -> l.stream()).collect(Collectors.joining(","));

        System.out.println("list of letter--"+collect);

        List<List<Integer>> nested = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8, 9)
        );
//     Output>3 =>   [4, 5, 6, 7, 8, 9]
        List<Integer> nestedList = nested.stream()
                .flatMap(Collection::stream)
                .filter(i -> i > 3)
                .toList();
        System.out.println(nestedList);

        List<String> sentences = Arrays.asList(
                "java is great",
                "stream api is powerful",
                "java stream is fun"
        );
//        uniqueList---[api, fun, great, is, java, powerful, stream]
        List<String> uniqueList = sentences.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .distinct()
                .sorted()
                .toList();
        System.out.println("uniqueList---"+ uniqueList);

        List<String> data = Arrays.asList("1,2,3", "4,5", "6,7,8,9");
//       output -- Stream to Int and sum 45
        int reduce = data.stream()
                .flatMap(s -> Arrays.stream(s.split(",")))
                .mapToInt(Integer::parseInt)
                .reduce(0, Integer::sum);
        System.out.println("\nStream to Int and sum "+reduce);

        List<String> volwords = Arrays.asList("apple", "banana", "cherry");
//        Extract only vowels from all words, remove duplicates and print in sorted order.
//      output-  a e
        volwords.stream()
                .flatMap(w -> w.chars()
                        .mapToObj(c -> String.valueOf((char) c)))
                .filter("aeiou"::contains)
                .distinct()
                .sorted()
                .forEach(System.out::println);

//-----------------------------------------------------------------------------------------------------------------------------------------------
        Department it      = new Department("IT", "Bangalore");
        Department hr      = new Department("HR", "Mumbai");
        Department finance = new Department("Finance", "Delhi");

         List<Employee> employees = Arrays.asList(
                 new Employee("Alice",   "Female", 28, 75000, it),
                 new Employee("Bob",     "Male",   35, 55000, hr),
                 new Employee("Charlie", "Male",   42, 90000, finance),
                 new Employee("Diana",   "Female", 30, 95000, it),
                 new Employee("Eve",     "Female", 25, 48000, hr),
                 new Employee("Frank",   "Male",   38, 82000, finance),
                 new Employee("Grace",   "Female", 29, 60000, it),
                 new Employee("Henry",   "Male",   45, 105000, finance)
         );


//         Q1. Find all employees from the IT department and print their names.
//         Alice
//         Diana
//         Grace
         employees.stream()
                 .filter(e->e.getDepartment().getDeptName().equals("IT"))
                 .map(Employee::getName)
                 .forEach(System.out::println);

         employees.stream()
                 .collect(Collectors.groupingBy(e -> e.getDepartment().getDeptName()))
                 .entrySet()
                 .stream()
                 .filter(e -> e.getKey().equals("IT"))   // ✅ compare dept name string
                 .flatMap(e -> e.getValue().stream())     // ✅ get List<Employee> from map
                 .map(Employee::getName)                  // ✅ extract names
                 .forEach(System.out::println);

//         Q2. Find the highest paid employee across all departments.

         Employee employee = employees.stream()
                 .max(Comparator.comparingDouble(Employee::getSalary)).get();
         System.out.println("Empl "+employee.getName());

         employees.stream()
                 .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                 .limit(1)
                 .forEach(System.out::println);

         employees.stream()
                 .reduce((e1, e2) -> e1.getSalary() > e2.getSalary() ? e1 : e2)
                 .ifPresent(e -> System.out.println(e.getName()));


//         Q3. Get the average salary of each department.

         Map<Department, Double> collect1 = employees.stream()
                 .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)));
         collect1.forEach((dept, avg) ->
                 System.out.printf("%s → %.2f",
                         dept.getDeptName(), avg));
         System.out.println("\n average "+ collect1.toString());

//         Q4. Find all female employees with salary greater than 60000 and print their names sorted alphabetically.
         List<Employee> female = employees.stream()
                 .filter(e -> e.getGender().equals("Female"))
                 .filter(e -> e.getSalary() > 60000)
                 .sorted(Comparator.comparing(Employee::getName))
                 .toList();
         System.out.println("\n female--"+female.toString());

         // ✅ cleaner — both conditions in single filter
         employees.stream()
                 .filter(e -> e.getGender().equals("Female")
                         && e.getSalary() > 60000)
                 .sorted(Comparator.comparing(Employee::getName))
                 .map(Employee::getName)
                 .forEach(System.out::println);


//         Q5. Count the number of employees in each department.

         employees
                 .stream()
                 .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()))
                 .forEach((department, aLong) -> System.out.println("Dep "+department.getDeptName() +" number "+aLong));

//         Q6. Find the department with the highest average salary.
         Map<Department, Double> collect2 = employees.stream()
                 .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)));
         Optional<Map.Entry<Department, Double>> max = collect2.
                 entrySet()
                 .stream()
                 .max(Comparator.comparing(Map.Entry::getValue));

         System.out.println(collect2.toString());
         System.out.println(max.get());

//         Q7. Get the names of top 3 highest paid employees across all departments.
         List<String> list = employees.stream()
                 .sorted(Comparator.comparing(Employee::getSalary).reversed())
                 .limit(3)
                 .map(Employee::getName)
                 .toList();
         System.out.println(list);

//         Group employees by gender and find the average age of each gender group.
         employees
                 .stream()
                 .collect(Collectors.groupingBy(Employee::getGender,Collectors.averagingInt(Employee::getAge)))
                 .forEach((s,d) -> System.out.println("gender- "+s+" average- "+d));
     }
}
