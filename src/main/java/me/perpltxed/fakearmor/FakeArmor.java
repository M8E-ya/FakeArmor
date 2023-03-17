package me.perpltxed.fakearmor;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public final class FakeArmor extends JavaPlugin implements Listener {

  public static Plugin pl;

  @Override
  public void onEnable() {
    pl = this;
    new FakeEquipmentManager(this);
  }

}