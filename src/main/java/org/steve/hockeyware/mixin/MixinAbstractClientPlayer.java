package org.steve.hockeyware.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.steve.hockeyware.HockeyWare;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(value = {AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo getPlayerInfo();

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        UUID uuid = Objects.requireNonNull(getPlayerInfo()).getGameProfile().getId();
        if (HockeyWare.INSTANCE.capeManager.hasCape(uuid)) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/cape.png"));
        }
    }
}