package org.steve.hockeyware.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.steve.hockeyware.events.player.MoveEvent;
import org.steve.hockeyware.events.player.UpdateWalkingPlayerEvent;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Shadow
    private double lastReportedPosX;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private boolean prevOnGround;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    private boolean autoJumpEnabled;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalkingPlayerPre(CallbackInfo info) {
        UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(posX, posY, posZ, onGround, rotationYaw, rotationPitch);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            info.cancel();
            modifiedOnUpdateWalkingPlayer(event.getX(), event.getY(), event.getZ(), event.isOnGround(), event.getYaw(), event.getPitch());
        }
    }

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    private void modifiedOnUpdateWalkingPlayer(double x, double y, double z, boolean grounded, float yaw, float pitch) {
        if (isSprinting() != serverSprintState) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, isSprinting() ? CPacketEntityAction.Action.START_SPRINTING : CPacketEntityAction.Action.STOP_SPRINTING));
            serverSprintState = isSprinting();
        }

        if (isSneaking() != serverSneakState) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, isSneaking() ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
            serverSneakState = isSneaking();
        }

        if (isCurrentViewEntity()) {
            ++positionUpdateTicks;

            boolean moved = Math.pow(x - lastReportedPosX, 2) + Math.pow(y - lastReportedPosY, 2) + Math.pow(z - lastReportedPosZ, 2) > 9.0E-4D || positionUpdateTicks >= 20;
            boolean rotated = yaw - lastReportedYaw != 0.0f || pitch - lastReportedPitch != 0.0f;

            if (isRiding()) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.PositionRotation(motionX, -999.0, motionZ, yaw, pitch, grounded));
                moved = false;
            }

            if (moved && rotated) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.PositionRotation(x, y, z, yaw, pitch, grounded));
            } else if (moved) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, grounded));
            } else if (rotated) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, grounded));
            } else if (prevOnGround != grounded) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(grounded));
            }

            if (moved) {
                lastReportedPosX = x;
                lastReportedPosY = y;
                lastReportedPosZ = z;
                positionUpdateTicks = 0;
            }

            if (rotated) {
                lastReportedYaw = yaw;
                lastReportedPitch = pitch;
            }

            prevOnGround = grounded;
            autoJumpEnabled = Minecraft.getMinecraft().gameSettings.autoJump;
        }
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(AbstractClientPlayer player, MoverType moverType, double x, double y, double z) {
        MoveEvent event = new MoveEvent(moverType, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);

        if (!event.isCanceled() || event.stillMove()) {
            super.move(moverType, event.getX(), event.getY(), event.getZ());
        }
    }
}
