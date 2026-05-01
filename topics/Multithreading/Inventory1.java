// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
class Inventory1 {
   private int items = 0;

   Inventory1() {
   }

   public synchronized void addItems() {
      ++this.items;
   }

   public int getItems() {
      return this.items;
   }
}
