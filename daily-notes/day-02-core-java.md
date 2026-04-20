## Core Java
### [GPT Link] (https://chatgpt.com/c/69e3a4ae-c3c4-8322-9d75-921c1fd1ad4e)
### [Gemini Link] (https://gemini.google.com/u/2/app/f3484756a967acbd?hl=en-IN&pageId=none)
### [Doc Link](https://docs.google.com/document/d/1eveGn-OK7XnWZdwcQXpPRJZstfWoAkLUVQ7uuJ0cRIQ/edit?tab=t.0)
### 1. OOP
# Object-Oriented Programming (OOP) is a paradigm based on modeling real-world entities as objects that encapsulate state (data) and behavior (methods).

## The 4 core pillars:

Encapsulation
Abstraction
Inheritance
Polymorphism

I’ll explain each with clear real-world analogy + Java practical example + interview perspective.

### 1️⃣ Encapsulation
📌 Definition

## Binding data and methods together into a single unit and restricting direct access to some components.

## Achieved using: private variables + public getter/setter methods.

🏦 Real-world Example: Bank Account

You cannot directly modify your bank balance. You must use:

deposit()
withdraw()

The balance is hidden.

```Java Example
class BankAccount {

    private double balance;   // hidden data

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}
```
🔎 What’s happening?
balance is private → cannot access directly.
Access only through methods → controlled access.

### 🎯 Why Encapsulation?
Data security
Maintainability
Validation control
Loose coupling


#### 2️⃣ Abstraction
📌 Definition

### Hiding internal implementation and showing only essential features.

### Achieved using:

### abstract class
### interface

🚗 Real-world Example: Car

You use:

start()
accelerate()
brake()

You don’t know how the engine internally works.

``` Java Example (Abstract Class)
abstract class Vehicle {
    abstract void start();
}

class Car extends Vehicle {
    void start() {
        System.out.println("Car starts with key ignition");
    }
}
💻 Java Example (Interface)
interface Payment {
    void pay();
}

class UpiPayment implements Payment {
    public void pay() {
        System.out.println("Paid using UPI");
    }
}
```
🎯 Why Abstraction?
Reduces complexity
Improves security
Allows flexibility in implementation

#### 3️⃣ Inheritance
📌 Definition

### One class acquires properties and behavior of another class.

### Achieved using extends

👨‍👩‍👦 Real-world Example: Parent → Child

Child inherits:

surname
some features

```Java Example
class Animal {
    void eat() {
        System.out.println("Eating...");
    }
}

class Dog extends Animal {
    void bark() {
        System.out.println("Barking...");
    }
}
```
🔎 What’s happening?
Dog inherits eat() from Animal
Dog also has its own method bark()

🎯 Why Inheritance?
Code reusability
Hierarchical classification
Reduces duplication

#### 4️⃣ Polymorphism
📌 Definition

### One interface, multiple implementations.

### Two types:

### Compile-time (Method Overloading)
### Runtime (Method Overriding)

```java
🔹 Compile-Time Polymorphism (Overloading)
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }
}
```
Same method name, different parameters.

```java
🔹 Runtime Polymorphism (Overriding)
class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    void sound() {
        System.out.println("Dog barks");
    }
}

Now:

Animal obj = new Dog();
obj.sound();

Output:

Dog barks
```
👉 Method decided at runtime → Dynamic Method Dispatch.

🎯 Why Polymorphism?
Flexibility
Extensibility
Loose coupling
Supports Open-Closed Principle

🔥 How All 4 Work Together (Real Enterprise Example)

Consider a Payment System:
###
Encapsulation → Payment details are private
Abstraction → Payment interface
Inheritance → CreditCardPayment, UpiPayment
Polymorphism → Same pay() method, different implementations
###
Payment payment = new UpiPayment();
payment.pay();

System doesn’t care which payment type → clean architecture.

| Concept       | Keyword                  | Purpose             |
| ------------- | ------------------------ | ------------------- |
| Encapsulation | private + getter/setter  | Data hiding         |
| Abstraction   | abstract / interface     | Hide implementation |
| Inheritance   | extends                  | Reuse code          |
| Polymorphism  | overloading / overriding | Flexibility         |

#### Q1: How does the JVM handle method overriding internally at runtime?

**Answer:** Method overriding is an example of Runtime Polymorphism (or Dynamic Method Dispatch). Internally, the JVM uses a structure called a Virtual Method Table (vtable) for every class.

When a subclass overrides a method, its vtable points to the new implementation. When a method is called on a superclass reference holding a subclass object, the JVM looks up the actual object in memory at runtime, navigates to its specific class's vtable, and executes the correct overridden method.

To truly grasp how the JVM handles method overriding at runtime, it helps to visualize the separation between the reference variable (what the compiler sees) and the actual object in memory (what the JVM sees at runtime).

Here is a breakdown of the concept, a clear code example, and an interactive visualization to see the Virtual Method Table (vtable) in action.

1. The Core Concept: What is a vtable?
Whenever a class is loaded into the JVM, the JVM creates a vtable (Virtual Method Table) for it.

A vtable is essentially an array of pointers (memory addresses) that point to the actual implementation of the methods for that class.

If a subclass inherits a method and does not override it, its vtable points to the superclass's implementation.

If a subclass does override a method, its vtable is updated to point to its own new implementation.

When you call a method, the JVM looks at the actual object on the heap, grabs its vtable, and follows the pointer to the correct code.

#### 2. The Code Example
```java
class Animal {
    public void speak() {
        System.out.println("Animal makes a sound");
    }
    
    public void eat() {
        System.out.println("Animal is eating");
    }
}

class Dog extends Animal {
    // Overriding the speak method
    @Override
    public void speak() {
        System.out.println("Dog barks: Woof!");
    }
    
    // Dog inherits eat() but does not override it.
}

public class Main {
    public static void main(String[] args) {
        // Scenario 1: Reference and Object are the same
        Animal myAnimal = new Animal(); 
        myAnimal.speak(); // Output: Animal makes a sound
        
        System.out.println("---");

        // Scenario 2: Superclass reference holding Subclass object (Upcasting)
        Animal myDog = new Dog(); 
        
        // At compile time, the compiler checks if 'Animal' has a 'speak()' method. It does.
        // At runtime, the JVM looks at the ACTUAL object (Dog), checks its vtable, and calls Dog's speak().
        myDog.speak(); // Output: Dog barks: Woof!
        
        // At runtime, the JVM looks at the Dog's vtable. Since eat() wasn't overridden, 
        // the pointer directs back to the Animal's eat() method.
        myDog.eat();   // Output: Animal is eating
    }
}
```

#### 3. Step-by-Step Runtime Execution for Animal myDog = new Dog();

**Stack Memory:** The JVM creates a reference variable named myDog of type Animal on the thread's stack.

**Heap Memory:** The JVM allocates memory on the heap for a new Dog object. myDog now holds the memory address of this Dog object.

**Method Invocation (myDog.speak()):** The JVM goes to the heap address stored in myDog.

It sees that the object is of type Dog.

It looks up the Dog class's vtable.

It finds the entry for speak(). Because Dog overrode this method, the vtable pointer directs the JVM to the Dog.speak() bytecode.

The JVM executes "Dog barks: Woof!".

Here is an interactive visualization so you can simulate how the JVM routes these method calls through the vtables based on the object instantiated.

#### Q2: Since Java 8 introduced default methods in interfaces, how do interfaces differ from abstract classes? When should you use which?

**Answer:** While interfaces can now have concrete behavior via default methods, the core distinction remains: State.

**Abstract Classes:** Can hold state (instance variables), have constructors, and define protected/private non-static methods. They define what an object is (a core identity).

**Interfaces:** Cannot hold state (all fields are implicitly public static final). They have no constructors. They define what an object can do (a capability or contract).

**When to use:** Use an abstract class to share common code and state among closely related classes (e.g., AbstractDatabaseConnection with connection string state). Use an interface to define a role that any class can implement, regardless of its place in the class hierarchy (e.g., Serializable or Runnable), taking advantage of multiple interface implementation.

#### What does it mean that an Abstract Class can "Hold State"?

In Object-Oriented Programming, "State" simply means the data or properties specific to an individual object. When we say an Abstract Class can hold state, we mean it can declare instance variables (fields that are not static and not final). When you create a subclass and instantiate it, memory is allocated for those specific variables. The object "remembers" this data, and it can change over the object's lifetime.

An Interface, on the other hand, cannot hold state. Any variable you declare inside an interface is automatically made public static final by the Java compiler. This means it is a constant shared by everything, not a piece of personal data for an individual object.

#### The Difference in Action
Let's look at a concrete example. Imagine we are building a game with different types of characters.

1. The Abstract Class (Holds State)
We use an Abstract Class to define what a character is and give it fundamental properties (state).

```Java
public abstract class GameCharacter {
    // THIS IS STATE: Every character gets their own unique name and health.
    protected String name; 
    protected int health;  

    // Constructor to initialize the state
    public GameCharacter(String name, int health) {
        this.name = name;
        this.health = health;
    }

    // A concrete method using the state
    public void takeDamage(int damage) {
        this.health -= damage;
        System.out.println(name + " took damage! Health is now " + health);
    }
    
    // Abstract method (forced to be implemented by subclasses)
    public abstract void attack();
}
2. The Interface (No State, Only Behavior Contract)
Now we want some characters to be able to use magic. Magic isn't what a character is, it's something they can do. We use an interface.

Java
public interface MagicUser {
    // NOT STATE: This is implicitly `public static final`. It is a constant.
    // You cannot create an instance variable here.
    int MAX_SPELL_LEVEL = 5; 

    // Abstract method (contract)
    void castSpell();

    // Default method (Java 8+ concrete behavior, but still no state)
    default void readSpellbook() {
        System.out.println("Reading spellbook to learn new spells...");
    }
}
3. Putting it Together
Java
// Wizard IS A GameCharacter, and CAN DO MagicUser things.
public class Wizard extends GameCharacter implements MagicUser {

    public Wizard(String name) {
        // Initializes the state inherited from the Abstract Class
        super(name, 100); 
    }

    @Override
    public void attack() {
        System.out.println(name + " attacks with a staff!");
    }

    @Override
    public void castSpell() {
        System.out.println(name + " casts a fireball! (Max Level: " + MAX_SPELL_LEVEL + ")");
    }
}

public class Main {
    public static void main(String[] args) {
        Wizard gandalf = new Wizard("Gandalf");
        
        // Modifying state inherited from Abstract Class
        gandalf.takeDamage(20); 
        
        // Invoking Interface behavior
        gandalf.castSpell();    
    }
}
```
#### Visualizing State vs. Behavior
To make this completely clear, here is an interactive visualization of how the JVM allocates memory for these concepts. You can see how extending the abstract class physically gives the object memory blocks for its variables, whereas the interface simply provides behavioral pathways.

#### Q3: Explain the difference between `HashMap` and `ConcurrentHashMap` in Java.

**Answer:**

| Feature | `HashMap` | `ConcurrentHashMap` |
|---------|-----------|-----------------------|
| **Thread Safety** | Not thread-safe | Thread-safe |
| **Locking** | Uses single lock for the entire table | Uses fine-grained locking (lock striping) on segments/bins |
| **Performance** | Better in single-threaded environments | Better in multi-threaded environments |
| **Iterator** | Weakly consistent (may throw `ConcurrentModificationException`) | Strongly consistent (iterators don't throw exception during concurrent modification) |
| **Internal Structure** | Array of linked lists/trees | Array of `Node` arrays (segments) |

**Key Difference:** `ConcurrentHashMap` achieves thread safety without locking the entire map, allowing multiple threads to read and write different parts of the map simultaneously, making it ideal for concurrent applications.

#### Q4: "Favor Composition over Inheritance." Why is this a fundamental design principle in Java?

**Answer:** Inheritance is a static, rigid relationship that binds a subclass to the implementation details of its superclass at compile time. This often leads to the "Fragile Base Class" problem, where a change in the superclass breaks the subclasses. It also breaks true encapsulation.

**Composition** (using instance variables that are references to other objects) is highly flexible. It allows you to change behavior at runtime (by swapping out the composed object, often using the Strategy Design Pattern) and prevents deep, unmanageable class hierarchies since Java restricts you to single inheritance.

#### Q5: To understand why "Favor Composition over Inheritance" is such a critical design principle in Java, we need to look at how these two approaches handle change and flexibility.

At a 4-year experience level, you know that requirements change constantly. Inheritance locks you into a rigid structure at compile time, while Composition gives you LEGO-like flexibility at runtime.

Let's look at the classic "Combinatorial Explosion" problem to illustrate this, followed by the code, and then an interactive visualizer.

##### The Problem: Combinatorial Explosion
Imagine you are building a simulation with different vehicles (Car, Boat) and different power sources (Gas, Electric).

##### Using Inheritance (IS-A relationship):
You create a base Vehicle class. Then you create Car and Boat. Now you need to add power sources. You end up with:

GasCar extends Car

ElectricCar extends Car

GasBoat extends Boat

ElectricBoat extends Boat

If you add a new vehicle (Airplane) and a new power source (Nuclear), your class hierarchy explodes. You have to create NuclearAirplane, GasAirplane, NuclearCar, etc. Furthermore, if an ElectricCar runs out of battery, you cannot swap its engine to a Gas engine at runtime. It is permanently an ElectricCar in memory.

##### Using Composition (HAS-A relationship):
Instead of inheriting, you extract the changing behavior (the engine) into an interface. Your Vehicle has an Engine. You now only need:

Car extends Vehicle

Boat extends Vehicle

GasEngine implements Engine

ElectricEngine implements Engine

You mix and match them at runtime. Adding an Airplane is just one new class. Adding a NuclearEngine is just one new class.

##### The Code Example
Here is how you implement the flexible Composition approach (often using the Strategy Pattern).

```Java
// 1. Extract the behavior into an Interface
interface Engine {
    void start();
}

// 2. Create concrete implementations
class ElectricEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Starting silently with battery power...");
    }
}

class GasEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Vroom! Firing up cylinders...");
    }
}

// 3. The Base class uses COMPOSITION (HAS-A) instead of inheritance
class Vehicle {
    private String type;
    
    // The composed object (State)
    private Engine engine; 

    public Vehicle(String type, Engine engine) {
        this.type = type;
        this.engine = engine;
    }

    // THE MAGIC: We can swap the behavior AT RUNTIME
    public void setEngine(Engine newEngine) {
        this.engine = newEngine;
        System.out.println(type + " engine swapped!");
    }

    public void move() {
        System.out.print(type + " is trying to move: ");
        engine.start();
    }
}

public class Main {
    public static void main(String[] args) {
        // We create a Car and inject an Electric Engine
        Vehicle myCar = new Vehicle("Sedan", new ElectricEngine());
        myCar.move(); // Output: Sedan is trying to move: Starting silently...

        // Runtime Flexibility: Oh no, battery is dead! Swap the engine.
        // You CANNOT do this with Inheritance (you can't cast ElectricCar to GasCar)
        myCar.setEngine(new GasEngine());
        myCar.move(); // Output: Sedan is trying to move: Vroom!
    }
}
```

#### Q6: Explain the concept of "Weakly Consistent" iterators in Java Collections.

**Answer:** A weakly consistent iterator traverses the collection as it was at the time the iterator was created. It does not reflect any changes (additions or removals) made to the collection *after* the iterator was instantiated. While it won't throw a `ConcurrentModificationException` during iteration, the data it reads might be stale compared to the current state of the collection.

#### Q7: What is a marker interface, and how does it violate standard OOP principles? How do we replace it in modern Java?

**Answer:** A marker interface is an empty interface (like Serializable or Cloneable) used to signal to the JVM or a framework that a class has a certain property.

##### The Violation: It violates standard OOP because an interface is supposed to define a contract of behavior (methods), but a marker interface defines zero behavior. It is essentially using the type system to add metadata.

##### Modern Replacement: Since Java 5, Annotations (like @Entity or @Deprecated) are the correct way to attach metadata to a class, leaving interfaces to strictly define behavior contracts.

Imagine you are packing boxes for a move.

Standard OOP Interfaces are like saying: "Every box must have handles so I can carry it." (It dictates a behavior or capability).

A Marker Interface is like slapping a red "FRAGILE" sticker on a box. The sticker doesn't give the box new handles or change how the box works. It’s just metadata—a message to the movers (the JVM or a framework) to treat this box carefully.

Here is how that looks in code, why it's considered "bad design" today, and how we fix it.

##### 1. The Old Way: Marker Interfaces
A marker interface is literally just an interface with zero methods.

```Java
// 1. We create an empty interface. It has no methods.
public interface Deletable {
    // Nothing here!
}

// 2. We "tag" our class by implementing it.
public class UserAccount implements Deletable {
    private String username;
    
    public UserAccount(String username) {
        this.username = username;
    }
}

public class SystemCleaner {
    public void cleanUp(Object obj) {
        // 3. We check for the "tag" using instanceof
        if (obj instanceof Deletable) {
            System.out.println("This object is marked as deletable. Deleting...");
            // delete logic here
        } else {
            System.out.println("Cannot delete this object.");
        }
    }
}
```

##### 2. Why does this violate OOP?
In Object-Oriented Programming, the rule is: Interfaces define behavior (what an object can do). If I write implements Runnable, I am signing a contract that my class will have a run() method. But if I write implements Deletable (our marker), I don't have to write any methods at all.

I have polluted my class's "Type Hierarchy" (its family tree) just to add a sticky note. If you look at UserAccount, it now technically IS-A Deletable, but that doesn't make logical sense in the real world. It's using the type system for something it wasn't built for.

##### 3. The Modern Way: Annotations
Since Java 5, Java introduced Annotations. Annotations are specifically designed to be "sticky notes" (metadata). They let you tag classes, methods, or variables without messing up your OOP inheritance tree.

Here is how we rewrite the exact same logic using modern Java:

```Java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// 1. We create an Annotation instead of an Interface
// @Retention(RetentionPolicy.RUNTIME) means "keep this sticky note visible when the program is running"
@Retention(RetentionPolicy.RUNTIME) 
public @interface Deletable {
    // Nothing here!
}

// 2. We "tag" our class using the @ symbol. 
// Notice we are NO LONGER using 'implements'. Our OOP hierarchy is clean!
@Deletable
public class UserAccount {
    private String username;
    
    public UserAccount(String username) {
        this.username = username;
    }
}

public class SystemCleaner {
    public void cleanUp(Object obj) {
        // 3. We check for the "tag" using Reflection
        if (obj.getClass().isAnnotationPresent(Deletable.class)) {
            System.out.println("This object is tagged with @Deletable. Deleting...");
            // delete logic here
        } else {
            System.out.println("Cannot delete this object.");
        }
    }
}
```

##### Summary of the Shift
Pre-Java 5: Developers had no way to attach metadata to classes, so they hacked the interface system (e.g., Serializable, Cloneable).

Post-Java 5: We use Annotations (e.g., @Entity, @Override, @Deprecated, @Autowired). You should almost never create a new Marker Interface in a modern Java application.

### Happens-before in Java
Yeah, you're getting the general idea there — happens-before ensures a specific ordering between operations across threads. But let me push you a bit further: can you give me a concrete example of when happens-before matters? Like, what goes wrong if you don't have those guarantees, and what are some specific happens-before rules in Java that enforce this? So happens-before is about guaranteeing that if thread A does something, thread B will see those changes in the correct order. Without it, one thread might see stale or reordered data from another thread, causing bugs that are nearly impossible to track down. Some key happens-before rules are: a write to a volatile variable happens before any subsequent read of it, actions in a synchronized block happen before the next synchronized block on the same lock, and thread start happens before any action in that thread. Basically, the JVM uses these rules to ensure memory visibility and ordering across threads.

### Method hiding in java
In Java, if a parent class and child class both have a static method with the same name, what happens when you call it? Is it overriding or something else?
Close, but not quite! Actually, you can have a static method with the same name in the child class — it's called method hiding, not overriding. The difference is that with static methods, the method called depends on the reference type, not the object type. So if you have a Parent reference pointing to a Child object and call that static method, it'll call the Parent's version. With instance methods, it would call the Child's version due to polymorphism. That's the key distinction. 

### Bridge pattern
Sure. The bridge pattern is a design pattern that decouples an abstraction from its implementation so they can vary independently. Basically, instead of having a class hierarchy where you mix what something does with how it does it, you separate those concerns into two hierarchies connected by a bridge. A classic example is a Shape abstraction with different implementations for different drawing platforms — you don't want Circle and Square to know about Windows versus Linux rendering. The bridge lets Shape classes work with any rendering implementation. It reduces code duplication and makes it easier to add new implementations.

### Try-with-resources
Try-with-resources is a feature from Java 7 onwards that automatically closes resources for you. Instead of manually closing things in a finally block, you declare the resource in parentheses after try, and it automatically calls close() when you're done. So try with resources (new FileReader("file.txt")) automatically closes the FileReader without needing finally. It's cleaner, safer, and you don't risk forgetting to close resources. Much better than traditional try-catch-finally. 
 
 ## Here's an expert-level question then: can you explain what the happens-before relationship is in the context of try-with-resources, and how it ensures thread safety when closing resources?

With try-with-resources, there's a happens-before relationship between the resource's close() method and the end of the try block. This means any changes made by the resource are guaranteed to be visible to subsequent code. So if multiple threads are using try-with-resources with the same resource, the JVM ensures proper memory visibility and synchronization when closing. It's about guaranteeing thread-safe cleanup.

### Escape analysis
Escape analysis is a JVM optimization technique where the compiler analyzes whether an object created in a method stays local to that method or "escapes" to other parts of the code. If an object doesn't escape — meaning it's only used within that method — the JVM can optimize by allocating it on the stack instead of the heap, or even eliminating it entirely through scalar replacement. This avoids garbage collection overhead and improves performance. It's a pretty sophisticated optimization that happens behind the scenes. 

### Weak references
Weak references are objects that can be garbage collected even if there are references to them. Unlike strong references, if an object only has weak references pointing to it and memory is needed, the garbage collector will reclaim it. You'd use weak references for things like caches — if you cache an object with a weak reference and memory runs low, the cache entry gets garbage collected automatically without causing memory leaks. Another example is WeakHashMap, which uses weak keys, so entries disappear when the key is no longer strongly referenced elsewhere. 

#### Thing to work on
So the main things to work on are: the four types of references — strong, soft, weak, and phantom references. Exception handling and custom exceptions. Design patterns like singleton, factory, builder, and adapter. Concurrency utilities like AtomicInteger, CountDownLatch, CyclicBarrier, and Semaphore. The Stream API and functional programming with lambdas. And finally, some JDBC and database connection pooling concepts since those often come up in senior roles. Start with references and design patterns, then move into the concurrency utilities. That'll round out your knowledge nicely before the actual interview. You've got a solid foundation — just shore up those gaps and you'll be in great shape, Shubham. Good luck with it!