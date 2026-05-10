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