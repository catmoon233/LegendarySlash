package net.exmo.rough_blade.content.shineArt;

import net.exmo.rough_blade.content.shineArt.arts.ShineDashArt;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ShineArtHandle {
    public static Map<ResourceLocation,ShineArt> shineArts = new HashMap<>();
    public static final ShineDashArt SHINE_DASH_ART = (ShineDashArt) new ShineDashArt().build(ResourceLocation.tryParse("rough_blade:shine_dash_art"));
}
