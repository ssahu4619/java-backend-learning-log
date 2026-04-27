class Inventory {
    private int items = 0;

    // This is our "Critical Section"
    public synchronized  void addItems() {
        items++; 
    }

    public int getItems() {
        return items;
    }
}

public class RaceConditionDemo {
    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory();

        // Thread A adds 10,000 items
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10000; i++) inventory.addItems();
        });

        // Thread B adds 10,000 items
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10000; i++) inventory.addItems();
        });

        threadA.start();
        threadB.start();

        // Wait for both to finish
        threadA.join();
        threadB.join();

        // What is the final count?
        System.out.println("Final Inventory Count: " + inventory.getItems());
    }
}