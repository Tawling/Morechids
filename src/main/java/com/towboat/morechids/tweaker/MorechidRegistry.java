package com.towboat.morechids.tweaker;

import com.towboat.morechids.asm.MorechidClassBuilder;
import com.towboat.morechids.block.subtile.CustomOrechidSubtile;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.HashMap;
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

    public static MorechidDefinition createMorechid(String identifier) {

        System.out.println("Registering morechid: " + identifier);

        MorechidDefinition def = new MorechidDefinition(identifier);
        morechids.put(identifier, def);

        Class<? extends CustomOrechidSubtile> flowerClass = MorechidClassBuilder.generateMorechid(def.getIdentifier());

        def.setMorechidClass(flowerClass);

        BotaniaAPI.registerSubTile(def.getIdentifier(), flowerClass);
        BotaniaAPI.addSubTileToCreativeMenu(def.getIdentifier());

        BotaniaAPIClient.registerSubtileModel(flowerClass, new ModelResourceLocation("morechids:" + identifier));
        return def;
    }



    @ZenMethod
    public static MorechidDefinition getFlower(String identifier) {
        return morechids.get(identifier);
    }
}

