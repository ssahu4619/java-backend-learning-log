// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
class Inventory {
   private int items = 0;

   Inventory() {
   }

   public synchronized void addItems() {
      ++this.items;
   }

   public int getItems() {
      return this.items;
   }
}
