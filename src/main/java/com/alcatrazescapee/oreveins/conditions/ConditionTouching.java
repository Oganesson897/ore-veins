package com.alcatrazescapee.oreveins.conditions;

import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.alcatrazescapee.oreveins.util.IPredicate;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.alcatrazescapee.oreveins.api.ICondition;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenConstructor;

@ParametersAreNonnullByDefault
@ZenClass("mods.oreveins.condition.ConditionTouching")
public class ConditionTouching implements ICondition
{
    public Predicate<IBlockState> blockMatcher;
    public int minMatches, maxMatches;

    private ConditionTouching(Predicate<IBlockState> blockMatcher, int minMatches, int maxMatches)
    {
        this.blockMatcher = blockMatcher;
        this.minMatches = minMatches;
        this.maxMatches = maxMatches;
    }

    @ZenConstructor
    public ConditionTouching(crafttweaker.api.block.IBlockState blockState, @Optional(valueLong = 1) int minMatches, @Optional(valueLong = Integer.MAX_VALUE) int maxMatches)
    {
        this.blockMatcher = (state) -> state.equals(CraftTweakerMC.getBlockState(blockState));
        this.minMatches = minMatches;
        this.maxMatches = maxMatches;
    }

    @Override
    public boolean test(World world, BlockPos pos)
    {
        int matchCount = 0;
        for (EnumFacing face : EnumFacing.values())
        {
            if (blockMatcher.test(world.getBlockState(pos.offset(face))))
            {
                matchCount++;
            }
            if (minMatches <= matchCount && matchCount <= maxMatches)
            {
                return true;
            }
        }
        return false;
    }

    public static final class Factory implements ICondition.Factory<ConditionTouching>
    {
        @Override
        @Nonnull
        public ConditionTouching parse(JsonObject json, JsonDeserializationContext context)
        {
            IBlockState stateToMatch = context.deserialize(json.get("block"), IBlockState.class);
            Predicate<IBlockState> blockMatcher = state -> state == stateToMatch;
            int min = JsonUtils.getInt(json, "min", 1);
            int max = JsonUtils.getInt(json, "max", 8);
            return new ConditionTouching(blockMatcher, min, max);
        }
    }
}
