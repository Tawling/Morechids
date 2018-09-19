package com.towboat.morechids.tweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Conversion {

    IIngredient input;
    IIngredient output;
    double weight;
    public Conversion(IIngredient input, IIngredient output, double weight) {
        this.input = input;
        this.output = output;
        this.weight = weight;
    }

    public boolean isRemoval() {
        return weight == 0;
    }

    public boolean matchesInput(IItemStack inputStack) {
        return this.input.matches(inputStack);
    }

    public List<ItemStack> getOutputItemStacks(){
        IItemStack[] iStacks = output.getItemArray();
        List<ItemStack> outputList = new ArrayList<>();

        for(IItemStack iStack : iStacks) {
            if (iStack == null) continue;
            Object internal = iStack.getInternal();
            if (internal instanceof ItemStack) {
                outputList.add((ItemStack) internal);
            }
        }

        return outputList;
    }
}
