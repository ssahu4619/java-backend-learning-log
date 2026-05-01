import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ExceptionHandel {
//    public static void main(String[] args) {
//
//        CompletableFuture<Void> future =
//                CompletableFuture.runAsync(() -> {
//                    throw new RuntimeException("Boom");
//                });
//
//        try {
//            future.join();
//        } catch (CompletionException e) {
//            System.out.println("Caught: " + e.getCause());
//        }
//
//        System.out.println("Done");
//    }

    void main(){
        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> {
                            throw new RuntimeException("Boom");
                        })
                        .exceptionally(ex -> {

                            System.out.println("Handled: " + ex);

                            return null;
                        });

        future.join();
    }
}