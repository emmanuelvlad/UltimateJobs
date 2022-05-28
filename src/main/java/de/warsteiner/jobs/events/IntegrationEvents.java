package de.warsteiner.jobs.events;

import de.warsteiner.datax.custom.PlayerLanguageChangeEvent;
import de.warsteiner.datax.utils.objects.Language;
import de.warsteiner.jobs.UltimateJobs;
import de.warsteiner.jobs.utils.objects.JobsPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class IntegrationEvents implements Listener {
	
    private UltimateJobs plugin = UltimateJobs.getPlugin();

    @EventHandler
    public void onPluginEnable(final PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("NotQuests") && plugin.getNotQuestManager().getNotQuestsInstance() == null) {
            // Turn on support for NotQuests
            plugin.getNotQuestManager().setClass();
        }
    }
    
    
    @EventHandler
    public void onLanguageChange(PlayerLanguageChangeEvent event) {
    	Player player = event.getPlayer();
    	Language newlanguage = event.getNewLanguage();
    	
    	JobsPlayer jb = plugin.getPlayerAPI().getRealJobPlayer(""+player.getUniqueId());
    	
    	Language got = plugin.getLanguageAPI().getLanguages().get(newlanguage.getName());
    	
    	jb.updateLanguage(got);
    	
    }
    
    
}
