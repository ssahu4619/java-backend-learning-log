import java.util.concurrent.CompletableFuture;

public class RunAsyncExample {

    public static void main(String[] args) throws Exception {

        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> {
                    System.out.println("Task started");
                    System.out.println(
                            "Running on: " +
                                    Thread.currentThread().getName()
                    );
                });
        Thread.sleep(500);
        System.out.println("Main thread continues...");
    }
}
