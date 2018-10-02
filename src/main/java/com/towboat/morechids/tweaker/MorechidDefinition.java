package com.towboat.morechids.tweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;

@ZenClass("mods.morechids.MorechidDefinition")
@ZenRegister
public class MorechidDefinition {
    public String identifier;
    public int manaCost = 17500;
    public int maxMana = 17500;
    public int timeCost = 0;
    public int cooldown = 100;
    public int range = 5;
    public int rangeY = 3;
    public int particleColor = 0x818181;
    public boolean playSound = true;
    public boolean blockBreakParticles = true;
    public int rangeCheckInterval = 1;

    public int manaCostGOG = 17500;
    public int maxManaGOG = 17500;
    public int timeCostGOG = 0;
    public int cooldownGOG = 100;
    public int rangeGOG = 5;
    public int rangeYGOG = 3;
    public int particleColorGOG = 0x818181;
    public boolean playSoundGOG = true;
    public boolean blockBreakParticlesGOG = true;
    public int rangeCheckIntervalGOG = 1;

    public HashMap<Object, RecipeEntry> recipeEntries = new HashMap<>();
    public HashMap<Object, MorechidRecipe> recipes;

    public Class morechidClass;

    public MorechidDefinition(String identifier) {
        this.identifier = identifier;
    }

    @ZenGetter("identifier")
    public String getIdentifier() {
        return this.identifier;
    }

    public void setMorechidClass(Class c) {
        morechidClass = c;
    }

    public Class getMorechidClass() {
        return morechidClass;
    }

    public boolean matches(IBlockState state) {
        return recipes.containsKey(state) || recipes.containsKey(state.getBlock());
    }

    @ZenMethod
    public void addRecipe(IIngredient input, IIngredient output, double weight) {
        if (input == null || output == null) {
            return;
        }

        IBlockState[] inputBlocks = convertToBlocks(input);
        IBlockState[] outputBlocks = convertToBlocks(output);

        for (IBlockState block : inputBlocks) {
            RecipeEntry mapping = recipeEntries.get(block);
            if (mapping == null) {
                mapping = new RecipeEntry();
            }

            //TODO: Check for duplicate sets?

            BlockOutput bo = new BlockOutput();
            for (IBlockState b : outputBlocks) {
                bo.add(b);
            }
            bo.weight = weight;
            mapping.add(bo);
            recipeEntries.put(block, mapping);
        }
    }

    @ZenMethod
    public void removeRecipe(IIngredient input, IIngredient output) {
        IBlockState[] inputBlocks = convertToBlocks(input);
        IBlockState[] outputBlocks = convertToBlocks(output);
        for (IBlockState block : inputBlocks) {
            RecipeEntry mapping = recipeEntries.get(block);
            if (mapping == null) {
                continue;
            }

            for (BlockOutput bo : mapping) {
                for (IBlockState b : outputBlocks) {
                    bo.remove(b);
                }
                if (bo.isEmpty()) {
                    mapping.remove(bo);
                }
            }
            if (mapping.isEmpty()) {
                recipeEntries.remove(block);
            }
        }
    }

    @ZenMethod
    public void removeOutput(IIngredient block) {
        IBlockState state = convertToBlocks(block)[0];
        for (RecipeEntry mapping : recipeEntries.values()) {
            for (BlockOutput bo : mapping) {
                bo.remove(state);
                if (bo.isEmpty())
                    mapping.remove(bo);
            }
            if (mapping.isEmpty()) {
                recipeEntries.remove(state);
            }
        }
    }

    @ZenMethod
    public void removeInput(IIngredient block) {
        IBlockState state = convertToBlocks(block)[0];
        recipeEntries.remove(state);
        recipeEntries.remove(state.getBlock());
    }

    public void flattenRecipes() {
        if (recipeEntries == null) return;
        recipes = new HashMap<>();
        recipeEntries.forEach((input, entry) -> {
            MorechidRecipe recipe = new MorechidRecipe(input);
            entry.forEach((outputSet) -> {
                outputSet.forEach((block) -> {
                    double weight = recipe.containsKey(block) ? recipe.get(block) : 0;
                    weight += outputSet.weight / outputSet.size();
                    recipe.put(block, weight);
                });
            });
            recipes.put(input, recipe);
        });
        recipeEntries = null;
    }

    private IBlockState[] convertToBlocks(IIngredient input) {

        Object obj = InputHelper.toObject(input);

        System.out.println(input.getClass());
        if (!(input instanceof ILiquidStack || input instanceof MCOreDictEntry)) {
            if (obj == null || (obj instanceof ItemStack && !InputHelper.isABlock((ItemStack) obj))) {
                return new IBlockState[0];
            }
        }

        if (input instanceof MCOreDictEntry) {
            ItemStack[] stacks = InputHelper.toStacks(input.getItemArray());
            System.out.print("Total blocks in ItemStack[]: ");
            System.out.println(stacks.length);
            if (stacks.length == 0) return new IBlockState[0];
            IBlockState[] res = new IBlockState[stacks.length];
            for (int i = 0; i < stacks.length; i++) {
                ItemStack stack = stacks[i];
                res[i] = Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getMetadata());
                System.out.println(res[i]);
            }
            System.out.print("Total blocks in oredict: ");
            System.out.println(res.length);
            return res;
        }

        if (obj instanceof ItemStack) {
            obj = Block.getBlockFromItem(((ItemStack) obj).getItem()).getStateFromMeta(((ItemStack)obj).getMetadata());
            return new IBlockState[]{(IBlockState)obj};
        }

        if (input instanceof ILiquidStack) {
            obj = InputHelper.toFluid((ILiquidStack) input).getFluid().getBlock().getDefaultState();
            return new IBlockState[]{(IBlockState)obj};
        }

        return new IBlockState[0];
    }
}

