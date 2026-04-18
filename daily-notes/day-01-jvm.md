# Java Interview Preparation Tracker


[Gemini Link](https://gemini.google.com/u/2/app/92324c0a149e8a99?hl=en-IN&pageId=none)
[Jvm Doc Link](https://docs.google.com/document/d/1ZTppQWDAFDSwDW8MAl-HLpgi-ikI4K5s2lBS78wpYR0/edit?tab=t.0)

Here is a quick summary of what we have studied so far in our conversation:

### 1. Foundation: Java Architecture & JVM
* **JVM Memory Model:** Stack (thread-specific, fast) vs. Heap (shared, holds objects), Metaspace.
* **Garbage Collection (GC):** G1GC or ZGC.
* **Execution:** Java compiles to Bytecode (`.class`), which the JVM interprets and compiles just-in-time to machine code.

### 2. Object-Oriented Programming (OOP) Deep Dive
* **Composition vs. Inheritance:** Favor composition to avoid rigid "is-a" relationships.
* **Interface vs. Abstract Class:** Default methods in Java 8+.

### 3. Java Collections Framework
* **HashMap:** Hashing, buckets, collision handling (Treeification in Java 8).
* **ConcurrentHashMap:** Segment locking (Java 7) vs. CAS and node-locking (Java 8+).

### 4. Modern Java (Java 8 to Java 21)
* **Features:** Streams API, Optional, `var`, Records, Pattern Matching.
* **Project Loom:** Virtual threads for lightweight, high-throughput concurrency.

### 5. Platform Independence (Java vs. Python)
* **Java:** Compiled to platform-agnostic Bytecode, then interpreted/JIT-compiled by the JVM. Statically typed and highly robust for enterprise.
* **Python:** Interpreted dynamically at runtime via PVM. Slower execution but faster development.

### 6. JIT & Tiered Compilation
* **C1 Compiler (The Fast Barista):** Fast startup, low-level optimization.
* **C2 Compiler (The Efficiency Expert):** High-throughput, deep profiling, heavily optimizes "Hot Spots" over time.
* **JIT Optimizations:** Method Inlining, Dead Code Elimination, Escape Analysis.

### 7. Evolution of Modern GC Algorithms
* **G1GC:** Default in Java 9+. Splits heap into regions, cleans regions with most garbage first. Good for 4GB-32GB heaps.
* **Shenandoah GC:** Concurrent compaction, ultra-low latency.
* **ZGC:** Sub-millisecond pauses on multi-terabyte heaps using Colored Pointers and Load Barriers.

---
 The Core Problem: Why did we need new GCs?

 In older Java versions (Java 8 and below), the default was the Parallel GC or CMS (Concurrent Mark Sweep). They worked by splitting the heap into massive, contiguous chunks (Young and Old generations).When the heap got full, the JVM had to pause your entire application to clean up memory.On a 1GB heap, an STW pause might take 50 milliseconds (unnoticeable).On a 100GB heap (common in modern microservices or big data), that pause could take seconds or even minutes.If your server pauses for 5 seconds, database connections drop, API calls timeout, and users see error pages. 
 
 The evolution of G1GC -> Shenandoah -> ZGC is the quest to handle massive heaps with zero or near-zero pause times.
 
 Here is the "pro" breakdown of the modern heavyweights.
 
 1. G1GC (Garbage-First)Status: Default since Java 9.The Goal: A balance of high throughput and predictable pause times.How it works (The Innovation):Instead of splitting the heap into giant halves, G1GC chops the heap into thousands of small, equally sized Regions (usually 1MB to 32MB each).It tracks which regions contain the most garbage. When it runs, it cleans the regions with the most garbage first (hence the name). Because it only cleans a few regions at a time rather than the whole heap, it can strictly control how long the application pauses.The Usecase:Standard microservices, web applications, and systems with heaps ranging from 4GB to 32GB where you want reliable, sub-200ms pause times.How to use it (JVM Argument):Bash# Enable G1GC and tell it to try and keep pauses under 200 milliseconds

java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar my-app.jar

2. Shenandoah GCStatus: Introduced by Red Hat, available in Java 12+.The Goal: Ultra-low latency, regardless of heap size.How it works (The Innovation):Even G1GC has to pause the application when it moves surviving objects around (Compaction) to free up space. Shenandoah introduced Concurrent Compaction.It is able to safely move objects from one memory address to another while the Java application threads are still actively running and reading those objects. It does this using a "Brooks Pointer" (a forwarding pointer inside the object). The STW pause is only tied to the number of root references, meaning a 200GB heap pauses just as fast as a 2GB heap.The Usecase:Applications where response time is critical and cannot tolerate spikes—such as high-frequency trading, real-time gaming servers, or low-latency APIs.How to use it (JVM Argument)

java -XX:+UseShenandoahGC -jar my-app.jar

3. ZGC (Z Garbage Collector)Status: Introduced by Oracle (experimental in Java 11, production in 15, default in Java 21 for certain profiles).The Goal: The ultimate GC. Sub-millisecond (less than 1ms) pause times on multi-terabyte heaps.How it works (The Innovation):ZGC is a marvel of engineering. Instead of storing GC metadata inside the objects themselves, ZGC uses Colored Pointers. It actually alters the bits of the 64-bit memory address itself to track whether an object is reachable, moved, or marked for deletion.Combined with Load Barriers (tiny bits of code injected by the JIT compiler whenever an object is read), ZGC can clean up a 16 Terabyte heap while keeping STW pauses to less than 1 millisecond. In modern Java, it is practically magical.The Usecase:Massive scale. Big data processing (Apache Spark/Kafka), gigantic caching layers, or any enterprise application where you have heaps ranging from 32GB up to 16TB and demand zero latency.How to use it (JVM Argument)

java -XX:+UseZGC -jar my-app.jar

  Collector,Heap Size Target,Pause Time (STW),Key Technology,Best For
G1GC,Medium (4GB - 32GB),~200ms (Predictable),Heap Regions,Standard backend services (The safe default).
Shenandoah,Large (10GB - 100GB),< 10ms,Concurrent Compaction,Ultra-low latency APIs.
ZGC,Massive (Up to 16TB),< 1ms,Colored Pointers & Load Barriers,"Big Data, massive caches, absolutely zero-lag systems."

## 📄 Notes Extracted from Document: "Java Interview Prep: Memory Model"
*(Extracted up to the Advanced Garbage Collection section)*

### 1. The Java Memory Model: An Exhaustive Analysis of Heap and Stack
* **The Stack:** A thread-specific memory area used for static memory allocation and execution context. It stores Local Primitive Variables, Object References (pointers to the heap), and Method Execution State. It relies on a LIFO mechanism, making it exceptionally fast, automatically managed, and inherently thread-safe.
* **The Heap:** The centralized, globally shared memory pool for dynamically instantiated objects (`new` keyword). It is unorganized, not thread-safe by default, and requires the Garbage Collector (GC) to clean up orphaned objects.
* **Metaspace and Native Memory:** Metaspace stores class metadata and method bytecodes, residing in native OS memory to dynamically expand and avoid the fixed-size limits of the legacy PermGen.

### 2. Expert-Level JVM Internal Mechanics and JIT Compilation
* **Tiered Compilation:** * **C1 Compiler:** Analyzes execution flow rapidly for lightweight compilations to accelerate startup.
  * **C2 Compiler:** Assumes control of "hotspots" (frequently executed methods) and performs highly sophisticated optimizations (e.g., loop unrolling, method inlining) for peak throughput.
* **Escape Analysis and Scalar Replacement:** The C2 compiler checks if an object ever "escapes" a method. If it does not, the JIT compiler dismantles the object (Scalar Replacement of Aggregates) and allocates its primitive fields directly on the fast stack, bypassing the heap entirely and eliminating GC overhead.

### 3. The Java Memory Model: Concurrency Rules and Synchronization
* **Instruction Reordering:** To prevent dangerous hardware instruction reordering, the JMM injects "memory barriers" around synchronization blocks.
* **Happens-Before Relationship:** A behavioral contract guaranteeing that memory results of one action are universally visible to and sequentially ordered before another action.
* **The `volatile` Keyword:** Forces the JVM to issue a memory barrier. Writes bypass the CPU cache and flush directly to main RAM, ensuring global visibility across threads and acting as a building block for lock-free concurrent programming.
