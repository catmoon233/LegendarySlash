package net.exmo.rough_blade.events;

import net.exmo.rough_blade.content.CustomConfig;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LoadCustomArtsCostsEvent extends Event {
    public  CustomConfig.addFunction addFunction;
    public LoadCustomArtsCostsEvent(CustomConfig.addFunction addFunction){
        this.addFunction = addFunction;
    }
    public  CustomConfig.addFunction getAddFunction(){
        return this.addFunction;
    }

}
