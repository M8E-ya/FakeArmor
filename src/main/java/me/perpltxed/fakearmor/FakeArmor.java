package me.perpltxed.fakearmor;

import com.comphenix.protocol.wrappers.EnumWrappers;
import me.perpltxed.fakearmor.events.EquipmentSendingEvent;
import me.perpltxed.fakearmor.events.FakeEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public final class FakeArmor extends JavaPlugin implements Listener {

  public static Plugin pl;
  public static Set<UUID> Observers = new HashSet<>();

  @Override
  public void onEnable() {
    pl = this;
    Bukkit.getServer().getPluginManager().registerEvents(this, this);
    new FakeEquipment(this) {
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
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Observers.add(event.getPlayer().getUniqueId());
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Observers.remove(event.getPlayer().getUniqueId());
  }
}