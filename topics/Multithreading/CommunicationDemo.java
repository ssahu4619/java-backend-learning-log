import java.util.LinkedList;
import java.util.Queue;

class Restaurant {
    private final Queue<String> tray = new LinkedList<>();
    private final int CAPACITY = 1;

    // --- THE CHEF ---
    public synchronized void produceBurger() throws InterruptedException {
        // We use 'while', not 'if'. (I will explain why below!)
        while (tray.size() == CAPACITY) {
            System.out.println("Chef: Tray is full. I will wait...");
            wait(); // Releases the lock and goes to sleep
        }

        System.out.println("Chef: Cooking and adding a burger.");
        tray.add("Burger");
        
        // Wake up the Waiter!
        notify(); 
    }

    // --- THE WAITER ---
    public synchronized void consumeBurger() throws InterruptedException {
        while (tray.isEmpty()) {
            System.out.println("Waiter: Tray is empty. I will wait...");
            wait(); // Releases the lock and goes to sleep
        }

        String food = tray.poll(); // Take the burger off the tray
        System.out.println("Waiter: Took the " + food + " to the customer.");
        
        // Wake up the Chef!
        notify(); 
    }
}

public class CommunicationDemo {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        // Chef Thread
        Thread chef = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) restaurant.produceBurger();
            } catch (InterruptedException e) {}
        });

        // Waiter Thread
        Thread waiter = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) restaurant.consumeBurger();
            } catch (InterruptedException e) {}
        });

        waiter.start(); // Start waiter first so he finds an empty tray
        chef.start();
    }
}