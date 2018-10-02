package com.towboat.morechids.integration.jei;

import com.towboat.morechids.tweaker.MorechidDefinition;
import com.towboat.morechids.tweaker.MorechidRecipe;
import com.towboat.morechids.tweaker.MorechidRegistry;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JEIMorechidsPlugin.java
 * <p>
 * Author:  Taw
 * Date:    9/30/2018
 */
@JEIPlugin
public class JEIMorechidsPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        //TODO: for each defined Morechid, add as a category
        MorechidRegistry.morechids.forEach((s, def) -> {
             registry.addRecipeCategories(new MorechidRecipeCategory(registry.getJeiHelpers().getGuiHelper(), def));
        });
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        MorechidRegistry.morechids.forEach((s, def) -> {
            ArrayList<MorechidRecipeWrapper> wrappers = new ArrayList<>();
            def.recipes.values().forEach((recipe) -> {
                recipe.entrySet().forEach((entry) -> {
                    wrappers.add(new MorechidRecipeWrapper(def.getIdentifier(), recipe, entry));
                });
            });
            registry.addRecipes(wrappers.stream().sorted().collect(Collectors.toList()), MorechidRecipeCategory.getUidForDefinition(def));
            registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(def.getIdentifier()), MorechidRecipeCategory.getUidForDefinition(def));
            registry.addRecipeCatalyst(ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), def.getIdentifier()), MorechidRecipeCategory.getUidForDefinition(def));
        });
    }
}
