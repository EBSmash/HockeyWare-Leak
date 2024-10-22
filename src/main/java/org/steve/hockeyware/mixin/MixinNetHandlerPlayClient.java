package org.steve.hockeyware.mixin;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.steve.hockeyware.events.player.DeathEvent;
import org.steve.hockeyware.features.Globals;

@Mixin(value = {NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient implements Globals {
    @Inject(method = {"handleEntityMetadata"}, at = {@At(value = "RETURN")})
    private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
        EntityPlayer player;
        Entity entity;
        if (mc.world != null && (entity = mc.world.getEntityByID(packetIn.getEntityId())) instanceof EntityPlayer && (player = (EntityPlayer) entity).getHealth() <= 0.0f) {
            MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
        }
    }
}