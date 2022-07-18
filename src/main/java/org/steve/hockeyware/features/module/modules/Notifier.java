package org.steve.hockeyware.features.module.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.steve.hockeyware.events.client.ModuleToggleEvent;
import org.steve.hockeyware.events.player.EntitySpawnEvent;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.setting.Setting;
import org.steve.hockeyware.util.Timer;
import org.steve.hockeyware.util.client.ClientMessage;
import org.steve.hockeyware.util.client.InventoryUtil;

public class Notifier extends Module {

    public static Notifier INSTANCE;
    public final Setting<Boolean> modules = new Setting<>("Modules", true);
    public final Setting<Boolean> totemPops = new Setting<>("TotemPops", true);
    public final Setting<Boolean> visualrange = new Setting<>("VisualRange", true);
    public final Setting<Boolean> totemwarning = new Setting<>("NoTotemWarning", false);
    private final Timer timer = new Timer();

    public Notifier() {
        super("Notifier", "Notifies you of stuff", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true) != 0 && totemwarning.getValue()) {
            if (timer.hasReached(250)) {
                if (StrictTotem.INSTANCE.isOn()) {
                    ClientMessage.sendOverwriteClientMessage("There is no totem in your offhand!");
                    timer.reset();
                }
            }
        }
    }

    @SubscribeEvent
    public void onModuleToggled(ModuleToggleEvent event) {
        if (this.modules.getValue()) {
            Module module = event.getModule(); //bad syntax but its fine
            if (!("ClickGUI".equals(module.getName()))) {
                if (!("ChestSwap".equals(module.getName()))) {
                    ClientMessage.sendMessage(module.getName() + ChatFormatting.BOLD + (module.isToggled() ? ChatFormatting.GREEN + " Enabled!" : ChatFormatting.RED + " Disabled!"));
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!fullNullCheck()) return;
        if (visualrange.getValue() && event.getEntity() instanceof EntityPlayer && event.getEntity() != mc.player && event.getEntity() != null) {
            if (event.getType().equals(EntitySpawnEvent.Type.Spawn)) {
                if (event.getEntity().equals(getHockey().friendManager.getPlayer())) {
                    ClientMessage.sendOverwriteClientMessage(ChatFormatting.AQUA + event.getEntity().getName() + ChatFormatting.RESET + " has entered visual range!");
                } else {
                    ClientMessage.sendOverwriteClientMessage(event.getEntity().getName() + " has entered visual range!");
                }
            }
            if (event.getType().equals(EntitySpawnEvent.Type.Despawn)) {
                if (event.getEntity().equals(getHockey().friendManager.getPlayer())) {
                    ClientMessage.sendOverwriteClientMessage(ChatFormatting.AQUA + event.getEntity().getName() + ChatFormatting.RESET + " has left visual range!");
                } else if (!mc.getSession().getUsername().equals(event.getEntity().getName())) {
                    ClientMessage.sendOverwriteClientMessage(event.getEntity().getName() + " has left visual range!");
                }
            }
        }
    }
}
