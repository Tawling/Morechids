package com.towboat.morechids.tweaker;

import net.minecraft.block.state.IBlockState;

import java.util.HashSet;

/**
 * BlockOutput.java
 * <p>
 * Author:  Taw
 * Date:    9/21/2018
 */
public class BlockOutput extends HashSet<IBlockState> {
    public double weight;

    public IBlockState selectBlock() {
        return (IBlockState)this.toArray()[(int)Math.floor(Math.random()*this.size())];
    }
}