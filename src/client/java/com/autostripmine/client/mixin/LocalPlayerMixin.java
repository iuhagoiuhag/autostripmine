package com.autostripmine.client.mixin;

import com.autostripmine.client.StripMineController;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "applyInput", at = @At("TAIL"))
    private void onApplyInput(CallbackInfo ci) {
        if (StripMineController.ACTIVE) {
            LocalPlayer self = (LocalPlayer) (Object) this;
            self.xxa = 0.0f;
            self.zza = 1.0f;
        }
    }
}
