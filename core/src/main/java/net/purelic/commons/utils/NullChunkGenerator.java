package net.purelic.commons.utils;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class NullChunkGenerator extends ChunkGenerator {

    @Override
    @Nonnull
    public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biome) {
        return createChunkData(world);
    }

    @SuppressWarnings("deprecation")
    public byte[] generate(World world, Random random, int x, int z) {
        return new byte[65536];
    }

}
