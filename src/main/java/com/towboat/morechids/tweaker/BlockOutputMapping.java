package com.towboat.morechids.tweaker;

import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;

/**
 * BlockOutputMapping.java
 * <p>
 * Author:  Taw
 * Date:    9/21/2018
 */
public class BlockOutputMapping extends ArrayList<BlockOutput> {
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

    public IBlockState selectBlock() {
        double cutoff = Math.random()*totalWeight;
        for (BlockOutput bo : this) {
            cutoff -= bo.weight;
            if (cutoff <= 0) {
                return (IBlockState)bo.toArray()[(int)Math.floor(Math.random()*bo.size())];
            }
        }
        return null;
    }
}