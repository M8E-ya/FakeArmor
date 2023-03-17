package me.perpltxed.fakearmor;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Resident;

import java.util.Optional;

public class Utils {

  public static Optional<Government> getGovernment(Resident resident) {
    try{
      return Optional.ofNullable(resident.hasTown() ? (resident.getTown().hasNation() ? resident.getTown().getNation() : resident.getTown()) : null);
    }catch (TownyException e) {
      return Optional.empty();
    }
  }

}
