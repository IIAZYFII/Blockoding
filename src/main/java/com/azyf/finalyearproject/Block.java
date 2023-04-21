package com.azyf.finalyearproject;

/**
 * This class is a code representation of the physical blocks.
 * @author Hussain Asif.
 * @version 1.0.
 */
public class Block {
   public String name;
   public Category category;

    /**
     * A constructor to construct a block object.
     * @param name The name of the block.
     * @param category The category of the block.
     */
   public Block(String name, Category category) {
       this.name = name;
       this.category = category;
   }

    /**
     * Gets the name of the block.
     * @return The name of the block.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the category of the block.
     * @return The category of the block.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Converts the block into a String format.
     * @return A String containing the block's name and category.
     */
    @Override
    public String toString() {
        return "block: " + name + " category: " + category;
    }
}
