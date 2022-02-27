package de.warsteiner.jobs.utils.playercommand;

import org.bukkit.command.CommandSender;

import de.warsteiner.jobs.api.JobsPlayer;

public abstract class SubCommand {
	
    public abstract String getName();
 
    public abstract String getDescription();
 
    public abstract void perform(CommandSender sender, String[] args, JobsPlayer jb);
	
    public abstract String FormatTab();

    public abstract int getTabLength();
    
    public abstract boolean isEnabled();
}
