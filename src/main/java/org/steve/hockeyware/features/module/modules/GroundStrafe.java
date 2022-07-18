package org.steve.hockeyware.features.module.modules;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.steve.hockeyware.events.player.MoveEvent;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.setting.Setting;
import org.steve.hockeyware.util.client.PlayerUtil;

public class GroundStrafe extends Module {

    public static final Setting<Mode> mode = new Setting<>("Mode", Mode.New);
    public static final Setting<Double> speed = new Setting<>("Speed", 0.4, 0, 5);
    public static final Setting<Boolean> useTimer = new Setting<>("UseTimer", true);
    public static final Setting<Boolean> onGroundOnly = new Setting<>("OnGroundOnly", false);
    public static final Setting<Boolean> noLiquid = new Setting<>("NoLiquid", true);
    public static GroundStrafe INSTANCE;
    double playerSpeed;
    //public static final Setting<Boolean> smartEnable = new Setting<>("EnableOnStairs", false); //TODO: FIX THIS
    private float tickLength = 50.0f;

    public GroundStrafe() {
        super("GroundStrafe", "Zooooooooooom", Category.Movement);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if ((noLiquid.getValue() && mc.player.isInLava() || mc.player.isInWater()) || mc.player.isElytraFlying())
            return;
        if (mode.getValue().equals(Mode.Old)) {
            playerSpeed = speed.getValue();

            playerSpeed *= PlayerUtil.getBaseMoveSpeed() / 0.2873;

            if (mc.player.onGround) {
                double[] dir = PlayerUtil.forward(playerSpeed);
                event.setX(dir[0]);
                event.setZ(dir[1]);
            }
        }
        if (mode.getValue().equals(Mode.New)) {
            if (mc.player.collidedHorizontally || mc.player.movementInput.sneak || mc.player.moveForward == 0) return;

            if (mc.player.onGround || !onGroundOnly.getValue()) {
                if (mc.player.ticksExisted % 2 == 0) {
                    setTicks(1.0f);
                } else {
                    if (useTimer.getValue()) {
                        setTicks(1.2f);
                    } else {
                        setTicks(1.0f);
                    }
                }
                playerSpeed = PlayerUtil.getBaseMoveSpeed();
            }
        } else {
            setTicks(1.0f);
        }

        playerSpeed = Math.max(playerSpeed, PlayerUtil.getBaseMoveSpeed());
        PlayerUtil.setVanilaSpeed(event, playerSpeed);

        if (!useTimer.getValue()) {
            setTicks(1.0f);
        }
    }

    @Override
    public void onDisable() {
        setTicks(1.0f);
    }

    public void onUpdate() {
        mc.timer.tickLength = this.tickLength;
    }

    public void setTicks(float ticks) {
        this.setTicks(50.0f, ticks);
    }

    public void setTicks(float factor, float ticks) {
        this.setTickLength(factor / ticks);
    }

    public void reset() {
        this.tickLength = 50.0f;
    }

    public float getTickLength() {
        return tickLength;
    }

    public void setTickLength(float tickLength) {
        this.tickLength = tickLength;
    }

    public enum Mode {
        Old, New
    }
}
