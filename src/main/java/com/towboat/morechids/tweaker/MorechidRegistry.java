package com.towboat.morechids.tweaker;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.List;
import java.util.Map;

/**
 * MorechidRegistry.java
 * <p>
 * Author:  Taw
 * Date:    9/13/2018
 */

public class MorechidRegistry {

    public static final MorechidRegistry INSTANCE = new MorechidRegistry();

    Map<String, MorechidDefinition> morechids;
    Map<String, ObscureDaisyDefinition> obscureDaisies;

    private MorechidRegistry(){}

    public void registerFlower(String identifier) {

    }

    public void getFlower(String identifier) {

    }

    class MorechidDefinition {
        String identifier;
        int manaCost;
        int delay;
        int range;
        int rangeY;
        int particleColor;
        List<MorechidRecipe> recipes;
    }

    class ObscureDaisyDefinition {
        String identifier;
        int defaultTime;
        int range;
        int rangeY;
        int particleColor;
        List<ObscureDaisyRecipe> recipes;
    }

    class ObscureDaisyRecipe {
        Block input;
        int delay;
        List<WeightedBlockState> outputs;
    }

    class MorechidRecipe {
        Block input;
        List<WeightedBlockState> outputs;
    }

    class WeightedBlockState {
        IBlockState blockState;
        double weight;
    }
}

