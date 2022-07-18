package org.steve.hockeyware.manager;

import com.google.common.base.Strings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.steve.hockeyware.HockeyWare;
import org.steve.hockeyware.events.PacketEvent;
import org.steve.hockeyware.events.player.ConnectionEvent;
import org.steve.hockeyware.events.player.TotemPopEvent;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.features.module.modules.ClickGUI;
import org.steve.hockeyware.features.module.modules.SelfWeb;
import org.steve.hockeyware.util.Timer;
import org.steve.hockeyware.util.client.ClientMessage;
import org.steve.hockeyware.util.client.ColorUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;
import java.util.UUID;

import static org.steve.hockeyware.features.module.modules.HUD.copyCoords;

public class EventManager implements Globals {
    public static final KeyBinding[] KEYS;
    private static EventManager INSTANCE;

    static {
        KEYS = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
    }

    private final Timer logoutTimer = new Timer();
    public boolean hasRan;

    public EventManager() {
    }

    public static EventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventManager();
        }

        return INSTANCE;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (fullNullCheck() && event.getEntity() == mc.player) {
            getHockey().popManager.onUpdate();
            for (Module module : getHockey().moduleManager.getModules()) {
                if (module.isToggled()) {
                    module.onUpdate();
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (ClickGUI.rainbow.getValue() && fullNullCheck()) {
            ClickGUI.getred.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getRed());
            ClickGUI.getgreen.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getBlue());
            ClickGUI.insidered.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getRed());
            ClickGUI.insidegreen.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getGreen());
            ClickGUI.insideblue.setValue(ColorUtil.releasedDynamicRainbow(3, ClickGUI.saturation.getValue(), ClickGUI.brightness.getValue()).getBlue());
        }
        if (copyCoords.getValue() && fullNullCheck()) {
            String coordinates = (int) mc.player.posX + " " + (int) mc.player.posY + " " + (int) mc.player.posZ;
            StringSelection stringSelection = new StringSelection(coordinates);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            ClientMessage.sendMessage("Copied coordinates to clipboard.");
            copyCoords.setValue(false);
        }
        if (SelfWeb.enableInHole.getValue() && SelfWeb.isInHole(mc.player) && !SelfWeb.isInBurrow(mc.player) && fullNullCheck()) {
            SelfWeb.INSTANCE.setToggled(true);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (fullNullCheck()) {
            for (Module module : getHockey().moduleManager.getModules()) {
                if (module.isOn()) {
                    module.onRender3D();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderHudText(RenderGameOverlayEvent.Text event) {
        if (fullNullCheck()) {
            for (Module module : getHockey().moduleManager.getModules()) {
                if (module.isToggled()) {
                    module.onRender2D();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent((EntityPlayer) packet.getEntity(mc.world)));
            }
        } else if (event.getPacket() instanceof SPacketPlayerListItem && fullNullCheck() && logoutTimer.hasReached(1L)) {
            SPacketPlayerListItem packet = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction())) {
                return;
            }
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(id, name, ConnectionEvent.Type.Join));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            MinecraftForge.EVENT_BUS.post(new ConnectionEvent(entity, id, logoutName, ConnectionEvent.Type.Leave));
                            break;
                        }
                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(id, null, ConnectionEvent.Type.Other));
                    }
                    default:
                        break;
                }
            });
        }
    }

    @SubscribeEvent
    public void onPlayerJoinedServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        HockeyWare.LOGGER.info("Joined");
    }


}