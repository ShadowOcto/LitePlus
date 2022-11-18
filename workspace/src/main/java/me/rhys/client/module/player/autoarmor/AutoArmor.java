package me.rhys.client.module.player.autoarmor;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {

    long lastDelay = 0;

    public Timer timer = new Timer();
    private Timer glitchFixer = new Timer();

    @Name("Only INV")
    public static boolean checkInv = false;

    @Name("Delay")
    @Clamp(min = 0, max = 200)
    public static float delay = 10f;

    public static boolean isDone = false;

    public AutoArmor(String name, String description, Category category, int keyCode) {
        super(name, description, category, keyCode);
    }

    public boolean isWearing() {
        return !timer.hasReached(200);
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (checkInv) {
            if (!(mc.currentScreen instanceof GuiInventory))
                return;
        }

        if (mc.thePlayer.openContainer != null) {
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                return;
            }
        }

        if (!timer.hasReached(lastDelay))
            return;

        //if (ModuleManager.chestStealerMod.isStealing())
            //return;

        lastDelay = (long) (delay);

        if (mc.thePlayer.capabilities.isCreativeMode) {
            timer.reset();
            return;
        }

        int slot;

        for (ArmorType armorType : ArmorType.values()) {
            if ((slot = this.findArmor(armorType, InvUtils.getArmorScore(mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal())))) != -1) {
                // set
                isDone = false;
                if (mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal()) != null) {
                    dropArmor(armorType.ordinal());
                    timer.reset();
                    return;
                }
                warmArmor(slot);
                timer.reset();
                return;
            } else {
                isDone = true;
            }
        }
    }

    private int findArmor(ArmorType armorType, float minimum) {
        float best = 0;
        int result = -1;

        for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (InvUtils.getArmorScore(itemStack) < 0 || InvUtils.getArmorScore(itemStack) <= minimum
                    || InvUtils.getArmorScore(itemStack) < best || !isValid(armorType, itemStack))
                continue;

            best = InvUtils.getArmorScore(itemStack);
            result = i;
        }

        return result;
    }

    private boolean isValid(ArmorType type, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor))
            return false;

        ItemArmor armor = (ItemArmor) itemStack.getItem();

        if (type == ArmorType.HELMET && armor.armorType == 0)
            return true;

        if (type == ArmorType.CHEST_PLATE && armor.armorType == 1)
            return true;

        if (type == ArmorType.LEGGINGS && armor.armorType == 2)
            return true;

        if (type == ArmorType.BOOTS && armor.armorType == 3)
            return true;

        return false;
    }


    private void warmArmor(int slot_In) {
        if (slot_In >= 0 && slot_In <= 8) { // 0-8 is hotbar
            clickSlot(slot_In + 36, 0, true);
        } else {
            clickSlot(slot_In, 0, true);
        }
    }


    private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, mouseButton,
                shiftClick ? 1 : 0, this.mc.thePlayer);
    }


    private void dropArmor(int armorSlot) {
        int slot = InvUtils.armorSlotToNormalSlot(armorSlot);
        if (!InvUtils.isFull()) {

            this.clickSlot(slot, 0, true);
        } else {
            this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4,
                    this.mc.thePlayer);
        }
    }
    enum ArmorType {
        BOOTS, LEGGINGS, CHEST_PLATE, HELMET
    }
}