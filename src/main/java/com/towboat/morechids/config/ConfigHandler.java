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
        while (reader.hasNext()) {
            String name = reader.nextName();
            System.out.print("Name: ");
            if (name.equals("manaCost")) {
                def.manaCost = reader.nextInt();
            } else if (name.equals("timeCost")) {
                def.timeCost = reader.nextInt();
            } else if (name.equals("delay")) {
                def.delay = reader.nextInt();
            } else if (name.equals("range")) {
                def.range = reader.nextInt();
            } else if (name.equals("rangeY")) {
                def.rangeY = reader.nextInt();
            } else if (name.equals("particleColor")) {
                def.particleColor = Integer.parseInt(reader.nextString(), 16);
            } else if (name.equals("playSound")) {
                def.playSound = reader.nextBoolean();
            }
        }
        reader.endObject();
    }
}
