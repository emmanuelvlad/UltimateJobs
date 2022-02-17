package de.warsteiner.jobs.events;

import de.warsteiner.jobs.UltimateJobs;
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
}
