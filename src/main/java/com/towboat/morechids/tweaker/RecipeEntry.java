package com.towboat.morechids.tweaker;

import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.Random;

/**
 * RecipeEntry.java
 * <p>
 * Author:  Taw
 * Date:    9/21/2018
 */
public class RecipeEntry extends ArrayList<BlockOutput> {
    public double totalWeight;

    @Override
    public boolean add(BlockOutput output) {
        boolean res = super.add(output);
        if (res) totalWeight += output.weight;
        return res;
    }

    @Override
    public boolean remove(Object output) {
        boolean res = super.remove(output);
        if (res) totalWeight -= ((BlockOutput)output).weight;
        return res;
    }

    public IBlockState selectBlock(Random rand) {
        BlockOutput bo = selectBlockOutput(rand);
        if (bo == null) return null;
        return bo.selectBlock(rand);
    }

    public BlockOutput selectBlockOutput(Random rand) {
        double cutoff = rand.nextDouble() * totalWeight;
        for (BlockOutput bo : this) {
            cutoff -= bo.weight;
            if (cutoff <= 0) {
                return bo;
            }
        }
        return null;
    }
}