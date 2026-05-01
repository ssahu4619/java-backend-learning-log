import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunAsyncExample2 {

    public static void main(String[] args) throws Exception {
        System.out.println("Main started");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Background task started");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            System.out.println("Background task finished");
        },executor);
        System.out.println("waiting for background task to finish by sleeping");
//        future.join();
//        Thread.sleep(2000);
        System.out.println("Main finished");
        executor.shutdown();
    }
}