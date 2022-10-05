package com.azyf.finalyearproject;

public class Block {
   public String name;
   public Category category;

   public Block(String name, Category category) {
       this.name = name;
       this.category = category;
   }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }


}
