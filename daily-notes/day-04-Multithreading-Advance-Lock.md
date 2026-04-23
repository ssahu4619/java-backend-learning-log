# Java Multithreading Masterclass: Fundamentals to Advanced Locks

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