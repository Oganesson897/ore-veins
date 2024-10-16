package com.alcatrazescapee.oreveins.crafttweaker;

import com.alcatrazescapee.oreveins.vein.*;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.oreveins.VeinRegistry")
@ZenRegister
public class CTVeinRegistry {

    @ZenMethod
    public static CTVeinTypeBuilder createCluster(String name) {
        return new CTVeinTypeBuilder(name, new VeinTypeCluster());
    }

    @ZenMethod
    public static CTVeinTypeBuilder createCone(String name) {
        return new CTVeinTypeBuilder(name, new VeinTypeCone());
    }

    @ZenMethod
    public static CTVeinTypeBuilder createCurve(String name) {
        return new CTVeinTypeBuilder(name, new VeinTypeCurve());
    }

    @ZenMethod
    public static CTVeinTypeBuilder createPipe(String name) {
        return new CTVeinTypeBuilder(name, new VeinTypePipe());
    }

    @ZenMethod
    public static CTVeinTypeBuilder createSphere(String name) {
        return new CTVeinTypeBuilder(name, new VeinTypeSphere());
    }

}
