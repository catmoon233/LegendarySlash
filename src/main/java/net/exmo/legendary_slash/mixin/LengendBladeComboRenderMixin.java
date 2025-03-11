package net.exmo.legendary_slash.mixin;

import com.dinzeer.legendblade.client.screen.ComboScreen;
import net.exmo.legendary_slash.Legendary_slash;
import net.minecraftforge.client.event.RenderGuiEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComboScreen.class)
public abstract class LengendBladeComboRenderMixin {
    @Inject(at = @At("HEAD"), method = "eventHandler", cancellable = true ,remap = false)
    private static void eventHandler(RenderGuiEvent.Pre event, CallbackInfo ci) {
       ci.cancel();
    }

}
