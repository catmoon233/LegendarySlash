package net.exmo.legendary_slash.mixin;

import mods.flammpfeil.slashblade.registry.combo.ComboCommands;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.exmo.legendary_slash.init.ComboAddCommands;
import net.exmo.legendary_slash.init.ComboStateRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;
import java.util.Map;

@Mixin(ComboCommands.class)
public class ComboCommandsAddMixin {
    @Shadow(remap = false) @Final private static Map<EnumSet<InputCommand>, ResourceLocation> DEAFULT_STANDBY;

    @Inject(method = "initDefaultStandByCommands" ,at = @At("HEAD"), remap = false)
    private static void initDefaultStandByCommands(CallbackInfo ci) {
        DEAFULT_STANDBY.put(
                EnumSet.of(InputCommand.L_DOWN, InputCommand.R_CLICK),
                ComboStateRegistry.ZD.getId());
    }


}
