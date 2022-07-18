package org.steve.hockeyware.features.module.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.steve.hockeyware.features.module.Category;
import org.steve.hockeyware.features.module.Module;
import org.steve.hockeyware.setting.Setting;
import org.steve.hockeyware.util.client.BlockUtil;
import org.steve.hockeyware.util.client.InventoryUtil;

public class SelfWeb extends Module {

    public static final Setting<Boolean> enableInHole = new Setting<>("EnableInHole", false);
    public static SelfWeb INSTANCE;

    public SelfWeb() {
        super("SelfWeb", "Automatically puts you in a web", Category.Combat);
        INSTANCE = this;
    }

    public static boolean isInBurrow(EntityPlayer player) {
        return BlockUtil.getBlock(new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ))).equals(Blocks.OBSIDIAN) || BlockUtil.getBlock(new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ))).equals(Blocks.BEDROCK) || BlockUtil.getBlock(new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ))).equals(Blocks.WEB);
    }

    public static boolean isInHole(EntityPlayer player) {
        return !BlockUtil.getBlock(new BlockPos(player.posX + 1, player.posY, player.posZ)).equals(Blocks.AIR) && !BlockUtil.getBlock(new BlockPos(player.posX - 1, player.posY, player.posZ)).equals(Blocks.AIR) && !BlockUtil.getBlock(new BlockPos(player.posX, player.posY, player.posZ + 1)).equals(Blocks.AIR) && !BlockUtil.getBlock(new BlockPos(player.posX, player.posY, player.posZ - 1)).equals(Blocks.AIR) && player.posY == new BlockPos(player.posX, Math.floor(player.posY), player.posZ).getY();
    }

    @Override
    public void onUpdate() {
        if (isInBurrow(mc.player) || !InventoryUtil.isInHotbar(Item.getItemFromBlock(Blocks.WEB))) {
            this.toggle(true);
            return;
        }
        int oldslot = mc.player.inventory.currentItem;
        InventoryUtil.switchToSlot(InventoryUtil.getHotbarSlot(Item.getItemFromBlock(Blocks.WEB)), true);
        BlockUtil.placeBlock(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), EnumHand.MAIN_HAND, true, true, true);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldslot));
        if (this.isOn()) this.toggle(true);
    }
}
