import java.util.concurrent.*;

public class FutureExceptionExample {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(() -> {
            throw new RuntimeException("Something went wrong");
        });

        try {
            future.get(); // will throw
        } catch (ExecutionException e) {
            System.out.println("Caught exception: " + e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}