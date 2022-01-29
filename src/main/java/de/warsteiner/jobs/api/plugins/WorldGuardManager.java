package de.warsteiner.jobs.api.plugins;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry; 
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.warsteiner.jobs.UltimateJobs;
 

public class WorldGuardManager {

	public static WorldGuardPlugin wg;
	
	public static StateFlag BREAK_ACTION;
	public static StateFlag PLACE_ACTION;
	public static StateFlag KILL_ACTION;
	public static StateFlag FARM_ACTION;
	public static StateFlag FISH_ACTION;
	public static StateFlag MILK_ACTION; 
	public static StateFlag CRAFT_ACTION; 
	public static StateFlag SHEAR_ACTION;
	public static StateFlag AD_ACTION;
	public static StateFlag EAT_ACTION;
	public static StateFlag HONEY_ACTION;

	public WorldGuardManager getManager() {
		return this;
	}

	public static void setClass() {

		Plugin wgPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		Plugin editPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

		wg = (WorldGuardPlugin) wgPlugin;
 
		UltimateJobs.getPlugin().getSupportedPlugins().add(editPlugin);
		UltimateJobs.getPlugin().getSupportedPlugins().add(wgPlugin);
	}
 

	@SuppressWarnings("rawtypes")
	public static void load() {
		WorldGuard worldGuard = WorldGuard.getInstance();
		FlagRegistry flagRegistry = worldGuard.getFlagRegistry();
		flagRegistry.register((Flag) (BREAK_ACTION = new StateFlag("break-action", false)));
		flagRegistry.register((Flag) (PLACE_ACTION = new StateFlag("place-action", false)));
		flagRegistry.register((Flag) (KILL_ACTION = new StateFlag("kill-action", false)));
		flagRegistry.register((Flag) (FARM_ACTION = new StateFlag("farm-action", false)));
		flagRegistry.register((Flag) (FISH_ACTION = new StateFlag("fish-action", false)));
		flagRegistry.register((Flag) (MILK_ACTION = new StateFlag("milk-action", false))); 
		flagRegistry.register((Flag) (CRAFT_ACTION = new StateFlag("craft-action", false))); 
		flagRegistry.register((Flag) (CRAFT_ACTION = new StateFlag("shear-action", false))); 
		flagRegistry.register((Flag) (AD_ACTION = new StateFlag("advancement-action", false))); 
		flagRegistry.register((Flag) (EAT_ACTION = new StateFlag("eat-action", false))); 
		flagRegistry.register((Flag) (HONEY_ACTION = new StateFlag("honey-action", false))); 
	}
	
	public static StateFlag getFlagFromName(String b) {
		if(b.equalsIgnoreCase("honey-action")) { return HONEY_ACTION; }
		if(b.equalsIgnoreCase("eat-action")) { return EAT_ACTION; }
		if(b.equalsIgnoreCase("advancement-action")) { return AD_ACTION; }
		if(b.equalsIgnoreCase("break-action")) { return BREAK_ACTION; }
		if(b.equalsIgnoreCase("place-action")) { return PLACE_ACTION; }
		if(b.equalsIgnoreCase("kill-action")) { return KILL_ACTION; }
		if(b.equalsIgnoreCase("farm-action")) { return FARM_ACTION; }
		if(b.equalsIgnoreCase("fish-action")) { return FISH_ACTION; }
		if(b.equalsIgnoreCase("milk-action")) { return MILK_ACTION; } 
		if(b.equalsIgnoreCase("craft-action")) { return CRAFT_ACTION; } 
		if(b.equalsIgnoreCase("shear-action")) { return SHEAR_ACTION; } 
		return null;
	}

	public static String checkFlag(org.bukkit.Location location, String flag, Player p, String f) {

		int priority = -1;
		WorldGuard instance = WorldGuard.getInstance();
		RegionContainer container = instance.getPlatform().getRegionContainer();
		BukkitWorld bukkitWorld = new BukkitWorld(p.getWorld());
		RegionQuery query = container.createQuery();
		Location wLoc = new Location((Extent) bukkitWorld, location.getX(), location.getY(), location.getZ());
		ProtectedRegion selected = null;
		for (ProtectedRegion r : query.getApplicableRegions(wLoc)) {
			if (r.getPriority() > priority) {
				priority = r.getPriority();
				selected = r;
			}
		}

		String state = "ALLOW";
		StateFlag d = getFlagFromName(f);
		if (selected != null) {
			if (selected.getFlag(d) != null) {
				if (selected.getFlag(d) == State.DENY) {
					state = "DENY";
				}
			}
		}

		return state;

	}

}
