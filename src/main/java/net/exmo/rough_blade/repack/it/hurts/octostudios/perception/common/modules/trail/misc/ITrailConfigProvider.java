package net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.misc;

import it.hurts.octostudios.octolib.modules.particles.trail.TrailProvider;
import net.exmo.rough_blade.repack.it.hurts.octostudios.perception.common.modules.trail.config.data.TrailConfigData;

public interface ITrailConfigProvider extends TrailProvider {
    TrailConfigData getTrailConfigData();

    void setTrailConfigData(TrailConfigData data);
}