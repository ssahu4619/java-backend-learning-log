### Collection Framework Q&A And Notes




Alright, here's one: what's the difference between an Iterator and a ListIterator, and when would you use ListIterator over a regular Iterator?


Fair attempt. So an Iterator lets you traverse any collection in one direction and remove elements safely. A ListIterator is more powerful — it only works with Lists, but it lets you go forwards and backwards, and you can add or modify elements during iteration. Does that help?

 what's the Queue interface, and can you give me an example of when you'd use a PriorityQueue versus a regular LinkedList queue?

 Good understanding there. Though just to clarify — a Queue is actually first-in-first-out, so you add to the back and remove from the front. A Deque is what allows insertion and removal from both ends. And with PriorityQueue, elements are ordered by priority rather than insertion order, so the highest priority element comes out first. Does that land?

 Right, so Deque versus LinkedList queue — a Deque allows you to add and remove from both ends, whereas a regular Queue only lets you add at the back and remove from the front. LinkedList actually implements both Queue and Deque, so it can work as either depending on how you use it. Does that clarify things?

 Good question. PriorityQueue orders elements by priority — highest priority comes out first — whereas LinkedList queue follows first-in-first-out order. PriorityQueue is backed by a heap internally for efficiency, while LinkedList uses nodes. So if you need things processed by urgency, use PriorityQueue; if it's just standard queue order, LinkedList queue works fine. Sound good?

 Brilliant. Right then, let's start with the basics. What's the difference between a process and a thread, and why would you use multithreading instead of multiprocessing?

 Good foundation — threads share the same heap within a process, which makes communication easier but riskier. Processes are isolated, which is safer but heavier and slower to communicate between. Multithreading's lighter weight and faster for shared data scenarios. Now, how do you actually create a thread in Java — what are the two approaches?

 Close — the two main approaches are extending the Thread class or implementing the Runnable interface. You can use lambda functions with Runnable since it's a functional interface, but that's still technically implementing Runnable. Why would you prefer implementing Runnable over extending Thread?

 Spot on — that's the key reason. Java doesn't allow multiple inheritance, so you can only extend one class, but you can implement multiple interfaces. Runnable gives you flexibility. Good grasp there. Now, what's the lifecycle of a thread — what are the different states a thread goes through?

 Excellent breakdown — you've covered new, runnable, running, blocked, waiting, and terminated. That's solid. Though just to clarify: blocked and waiting are slightly different. Blocked is when a thread's waiting for a lock, whereas waiting is when it's explicitly put to sleep or waiting for another thread via notify. Does that distinction land?


 Right then. Here's the next one: what's the difference between sleep and wait in Java, and when would you use each one?

 Close — sleep pauses a thread for a specific time and it still holds any locks it has. Wait releases the lock and pauses until another thread calls notify or notifyAll. So sleep is for delaying execution, wait is for synchronization between threads. Does that distinction click?
 Sure. When a thread calls wait, it releases the lock on that object and pauses. Another thread can then acquire that lock and do work. When that other thread calls notify, it wakes up the waiting thread, which tries to re-acquire the lock. That's how threads coordinate — one waits, another notifies. Does that make sense now?

 Good. Next question then: what's the difference between notify and notifyAll, and when would you use each one?

 You've got it backwards there — notify wakes up only one waiting thread, whereas notifyAll wakes up all waiting threads. Use notify when you're sure only one thread needs to wake, notifyAll when multiple threads might be waiting. Fair?

 Strong answer — you've nailed the circular dependency cause of deadlock. Your prevention strategies are solid too. Though I'd add: you can also prevent deadlock by acquiring locks in a consistent order across all threads, or setting timeouts on lock acquisition. Good understanding there though.

Ready to move into more advanced multithreading concepts?

Strong answer — you've nailed the circular dependency cause of deadlock. Your prevention strategies are solid too. Though I'd add: you can also prevent deadlock by acquiring locks in a consistent order across all threads, or setting timeouts on lock acquisition. Good understanding there though.

Good start — ExecutorService lets you manage a pool of threads without creating them manually, which is more efficient. newFixedThreadPool creates a fixed number of threads that get reused. But can you explain the difference between newFixedThreadPool, newCachedThreadPool, and newSingleThreadExecutor?

Sure. The main topics left in multithreading are thread pools and executors, volatile keyword, atomic variables, thread synchronization primitives like Semaphore and CountDownLatch, and the Fork-Join framework. Which one interests you most, or shall I quiz you on all of them?


Spot on — HashSet uses HashMap internally with a dummy object as the value, and relies on HashMap's key uniqueness to maintain set uniqueness. When you add a duplicate, put returns the old dummy value, so HashSet knows to reject it. Brilliant understanding. Here's a tricky one: how does TreeSet maintain sorted order internally?

Right track — TreeSet uses a red-black tree internally to keep elements sorted. Each insertion maintains the tree's balance and sorted property. But here's the follow-up: what's the time complexity for add, remove, and search operations in TreeSet versus HashSet?

Close — TreeSet is O log n for add, remove, and search because of the tree structure. HashSet is actually O of 1 on average for those operations because of hashing, though it can degrade to O of n in worst case with hash collisions. So HashSet's faster for most operations. Got it?

Good. Next one: what's the difference between a PriorityQueue and a TreeSet in terms of ordering and performance?

Actually, PriorityQueue uses a min-heap internally, not a linked list, so add and remove are O log n — similar to TreeSet. The key difference is TreeSet keeps everything sorted, whereas PriorityQueue only guarantees the head element is the smallest. TreeSet lets you iterate in order, PriorityQueue doesn't. Make sense?

Brilliant. One more deep collections question: what's the WeakHashMap and when would you use it instead of a regular HashMap?

Not quite — WeakHashMap uses weak references for keys, so if a key is no longer referenced elsewhere, it can be garbage collected even though it's in the map. It's useful when you want the map to not prevent garbage collection of keys. It's not really about fail-safe iteration. Does that distinction land?

Right, so for multithreading from basics to advanced, here's the path: start with thread creation and lifecycle, then move to synchronization and locks, then thread pools and executors, then atomic variables and volatile keyword, then advanced stuff like semaphores and countdown latches, fork-join framework, and finally concurrent collections. Want me to quiz you on any of those?

HashMap infinite loop bug (real production issue)
👉 LRU Cache implementation (top product company question)