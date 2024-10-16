/*
 * Part of the Ore Veins Mod by alcatrazEscapee
 * Work under Copyright. Licensed under the GPL-3.0.
 * See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.oreveins.vein;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;
import net.minecraft.block.state.IBlockState;

import com.alcatrazescapee.oreveins.util.IWeightedList;

@SuppressWarnings({"FieldCanBeLocal"})
public class Indicator
{
    @SerializedName("max_depth")
    public int maxDepth = 32;
    public int rarity = 10;
    @SerializedName("ignore_vegetation")
    public boolean ignoreVegetation = true;
    @SerializedName("ignore_liquids")
    public boolean ignoreLiquids = false;

    @SerializedName("blocks")
    public IWeightedList<IBlockState> states = null;
    @SerializedName("blocks_under")
    public List<IBlockState> underStates = null;

    @Nonnull
    public IBlockState getStateToGenerate(Random random)
    {
        return states.get(random);
    }

    public boolean validUnderState(IBlockState state)
    {
        return underStates == null || underStates.contains(state);
    }

    public int getMaxDepth()
    {
        return maxDepth;
    }

    public int getRarity()
    {
        return rarity;
    }

    public boolean shouldIgnoreVegetation()
    {
        return ignoreVegetation;
    }

    public boolean shouldIgnoreLiquids()
    {
        return ignoreLiquids;
    }

    public boolean isValid()
    {
        return states != null && !states.isEmpty() &&
                maxDepth > 0 && rarity > 0;
    }
}
