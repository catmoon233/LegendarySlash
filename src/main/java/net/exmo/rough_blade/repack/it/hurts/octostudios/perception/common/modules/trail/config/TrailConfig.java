package net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config;



import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.data.TrailConfigData;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;


public class TrailConfig  {
    public static Map<String, TrailConfigData> entityTrails = new HashMap<>() {{
        put("rough_blade:starry_sky_slash", new TrailConfigData(2.5F, 20, 0.005F, 1, "#FF0088FF", "#800000FF", new Vector3f(0.4F,  -0.6F, -0.6F), 0.15F, 0.15F));
        put("rough_blade:fallen_star_entity", new TrailConfigData(0.3F, 20, 0.005F, 1, "#FF0088FF", "#800000FF", new Vector3f(0F,  0F, 0F), 0.2F, 0.2F));
        put("rough_blade:track_tail_entity", new TrailConfigData(0.15F, 20, 0.005F, 1, "#FF0088FF", "#800000FF", new Vector3f(0F,  0F, 0F), 0.3F, 0.2F));

    }};


    private Map<String, TrailConfigData> particleTrails = new HashMap<>();
}