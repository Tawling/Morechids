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

import com.towboat.morechids.tweaker.BlockOutputMapping;
import com.towboat.morechids.tweaker.MorechidDefinition;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.api.subtile.signature.SubTileSignature;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.ArrayList;
import java.util.List;

public class CustomOrechidSubtile extends SubTileFunctional implements SubTileSignature {

    public MorechidDefinition definition;
    public String name;

    public CustomOrechidSubtile() {
        super();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(supertile.getWorld().isRemote || redstoneSignal > 0 || !canOperate())
            return;

        int cost = getCost();
        if(mana >= cost && ticksExisted % getDelay() == 0) {
            BlockPos coords = getCoordsToPut();
            if(coords != null) {
                ItemStack stack = getOreToPut(supertile.getWorld().getBlockState(coords));
                if(!stack.isEmpty()) {
                    Block block = Block.getBlockFromItem(stack.getItem());
                    int meta = stack.getItemDamage();
                    supertile.getWorld().setBlockState(coords, block.getStateFromMeta(meta), 1 | 2);
                    if(ConfigHandler.blockBreakParticles)
                        supertile.getWorld().playEvent(2001, coords, Block.getIdFromBlock(block) + (meta << 12));
                    if (definition.playSound) {
                        supertile.getWorld().playSound(null, supertile.getPos(), ModSounds.orechid, SoundCategory.BLOCKS, 2F, 1F);
                    }

                    mana -= cost;
                    sync();
                }
            }
        }
    }

    public ItemStack getOreToPut(IBlockState block) {
        BlockOutputMapping mapping = definition.recipes.get(block);
        if (mapping == null) {
            mapping = definition.recipes.get(block.getBlock());
            if (mapping == null) {
                return ItemStack.EMPTY;
            }
        }

        IBlockState state = mapping.selectBlock();
        if (state == null) {
            return ItemStack.EMPTY;
        }
        Block b = state.getBlock();
        if (b == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(state.getBlock());
    }

    private BlockPos getCoordsToPut() {
        List<BlockPos> possibleCoords = new ArrayList<>();

        int rangeX = getRange();
        int rangeY = getRangeY();

        for(BlockPos pos : BlockPos.getAllInBox(getPos().add(-rangeX, -rangeY, -rangeX), getPos().add(rangeX, rangeY, rangeX))) {
            IBlockState state = supertile.getWorld().getBlockState(pos);
            if(definition.matches(state))
                possibleCoords.add(pos);
        }

        if(possibleCoords.isEmpty())
            return null;
        return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
    }

    public boolean canOperate() {
        return true;
    }

    public int getCost() {
        return definition.manaCost;
    }

    public int getDelay() {
        return definition.delay;
    }

    public int getRange() {
        return definition.range;
    }

    public int getRangeY() {
        return definition.rangeY;
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
        return definition.particleColor;
    }

    @Override
    public int getMaxMana() {
        return getCost();
    }

    @Override
    public LexiconEntry getEntry() {
        return LexiconData.orechid;
    }

    public String getIdentifier() {
        return definition.getIdentifier();
    }
    @Override
    public String getUnlocalizedNameForStack(ItemStack itemStack) {
        return "morechids:morechid." + getIdentifier();
    }

    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack itemStack) {
        return "morechids:morechid." + getIdentifier() + ".reference";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(ItemStack stack, World world, List<String> tooltip) {
        if(getCost() == 0)
            tooltip.add(TextFormatting.BLUE + I18n.translateToLocal("botania.flowerType.special"));
        else
            tooltip.add(TextFormatting.BLUE + I18n.translateToLocal("botania.flowerType.functional"));
    }
}