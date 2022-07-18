package org.steve.hockeyware.features.module.modules;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.steve.hockeyware.events.PacketEvent;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.util.client.InventoryUtil;

public class StrictTotem extends Module {

    public static StrictTotem INSTANCE;

    public StrictTotem() {
        super("AutoTotem", "Automatically puts a totem in your offhand", Category.Combat);
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        return String.valueOf(InventoryUtil.getItemCount(Items.TOTEM_OF_UNDYING, true));
    }

    @Override
    public void onUpdate() {
        if (Globals.mc.currentScreen instanceof GuiContainer && !(Globals.mc.currentScreen instanceof GuiInventory))
            return;
        int totemslot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING);
        if (Globals.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && totemslot != -1) {
            Globals.mc.playerController.windowClick(Globals.mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, Globals.mc.player);
            Globals.mc.playerController.windowClick(Globals.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, Globals.mc.player);
            Globals.mc.playerController.windowClick(Globals.mc.player.inventoryContainer.windowId, totemslot, 0, ClickType.PICKUP, Globals.mc.player);
            Globals.mc.playerController.updateController();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketClickWindow) {
            Globals.mc.player.connection.sendPacket(new CPacketEntityAction(Globals.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
    }
}
