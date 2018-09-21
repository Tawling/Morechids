package com.towboat.morechids;

import com.towboat.morechids.block.subtile.CustomOrechidSubtile;
import com.towboat.morechids.proxy.CommonProxy;
import com.towboat.morechids.asm.MorechidClassBuilder;
import com.towboat.morechids.tweaker.MorechidDefinition;
import com.towboat.morechids.tweaker.MorechidRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.function.BiConsumer;

import crafttweaker.CraftTweakerAPI;

@Mod(modid = Morechids.MODID, name = Morechids.NAME, version = Morechids.VERSION, dependencies="required-after:crafttweaker;required-after:botania;required-after:mtlib;")
public class Morechids {
    public static final String MODID = "morechids";
    public static final String NAME = "Morechids";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static Morechids instance;

    public static boolean scriptsSuccessful;

    @SidedProxy(serverSide = "com.towboat.morechids.proxy.CommonProxy", clientSide = "com.towboat.morechids.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MorechidRegistry.createMorechid("morechid1");
        MorechidRegistry.createMorechid("morechid2");
        MorechidRegistry.createMorechid("morechid3");

        System.out.println("Registering Morechids and Obscure Daisies");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("---------------------------- INIT");

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("---------------------------- POST_INIT");

    }
}
