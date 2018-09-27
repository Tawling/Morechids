package com.towboat.morechids.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.towboat.morechids.tweaker.MorechidDefinition;
import com.towboat.morechids.tweaker.MorechidRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * ConfigHandler.java
 * <p>
 * Author:  Taw
 * Date:    9/21/2018
 */
public class ConfigHandler {
    public static void readConfig(File configFile) throws IOException {
        try {
            JsonReader reader = new Gson().newJsonReader(new FileReader(configFile));
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    MorechidDefinition def = MorechidRegistry.createMorechid(name);
                    readMorechidObject(reader, def);
                }
                reader.endObject();
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            // Uh what?
        }
    }

    public static void readMorechidObject(JsonReader reader, MorechidDefinition def) throws IOException {
        reader.beginObject();
        // flags for GOG values to allow overrides.
        boolean mc = false,
                tc = false,
                cd = false,
                r = false,
                ry = false,
                pc = false,
                ps = false,
                rci = false;
        while (reader.hasNext()) {
            String name = reader.nextName();
            System.out.print("Name: ");
            if (name.equals("manaCost")) {
                def.manaCost = reader.nextInt();
                if (!mc) def.manaCostGOG = def.manaCost;
            } else if (name.equals("timeCost")) {
                def.timeCost = reader.nextInt();
                if (!tc) def.timeCostGOG = def.timeCost;
            } else if (name.equals("cooldown")) {
                def.cooldown = reader.nextInt();
                if (!cd) def.cooldownGOG = def.cooldown;
            } else if (name.equals("range")) {
                def.range = reader.nextInt();
                if (!r) def.range = def.rangeGOG;
            } else if (name.equals("rangeY")) {
                def.rangeY = reader.nextInt();
                if (!ry) def.rangeY = def.rangeYGOG;
            } else if (name.equals("particleColor")) {
                def.particleColor = Integer.parseInt(reader.nextString(), 16);
                if (!pc) def.particleColorGOG = def.particleColor;
            } else if (name.equals("playSound")) {
                def.playSound = reader.nextBoolean();
                if (!ps) def.playSoundGOG = def.playSound;
            } else if (name.equals("rangeCheckInterval")) {
                def.rangeCheckInterval = reader.nextInt();
                if (!rci) def.rangeCheckIntervalGOG = def.rangeCheckInterval;
            } else if (name.equals("manaCostGOG")) {
                def.manaCostGOG = reader.nextInt();
                mc = true;
            } else if (name.equals("timeCostGOG")) {
                def.timeCostGOG = reader.nextInt();
                tc = true;
            } else if (name.equals("delayGOG")) {
                def.cooldownGOG = reader.nextInt();
                cd = true;
            } else if (name.equals("rangeGOG")) {
                def.rangeGOG = reader.nextInt();
                r = true;
            } else if (name.equals("rangeYGOG")) {
                def.rangeYGOG = reader.nextInt();
                ry = true;
            } else if (name.equals("particleColorGOG")) {
                def.particleColorGOG = Integer.parseInt(reader.nextString(), 16);
                pc = true;
            } else if (name.equals("playSoundGOG")) {
                def.playSoundGOG = reader.nextBoolean();
                ps = true;
            } else if (name.equals("rangeCheckIntervalGOG")) {
                def.rangeCheckIntervalGOG = reader.nextInt();
                rci = true;
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }
}
