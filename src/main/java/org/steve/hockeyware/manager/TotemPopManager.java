package org.steve.hockeyware.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.steve.hockeyware.events.player.DeathEvent;
import org.steve.hockeyware.events.player.TotemPopEvent;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.modules.Notifier;
import org.steve.hockeyware.util.client.ClientMessage;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager implements Globals {

    private final Map<EntityPlayer, Integer> pops = new ConcurrentHashMap<>();
    private final Set<EntityPlayer> announcers = new ConcurrentSet<>();
    String totemSpelling;

    public void onUpdate() {
        if (!this.announcers.isEmpty() && Notifier.INSTANCE.isToggled() && Notifier.INSTANCE.totemPops.getValue()) {
            for (EntityPlayer player : this.announcers) {
                int count = this.pops.getOrDefault(player, -1);
                if (count != -1) {
                    if (count == 1) {
                        totemSpelling = " totem!";
                    } else if (count > 1) {
                        totemSpelling = " totems!";
                    }
                    if (player.getName().equals(mc.player.getName())) {
                        ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + "I" + ChatFormatting.RESET + " have popped " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                    }
                } else if (getHockey().friendManager.isFriendByName(player.getName()) && !player.getName().equals(mc.player.getName())) {
                    ClientMessage.sendMessage("Your friend " + ChatFormatting.AQUA + player.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.AQUA + count + ChatFormatting.RESET + totemSpelling);
                } else if (!getHockey().friendManager.isFriendByName(player.getName()) && !player.getName().equals(mc.player.getName())) {
                    ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + player.getName() + ChatFormatting.RESET + " has popped " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                }

                this.announcers.remove(player);
            }
        }
    }

    @SubscribeEvent
    public void onTotemPop(TotemPopEvent event) {
        if (event.getPlayer() != null) {
            this.announcers.add(event.getPlayer());
            this.pops.merge(event.getPlayer(), 1, Integer::sum);
        }
    }


    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (Notifier.INSTANCE.totemPops.getValue() && Notifier.INSTANCE.isToggled()) {
            if (this.pops.containsKey(event.getPlayer())) {
                int count = this.pops.getOrDefault(event.getPlayer(), -1);
                if (count != -1) {
                    if (count == 1) {
                        totemSpelling = " totem!";
                    } else if (count > 1) {
                        totemSpelling = " totems!";
                    }
                    if (event.getPlayer().equals(mc.player)) {
                        ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + "I" + ChatFormatting.RESET + " just died after popping " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                    }
                } else if (getHockey().friendManager.isFriendByName(event.getPlayer().getName()) && !event.getPlayer().equals(mc.player)) {
                    ClientMessage.sendMessage("Your friend " + ChatFormatting.AQUA + event.getPlayer().getName() + ChatFormatting.RESET + " died after popping " + ChatFormatting.AQUA + count + ChatFormatting.RESET + totemSpelling);
                } else if (!getHockey().friendManager.isFriendByName(event.getPlayer().getName()) && !event.getPlayer().equals(mc.player)) {
                    ClientMessage.sendMessage(ChatFormatting.LIGHT_PURPLE + event.getPlayer().getName() + ChatFormatting.RESET + " died after popping " + ChatFormatting.LIGHT_PURPLE + count + ChatFormatting.RESET + totemSpelling);
                }
            }
            this.pops.remove(event.getPlayer());
            this.announcers.remove(event.getPlayer());
        }
    }
}