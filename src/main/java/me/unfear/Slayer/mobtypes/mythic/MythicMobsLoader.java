package me.unfear.Slayer.mobtypes.mythic;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.unfear.Slayer.Slayer;
import me.unfear.Slayer.mobtypes.MobType;

public class MythicMobsLoader {

	public static final MythicBukkit mythicMobs = (MythicBukkit) Slayer.inst.getServer().getPluginManager()
			.getPlugin("MythicMobs");

	public static MobType load(ConfigurationSection section, String key) {
		try {
			Integer.parseInt(key);
		} catch (NumberFormatException e) {
			Slayer.inst.getLogger().severe("Unable to load mob-type, id isn't an integer (id: " + key + ")");
			Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
			return null;
		}

		final int id = Integer.parseInt(key);

		final String name = section.getString("name");
		final String mythicMob = section.getString("mythicmob");

		if (name == null || mythicMob == null) {
			Slayer.inst.getLogger().severe("Unable to load MYTHIC_MOBS mob-type, missing value (id: " + key + ")");
			Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
			return null;
		}

		boolean validMob = false;
		for (MythicMob mob : mythicMobs.getMobManager().getMobTypes()) {
			if (mob.getInternalName().equalsIgnoreCase(mythicMob)) {
				validMob = true;
				break;
			}
		}

		if (!validMob) {
			Slayer.inst.getLogger()
					.severe("Unable to load MYTHIC_MOBS mob-type, mythicmob does not exist (id: " + key + ")");
			Slayer.inst.getPluginLoader().disablePlugin(Slayer.inst);
			return null;
		}

		final String materialString = section.getString("material");
		try {
			return new MythicMobType(id, name, Material.valueOf(materialString), mythicMob);
		} catch (IllegalArgumentException e) {
			Slayer.inst.getLogger().warning(
					"Invalid material for MYTHIC_MOBS mob-type, defaulting to skeleton skull (id: " + key + ")");
			return new MythicMobType(id, name, Material.SKELETON_SKULL, mythicMob);
		}
	}

}
