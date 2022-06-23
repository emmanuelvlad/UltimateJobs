package de.warsteiner.jobs.jobs;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import de.warsteiner.jobs.UltimateJobs;

public class JobActionGrowSapling implements Listener {
	
	private static UltimateJobs plugin = UltimateJobs.getPlugin();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGrow(StructureGrowEvent event) { 
		
		if (event.isCancelled()) {
			if(plugin.getFileManager().getConfig().getBoolean("CancelEvents")) {
				event.setCancelled(true);
			}
			return;
		}
		
		TreeType type = event.getSpecies();
	  
		Block block = event.getLocation().getBlock();
		
		if(block == null) {
			return;
		}
		 
		if(block.hasMetadata("saplingby")) {
			
			String player = compareData(block);
			 
			if(player != null) {
				plugin.getJobWorkManager().executeSaplingGrowAction(""+type, UUID.fromString(player.toString()), block);
			}
			
		}
		 
	}
	
	public String compareData(Block block) {
		
		List<MetadataValue> values = block.getMetadata("saplingby");
		if (!values.isEmpty()) {

			for(MetadataValue value : values) { 
				String val = value.value().toString();
				
				if(val.contains("uuid;")) {
					
					String[] split = val.split(";");
					
					String player = split[1];
					
					
					return player;
					
				}
			}
			
		}
		return null;
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		e.getBlock().setMetadata("saplingby",  new FixedMetadataValue(UltimateJobs.getPlugin(), "uuid;"+e.getPlayer().getUniqueId())); 
	}
	
}
