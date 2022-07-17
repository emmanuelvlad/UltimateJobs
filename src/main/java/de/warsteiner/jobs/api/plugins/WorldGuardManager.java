package de.warsteiner.jobs.api.plugins;

import org.bukkit.Bukkit; 
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
 
public class WorldGuardManager {

	public static WorldGuardPlugin wg;
	
	public static StateFlag BREAK_ACTION;
	public static StateFlag PLACE_ACTION;
	public static StateFlag KILL_ACTION;
	public static StateFlag FARM_ACTION;
	public static StateFlag FARM_GROW_ACTION;
	public static StateFlag FISH_ACTION;
	public static StateFlag MILK_ACTION; 
	public static StateFlag CRAFT_ACTION; 
	public static StateFlag SHEAR_ACTION;
	public static StateFlag AD_ACTION;
	public static StateFlag EAT_ACTION;
	public static StateFlag HONEY_ACTION;
	public static StateFlag STRIP_ACTION;
	public static StateFlag TAME_ACTION;
	public static StateFlag BREED_ACTION;
	public static StateFlag MMKILL_ACTION;
	public static StateFlag DRINK_ACTION;
	public static StateFlag BERRY_ACTION;
	public static StateFlag MMORES_BREAK;
	public static StateFlag KILLBYBOW;
	public static StateFlag GROWSAP; 
	public static StateFlag TR;
	public static StateFlag TRADE_EMERALDS;
	public static StateFlag SMELT;
	public static StateFlag EXPLORE; 
	public static StateFlag ENCHANT; 
	
	public WorldGuardManager getManager() {
		return this;
	}

	public static void setClass() {

		Plugin wgPlugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 
		wg = (WorldGuardPlugin) wgPlugin; 
	}
 

	@SuppressWarnings("rawtypes")
	public static void load() {
		WorldGuard worldGuard = WorldGuard.getInstance();
		FlagRegistry flagRegistry = worldGuard.getFlagRegistry();
		flagRegistry.register((Flag) (ENCHANT = new StateFlag("enchant-action", false)));
		flagRegistry.register((Flag) (EXPLORE = new StateFlag("explore-action", false)));
		flagRegistry.register((Flag) (SMELT = new StateFlag("smelt-action", false)));
		flagRegistry.register((Flag) (TRADE_EMERALDS = new StateFlag("trade-emeralds-action", false)));
		flagRegistry.register((Flag) (BREAK_ACTION = new StateFlag("break-action", false)));
		flagRegistry.register((Flag) (PLACE_ACTION = new StateFlag("place-action", false)));
		flagRegistry.register((Flag) (KILL_ACTION = new StateFlag("kill-action", false)));
		flagRegistry.register((Flag) (FARM_ACTION = new StateFlag("farm-break-action", false)));
		flagRegistry.register((Flag) (FISH_ACTION = new StateFlag("fish-action", false)));
		flagRegistry.register((Flag) (MILK_ACTION = new StateFlag("milk-action", false))); 
		flagRegistry.register((Flag) (CRAFT_ACTION = new StateFlag("craft-action", false))); 
		flagRegistry.register((Flag) (CRAFT_ACTION = new StateFlag("shear-action", false))); 
		flagRegistry.register((Flag) (AD_ACTION = new StateFlag("advancement-action", false))); 
		flagRegistry.register((Flag) (EAT_ACTION = new StateFlag("eat-action", false))); 
		flagRegistry.register((Flag) (HONEY_ACTION = new StateFlag("honey-action", false))); 
		flagRegistry.register((Flag) (STRIP_ACTION = new StateFlag("strip-action", false))); 
		flagRegistry.register((Flag) (TAME_ACTION = new StateFlag("tame-action", false))); 
		flagRegistry.register((Flag) (BREED_ACTION = new StateFlag("breed-action", false))); 
		flagRegistry.register((Flag) (MMKILL_ACTION = new StateFlag("mmkill-action", false))); 
		flagRegistry.register((Flag) (DRINK_ACTION = new StateFlag("drink-action", false))); 
		flagRegistry.register((Flag) (BERRY_ACTION = new StateFlag("collectberrys-action", false))); 
		flagRegistry.register((Flag) (MMORES_BREAK = new StateFlag("moreores-break-action", false))); 
		flagRegistry.register((Flag) (KILLBYBOW = new StateFlag("killbybow-action", false))); 
		flagRegistry.register((Flag) (GROWSAP = new StateFlag("grow-saplings-action", false))); 
		flagRegistry.register((Flag) (FARM_GROW_ACTION = new StateFlag("farm-grow-action", false))); 
		flagRegistry.register((Flag) (TR = new StateFlag("find-treasure-action", false)));
	}
	
	public static StateFlag getFlagFromName(String b) {
		if(b.equalsIgnoreCase("enchant-action")) { return ENCHANT; }
		if(b.equalsIgnoreCase("explore-action")) { return EXPLORE; }
		if(b.equalsIgnoreCase("smelt-action")) { return SMELT; }
		if(b.equalsIgnoreCase("trade-emeralds-action")) { return TRADE_EMERALDS; }
		if(b.equalsIgnoreCase("find-treasure-action")) { return TR; }
		if(b.equalsIgnoreCase("farm-grow-action")) { return FARM_GROW_ACTION; }
		if(b.equalsIgnoreCase("strip-action")) { return STRIP_ACTION; }
		if(b.equalsIgnoreCase("honey-action")) { return HONEY_ACTION; }
		if(b.equalsIgnoreCase("eat-action")) { return EAT_ACTION; }
		if(b.equalsIgnoreCase("advancement-action")) { return AD_ACTION; }
		if(b.equalsIgnoreCase("break-action")) { return BREAK_ACTION; }
		if(b.equalsIgnoreCase("place-action")) { return PLACE_ACTION; }
		if(b.equalsIgnoreCase("kill-action")) { return KILL_ACTION; }
		if(b.equalsIgnoreCase("farm-break-action")) { return FARM_ACTION; }
		if(b.equalsIgnoreCase("fish-action")) { return FISH_ACTION; }
		if(b.equalsIgnoreCase("milk-action")) { return MILK_ACTION; } 
		if(b.equalsIgnoreCase("craft-action")) { return CRAFT_ACTION; } 
		if(b.equalsIgnoreCase("tame-action")) { return TAME_ACTION; } 
		if(b.equalsIgnoreCase("shear-action")) { return SHEAR_ACTION; } 
		if(b.equalsIgnoreCase("breed-action")) { return BREED_ACTION; } 
		if(b.equalsIgnoreCase("mmkill-action")) { return MMKILL_ACTION; }
		if(b.equalsIgnoreCase("drink-action")) { return DRINK_ACTION; } 
		if(b.equalsIgnoreCase("collectberrys-action")) { return BERRY_ACTION; } 
		if(b.equalsIgnoreCase("moreores-break-action")) { return MMORES_BREAK; } 
		if(b.equalsIgnoreCase("killbybow-action")) { return KILLBYBOW; } 
		if(b.equalsIgnoreCase("grow-saplings-action")) { return GROWSAP; }  
		return null;
	}

	public static String checkFlag(org.bukkit.Location location, String f) {

		int priority = -1;
		WorldGuard instance = WorldGuard.getInstance();
		RegionContainer container = instance.getPlatform().getRegionContainer();
		BukkitWorld bukkitWorld = new BukkitWorld(location.getWorld());
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
