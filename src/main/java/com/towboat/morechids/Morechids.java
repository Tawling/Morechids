package com.towboat.morechids;

import com.towboat.morechids.config.ConfigHandler;
import com.towboat.morechids.proxy.CommonProxy;
import com.towboat.morechids.tweaker.BlockOutput;
import com.towboat.morechids.tweaker.BlockOutputMapping;
import com.towboat.morechids.tweaker.MorechidDefinition;
import com.towboat.morechids.tweaker.MorechidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Mod(modid = Morechids.MODID, name = Morechids.NAME, version = Morechids.VERSION, dependencies="required-after:crafttweaker;required-after:botania;required-after:mtlib;")
public class Morechids {
    public static final String MODID = "morechids";
    public static final String NAME = "Morechids";
    public static final String VERSION = "1.0.1";

    @Mod.Instance(MODID)
    public static Morechids instance;

    @SidedProxy(serverSide = "com.towboat.morechids.proxy.CommonProxy", clientSide = "com.towboat.morechids.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Registering Morechids");
        File configFile = new File(event.getSuggestedConfigurationFile().getPath().replace(".cfg", ".json"));
        if (configFile.exists()) {
            try {
                ConfigHandler.readConfig(configFile);
            } catch (IOException e) {
                System.out.println("Something is wrong with the morechids.json config file. Some flowers may not be registered.");
            }
        } else {
            try {
                configFile.createNewFile();
            } catch(IOException e) {
                System.out.println("An error occured when creating config file for morechids");
            }
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // prune empty recipes and mappings.
        MorechidRegistry.morechids.forEach((identifier, def) -> {
            ArrayList<Object> removals = new ArrayList<>();
            def.recipes.forEach((state, outputs) -> {
                if (outputs.isEmpty()) {
                    removals.add(state);
                } else {
                    for (int i = outputs.size()-1; i >= 0; i--) {
                        if (outputs.get(i).isEmpty()) {
                            outputs.remove(i);
                        }
                    }
                }
            });
            for (Object o: removals) {
                def.recipes.remove(o);
            }
        });
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
