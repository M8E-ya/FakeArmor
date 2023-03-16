package me.perpltxed.fakearmor.events;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.google.common.collect.MapMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static me.perpltxed.fakearmor.FakeArmor.Observers;

public abstract class FakeEquipment {

  private ProtocolManager manager;

  private List<Pair<EnumWrappers.ItemSlot, ItemStack>> pair;

  public FakeEquipment(Plugin plugin) {
    this.manager = ProtocolLibrary.getProtocolManager();
    registerPacketListener(plugin);
  }

  private void registerPacketListener(Plugin plugin) {
    this.manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_EQUIPMENT) {
      public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(packet);
        Entity entity = wrapper.getEntity(event);
        if (entity instanceof Player) {
          Player player = (Player) entity;
          //checks the player alliance and do stuff
          boolean isPlayerInWar = true;
          if (isPlayerInWar) {
            event.setCancelled(true);
            List<WrapperPlayServerEntityEquipment> packets = Arrays.asList(getPacketForBoots(player), getPacketForLeggins(player), getPacketForChestplate(player), getPacketForHelmet(player));
            packets.forEach(packet1 -> Observers.stream().map(Bukkit::getPlayer).forEach(packet1::sendPacket));
          }
        }
      }
    });
  }

  private WrapperPlayServerEntityEquipment getPacketForBoots(Entity entity) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.FEET);
    wrapper.setItem(new ItemStack(Material.LEATHER_BOOTS));
    return wrapper;
  }

  //create method getPacketForLeggins, getPacketForChestplate, getPacketForHelmet
  private WrapperPlayServerEntityEquipment getPacketForLeggins(Entity entity) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.LEGS);
    wrapper.setItem(new ItemStack(Material.LEATHER_LEGGINGS));
    return wrapper;
  }

  private WrapperPlayServerEntityEquipment getPacketForChestplate(Entity entity) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.CHEST);
    wrapper.setItem(new ItemStack(Material.LEATHER_CHESTPLATE));
    return wrapper;
  }

  private WrapperPlayServerEntityEquipment getPacketForHelmet(Entity entity) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.HEAD);
    wrapper.setItem(new ItemStack(Material.LEATHER_HELMET));
    return wrapper;
  }

}