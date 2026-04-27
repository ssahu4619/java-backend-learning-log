class MyTask implements Runnable {
    @Override
    public void run() {
        // This prints out the name of the thread doing the work
        System.out.println("Executing inside: " + Thread.currentThread().getName());
    }
}

public class thread {
  
    public static void main(String[] args) {
        System.out.println("Main method started by: " + Thread.currentThread().getName());
        
        Thread t1 = new Thread(new MyTask());
        
        // MISTAKE: Calling run() directly
        System.out.println("\n--- Calling t1.run() ---");
        t1.run(); 
        
        // CORRECT: Calling start()
        System.out.println("\n--- Calling t1.start() ---");
        t1.start(); 
    }
}