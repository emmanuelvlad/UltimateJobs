package de.warsteiner.jobs.utils.module;

import org.bukkit.command.CommandSender;

public abstract class JobsModule {

	public abstract String getPluginName();

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getDeveloper();

	public abstract String getVersion();

	public abstract void reload(CommandSender sender);

}
