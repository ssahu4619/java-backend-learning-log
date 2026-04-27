import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ModernRestaurant {
    
    // We replace the standard Queue with a BlockingQueue.
    // We pass '1' into the constructor to set the strict CAPACITY.
    private final BlockingQueue<String> tray = new ArrayBlockingQueue<>(1);

    // --- THE CHEF ---
    public void produceBurger() throws InterruptedException {
        // 1. No synchronized keyword needed!
        // 2. No while loop needed!
        
        System.out.println("Chef: Cooking and trying to put a burger on the tray...");
        
        // .put() will immediately halt the thread if the tray is at capacity (1)
        tray.put("Burger"); 
        
        System.out.println("Chef: Successfully added a burger.");
    }

    // --- THE WAITER ---
    public void consumeBurger() throws InterruptedException {
        System.out.println("Waiter: Checking the tray for food...");
        
        // .take() will immediately halt the thread if the tray is empty (0)
        String food = tray.take(); 
        
        System.out.println("Waiter: Took the " + food + " to the customer.");
    }
}

public class ModernCommunicationDemo {
    public static void main(String[] args) {
        ModernRestaurant restaurant = new ModernRestaurant();

        Thread chef = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) restaurant.produceBurger();
            } catch (InterruptedException e) {
                // Good practice: Restore the interrupted status
                Thread.currentThread().interrupt(); 
            }
        });

        Thread waiter = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) restaurant.consumeBurger();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        waiter.start(); 
        chef.start();
    }
}