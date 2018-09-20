package com.towboat.morechids.tweaker;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.Map;

/**
 * MorechidRegistry.java
 * <p>
 * Author:  Taw
 * Date:    9/13/2018
 */

@ZenClass("mods.morechids")
@ZenRegister
public class MorechidRegistry {

    static Map<String, MorechidDefinition> morechids;
    static Map<String, ObscureDaisyDefinition> obscureDaisies;

    @ZenMethod
    public static void createMorechid(String identifier) {
        MorechidDefinition def = new MorechidDefinition(identifier);
    }

    public static void getFlower(String identifier) {

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

