import java.util.concurrent.Semaphore;

public class RateLimiterDemo {
    
    // Create a Semaphore with exactly 3 permits
    private static final Semaphore bouncer = new Semaphore(3);

    public static void main(String[] args) {
        // Spin up 10 threads trying to access the API at once
        for (int i = 1; i <= 10; i++) {
            Thread user = new Thread(() -> accessExpensiveAPI());
            user.setName("User-" + i);
            user.start();
        }
    }

    private static void accessExpensiveAPI() {
        try {
            System.out.println(Thread.currentThread().getName() + " is waiting in line...");
            
            // 1. Ask the bouncer for a permit (Blocks if 0 are left)
            bouncer.acquire(); 
            
            // --- CRITICAL SECTION (Max 3 threads inside at once) ---
            System.out.println("   -> " + Thread.currentThread().getName() + " is inside the API!");
            Thread.sleep(2000); // Simulate 2 seconds of heavy work
            // --------------------------------------------------------
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("<- " + Thread.currentThread().getName() + " left the API. (Returning permit)");
            // 2. ALWAYS return the permit in a finally block!
            bouncer.release(); 
        }
    }
}