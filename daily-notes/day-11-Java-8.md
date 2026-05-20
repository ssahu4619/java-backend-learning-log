# Java 8 — Let's Start 🚀

## 1️⃣ Default & Static Methods in Interfaces

**The Problem before Java 8:**
If you added a new method to an interface, every implementing class broke. Java 8 fixed this.

```java
interface Vehicle {
    void start();

    // Default method — implementing classes don't HAVE to override
    default void honk() {
        System.out.println("Beep beep!");
    }

    // Static method — called on the interface itself
    static int getWheels() {
        return 4;
    }
}

class Car implements Vehicle {
    public void start() { System.out.println("Car started"); }
    // honk() is inherited automatically
}

// Usage
Car c = new Car();
c.honk();                  // Beep beep!
Vehicle.getWheels();       // 4
```

### Q. What's the difference between default and static methods in an interface?

- **default** — instance-level, inherited by implementing classes, can be overridden.
- **static** — belongs to the interface, called via `InterfaceName.method()`, NOT inherited.

### Q. What happens if a class implements two interfaces with the same default method?

```java
interface A { default void hello() { System.out.println("A"); } }
interface B { default void hello() { System.out.println("B"); } }

class C implements A, B {
    // MUST override, otherwise compile error
    public void hello() { A.super.hello(); } // explicitly pick one
}
```

Compiler forces you to override and resolve the conflict manually.

### Q. Can interfaces have constructors in Java 8?

No. Interfaces still cannot have constructors or instance fields.

---

## 2️⃣ Functional Interfaces

A Functional Interface has exactly one abstract method (SAM — Single Abstract Method). It can have multiple default / static methods.

```java
@FunctionalInterface
interface Greeting {
    void sayHello(String name); // only ONE abstract method
}

// Used with lambda
Greeting g = name -> System.out.println("Hello, " + name);
g.sayHello("Rahul"); // Hello, Rahul
```

### Built-in Functional Interfaces (must know!)

| Interface | Method | Use |
| :--- | :--- | :--- |
| `Predicate<T>` | `boolean test(T t)` | condition check |
| `Function<T,R>` | `R apply(T t)` | transform T → R |
| `Consumer<T>` | `void accept(T t)` | consume, no return |
| `Supplier<T>` | `T get()` | supply a value |
| `BiFunction<T,U,R>` | `R apply(T,U)` | two inputs |

```java
Predicate<Integer> isEven = n -> n % 2 == 0;
System.out.println(isEven.test(4)); // true

Function<String, Integer> length = String::length;
System.out.println(length.apply("Java")); // 4

Consumer<String> print = System.out::println;
print.accept("Hello!"); // Hello!

Supplier<Double> random = Math::random;
System.out.println(random.get()); // 0.732...
```

---

## 🎯 Interview Questions

### Q4. Can a Functional Interface extend another interface?

✅ Yes, as long as it doesn't add a second abstract method. It can inherit one abstract method and still be functional.

### Q5. Is Runnable a functional interface?

✅ Yes — it has exactly one abstract method: `void run()`.

### Q6. What's the difference between Function<T,R> and UnaryOperator<T>?

`UnaryOperator<T>` extends `Function<T,T>` — input and output are the same type.

Quick Recap — Key Methods Used 🎯

| Method | Purpose |
|---|---|
| `partitioningBy()` | split into true/false groups |
| `groupingBy()` | group by a classifier |
| `mapping()` | transform inside a group |
| `toMap()` | convert to Map |
| `summaryStatistics()` | all stats in one call |
| `thenComparing()` | chain sort conditions |
| `Predicate.and/or/negate` | chain conditions |

## 3️⃣ Stream API

### 1. What is Stream API?

A **Stream** is a sequence of elements supporting sequential and parallel aggregate operations. It represents a pipeline of operations through which data flows. A stream pipeline consists of:

1. **Source**: A collection, array, generator function, or I/O channel that provides the data.
2. **Intermediate Operations**: Operations (like `filter()`, `map()`, `sorted()`) that transform the stream into another stream. These are lazy and do not execute until a terminal operation is called.
3. **Terminal Operations**: An operation (like `collect()`, `forEach()`, `reduce()`) that produces a non-stream result (a collection, primitive value, or side-effect) and closes the stream.

#### Key Characteristics of Streams:
* **No Storage:** Streams do not store data elements. They carry values from a source through a pipeline of computational operations.
* **Non-destructive:** Stream operations do not modify the underlying source (e.g., filtering a collection returns a new stream, leaving the original collection unchanged).
* **Lazy Evaluation:** Intermediate operations are not performed until the terminal operation is initiated.
* **Single-use:** A stream can only be traversed once. Attempting to reuse a closed stream throws an `IllegalStateException`.

---

### 2. How to get a Stream?

There are several ways to create and obtain a `Stream` in Java:

#### A. From a Collection
The most common way is using the `stream()` or `parallelStream()` method on any `java.util.Collection`:
```java
List<String> list = Arrays.asList("Java", "Spring", "Docker");
Stream<String> streamFromList = list.stream();
```

#### B. From an Array
Using the static helper method `Arrays.stream()`:
```java
String[] array = {"Java", "Spring", "Docker"};
Stream<String> streamFromArray = Arrays.stream(array);
```

#### C. Using `Stream.of()`
For ad-hoc values or arrays:
```java
Stream<Integer> streamOfValues = Stream.of(10, 20, 30, 40);
```

#### D. Using Infinite/Generated Streams
Useful for creating stream sequences programmatically:
* **`Stream.iterate()`**: Generates an ordered sequential stream by applying a function iteratively.
  ```java
  // Generates 0, 2, 4, 6, 8, ...
  Stream<Integer> evenNumbers = Stream.iterate(0, n -> n + 2).limit(10);
  ```
* **`Stream.generate()`**: Generates an unordered sequential stream using a `Supplier`.
  ```java
  // Generates a stream of random doubles
  Stream<Double> randomNumbers = Stream.generate(Math::random).limit(5);
  ```

#### E. From File I/O
Using java.nio.file.Files to stream lines of a file:
```java
try (Stream<String> lines = Files.lines(Paths.get("data.txt"))) {
    lines.forEach(System.out::println);
} catch (IOException e) {
    e.printStackTrace();
}
```

#### F. Using `Stream.empty()`
Creates an empty stream (often used to avoid returning null):
```java
Stream<String> emptyStream = Stream.empty();
```
# Update 1: 2026-05-20 18:30
