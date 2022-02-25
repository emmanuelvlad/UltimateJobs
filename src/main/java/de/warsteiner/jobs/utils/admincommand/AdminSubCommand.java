package de.warsteiner.jobs.utils.admincommand;

import org.bukkit.command.CommandSender;
 

public abstract class AdminSubCommand {
	
    public abstract String getName();

    public abstract String getUsage();
    
    public abstract String getDescription();
 
    public abstract void perform(CommandSender sender, String[] args);
	
    public abstract String FormatTab();

    public abstract int getTabLength();
}
