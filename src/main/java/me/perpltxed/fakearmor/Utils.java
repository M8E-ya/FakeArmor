package me.perpltxed.fakearmor;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Resident;

import java.util.Optional;

public class Utils {

  public static Optional<Government> getGovernment(Resident resident) {
    try{
      return Optional.ofNullable((Government) resident.getNation()).or(() -> {
        try {
          return Optional.ofNullable((Government) resident.getTown());
        } catch (NotRegisteredException e) {
          throw new RuntimeException(e);
        }
      });
    }catch (TownyException e) {
      return Optional.empty();
    }
  }

}
