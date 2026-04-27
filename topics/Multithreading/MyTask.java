// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
class MyTask implements Runnable {
   MyTask() {
   }

   public void run() {
      System.out.println("Executing inside: " + Thread.currentThread().getName());
   }
}
