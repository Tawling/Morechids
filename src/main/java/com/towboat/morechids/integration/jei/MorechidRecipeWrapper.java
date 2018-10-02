/**
 * This class was adapted from vazkii.botania.client.integration.jei.puredaisy.PureDaisyRecipeWrapper
 * Modifications were made by Taw
 * Original header included below
 * ***
 *
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p/>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package com.towboat.morechids.integration.jei;

import com.google.common.collect.ImmutableList;
import com.towboat.morechids.tweaker.MorechidRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class MorechidRecipeWrapper implements IRecipeWrapper, Comparable<MorechidRecipeWrapper> {

    private List<ItemStack> inputs = ImmutableList.of();
    private ItemStack output = null;
    private FluidStack fluidInput = null;
    private FluidStack fluidOutput = null;

    private String identifier = null;

    public MorechidRecipeWrapper(String identifier, MorechidRecipe recipe, Map.Entry<IBlockState, Double> recipeOutput) {
        if(recipe.getInput() instanceof Block || recipe.getInput() instanceof IBlockState) {
            IBlockState state = recipe.getInput() instanceof IBlockState ? (IBlockState) recipe.getInput() : ((Block) recipe.getInput()).getDefaultState();
            Block b = state.getBlock();

            if(FluidRegistry.lookupFluidForBlock(b) != null) {
                fluidInput = new FluidStack(FluidRegistry.lookupFluidForBlock(b), 1000);
            } else {
                inputs = ImmutableList.of(new ItemStack(b, recipe.size() == 0 ? 1 : 64, b.getMetaFromState(state)));
            }
        }

        Block outBlock = recipeOutput.getKey().getBlock();
        if(FluidRegistry.lookupFluidForBlock(outBlock) != null) {
            fluidOutput = new FluidStack(FluidRegistry.lookupFluidForBlock(outBlock), 1000);
        } else {
            output = new ItemStack(
                    outBlock,
                    recipe.size() == 0 ? 1 : (int)Math.floor(Math.max(1, Math.round(64 * recipeOutput.getValue() / recipe.getTotalWeight()))),
                    outBlock.getMetaFromState(recipeOutput.getKey())
            );
        }
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputs);

        if (fluidInput != null) {
            ingredients.setInput(FluidStack.class, fluidInput);
        }

        if (!output.isEmpty()) {
            ingredients.setOutput(ItemStack.class, output);
        }

        if (fluidOutput != null) {
            ingredients.setOutput(FluidStack.class, fluidOutput);
        }
    }

    @Override
    public int compareTo(MorechidRecipeWrapper o) {
        return output.getCount() - o.output.getCount();
    }
}