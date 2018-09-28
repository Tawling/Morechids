package com.towboat.morechids.tweaker;

import net.minecraft.block.state.IBlockState;

import java.util.HashSet;
import java.util.Random;

/**
 * BlockOutput.java
 * <p>
 * Author:  Taw
 * Date:    9/21/2018
 */
public class BlockOutput extends HashSet<IBlockState> {
    public double weight;

    public IBlockState selectBlock(Random rand) {
        return (IBlockState)this.toArray()[rand.nextInt(this.size())];
    }
}