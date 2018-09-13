/**
 * This class is a modification of vazkii.botania.common.block.subtile.functional.SubTileOrechid
 * Modifications were made by Taw
 * Original header included below
 * ***
 *
 *
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 11, 2014, 5:40:55 PM (GMT)]
 */
package com.towboat.morechids.block.subtile;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomOrechidSubtile extends SubTileFunctional {

    private static final int COST = 17500;
    private static final int COST_GOG = 700;
    private static final int DELAY = 100;
    private static final int DELAY_GOG = 2;
    private static final int RANGE = 5;
    private static final int RANGE_Y = 3;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(supertile.getWorld().isRemote || redstoneSignal > 0 || !canOperate())
            return;

        int cost = getCost();
        if(mana >= cost && ticksExisted % getDelay() == 0) {
            BlockPos coords = getCoordsToPut();
            if(coords != null) {
                ItemStack stack = getOreToPut(supertile.getWorld().getBlockState(coords).getBlock());
                if(!stack.isEmpty()) {
                    Block block = Block.getBlockFromItem(stack.getItem());
                    int meta = stack.getItemDamage();
                    supertile.getWorld().setBlockState(coords, block.getStateFromMeta(meta), 1 | 2);
                    if(ConfigHandler.blockBreakParticles)
                        supertile.getWorld().playEvent(2001, coords, Block.getIdFromBlock(block) + (meta << 12));
                    supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.orechid, SoundCategory.BLOCKS, 2F, 1F);

                    mana -= cost;
                    sync();
                }
            }
        }
    }

    public ItemStack getOreToPut(Block block) {
        List<WeightedRandom.Item> values = new ArrayList<>();
        Map<String, Integer> map = getOreMap(block);
        for(String s : map.keySet())
            values.add(new StringRandomItem(map.get(s), s));

        if (values.isEmpty()) {
            return null;
        }

        String ore = ((StringRandomItem) WeightedRandom.getRandomItem(supertile.getWorld().rand, values)).s;

        List<ItemStack> ores = OreDictionary.getOres(ore);

        for(ItemStack stack : ores) {
            Item item = stack.getItem();
            String clname = item.getClass().getName();

            // This poem is dedicated to Greg
            //
            // Greg.
            // I get what you do when
            // others say it's a grind.
            // But take your TE ores
            // and stick them in your behind.
            if(clname.startsWith("gregtech") || clname.startsWith("gregapi"))
                continue;
            if(!(item instanceof ItemBlock))
                continue;

            return stack;
        }

        return getOreToPut(block);
    }

    private BlockPos getCoordsToPut() {
        List<BlockPos> possibleCoords = new ArrayList<>();

        int rangeX = getRange();
        int rangeY = getRangeY();

        for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-rangeX, -rangeY, -rangeX), getPos().add(rangeX, rangeY, rangeX))) {
            IBlockState state = supertile.getWorld().getBlockState(pos);
            if(state.getBlock().isReplaceableOreGen(state, supertile.getWorld(), pos, getReplaceMatcher()))
                possibleCoords.add(pos);
        }

        if(possibleCoords.isEmpty())
            return null;
        return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
    }

    public boolean canOperate() {
        return true;
    }

    public Map<String, Integer> getOreMap(Block block) {
        return BotaniaAPI.oreWeights;
    }

    public Predicate<IBlockState> getReplaceMatcher() {
        return state -> state.getBlock() == Blocks.STONE && state.getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE;
    }

    public int getCost() {
        return Botania.gardenOfGlassLoaded ? COST_GOG : COST;
    }

    public int getDelay() {
        return Botania.gardenOfGlassLoaded ? DELAY_GOG : DELAY;
    }

    public int getRange() {
        return RANGE;
    }

    public int getRangeY() {
        return RANGE_Y;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(toBlockPos(), getRange());
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }

    @Override
    public int getColor() {
        return 0x818181;
    }

    @Override
    public int getMaxMana() {
        return getCost();
    }

    @Override
    public LexiconEntry getEntry() {
        return LexiconData.orechid;
    }

    private static class StringRandomItem extends WeightedRandom.Item {

        public final String s;

        public StringRandomItem(int par1, String s) {
            super(par1);
            this.s = s;
        }

    }
}