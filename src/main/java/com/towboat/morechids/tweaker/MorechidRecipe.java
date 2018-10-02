package com.towboat.morechids.tweaker;

import net.minecraft.block.state.IBlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * MorechidRecipe.java
 * <p>
 * Author:  Taw
 * Date:    10/1/2018
 */
public class MorechidRecipe extends HashMap<IBlockState, Double> {
    private Double totalWeight = null;
    private Object input;

    public MorechidRecipe(Object input) {
        super();
        this.input = input;
    }

    public Object getInput() {
        return input;
    }

    public double getTotalWeight() {
        if (totalWeight == null) {
            totalWeight = 0.0;
            values().forEach((d) -> {
                totalWeight += d;
            });
        }
        return totalWeight;
    }

    public IBlockState selectBlock(Random random) {
        double cutoff = random.nextDouble() * getTotalWeight();
        for (Map.Entry<IBlockState, Double> entry : this.entrySet()) {
            cutoff -= entry.getValue();
            if (cutoff <= 0) {
                return entry.getKey();
            }
        }
        return null;
    }
}
