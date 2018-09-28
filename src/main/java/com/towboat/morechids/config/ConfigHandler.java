package com.towboat.morechids.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.towboat.morechids.tweaker.MorechidDefinition;
import com.towboat.morechids.tweaker.MorechidRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

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
                    readMorechidObject(def, reader);
                }
                reader.endObject();
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            // Uh what?
        }
    }

    public static final String[] settingFields = new String[]{
            "manaCost",
            "maxMana",
            "timeCost",
            "cooldown",
            "range",
            "rangeY",
            "particleColor",
            "playSound",
            "blockBreakParticles",
            "rangeCheckInterval"
    };

    public static void readMorechidObject(MorechidDefinition def, JsonReader reader) throws IOException {
        HashMap<String, Object> dict = objectToDict(reader);
        validateAndAssignSettings(def, dict, false);
        validateAndAssignSettings(def, dict, true);
    }

    public static HashMap<String, Object> objectToDict(JsonReader reader) throws IOException {
        HashMap<String, Object> dict = new HashMap<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            JsonToken token = reader.peek();
            switch (token){
                case BOOLEAN:
                    dict.put(name, reader.nextBoolean());
                    break;
                case NUMBER:
                    dict.put(name, reader.nextDouble());
                    break;
                case STRING:
                    dict.put(name, reader.nextString());
                    break;
                case NULL:
                    dict.put(name, null);
                    reader.nextNull();
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        return dict;
    }

    public static void validateAndAssignSettings(MorechidDefinition def, HashMap<String, Object> dict, boolean gog) {
        String suffix = gog ? "GOG" : "";

        for (String fname : settingFields) {
            boolean valid = true;
            Object val;
            String fieldName = fname + suffix;

            if (dict.containsKey(fieldName)) {
                val = dict.get(fieldName);
            } else {
                continue;
            }

            try {
                Field field = MorechidDefinition.class.getDeclaredField(fieldName);

                switch (fname) {
                    case "manaCost":
                    case "timeCost":
                    case "cooldown":
                    case "range":
                    case "rangeY":
                        if (val instanceof Double) {
                            // Check for positive integer
                            double v = (Double) val;
                            if ( v < 0 || v != ((Double)val).intValue()) {
                                valid = false;
                            } else {
                                val = (int) v;
                            }
                        } else {
                            valid = false;
                        }
                        break;
                    case "maxMana":
                        if (val instanceof Double) {
                            double v = (Double) val;
                            if ( v < MorechidDefinition.class.getDeclaredField("manaCost"+suffix).getInt(def) || v != ((Double)val).intValue()) {
                                valid = false;
                            } else {
                                val = (int) v;
                            }
                        } else {
                            valid = false;
                        }
                        break;
                    case "particleColor":
                        if (val instanceof String) {
                            int color = Integer.parseInt((String)val, 16);
                            val = color;
                        } else {
                            valid = false;
                        }
                        break;
                    case "blockBreakParticles":
                    case "playSound":
                        if (!(val instanceof Boolean)) {
                            valid = false;
                        }
                        break;
                    case "rangeCheckInterval":
                        if (val instanceof Double) {
                            double v = (Double) val;
                            if (v < 1 || v > MorechidDefinition.class.getDeclaredField("cooldown"+suffix).getInt(def) || v != ((Double)val).intValue()) {
                                valid = false;
                            } else {
                                val = (int) v;
                            }
                        } else {
                            valid = false;
                        }
                        break;
                    default:
                        valid = false;
                }
                if (!valid) {
                    throw new Exception("Invalid value " + val + " specified for field " + fieldName + ".");
                }
                if (dict.containsKey(fieldName) && dict.get(fieldName) != null) {
                    field.set(def, val);
                    if (!gog) {
                        MorechidDefinition.class.getDeclaredField(fieldName+"GOG").set(def, val);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
