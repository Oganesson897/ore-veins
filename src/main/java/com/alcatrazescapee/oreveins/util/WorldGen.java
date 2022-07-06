package com.alcatrazescapee.oreveins.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGen {
    public Biome getBiomeInChunk(World world, int chunkX, int chunkZ) {
        return world.getBiomeForCoordsBody(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));
    }
}
