package com.alcatrazescapee.oreveins.vein;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import com.alcatrazescapee.oreveins.api.AbstractVein;
import com.alcatrazescapee.oreveins.api.AbstractVeinType;

@SuppressWarnings({"unused", "WeakerAccess"})
@ParametersAreNonnullByDefault
public class VeinTypeCurve extends AbstractVeinType<VeinTypeCurve.VeinCurve>
{
    float radius = 5;
    float angle = 45;

    @Override
    public boolean inRange(VeinCurve vein, int xOffset, int zOffset)
    {
        return (xOffset < horizontalSize * vein.getSize()) && (zOffset < horizontalSize * vein.getSize());
    }

    @Override
    public float getChanceToGenerate(VeinCurve vein, BlockPos pos)
    {
        for (CurveSegment segment : vein.getSegmentList())
        {
            Vec3d blockPos = new Vec3d(pos);
            Vec3d centeredPos = blockPos.subtract(segment.begin);

            // rotate block pos around Y axis
            double yaw = segment.yaw;
            Vec3d posX = new Vec3d(Math.cos(yaw) * centeredPos.x + Math.sin(yaw) * centeredPos.z,
                    centeredPos.y,
                    -Math.sin(yaw) * centeredPos.x + Math.cos(yaw) * centeredPos.z);

            // rotate block pos around Z axis
            double pitch = segment.pitch;
            Vec3d posY = new Vec3d(Math.cos(pitch) * posX.x - Math.sin(pitch) * posX.y,
                    Math.sin(pitch) * posX.x + Math.cos(pitch) * posX.y,
                    posX.z);

            double rad = Math.sqrt(posY.x * posY.x + posY.z * posY.z);
            double length = segment.length;

            if (((posY.y >= 0 && posY.y <= length) || (posY.y < 0 && posY.y >= length)) && rad < this.radius)
            {
                return 0.005f * density * (1f - 0.9f * (float) rad / this.radius);
            }
        }

        return 0.0f;
    }

    @Nonnull
    @Override
    public VeinCurve createVein(int chunkX, int chunkZ, Random rand)
    {
        int maxOffY = getMaxY() - getMinY() - verticalSize;
        int posY = getMinY() + verticalSize / 2 + ((maxOffY > 0) ? rand.nextInt(maxOffY) : 0);

        BlockPos pos = new BlockPos(
                chunkX * 16 + rand.nextInt(16),
                posY,
                chunkZ * 16 + rand.nextInt(16)
        );

        return new VeinCurve(this, pos, rand);
    }

    static class VeinCurve extends AbstractVein<VeinTypeCurve>
    {
        private final Random rand;
        private boolean isInitialized = false;
        private List<CurveSegment> segmentList;

        VeinCurve(VeinTypeCurve type, BlockPos pos, Random random)
        {
            super(type, pos, 0.5f * (1.0f + random.nextFloat()));
            this.rand = new Random(random.nextLong());
            this.segmentList = new ArrayList<>();
        }

        @Override
        public boolean inRange(int x, int z)
        {
            return getType().inRange(this, getPos().getX() - x, getPos().getZ() - z);
        }

        @Override
        public double getChanceToGenerate(@Nonnull BlockPos pos)
        {
            if (!isInitialized)
            {
                initialize(getType().horizontalSize, getType().verticalSize, getType().angle);
            }
            return getType().getChanceToGenerate(this, pos);
        }

        List<CurveSegment> getSegmentList()
        {
            return segmentList;
        }

        private Vec3d getRandomPointInCuboid(Random rand, Vec3d bottomLeft, Vec3d topRight)
        {
            final double x = bottomLeft.x + (topRight.x - bottomLeft.x) * rand.nextDouble();
            final double y = bottomLeft.y + (topRight.y - bottomLeft.y) * rand.nextDouble();
            final double z = bottomLeft.z + (topRight.z - bottomLeft.z) * rand.nextDouble();

            return new Vec3d(x, y, z);
        }

        private void initialize(int hSize, int vSize, float angle)
        {
            double kxy = Math.tan(angle * (1.0f - 2.0f * rand.nextFloat()));
            double kyz = Math.tan(angle * (1.0f - 2.0f * rand.nextFloat()));

            final double h2Size = hSize * size / 2d;
            final double v2Size = vSize / 2d;

            // four points for cubic Bezier curve
            // p1 and p4 placed on (hSize; hSize; vSize) box with center in vein position
            Vec3d p1, p2, p3, p4;
            double x1, y1, z1, x2, y2, z2;

            if (v2Size >= h2Size * Math.abs(kyz))
            {
                z1 = -h2Size;
                y1 = h2Size * kyz;
            }
            else
            {
                z1 = -v2Size * Math.abs(kyz);
                y1 = v2Size * Math.signum(kyz);
            }

            x1 = (1 >= Math.abs(kxy)) ? h2Size : h2Size * kxy;

            x2 = -x1;
            y2 = -y1;
            z2 = -z1;

            p1 = new Vec3d(x1 + getPos().getX(), y1 + getPos().getY(), z1 + getPos().getZ());
            p4 = new Vec3d(x2 + getPos().getX(), y2 + getPos().getY(), z2 + getPos().getZ());

            Vec3d bottomLeft = new Vec3d(Math.min(p1.x, p4.x), Math.min(p1.y, p4.y), Math.min(p1.z, p4.z));
            Vec3d topRight = new Vec3d(Math.max(p1.x, p4.x), Math.max(p1.y, p4.y), Math.max(p1.z, p4.z));

            p2 = getRandomPointInCuboid(rand, bottomLeft, topRight);
            p3 = getRandomPointInCuboid(rand, bottomLeft, topRight);

            // curve segmentation setup
            double step = 5.0 / h2Size;
            double t = 0.0;
            Vec3d pb, pe = new Vec3d(0.0, 0.0, 0.0);

            // curve segmentation
            while (t < 1.0)
            {
                pb = (t == 0.0) ? p1 : pe;

                t += step;
                if (t > 1.0) t = 1.0;

                double t11 = 1 - t;
                double t12 = t11 * t11;
                double t13 = t12 * t11;

                double t31 = 3 * t;
                double t32 = 3 * t * t;
                double t3 = t * t * t;

                pe = p1.scale(t13).add(p2.scale(t31 * t12).add(p3.scale(t32 * t11).add(p4.scale(t3))));

                Vec3d axis = pe.subtract(pb);

                // align segment axis with axis X
                double yaw = Math.atan(axis.z / axis.x);
                Vec3d axisX = new Vec3d(Math.cos(yaw) * axis.x + Math.sin(yaw) * axis.z,
                        axis.y,
                        -Math.sin(yaw) * axis.x + Math.cos(yaw) * axis.z);

                // align segment axis with axis Y
                double pitch = Math.atan(axisX.x / axisX.y);
                Vec3d axisY = new Vec3d(Math.cos(pitch) * axisX.x - Math.sin(pitch) * axisX.y,
                        Math.sin(pitch) * axisX.x + Math.cos(pitch) * axisX.y,
                        axisX.z);

                segmentList.add(new CurveSegment(pb, axisY.y, yaw, pitch));
            }

            isInitialized = true;
        }
    }

    private static class CurveSegment
    {
        final Vec3d begin;
        final double length;
        final double yaw;
        final double pitch;

        CurveSegment(Vec3d begin, double length, double yaw, double pitch)
        {
            this.begin = begin;
            this.length = length;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }
}
