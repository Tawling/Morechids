package com.towboat.morechids.tweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.mc1120.item.MCItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenClass("com.towboat.morechids.MorechidDefinition")
@ZenRegister
public class MorechidDefinition {
    private String identifier;

    private List<Conversion> conversions = new ArrayList<>();

    public MorechidDefinition(String identifier) {
        this.identifier = identifier;
    }

    @ZenGetter
    public String getIdentifier() {
        return this.identifier;
    }

    @ZenMethod
    public void addRecipe(IIngredient input, IIngredient output, double weight) {
        conversions.add(new Conversion(input, output, weight));
    }

    @ZenMethod
    public void removeRecipe(IIngredient input, IIngredient output) {
        conversions.add(new Conversion(input, output, 0));
    }

    public List<Conversion> getMatchingConversions(ItemStack stack){
        List<Conversion> matching = new ArrayList<>();
        IItemStack iStack = new MCItemStack(stack);
        for (Conversion conversion : conversions) {
            if (conversion.matchesInput(iStack)) {
                matching.add(conversion);
            }
        }
        return matching;
    }
}
