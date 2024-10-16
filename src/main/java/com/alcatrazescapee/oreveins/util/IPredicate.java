package com.alcatrazescapee.oreveins.util;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.function.Predicate;

@FunctionalInterface
@ZenClass("mods.oreveins.Predicate")
@ZenRegister
public interface IPredicate<T> extends Predicate<T> { }