package com.autostripmine.client.mixin;

import com.autostripmine.client.StripMineController;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "applyInput", at = @At("TAIL"))
    private void onApplyInput(CallbackInfo ci) {
        if (StripMineController.ACTIVE) {
            LocalPlayer self = (LocalPlayer) (Object) this;
            var rng = ThreadLocalRandom.current();
            self.xxa = (float)(rng.nextDouble() - 0.5) * 0.04f;
            self.zza = 0.98f + (float)rng.nextDouble() * 0.04f;
        }
    }
}
