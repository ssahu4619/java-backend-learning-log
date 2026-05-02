# Java Multithreading Masterclass: Fundamentals to Advanced Locks

### [Gpt Link] ( https://chatgpt.com/c/69e4ce12-ccc4-83ab-adc1-388423a98828)

This document is a comprehensive compilation of notes and code examples covering Java multithreading, from thread creation to advanced concurrency locks.

---

## 1. Thread Creation & The Golden Rule

In Java, there are two primary ways to create a thread, plus a modern shortcut.

### Method 1: Extending the `Thread` Class

```java
class MyBackgroundThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread is running: " + Thread.currentThread().getName());
    }
}

// Usage:
// MyBackgroundThread t1 = new MyBackgroundThread();
// t1.start();
```

### Method 2: Implementing the `Runnable` Interface *(Recommended)*

> This is preferred because Java only allows extending one parent class. Implementing `Runnable` keeps inheritance open.

```java
class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable task is running: " + Thread.currentThread().getName());
    }
}

// Usage:
// Thread t2 = new Thread(new MyTask());
// t2.start();
```

### Method 3: Lambda Expressions *(Modern Java)*

```java
Thread t3 = new Thread(() -> {
    System.out.println("Lambda thread is running: " + Thread.currentThread().getName());
});
t3.start();
```

### ⚠️ The Golden Rule: `start()` vs `run()`

| Method | Behavior |
|--------|----------|
| `t1.start()` | Tells the JVM to create a **new background thread**. The JVM then calls `run()` on that new thread. |
| `t1.run()` | Does **NOT** create a thread. Executes **synchronously** on the Main Thread, blocking it until done. |

---

## 2. Thread Synchronization: `Thread.join()`

The `thread.join()` method pauses the current thread's execution until the specified thread finishes its task.

### Example: Using `join()`

```java
public class ThreadWithJoin {
    public static void main(String[] args) {
        Thread workerThread = new Thread(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            System.out.println("Worker: Finished my task!");
        });

        workerThread.start();
        System.out.println("Main Thread: Waiting for worker to finish...");

        try {
            workerThread.join(); // The Main Thread halts here
        } catch (InterruptedException e) {}

        System.out.println("Main Thread: Worker is done. I am done too!");
    }
}
```

### Exercise: Complex Parallel Calculation

Calculate `result = (base1 ^ power1) + (base2 ^ power2)` using two parallel threads.

```java
import java.math.BigInteger;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1,
                                      BigInteger base2, BigInteger power2) {
        PowerCalculatingThread thread1 = new PowerCalculatingThread(base1, power1);
        PowerCalculatingThread thread2 = new PowerCalculatingThread(base2, power2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {}

        return thread1.getResult().add(thread2.getResult());
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private final BigInteger base;
        private final BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) < 0; i = i.add(BigInteger.ONE)) {
                result = result.multiply(base);
            }
        }

        public BigInteger getResult() { return result; }
    }
}
```

---

## 3. The Thread Lifecycle (States)

A thread can be in one of **six distinct states** (`Thread.State`):

| State | Description |
|-------|-------------|
| `NEW` | Instantiated, but `.start()` not yet called. |
| `RUNNABLE` | `.start()` called — executing or waiting for CPU. |
| `BLOCKED` | Waiting to acquire a monitor lock to enter a `synchronized` block/method. |
| `WAITING` | Waiting indefinitely for another thread (e.g., `join()`, `wait()`). |
| `TIMED_WAITING` | Waiting for a specified duration (e.g., `Thread.sleep(1000)`). |
| `TERMINATED` | Finished execution or crashed. Dead threads **cannot** be restarted. |

```java
public class ThreadStateDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        });

        System.out.println(worker.getState()); // NEW
        worker.start();
        System.out.println(worker.getState()); // RUNNABLE
        Thread.sleep(100);
        System.out.println(worker.getState()); // TIMED_WAITING
        worker.join();
        System.out.println(worker.getState()); // TERMINATED
    }
}
```

---

## 4. The Concurrency Problem (Race Conditions)

A **Race Condition** occurs when multiple threads access and modify shared data simultaneously.

Operations like `items++` are **not atomic** — they consist of **3 steps**: `READ → MODIFY → WRITE`. If threads interleave these steps, updates get lost.

> The part of your code that touches shared data is called a **Critical Section**.

```java
class Inventory {
    private int items = 0;

    // ❌ CRITICAL SECTION — causes Race Condition
    public void addItems() {
        items++;
    }
}
```

---

## 5. Synchronization & Monitor Locks

To fix Race Conditions, we ensure only one thread can execute a Critical Section at a time using **Intrinsic (Monitor) Locks**.

### Approach A: `synchronized` Method

Locks the entire method using the object's instance (`this`) as the lock.

```java
class Inventory {
    private int items = 0;

    public synchronized void addItems() {
        items++; // ✅ Thread-safe
    }
}
```

### Approach B: `synchronized` Block *(Preferred)*

Locks only the specific lines that touch shared data — improves performance.

```java
class Inventory {
    private int items = 0;

    public void addItems() {
        // Safe code outside the lock...
        synchronized (this) {
            items++; // ✅ Only this section is locked
        }
    }
}
```

---

## 6. Advanced Locks (`java.util.concurrent.locks`)

The `synchronized` keyword lacks **timeouts**, **fairness**, and **flexibility**. Advanced locks are used for complex scenarios.

### 6.1 `ReentrantLock`

> **Rule:** Always call `.unlock()` inside a `finally` block to prevent deadlocks if an exception occurs.

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Inventory {
    private int items = 0;
    private final Lock lock = new ReentrantLock();

    public void addItems() {
        lock.lock();
        try {
            items++;
        } finally {
            lock.unlock(); // ✅ ALWAYS in finally block
        }
    }
}
```

### 6.2 Superpower: `tryLock()`

Allows a thread to **attempt** acquiring a lock, but walk away or timeout if it's busy — prevents infinite blocking.

```java
public void updateInventorySafely() {
    try {
        if (lock.tryLock(2, TimeUnit.SECONDS)) {
            try {
                items++;
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("Server busy. Aborting operation.");
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

### 6.3 `ReadWriteLock`

Optimizes **read-heavy** operations by allowing **multiple concurrent readers**, but only **one exclusive writer**.

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Cache {
    private String data = "Initial";
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public String readData() {
        rwLock.readLock().lock(); // ✅ Unlimited concurrent readers
        try {
            return data;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void writeData(String newData) {
        rwLock.writeLock().lock(); // ✅ Exclusive access — blocks all readers & other writers
        try {
            data = newData;
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
```

---

## Summary

| Concept | Key Takeaway |
|---------|--------------|
| Thread Creation | Prefer `Runnable` over extending `Thread` |
| `start()` vs `run()` | Always use `start()` to spawn a new thread |
| `join()` | Waits for a thread to finish before continuing |
| Thread States | NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED |
| Race Condition | Occurs when multiple threads unsafely modify shared state |
| `synchronized` | Simple intrinsic locking for critical sections |
| `ReentrantLock` | Flexible locking with `tryLock()` and timeout support |
| `ReadWriteLock` | Optimized for read-heavy workloads |

---

## 7. How `ThreadPoolExecutor` Works Internally

Understanding `ThreadPoolExecutor` at a deep level lets you reason about performance, scaling, and production failures.

### 7.1 What is `ThreadPoolExecutor`?

It is the **actual implementation** behind the common factory methods:

```java
Executors.newFixedThreadPool(...)
Executors.newCachedThreadPool(...)
```

> Those are just convenience factory methods. The real work is done by `ThreadPoolExecutor`.

---

### 7.2 Constructor (The Most Important Piece)

```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(),
    threadFactory,
    rejectionHandler
);
```

---

### 7.3 Key Components

| Parameter | Description |
|-----------|-------------|
| `corePoolSize` | Minimum threads kept alive — always active, even when idle. |
| `maximumPoolSize` | Maximum threads allowed in the pool. |
| `BlockingQueue` | Holds tasks when all core threads are busy. |
| `keepAliveTime` | Time after which extra threads (beyond core) are terminated. |
| `RejectionHandler` | Strategy applied when queue is full **and** threads = `maxPoolSize`. |

#### Rejection Handler Options

| Policy | Behavior |
|--------|----------|
| `AbortPolicy` *(default)* | Throws `RejectedExecutionException`. |
| `CallerRunsPolicy` | The calling thread executes the task itself. |
| `DiscardPolicy` | Silently discards the task. |
| `DiscardOldestPolicy` | Drops the oldest queued task and retries. |

---

### 7.4 Internal Working — Step-by-Step Flow

This is the **most important interview concept**.

```
Task arrives via executor.execute(task)
         │
         ▼
  threads < corePoolSize?
    YES → Create a new core thread to run the task
    NO  ▼
  Queue has space?
    YES → Add task to the blocking queue
    NO  ▼
  threads < maximumPoolSize?
    YES → Create a new non-core thread to run the task
    NO  ▼
  Apply Rejection Handler
```

> **Key Insight:** Extra threads (beyond core) are only ever created when the **queue is full**.

---

### 7.5 Critical Behavioral Edge Cases

#### ❌ Case 1: Unbounded Queue (Common Mistake)

```java
new LinkedBlockingQueue<>(); // No capacity limit!
```

- Step 2 (queue push) **always succeeds** → `maximumPoolSize` is **never reached**.
- Result: No scaling beyond core threads, and a potential **memory overflow** (OOM).

#### ⚡ Case 2: `SynchronousQueue` (No Buffering)

```java
new SynchronousQueue<>();
```

- Holds **zero tasks** — a task must be handed directly to a waiting thread.
- Every new task immediately tries to create a new thread (up to `maxPoolSize`).
- Used internally by `Executors.newCachedThreadPool()`.

#### 🔒 Case 3: `FixedThreadPool` Reality

```java
Executors.newFixedThreadPool(5);
```

Internally translates to:

```java
new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
```

- Exactly **5 threads**, always.
- All additional tasks are queued indefinitely (unbounded queue).

---

### 7.6 Worker Thread Internal Loop

Each worker thread runs a loop similar to:

```java
while (task != null || (task = queue.take()) != null) {
    task.run();
}
```

**Thread lifecycle:** Take task → Execute → Take next task → Repeat → Die after `keepAliveTime` if idle.

---

### 7.7 Real Production Problems

| Problem | Cause | Effect |
|---------|-------|--------|
| **Thread Starvation** | Small pool + long-running tasks | Large backlog, slow system |
| **Memory Leak / OOM** | Unbounded queue + fast producers | Tasks pile up faster than consumed |
| **Context Switching Explosion** | `maxPoolSize` set too high | CPU thrashing, degraded throughput |

---

### 7.8 `execute()` vs `submit()`

| Feature | `execute()` | `submit()` |
|---------|-------------|------------|
| Return value | `void` | Returns a `Future<?>` |
| Exception handling | Exceptions propagate to `UncaughtExceptionHandler` | Exceptions are captured inside the `Future` |
| Use case | Fire-and-forget tasks | Tasks where you need the result or error |

---

### 7.9 Real-World Mapping (Spring Boot)

In a typical Spring Boot application:

- **Tomcat** uses an internal thread pool to handle HTTP requests.
- **`@Async`** methods run on a configurable `TaskExecutor` thread pool.
- **Database connection pools** (HikariCP) follow similar queuing and bounding logic.

**Example — API Server Under Load:**

```
1000 incoming requests
    → Thread pool size = 50
    → 50 threads actively handle requests
    → 950 requests queued
    → Controlled concurrency = stable, predictable system
```

---

### 7.10 Optimal Thread Pool Sizing

| Workload Type | Recommended Pool Size |
|---------------|-----------------------|
| **CPU-bound** (heavy computation) | ≈ Number of CPU cores |
| **I/O-bound** (DB, network, file) | > Number of CPU cores (threads wait on I/O) |

---

### 7.11 Interview Answer

> *"ThreadPoolExecutor manages task execution using a combination of core threads, a blocking queue, and a maximum thread limit. When a task arrives, it first tries to use an existing core thread, then queues the task if all core threads are busy, and only creates additional threads if the queue is full. If the thread count hits the maximum and the queue is also full, a rejection handler is triggered. This design controls concurrency, reduces context-switching overhead, and efficiently utilizes system resources."*