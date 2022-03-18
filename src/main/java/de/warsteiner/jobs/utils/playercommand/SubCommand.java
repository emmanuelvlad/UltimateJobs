package de.warsteiner.jobs.utils.playercommand;

import java.util.UUID;

import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.api.JobsPlayer;

public abstract class SubCommand {
	
    public abstract String getName(UUID UUID);
 
    public abstract String getDescription(UUID UUID);
 
    public abstract void perform(CommandSender sender, String[] args, JobsPlayer jb);
	
    public abstract String FormatTab();

    public abstract int getTabLength();
    
    public abstract boolean isEnabled();
    
    public abstract String getUsage(UUID UUID);
}
