package me.perpltxed.fakearmor.events;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.base.Preconditions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EquipmentSendingEvent {
  private Player client;

  private LivingEntity visibleEntity;

  private EnumWrappers.ItemSlot slot;

  private ItemStack equipment;

  EquipmentSendingEvent(Player client, LivingEntity visibleEntity, EnumWrappers.ItemSlot slot, ItemStack equipment) {
    this.client = client;
    this.visibleEntity = visibleEntity;
    this.slot = slot;
    this.equipment = equipment;
  }

  public Player getClient() {
    return this.client;
  }

  public LivingEntity getVisibleEntity() {
    return this.visibleEntity;
  }

  public ItemStack getEquipment() {
    return this.equipment;
  }

  public void setEquipment(ItemStack equipment) {
    this.equipment = equipment;
  }

  public EnumWrappers.ItemSlot getSlot() {
    return this.slot;
  }

  public void setSlot(EnumWrappers.ItemSlot slot) {
    this.slot = (EnumWrappers.ItemSlot) Preconditions.checkNotNull(slot, "slot cannot be NULL");
  }
}
