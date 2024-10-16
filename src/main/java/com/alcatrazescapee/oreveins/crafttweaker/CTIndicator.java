package com.alcatrazescapee.oreveins.crafttweaker;

import com.alcatrazescapee.oreveins.vein.Indicator;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.oreveins.Indicator")
@ZenRegister
public class CTIndicator extends Indicator {

    @ZenConstructor
    public CTIndicator(IBlockState state, @Optional(valueDouble = 1.0) double weight) {
        states.add(weight, CraftTweakerMC.getBlockState(state));
    }

    @ZenMethod
    public CTIndicator addBlock(IBlockState state, @Optional(valueDouble = 1.0) double weight) {
        states.add(weight, CraftTweakerMC.getBlockState(state));
        return this;
    }

    @ZenMethod
    public CTIndicator setRarity(int rarity) {
        this.rarity = rarity;
        return this;
    }

    @ZenMethod
    public CTIndicator setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    @ZenMethod
    public CTIndicator setIgnoreVegetation(boolean ignore) {
        this.ignoreVegetation = ignore;
        return this;
    }

    @ZenMethod
    public CTIndicator setIgnoreLiquids(boolean ignore) {
        this.ignoreLiquids = ignore;
        return this;
    }

    @ZenMethod
    public CTIndicator addBlocksUnder(IBlockState state) {
        underStates.add(CraftTweakerMC.getBlockState(state));
        return this;
    }

}
