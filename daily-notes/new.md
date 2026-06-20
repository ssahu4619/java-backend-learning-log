# Java Multithreading & Concurrency — Interview Notes

## 1. Foundations

**Process vs Thread**
- A **process** is an independent program with its own memory space (heap + stack).
- A **thread** is the smallest unit of execution within a process. It is lightweight.
- Threads of the same process **share the heap** but each thread has **its own stack**, program counter, and registers.
- Sharing the heap is exactly why we get **race conditions** and need synchronization.

**Why multithreading?**
- **I/O-bound tasks** (network calls, file reads, DB queries): concurrency genuinely speeds things up because threads can do useful work while others wait.
- **CPU-bound tasks** on a single core: multithreading improves *responsiveness*, not raw speed. Real speedup needs multiple cores (true parallelism).

**Concurrency vs Parallelism**
- *Concurrency* = dealing with many tasks at once (interleaving). 
- *Parallelism* = actually executing many tasks simultaneously on multiple cores.

---

## 2. Thread Safety & Race Conditions

**Race condition** — occurs when the outcome depends on the timing/interleaving of threads.

The root cause is usually a **non-atomic read-modify-write**. Example with a counter:
```
Thread A reads count = 5
Thread B reads count = 5
Thread A writes 6
Thread B writes 6   // one increment LOST
```

**Three things synchronization must guarantee:**
1. **Atomicity** — operation completes as one indivisible step.
2. **Visibility** — changes by one thread are seen by others (not stuck in CPU cache).
3. **Ordering** — instructions aren't reordered in a way that breaks correctness.

---

## 3. Synchronization Tools

| Tool | Provides | Notes |
|------|----------|-------|
| `synchronized` | Mutual exclusion + visibility | Heavy; only one thread in the block at a time |
| `volatile` | Visibility + ordering ONLY | Does **not** provide atomicity/mutual exclusion |
| `Atomic*` (AtomicInteger, etc.) | Atomic ops, lock-free | Best for simple counters/flags |
| `ReentrantLock` | Mutual exclusion + flexibility | `tryLock`, timeouts, fairness |
| `ReadWriteLock` | Concurrent reads, exclusive writes | Great for read-heavy data |

**Key clarifications:**
- `volatile` alone does NOT fix `count++` — that's still a non-atomic read-modify-write.
- `synchronized` gives both mutual exclusion AND visibility, so you don't *also* need `volatile`.
- For a simple counter, prefer **`AtomicInteger`** over `synchronized` — it's faster.

**AtomicInteger / CAS (Compare-And-Swap)**
- Reads current value, attempts to swap with new value **only if** current == expected.
- If another thread changed it in between, CAS fails and **retries**.
- Lock-free → no blocking → far better throughput than locks under contention.

**Lock vs tryLock**
- `lock()` blocks **indefinitely** until acquired.
- `tryLock()` returns immediately: `true` if acquired, `false` if not.
- `tryLock(timeout)` waits up to a deadline, then gives up — useful for **deadlock avoidance**.

---

## 4. wait() / notify() / notifyAll()

- Used for inter-thread **communication** (e.g., producer-consumer).
- `wait()` — releases the lock and parks the thread until notified.
- `notify()` — wakes **one** waiting thread.
- `notifyAll()` — wakes **all** waiting threads; they then compete for the lock.

**Why must they be called inside a `synchronized` block?**
- They operate on an object's **monitor (intrinsic lock)**.
- You must **own the monitor** to call them.
- Calling them without holding the lock throws **`IllegalMonitorStateException`** ("you can't wait/notify on a monitor you don't own").

**Always use `wait()` in a loop**, not an `if`, to guard against spurious wakeups:
```java
synchronized (lock) {
    while (!condition) {
        lock.wait();
    }
    // proceed
}
```

---

## 5. Deadlock

Occurs when two+ threads each hold a lock the other needs → circular wait, nobody proceeds.

**Four necessary (Coffman) conditions — break any ONE to prevent:**
1. Mutual exclusion
2. Hold and wait
3. No preemption
4. **Circular wait**

**Prevention strategies:**
- **Consistent lock ordering** (always acquire Lock A before Lock B) → breaks circular wait. *Best approach.*
- **`tryLock` with timeout** → thread backs off and retries instead of hanging forever.
- Keep lock scope small; avoid nested locks where possible.

---

## 6. Coordination Primitives (java.util.concurrent)

These are **not** locks or queues — they're **synchronization/coordination tools**.

**CountDownLatch**
- One-time use. Threads call `countDown()`; waiters block on `await()` until count hits 0.
- Use: wait for N tasks to finish before proceeding once.

**CyclicBarrier**
- Reusable. N threads call `await()`; all block until the Nth arrives, then **all proceed together**.
- The barrier then resets for reuse.
- Difference from `wait()`: a thread at the barrier just blocks — it doesn't release a resource lock for others; everyone advances together.

**Semaphore**
- Controls access to a limited number of permits (e.g., a connection pool of 5).

| | Reusable? | Purpose |
|---|---|---|
| CountDownLatch | No | Wait for N events once |
| CyclicBarrier | Yes | Sync N threads at a checkpoint repeatedly |
| Semaphore | Yes | Limit concurrent access to a resource |

---

## 7. Producer–Consumer Pattern

**Best practical solution: `BlockingQueue`** (e.g., `LinkedBlockingQueue`, `ArrayBlockingQueue`).
- Producer calls `put()` — **blocks if full**.
- Consumer calls `take()` — **blocks if empty**.
- All wait/notify coordination is handled internally.

**From scratch:** use `synchronized` + `wait()`/`notifyAll()` — producer waits when full, consumer waits when empty, each notifies the other.

---

## 8. ExecutorService Framework ⭐

**Problem it solves:** creating a new thread per task is expensive and can exhaust memory (e.g., 1000 requests → 1000 threads → OutOfMemoryError). Thread pools **reuse** a fixed set of threads.

**How it works:** submit tasks to a pool → tasks queue up → idle threads pick tasks from the queue → thread is **reused** after completing, not destroyed.

**Common factory methods (`Executors`):**
| Method | Behaviour |
|--------|-----------|
| `newFixedThreadPool(n)` | Fixed n threads; predictable load |
| `newCachedThreadPool()` | Grows/shrinks dynamically; bursty load |
| `newSingleThreadExecutor()` | One thread; sequential execution |
| `newScheduledThreadPool(n)` | Delayed / periodic tasks |

**Scheduling (ScheduledExecutorService):**
- `scheduleAtFixedRate` — next run starts every period **regardless** of task duration (can pile up if slow).
- `scheduleWithFixedDelay` — fixed gap **between end of one run and start of next**.
- Wrap scheduled task bodies in try/catch — an uncaught exception silently **stops** future runs.

**Submitting work:**
- `execute(Runnable)` — fire and forget, no result.
- `submit(Callable<T>)` — returns a **`Future<T>`**.

**Always shut down the pool:** `shutdown()` (graceful) or `shutdownNow()` (interrupt running tasks). Otherwise the JVM may not exit.

**Production tip:** prefer constructing `ThreadPoolExecutor` directly to control core/max pool size, queue type, and rejection policy, rather than the `Executors` shortcuts (which can have unbounded queues).

---

## 9. Future

- Represents the **result of an async computation**.
- `future.get()` — **blocks** until the result is ready.
- `future.get(timeout)` — blocks up to a limit.
- `future.isDone()`, `future.cancel()`.

**Limitations of Future (why CompletableFuture exists):**
- `get()` is blocking.
- Can't chain operations or combine multiple futures easily.
- No built-in exception handling pipeline.

---

## 10. CompletableFuture ⭐

A richer, non-blocking, **composable** Future. Supports chaining, combining, and error handling.

**Creating:**
- `CompletableFuture.supplyAsync(() -> value)` — runs a task that returns a value.
- `CompletableFuture.runAsync(() -> {...})` — runs a task with no return.

**Chaining / transforming:**
| Method | Takes result? | Returns value? | Use for |
|--------|---------------|----------------|---------|
| `thenApply` | Yes | Yes | Transform the result |
| `thenAccept` | Yes | No | Consume result (e.g., logging) |
| `thenRun` | No | No | Fire-and-forget after completion |
| `thenCompose` | Yes | Yes (flattened) | Chain another async call (avoids nested futures) |
| `thenCombine` | Combines **two** futures | Yes | Merge results of 2 independent futures |

**Waiting on many:**
- `CompletableFuture.allOf(f1, f2, ...)` — completes when **all** complete (returns `Void`; still call `get()` on each to read results).
- `CompletableFuture.anyOf(...)` — completes when the **first** one completes.

**Combining results of MANY futures** (since `thenCombine` only handles 2):
```java
List<CompletableFuture<T>> futures = ...;
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
List<T> results = futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
```

**Error handling:**
| Method | Handles |
|--------|---------|
| `exceptionally(ex -> fallback)` | Exception case only |
| `handle((result, ex) -> ...)` | **Both** success and failure |
| `whenComplete((result, ex) -> ...)` | Side-effect on completion (doesn't transform) |

**Batch-with-failures pattern:** submit tasks via executor → `allOf` to wait → iterate calling `join()`/`get()`, catching per-task exceptions → aggregate results.

---

## 11. Concurrent Collections

| Collection | Use case |
|-----------|----------|
| `ConcurrentHashMap` | Multi-threaded map; **bucket/segment-level locking** — writes lock only one bucket, others read/write freely. Modern default for concurrency. |
| `CopyOnWriteArrayList` | Read-heavy, write-light. Each write copies the whole array then swaps reference; reads never block. |
| `BlockingQueue` | Producer-consumer. |

**HashMap vs Hashtable vs ConcurrentHashMap**
- `HashMap` — not thread-safe, fastest.
- `Hashtable` — thread-safe but locks the **entire** table → slow. **Legacy**, avoid.
- `ConcurrentHashMap` — thread-safe with fine-grained locking → the modern choice.

**Copy-on-write mechanics:** reads work on the current snapshot; a write creates a copy, modifies it, then **atomically swaps** the reference. Iterators see a stable snapshot. Downside: expensive writes.

**ConcurrentHashMap does NOT use copy-on-write** — it uses bucket-level locking, which is better balanced for mixed read/write workloads.

**Fail-fast vs fail-safe iterators**
- *Fail-fast* (ArrayList, HashMap): throws `ConcurrentModificationException` if modified during iteration. Catches bugs early.
- *Fail-safe* (CopyOnWriteArrayList, ConcurrentHashMap): iterates over a snapshot; no exception, but may not reflect recent changes.

---

## 12. happens-before Relationship

- A **memory visibility + ordering guarantee**, not about lock-release order.
- If action A *happens-before* B, then A's changes are **visible** to B, in order.
- Sources of happens-before:
  - Unlocking a monitor happens-before a later lock of the same monitor.
  - A `volatile` write happens-before a later read of that variable.
  - `Thread.start()` happens-before the thread's actions; a thread's actions happen-before `join()` returns.
- Without a happens-before edge, one thread may read **stale cached** values written by another.

---

## 13. Quick Interview Soundbites

- "Each thread has its own stack but shares the heap — that's why we need synchronization."
- "`volatile` = visibility; `synchronized` = mutual exclusion + visibility; `Atomic*` = lock-free atomicity."
- "For a counter under contention, `AtomicInteger` (CAS) beats `synchronized`."
- "Break circular wait with consistent lock ordering to prevent deadlock."
- "`wait`/`notify` need the monitor — outside `synchronized` you get `IllegalMonitorStateException`."
- "Thread pools reuse threads; never spawn one thread per request."
- "`CompletableFuture` is a composable, non-blocking Future — chain with `thenApply`, combine with `thenCombine`, wait with `allOf`, handle errors with `handle`/`exceptionally`."
- "`ConcurrentHashMap` over `Hashtable` — fine-grained locking beats locking the whole table."

----------------------------------------------------------------------------------------------------------------------------------------------
#### Design Patterns
Good question, Shubham! Design patterns in Java are proven, reusable solutions to common problems in software design. They're categorized into three main types: Creational, Structural, and Behavioral. We've actually touched on a few of these already in your mock interview, like Builder, Singleton, Factory, and Decorator. Let me give you the full picture.

**Creational Patterns** — these deal with object creation mechanisms.

Singleton ensures a class has only one instance with a global access point, like a configuration manager or database connection pool. Factory creates objects without specifying the exact class, deciding which implementation to instantiate based on input. Abstract Factory is a factory of factories, creating families of related objects. Builder constructs complex objects step by step with a fluent API, which you use with Lombok's `@Builder`. Prototype creates new objects by cloning an existing object rather than creating from scratch.

**Structural Patterns** — these deal with how classes and objects are composed to form larger structures.

Adapter allows incompatible interfaces to work together, acting like a bridge between them. Decorator adds new functionality to an object dynamically without modifying its class, like Java IO streams wrapping each other. Facade provides a simplified interface to a complex subsystem, hiding the internal complexity. Proxy provides a placeholder or surrogate for another object to control access, which Spring uses heavily for AOP and lazy loading. Composite lets you treat individual objects and groups of objects uniformly, useful for tree structures. Bridge separates abstraction from implementation so they can vary independently.

**Behavioral Patterns** — these deal with communication and responsibility between objects.

Strategy defines a family of algorithms and makes them interchangeable at runtime, like choosing different payment methods. This connects directly to the Open/Closed Principle. Observer defines a one-to-many dependency where multiple objects are notified when one object changes state, used in event-driven systems. Template Method defines the skeleton of an algorithm in a parent class and lets subclasses override specific steps. Chain of Responsibility passes a request along a chain of handlers until one handles it, similar to Servlet filters. Command encapsulates a request as an object, allowing you to queue or log requests. Iterator provides a way to access elements of a collection sequentially, which Java's `Iterator` interface implements. State allows an object to change its behavior when its internal state changes.

A useful interview talking point: design patterns are built on top of design principles. For example, the Strategy pattern is a practical application of the Open/Closed Principle, and the Factory pattern supports Dependency Inversion. The Spring Framework itself uses many patterns internally, like Singleton for beans, Proxy for AOP, Factory for bean creation, and Template Method in classes like `JdbcTemplate`.
```java
// ============================================================
// 2. THREAD SAFE - Synchronized Method
// ============================================================
// Simple but slower: every call to getInstance() acquires a
// lock, even after the instance is already created.

class SynchronizedSingleton {
    private static SynchronizedSingleton instance;

    private SynchronizedSingleton() {
    }

    public static synchronized SynchronizedSingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedSingleton();
        }
        return instance;
    }
}


// ============================================================
// 3. THREAD SAFE - Double-Checked Locking (Best & Fastest)
// ============================================================
// Checks null twice: first without lock (fast), then with lock (only
// when needed). Most efficient thread-safe Singleton.

class DoubleCheckedLockingSingleton {
    private static volatile DoubleCheckedLockingSingleton instance;

    private DoubleCheckedLockingSingleton() {
    }

    public static DoubleCheckedLockingSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedLockingSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
}
```
----------------------------------------------------------------------------------------------------------------------------------------------  
# What are Design Patterns in Java?

Design Patterns are **proven reusable solutions** to commonly occurring software design problems.

Think of them as:

```text
Problem
   ↓
Best Practice Solution
   ↓
Design Pattern
```

They are not code libraries; they are design approaches.

---

# Three Categories of Design Patterns

```text
1. Creational Patterns
2. Structural Patterns
3. Behavioral Patterns
```

---

# 1. Creational Design Patterns

Deal with:

```text
Object Creation
```

### Commonly Asked

| Pattern          | Purpose                                        |
| ---------------- | ---------------------------------------------- |
| Singleton        | Only one object                                |
| Factory          | Create objects without exposing creation logic |
| Abstract Factory | Factory of factories                           |
| Builder          | Create complex objects step by step            |
| Prototype        | Clone existing objects                         |

---

# Singleton Pattern ⭐⭐⭐⭐⭐

Only one instance exists.

```java
public class Singleton {

    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {

        if(instance == null) {

            instance = new Singleton();
        }

        return instance;
    }
}
```

Use Cases:

* Logger
* Cache
* Configuration

Interview Question:

> How do you make Singleton thread-safe?

Answer:

```java
private static final Singleton INSTANCE =
        new Singleton();
```

or Enum Singleton.

---

# Factory Pattern ⭐⭐⭐⭐⭐

Creates objects without exposing creation logic.

---

### Interface

```java
interface Payment {

    void pay();
}
```

---

### Implementations

```java
class UpiPayment
        implements Payment {

    public void pay() {
        System.out.println("UPI");
    }
}
```

```java
class CardPayment
        implements Payment {

    public void pay() {
        System.out.println("CARD");
    }
}
```

---

### Factory

```java
class PaymentFactory {

    public static Payment getPayment(
            String type) {

        if(type.equals("UPI")) {
            return new UpiPayment();
        }

        return new CardPayment();
    }
}
```

Usage:

```java
Payment payment =
        PaymentFactory.getPayment("UPI");
```

---

# Builder Pattern ⭐⭐⭐⭐⭐

Used when constructor has many parameters.

Bad:

```java
Employee e =
new Employee(
        1,
        "John",
        "IT",
        "India",
        50000);
```

---

Good:

```java
Employee emp =
        Employee.builder()
                .id(1)
                .name("John")
                .department("IT")
                .build();
```

Used heavily in:

* Lombok `@Builder`
* Spring APIs

---

# 2. Structural Design Patterns

Deal with:

```text
Class/Object Structure
```

---

# Adapter Pattern ⭐⭐⭐⭐

Makes incompatible interfaces work together.

Example:

```text
Indian Charger
      ↓
Adapter
      ↓
US Socket
```

---

Java Example:

```java
interface Charger {

    void charge();
}
```

Adapter converts one implementation to another.

---

# Decorator Pattern ⭐⭐⭐⭐

Adds behavior dynamically.

Example:

```text
Coffee
   +
Milk
   +
Sugar
```

instead of creating:

```text
CoffeeWithMilk
CoffeeWithSugar
CoffeeWithMilkAndSugar
```

---

Real Examples:

```java
BufferedReader
```

decorates

```java
FileReader
```

---

# Facade Pattern ⭐⭐⭐

Provides a simplified interface.

Example:

```text
OrderService
```

internally calls:

```text
Inventory
Payment
Notification
```

Client sees one API.

---

# Proxy Pattern ⭐⭐⭐⭐

Acts as a placeholder.

Spring AOP uses:

```text
Proxy Objects
```

for:

```java
@Transactional
@Cacheable
@Async
```

---

# 3. Behavioral Design Patterns

Deal with:

```text
Communication Between Objects
```

---

# Strategy Pattern ⭐⭐⭐⭐⭐

Interview favorite.

Defines multiple algorithms and switches dynamically.

---

### Interface

```java
interface PaymentStrategy {

    void pay();
}
```

---

### Implementations

```java
class UpiPayment
        implements PaymentStrategy {

    public void pay() {
        System.out.println("UPI");
    }
}
```

```java
class CardPayment
        implements PaymentStrategy {

    public void pay() {
        System.out.println("CARD");
    }
}
```

---

### Usage

```java
PaymentStrategy strategy =
        new UpiPayment();

strategy.pay();
```

---

Spring uses Strategy Pattern extensively.

---

# Observer Pattern ⭐⭐⭐⭐⭐

One object notifies multiple subscribers.

Example:

```text
YouTube Channel
        ↓
Subscribers
```

Upload video:

```text
All subscribers notified
```

---

Java Example

```java
Observer
Observable
```

Spring Events also use this concept.

---

# Template Method Pattern ⭐⭐⭐⭐

Defines a skeleton of an algorithm.

Subclasses customize steps.

Example:

```java
JdbcTemplate
```

Spring interview favorite.

---

# Command Pattern ⭐⭐⭐

Encapsulates requests as objects.

Example:

```text
Remote Control
```

Button click:

```text
Command Object
```

executed later.

---

# State Pattern ⭐⭐⭐

Behavior changes based on object state.

Example:

```text
ATM Machine

No Card
Card Inserted
Pin Verified
```

Each state behaves differently.

---

# Design Patterns Used in Spring Boot

| Spring Feature          | Pattern                 |
| ----------------------- | ----------------------- |
| BeanFactory             | Factory                 |
| ApplicationContext      | Factory                 |
| @Bean                   | Factory                 |
| Singleton Bean Scope    | Singleton               |
| JdbcTemplate            | Template Method         |
| RestTemplate            | Template Method         |
| Spring Events           | Observer                |
| AOP                     | Proxy                   |
| Spring Security Filters | Chain of Responsibility |
| Dependency Injection    | Dependency Inversion    |

---

# Most Asked Patterns in Java Interviews

### Must Know

```text
Singleton
Factory
Builder
Strategy
Observer
Proxy
Adapter
Decorator
Template Method
```

---

# Quick Interview Revision

| Pattern         | One-Line Explanation           |
| --------------- | ------------------------------ |
| Singleton       | One instance only              |
| Factory         | Object creation abstraction    |
| Builder         | Step-by-step object creation   |
| Strategy        | Switch algorithms dynamically  |
| Observer        | One-to-many notification       |
| Proxy           | Control access to object       |
| Adapter         | Make incompatible classes work |
| Decorator       | Add behavior dynamically       |
| Template Method | Define algorithm skeleton      |

---

# 2-Minute Interview Answer

> Design Patterns are reusable solutions to common software design problems. They are divided into Creational, Structural, and Behavioral patterns. Commonly used patterns in Java include Singleton for single instances, Factory for object creation, Builder for constructing complex objects, Strategy for selecting algorithms at runtime, Observer for event notification, Proxy for AOP and security, Adapter for integrating incompatible interfaces, Decorator for dynamically adding behavior, and Template Method used in Spring's JdbcTemplate. Spring Framework heavily uses these patterns internally.
