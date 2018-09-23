package com.towboat.morechids.tweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.subtile.SubTileEntity;

import java.util.HashMap;

@ZenClass("mods.morechids.MorechidDefinition")
@ZenRegister
public class MorechidDefinition {
    public String identifier;
    public int manaCost = 17500;
    public int timeCost = 0;
    public int delay = 100;
    public int range = 5;
    public int rangeY = 3;
    public int particleColor = 0x818181;
    public boolean playSound = true;

    public int manaCostGOG = 700;
    public int timeCostGOG = 0;
    public int delayGOG = 2;
    public int rangeGOG = 5;
    public int rangeYGOG = 3;
    public int particleColorGOG = 0x818181;
    public boolean playSoundGOG = true;

    public HashMap<Object, BlockOutputMapping> recipes = new HashMap<>();

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
            BlockOutputMapping mapping = recipes.get(block);
            if (mapping == null) {
                mapping = new BlockOutputMapping();
            }

            //TODO: Check for duplicate sets?

            BlockOutput bo = new BlockOutput();
            for (IBlockState b : outputBlocks) {
                bo.add(b);
            }
            bo.weight = weight;
            mapping.add(bo);
            recipes.put(block, mapping);
        }
    }

    @ZenMethod
    public void removeRecipe(IIngredient input, IIngredient output) {
        IBlockState[] inputBlocks = convertToBlocks(input);
        IBlockState[] outputBlocks = convertToBlocks(output);
        for (IBlockState block : inputBlocks) {
            BlockOutputMapping mapping = recipes.get(block);
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
                recipes.remove(block);
            }
        }
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
            ItemStack[] stacks = InputHelper.toStacks(((MCOreDictEntry)input).getItemArray());
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

