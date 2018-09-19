package com.towboat.morechids;

import com.towboat.morechids.block.subtile.CustomOrechidSubtile;
import com.towboat.morechids.proxy.CommonProxy;
import com.towboat.morechids.asm.MorechidClassBuilder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.common.lib.LibBlockNames;

@Mod(modid = Morechids.MODID, name = Morechids.NAME, version = Morechids.VERSION)
public class Morechids {
    public static final String MODID = "morechids";
    public static final String NAME = "Morechids";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static Morechids instance;

    @SidedProxy(serverSide = "com.towboat.morechids.proxy.CommonProxy", clientSide = "com.towboat.morechids.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println(NAME + " is loading!");

        Class<? extends CustomOrechidSubtile> subclassedCustomOrechid = MorechidClassBuilder.generateMorechid("subclassedCustomOrechid");

        BotaniaAPI.registerSubTile("morechid", subclassedCustomOrechid);
        BotaniaAPI.addSubTileToCreativeMenu("morechid");
        BotaniaAPIClient.registerSubtileModel(subclassedCustomOrechid, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ORECHID));

        Class<? extends CustomOrechidSubtile> subclassedCustomOrechid2 = MorechidClassBuilder.generateMorechid("subclassedCustomOrechid2");

        BotaniaAPI.registerSubTile("morechid2", subclassedCustomOrechid2);
        BotaniaAPI.addSubTileToCreativeMenu("morechid2");
        BotaniaAPIClient.registerSubtileModel(subclassedCustomOrechid2, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ORECHID_IGNEM));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
