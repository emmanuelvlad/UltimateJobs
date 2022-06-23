package de.warsteiner.jobs.manager;
 
import java.text.DateFormat;
import java.text.SimpleDateFormat; 
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.warsteiner.jobs.UltimateJobs;
import net.md_5.bungee.api.ChatColor;

public class PluginManager {

	private UltimateJobs plugin = UltimateJobs.getPlugin();
	
	public static final Pattern HEX_PATTERN = Pattern.compile("#(\\w{5}[0-9a-f])#");

	public boolean isInstalled(String plugin) {
		Plugin Plugin = Bukkit.getServer().getPluginManager().getPlugin(plugin);
		if (Plugin != null) {
			return true;
		}
		return false;
	}

	public String getDateTodayFromCal() {
		DateFormat format = new SimpleDateFormat(plugin.getFileManager().getConfig().getString("Date"));
		Date data = new Date();
		return format.format(data);
	}
 
	
	public void startCheck() {
		new BukkitRunnable() {

			@Override
			public void run() {

				plugin.getExecutor().execute(() -> {

					if (plugin.getInit().isClosed()) {

						plugin.connect();

					}
				});
			}

		}.runTaskTimer(plugin, 0, 20 * plugin.getFileManager().getDataConfig().getInt("CheckConnectionEvery"));
	}
 
	public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

	public String toHex (String textToTranslate) {

	        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
	        StringBuffer buffer = new StringBuffer();

	        while(matcher.find()) {
	            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
	        }

	        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());

	    }
	
	public boolean isFullyGrownOld(Block block) {

		if (block.getBlockData() == null) {
			return false;
		}

		if (block.hasMetadata("placed-by-player")) {
			return false;
		}

		if (block.getType() == Material.MELON || block.getType() == Material.PUMPKIN
				|| block.getType() == Material.SUGAR_CANE) {
			return true;
		}

		BlockData bdata = block.getBlockData();
		if (bdata instanceof Ageable) {
			Ageable age = (Ageable) bdata;
			if (age.getAge() == age.getMaximumAge()) {
				return true;
			}
		}
		return false;
	}

}
