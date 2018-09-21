package com.towboat.morechids.tweaker;

import com.towboat.morechids.asm.MorechidClassBuilder;
import com.towboat.morechids.block.subtile.CustomOrechidSubtile;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MorechidRegistry.java
 * <p>
 * Author:  Taw
 * Date:    9/13/2018
 */

@ZenClass("mods.morechids.Registry")
@ZenRegister
public class MorechidRegistry {

    public static final Map<String, MorechidDefinition> morechids = new HashMap<>();
    public static final Map<String, ObscureDaisyDefinition> obscureDaisies = new HashMap<>();

    @ZenMethod
    public static MorechidDefinition createMorechid(String identifier) {

        System.out.println("Registering morechid: " + identifier);

        MorechidDefinition def = new MorechidDefinition(identifier);
        morechids.put(identifier, def);

        Class<? extends CustomOrechidSubtile> flowerClass = MorechidClassBuilder.generateMorechid(def.getIdentifier());

        def.setMorechidClass(flowerClass);

        BotaniaAPI.registerSubTile(def.getIdentifier(), flowerClass);
        BotaniaAPI.addSubTileToCreativeMenu(def.getIdentifier());

        BotaniaAPIClient.registerSubtileModel(flowerClass, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ORECHID));
        System.out.println("Loaded flower: " + def.getIdentifier());

        return def;
    }

    @ZenMethod
    public static MorechidDefinition getFlower(String identifier) {
        return morechids.get(identifier);
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

