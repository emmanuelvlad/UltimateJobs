package de.warsteiner.jobs.command.admincommand;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;

import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.command.AdminCommand;
import de.warsteiner.jobs.utils.admincommand.AdminSubCommand;

public class IDSub extends AdminSubCommand {

	@Override
	public String getName() {
		return "id";
	}

	@Override
	public String getDescription() {
		return "See the Java ID for Configs of the Item in You'r Hand";
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		if (args.length == 1) {
			 
			
			
			if(sender instanceof Player) {
				Player p = (Player) sender;
				 
			 
				if(p.getItemInHand() == null) {
					p.sendMessage(AdminCommand.prefix + "§cYou dont have a Vaild Item in You'r Hand!");
					return;
				}
				
				if(p.getItemInHand().getType() == Material.AIR) {
					p.sendMessage(AdminCommand.prefix + "§cYou dont have a Vaild Item in You'r Hand!");
					return;
				}
				
				ItemStack item = p.getItemInHand();
				
				String display = null;
				
				if (item.getType().name().contains("POTION")) {
					
					PotionMeta pm = (PotionMeta) item.getItemMeta(); 
					int level = 1;
					if (pm.getBasePotionData().isUpgraded()) {
						level = 2;
					}
				 
					String named = pm.getBasePotionData().getType().name().toUpperCase();

					String id = named + "_" + level;
					
					display = id;
				} else {
					display = item.getType().toString().toUpperCase();
				}
				
				p.sendMessage("§7");
				sender.sendMessage(AdminCommand.prefix + "§7ID of the Item in You'r Hand§8: §c§l"+display);
				p.sendMessage("§7");
				
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 3);
					
				return;
			} else {
				sender.sendMessage(AdminCommand.prefix + "§cThis Command is only for Players!");
			}
		}   else {
			if(sender instanceof Player) {
				Player player3 = (Player) sender;
				player3.playSound(player3.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 2);
			}
			sender.sendMessage(AdminCommand.prefix + "Correct Usage§8: §6"+getUsage());
		}
	}

	@Override
	public int getTabLength() {
		return 1;
	}

	@Override
	public String FormatTab() {
		return "command id";
	}

	@Override
	public String getUsage() { 
		return "/JobsAdmin id";
	}

	@Override
	public String getPermission() { 
		return "ultimatejobs.admin.id";
	}
	
	@Override
	public boolean showOnHelp() { 
		return true;
	}
	
}
