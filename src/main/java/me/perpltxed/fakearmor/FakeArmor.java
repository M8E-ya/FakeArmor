package me.perpltxed.fakearmor;

import com.comphenix.protocol.wrappers.EnumWrappers;
import me.perpltxed.fakearmor.events.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class FakeArmor extends JavaPlugin implements Listener {

    public static Plugin pl;
    public static Map<UUID, String> Games = new HashMap<>();

    @Override
    public void onEnable() {
        pl = this;
        new FakeEquipment((Plugin)this) {
            protected boolean onEquipmentSending(EquipmentSendingEvent equipmentEvent) {
                if (equipmentEvent.getSlot() == EnumWrappers.ItemSlot.HEAD && equipmentEvent.getEquipment().getType() == Material.DIAMOND_HELMET) {
                    equipmentEvent.setEquipment(new ItemStack(Material.LEATHER_HELMET));
                    return true;
                }
                if (equipmentEvent.getSlot() == EnumWrappers.ItemSlot.CHEST && equipmentEvent.getEquipment().getType() == Material.DIAMOND_CHESTPLATE) {
                    equipmentEvent.setEquipment(new ItemStack(Material.LEATHER_CHESTPLATE));
                    return true;
                }
                if (equipmentEvent.getSlot() == EnumWrappers.ItemSlot.LEGS && equipmentEvent.getEquipment().getType() == Material.DIAMOND_LEGGINGS) {
                    equipmentEvent.setEquipment(new ItemStack(Material.LEATHER_LEGGINGS));
                    return true;
                }
                if (equipmentEvent.getSlot() == EnumWrappers.ItemSlot.FEET && equipmentEvent.getEquipment().getType() == Material.DIAMOND_BOOTS) {
                    equipmentEvent.setEquipment(new ItemStack(Material.LEATHER_BOOTS));
                    return true;
                }
                return false;
            }
        };
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
        }, 0, 1);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
