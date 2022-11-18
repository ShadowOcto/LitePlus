package me.rhys.base.util;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by FlyCode on 18/07/2019 Package ml.depressionclient.depression.util
 */
public class InventoryHelper {

    public static void throwItem(EntityPlayer entity, int index) {
        Minecraft.getMinecraft().playerController.windowClick(entity.inventoryContainer.windowId, index, 0, 0, entity);
        Minecraft.getMinecraft().playerController.windowClick(entity.inventoryContainer.windowId, -999, 0, 0, entity);
    }

    public static float getItemDamage(ItemStack itemStack) {
        if (itemStack == null) return 0.0f;
        Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage;
                if (attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {
                    damage = attributeModifier.getAmount();
                } else {
                    damage = attributeModifier.getAmount() * 100.0D;
                }
                if (attributeModifier.getAmount() > 1.0D) {
                    return 1.0F + (float) damage;
                }
                return 1.0F;
            }
        }
        return 1.0F;
    }

    public static int getBestWeaponInInventory(EntityPlayer entity) {
        double damage = 0;
        ItemStack target = null;
        for (int i = 0; i < entity.inventory.getSizeInventory(); i++) {
            ItemStack stack = entity.inventory.getStackInSlot(i);
            if (stack != null && ((stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemAxe))) {
                double dmg = getItemDamage(stack);
                if (dmg > damage) {
                    damage = dmg;
                    target = stack;
                }
            }
        }
        if (target == null)
            return -1;
        return entity.inventoryContainer.getInventory().indexOf(target);
    }
}
