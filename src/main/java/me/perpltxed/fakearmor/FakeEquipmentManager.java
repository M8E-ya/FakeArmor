package me.perpltxed.fakearmor;

import com.comphenix.packetwrapper.WrapperPlayServerEntityEquipment;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.gmail.goosius.siegewar.SiegeController;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.perpltxed.fakearmor.Utils.getGovernment;

public class FakeEquipmentManager {

  private ProtocolManager manager;

  private List<Pair<EnumWrappers.ItemSlot, ItemStack>> pair;

  public FakeEquipmentManager(Plugin plugin) {
    this.manager = ProtocolLibrary.getProtocolManager();
    registerPacketListener(plugin);
  }

  private void registerPacketListener(Plugin plugin) {
    this.manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_EQUIPMENT) {
      public void onPacketSending(PacketEvent event) {
        if(event.getPacketType() != PacketType.Play.Server.ENTITY_EQUIPMENT) return; //probably not necessary because the listener already performs the packets filtering
        PacketContainer packet = event.getPacket();
        WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(packet);
        Entity entity = wrapper.getEntity(event);
        if (entity instanceof Player) {
          //Get the government of the player (town or nation)
          Player player = (Player) entity;
          Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
          Optional<Government> government = getGovernment(resident);
          //if a government is found then we check for a siege
          government.ifPresent(government1 -> {
            SiegeController.getSieges().stream().filter(siege -> siege.getAttacker().equals(government1) || siege.getDefender().equals(government1)).findFirst().ifPresent(siege -> {
              //if his goverment is in a siege then we check if he is in the siege zone
              if(!siege.getPlayersWhoWereInTheSiegeZone().contains(player)) return;
              //if he is we cancel the default packet and send our own
              event.setCancelled(true);
              //we prepare two packets, one for red (enemies) and one for green (allies)
              List<WrapperPlayServerEntityEquipment> redPackets = Arrays.asList(getPacketForBoots(player, Color.RED), getPacketForLeggings(player, Color.RED), getPacketForChestplate(player, Color.RED), getPacketForHelmet(player, Color.RED));
              List<WrapperPlayServerEntityEquipment> greenPackets = Arrays.asList(getPacketForBoots(player, Color.GREEN), getPacketForLeggings(player, Color.GREEN), getPacketForChestplate(player, Color.GREEN), getPacketForHelmet(player, Color.GREEN));
              //we iterate through all the players in the siege zone and send them the correct packet
              siege.getPlayersWhoWereInTheSiegeZone().stream().forEach(playerInSiege -> {
                Resident residentInSiege = TownyAPI.getInstance().getResident(playerInSiege.getUniqueId());
                Optional<Government> governmentInSiege = getGovernment(residentInSiege);
                governmentInSiege.ifPresent(governmentInSiege2 -> {
                  if (governmentInSiege2.equals(government1)) {
                    greenPackets.forEach(pk -> pk.sendPacket(playerInSiege));
                  } else {
                    redPackets.forEach(pk -> pk.sendPacket(playerInSiege));
                  }
                });
              });
            });
          });
        }
      }
    });
  }

  private WrapperPlayServerEntityEquipment getPacketForBoots(Entity entity, Color color) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.FEET);
    ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
    LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
    meta.setColor(color);
    itemStack.setItemMeta(meta);
    wrapper.setItem(itemStack);
    return wrapper;
  }

  private WrapperPlayServerEntityEquipment getPacketForLeggings(Entity entity, Color color) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.LEGS);
    ItemStack itemStack = new ItemStack(Material.LEATHER_LEGGINGS);
    LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
    meta.setColor(color);
    itemStack.setItemMeta(meta);
    wrapper.setItem(itemStack);
    return wrapper;
  }

  private WrapperPlayServerEntityEquipment getPacketForChestplate(Entity entity, Color color) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.CHEST);
    ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
    LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
    meta.setColor(color);
    itemStack.setItemMeta(meta);
    wrapper.setItem(itemStack);
    return wrapper;
  }

  private WrapperPlayServerEntityEquipment getPacketForHelmet(Entity entity, Color color) {
    WrapperPlayServerEntityEquipment wrapper = new WrapperPlayServerEntityEquipment(new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT));
    wrapper.setEntityID(entity.getEntityId());
    wrapper.setSlot(EnumWrappers.ItemSlot.HEAD);
    ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
    LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
    meta.setColor(color);
    itemStack.setItemMeta(meta);
    wrapper.setItem(itemStack);
    return wrapper;
  }

}