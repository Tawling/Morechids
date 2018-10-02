package com.towboat.morechids.tweaker;

import com.towboat.morechids.Morechids;
import com.towboat.morechids.asm.MorechidClassBuilder;
import com.towboat.morechids.block.subtile.CustomOrechidSubtile;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;

import java.io.*;
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
        morechids.put(identifier.toLowerCase(), def);

        Class<? extends CustomOrechidSubtile> flowerClass = MorechidClassBuilder.generateMorechid(def.getIdentifier());

        def.setMorechidClass(flowerClass);

        BotaniaAPI.registerSubTile(def.getIdentifier(), flowerClass);
        BotaniaAPI.addSubTileToCreativeMenu(def.getIdentifier());

        BotaniaAPIClient.registerSubtileModel(flowerClass, new ModelResourceLocation("morechids:" + identifier));
        try {
            BotaniaAPI.registerSubTileSignature(flowerClass, flowerClass.newInstance());
        }catch(Exception e){

        }

        ResourceInitializer.initializeResource(identifier);

        return def;
    }



    @ZenMethod
    public static MorechidDefinition getFlower(String identifier) {
        return morechids.get(identifier.toLowerCase());
    }


}

class ResourceInitializer {
    public static void initializeResource(String identifier) {
        identifier = identifier.toLowerCase();
        File folder = new File(Morechids.proxy.getDataDirectory(), "resources");
        if (!folder.exists()){
            folder.mkdir();
        }
        File main = new File(folder, "morechids");
        if (!main.exists()) {
            main.mkdir();
        }
        File blockstates = new File(main, "blockstates");
        if (!blockstates.exists()) {
            blockstates.mkdirs();
        }
        File textures = new File(main,"textures");
        if (!textures.exists()) {
            textures.mkdirs();
        }

        File json = new File(blockstates, identifier + ".json");
        if (!json.exists()) {
            try {
                FileWriter w = new FileWriter(json);
                w.write("{\n" +
                        "  \"forge_marker\": 1,\n" +
                        "  \"variants\": {\n" +
                        "    \"normal\": [{\n" +
                        "      \"model\": \"botania:shapes/cross_tinted\",\n" +
                        "      \"textures\": {\n" +
                        "        \"cross\": \"morechids:" + identifier + "\"\n" +
                        "      }\n" +
                        "    }],\n" +
                        "    \"inventory\": [{\n" +
                        "      \"model\": \"builtin/generated\",\n" +
                        "      \"transform\": \"forge:default-item\",\n" +
                        "      \"textures\": {\n" +
                        "        \"layer0\": \"morechids:" + identifier + "\"\n" +
                        "      }\n" +
                        "    }]\n" +
                        "  }\n" +
                        "}");
                w.flush();
                w.close();

                File png = new File(textures, identifier + ".png");
                if (!png.exists()) {
                    InputStream is = null;
                    OutputStream os = null;
                    try {
                        is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("morechids", "orechid.png")).getInputStream();
                        os = new FileOutputStream(png);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                    } finally {
                        is.close();
                        os.close();
                    }
                }


            } catch (IOException e) {

            }
        }
    }
}