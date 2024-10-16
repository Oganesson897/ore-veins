package com.alcatrazescapee.oreveins.api;

import java.util.function.BiPredicate;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;

@ParametersAreNonnullByDefault
@ZenClass("mods.oreveins.condition.Condition")
@ZenRegister
public interface ICondition extends BiPredicate<World, BlockPos>
{
    @Override
    boolean test(World world, BlockPos pos);

    interface Factory<T extends ICondition>
    {
        @Nonnull
        T parse(JsonObject json, JsonDeserializationContext context);
    }
}
