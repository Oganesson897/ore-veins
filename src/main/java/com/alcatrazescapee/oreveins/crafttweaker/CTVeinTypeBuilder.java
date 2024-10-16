package com.alcatrazescapee.oreveins.crafttweaker;

import com.alcatrazescapee.oreveins.api.AbstractVeinType;
import com.alcatrazescapee.oreveins.api.ICondition;
import com.alcatrazescapee.oreveins.util.IWeightedList;
import com.alcatrazescapee.oreveins.util.WeightedList;
import com.alcatrazescapee.oreveins.vein.VeinRegistry;
import com.alcatrazescapee.oreveins.vein.VeinTypeCluster;
import com.alcatrazescapee.oreveins.vein.VeinTypeCone;
import com.alcatrazescapee.oreveins.vein.VeinTypeCurve;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.api.world.IBiome;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.oreveins.VeinType")
@ZenRegister
public class CTVeinTypeBuilder<T extends AbstractVeinType<?>> {

    public final String name;
    public T type;

    private IBlockState stoneState = null;
    private final IWeightedList<IBlockState> oreStates = new WeightedList<>();

    public CTVeinTypeBuilder(String name, T type) {
        this.name = name;
        this.type = type;
    }

    /*
     * Optional Options
     */

    @ZenMethod
    public CTVeinTypeBuilder<T> setCount(int count) {
        type.count = count;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setRarity(int rarity) {
        type.rarity = rarity;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setMinY(int minY) {
        type.minY = minY;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setMaxY(int maxY) {
        type.maxY = maxY;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> useRelativeY(boolean useRelativeY) {
        type.useRelativeY = useRelativeY;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setDensity(float density) {
        type.density = density;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setVerticalSize(int verticalSize) {
        type.verticalSize = verticalSize;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setHorizontalSize(int horizontalSize) {
        type.horizontalSize = horizontalSize;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> addBiome(IBiome biome) {
        type.biomes.add(CraftTweakerMC.getBiome(biome).getBiomeName());
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setBiomesIsWhitelist(boolean whitelist) {
        type.biomesIsWhitelist = whitelist;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> addDimension(int dim) {
        type.dimensions.add(dim);
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setDimensionsIsWhitelist(boolean whitelist) {
        type.dimensionIsWhitelist = whitelist;
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> addIndicator(CTIndicator indicator, @Optional(valueDouble = 1.0) double weight) {
        type.indicator.add(weight, indicator);
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> addCondition(ICondition condition) {
        type.conditions.add(condition);
        return this;
    }

    /*
     * Extra Options
     */

    @ZenMethod
    public CTVeinTypeBuilder<T> setCluster(int cluster) {
        if (type instanceof VeinTypeCluster) {
            VeinTypeCluster t1 = (VeinTypeCluster) type;
            t1.clusters = cluster;
        }
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setInverted(boolean inverted) {
        if (type instanceof VeinTypeCone) {
            VeinTypeCone t1 = (VeinTypeCone) type;
            t1.inverted = inverted;
        }
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setShape(float shape) {
        if (type instanceof VeinTypeCone) {
            VeinTypeCone t1 = (VeinTypeCone) type;
            t1.shape = shape;
        }
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setRadius(float radius) {
        if (type instanceof VeinTypeCurve) {
            VeinTypeCurve t1 = (VeinTypeCurve) type;
            t1.radius = radius;
        }
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> setAngle(float angle) {
        if (type instanceof VeinTypeCurve) {
            VeinTypeCurve t1 = (VeinTypeCurve) type;
            t1.angle = angle;
        }
        return this;
    }

    /*
     * Important Options
     */

    @ZenMethod
    public CTVeinTypeBuilder<T> setStone(crafttweaker.api.block.IBlockState state) {
        stoneState = CraftTweakerMC.getBlockState(state);
        return this;
    }

    private void addOre(IBlockState state, double weight) {
        oreStates.add(weight, state);
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> addOre(crafttweaker.api.block.IBlockState state, @Optional(valueDouble = 1.0) double weight) {
        addOre(CraftTweakerMC.getBlockState(state), weight);
        return this;
    }

    @ZenMethod
    public CTVeinTypeBuilder<T> addOre(IOreDictEntry oreDict, @Optional(valueDouble = 1.0) double weight) {
        ItemStack stack = CraftTweakerMC.getItemStack(oreDict.getFirstItem());
        Block block = Block.getBlockFromItem(stack.getItem());
        addOre(block.getStateFromMeta(stack.getMetadata()), weight);
        return this;
    }

    @ZenMethod
    public void build() {
        type.stoneStates.add(stoneState);
        type.oreStates.add(oreStates);
        VeinRegistry.VEINS.put(name, type);
    }

}
